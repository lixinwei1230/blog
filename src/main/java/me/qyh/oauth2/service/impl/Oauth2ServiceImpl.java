package me.qyh.oauth2.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.RoleDao;
import me.qyh.dao.UserCodeDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.Role;
import me.qyh.entity.Role.RoleEnum;
import me.qyh.entity.User;
import me.qyh.entity.UserCode;
import me.qyh.entity.UserCode.UserCodeType;
import me.qyh.exception.LogicException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.helper.mail.Mailer;
import me.qyh.helper.mail.MimeMessageHelperHandler;
import me.qyh.oauth2.dao.OauthUserDao;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.entity.OauthUser.OauthType;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.oauth2.service.OauthService;
import me.qyh.server.UserServer;
import me.qyh.service.impl.ActivateSuccessHandler;
import me.qyh.service.impl.SafeUserNameChecker;
import me.qyh.service.impl.UserNameChecker;
import me.qyh.utils.Strings;
import me.qyh.utils.Times;
import me.qyh.utils.Validators;

public class Oauth2ServiceImpl implements OauthService {

	private static final String UNUSEDPASSWORD = "unused";
	private static final int RANDOMCODELENGTH = 6;
	private static final String MAIL_OAUTH_BIND = "mail/oauth/bind.ftl";
	private static final String MAIL_OAUTH_COMPLETE_USERINFO = "mail/oauth/completeUserInfo.ftl";
	private static final String MAIL_OAUTH_EMAIL_AUTHORIZE = "mail/oauth/authorizeEmail.ftl";

	@Autowired
	private OauthUserDao oauth2Dao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserCodeDao userCodeDao;
	@Value("${config.validation.user.nicknameMinLength}")
	private int minNicknameLength;
	@Value("${config.validation.user.nicknameMaxLength}")
	private int maxNicknameLength;
	@Value("${config.mail.frequency}")
	private int mailFrequency;
	@Value("${config.oauthCode.livetime}")
	private int oauthCodeLiveTime;
	@Autowired
	private Mailer mailer;
	@Autowired
	private WebFreemarkers freemarkers;
	@Autowired
	private MessageSource messageSource;
	private int randomCodeLength = RANDOMCODELENGTH;
	private UserNameChecker userNameChecker = new SafeUserNameChecker();
	@Autowired
	private ActivateSuccessHandler activateSuccessHandler;
	@Autowired
	private UserServer userServer;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public User autoBind(OauthPrincipal principal, OauthUser oauthUser) throws LogicException {
		OauthUser db = oauth2Dao.selectByUserIdAndType(principal.getOauthUserId(), principal.getType());
		if (db != null) {
			throw new LogicException("error.oauth.bind");
		}

		User random = randomUser();
		String nickname = oauthUser.getNickname();

		if (validNickname(nickname)) {
			random.setNickname(nickname);
		} else {
			random.setNickname(random.getUsername().substring(0, maxNicknameLength));
		}

		userDao.insert(random);

		random.setActivate(true);
		random.setActivateDate(random.getRegisterDate());
		userDao.update(random);

		insertUserRole(random, RoleEnum.ROLE_OAUTH);

		// 激活成功处理
		activateSuccessHandler.activateSuccess(random);

		oauthUser.setUser(random);
		oauthUser.setCreateDate(random.getRegisterDate());
		oauth2Dao.insert(oauthUser);

		return random;
	}

	@Override
	@Transactional(readOnly = true)
	public User get(OauthPrincipal principal) {
		OauthUser user = oauth2Dao.selectByUserIdAndType(principal.getOauthUserId(), principal.getType());
		if (user != null) {
			return userDao.selectById(user.getUser().getId());
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void sendAuthorizeEmail(final String email, User user) throws LogicException {
		checkOauthRole(user);

		User db = userDao.selectByEmail(email);
		if (db != null && !db.equals(user)) {
			throw new LogicException("error.user.isExists", email);
		}

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.OAUTH_AUTHORIZE_EMAIIL);
		if (userCode != null) {
			checkOauthMailFrequency(userCode);
			userCode.setAlive(false);
			userCodeDao.update(userCode);
		}

		String randomCode = generateRandomCode();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("code", randomCode);
		variables.put("expire", oauthCodeLiveTime);
		final String subject = messageSource.getMessage("mail.subject.oauth.completeUserInfo", new Object[] {},
				LocaleContextHolder.getLocale());
		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setTo(email);
				helper.setSubject(subject);
				helper.setText(freemarkers.processTemplateIntoString(MAIL_OAUTH_EMAIL_AUTHORIZE, variables), true);
			}
		}, true);

		userDao.updateFixedTerm(new User(user.getId(), email));

		UserCode toSend = new UserCode(randomCode, user, UserCodeType.OAUTH_AUTHORIZE_EMAIIL);
		userCodeDao.insert(toSend);

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void sendCompleteInfoEmail(String code, User user) throws LogicException {
		checkOauthRole(user);

		UserCode dbCode = userCodeDao.selectByUserAndType(user, UserCodeType.OAUTH_AUTHORIZE_EMAIIL);
		userCodeCheck(dbCode, code, oauthCodeLiveTime);

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.OAUTH_COMPLETE_USERINFO);
		if (userCode != null) {
			checkOauthMailFrequency(userCode);
			userCode.setAlive(false);
			userCodeDao.update(userCode);
		}

		user = userServer.getUserById(user.getId());
		final String email = user.getEmail();
		final String subject = messageSource.getMessage("mail.subject.oauth.completeUserInfo", new Object[] {},
				LocaleContextHolder.getLocale());
		String randomCode = Strings.uuid();
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("code", randomCode);
		variables.put("expire", oauthCodeLiveTime);
		variables.put("user", user);
		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setSubject(subject);
				helper.setTo(email);
				helper.setText(freemarkers.processTemplateIntoString(MAIL_OAUTH_COMPLETE_USERINFO, variables), true);
			}
		}, true);

		UserCode toSend = new UserCode(randomCode, user, UserCodeType.OAUTH_COMPLETE_USERINFO);
		userCodeDao.insert(toSend);
	}

	@Override
	@Transactional(readOnly = true)
	public void checkCompleteInfoEmail(String code, int userid) throws LogicException {
		User db = userServer.getUserById(userid);
		if (!db.hasRole(RoleEnum.ROLE_OAUTH) || !Validators.validateEmail(db.getEmail())) {
			throw new LogicException("error.oauth.invalidUser");
		}

		UserCode userCode = userCodeDao.selectByUserAndType(db, UserCodeType.OAUTH_COMPLETE_USERINFO);
		userCodeCheck(userCode, code, oauthCodeLiveTime);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void completeInfo(User user, int userid, String code) throws LogicException {
		User db = userServer.getUserById(userid);
		if (!db.hasRole(RoleEnum.ROLE_OAUTH)) {
			throw new LogicException("error.oauth.invalidUser");
		}

		userNameChecker.check(user.getUsername());

		User nameOwner = userDao.selectByName(user.getUsername());
		if (nameOwner != null) {
			throw new LogicException("error.user.isExists", user.getUsername());
		}

		UserCode userCode = userCodeDao.selectByUserAndType(db, UserCodeType.OAUTH_COMPLETE_USERINFO);
		userCodeCheck(userCode, code, oauthCodeLiveTime);
		userCode.setAlive(false);
		userCodeDao.update(userCode);

		user.setId(userid);
		userDao.updateFixedTerm(new User(userid, user.getUsername()));

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDao.update(user);

		insertUserRole(db, RoleEnum.ROLE_USER);
		deleteUserRole(db, RoleEnum.ROLE_OAUTH);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void sendBindEmail(OauthPrincipal principal, final String email) throws LogicException {
		User user = userServer.getUserByNameOrEmail(email);
		if (user.hasRole(RoleEnum.ROLE_OAUTH)) {
			throw new LogicException("error.oauth.invalidUser");
		}

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.OAUTH_BIND);
		if (userCode != null) {
			checkOauthMailFrequency(userCode);
			userCode.setAlive(false);
			userCodeDao.update(userCode);
		}

		String randomCode = generateRandomCode();

		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("code", randomCode);
		variables.put("expire", oauthCodeLiveTime);
		final String subject = messageSource.getMessage("mail.subject.oauth.bind", new Object[] {},
				LocaleContextHolder.getLocale());

		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setSubject(subject);
				helper.setTo(email);
				helper.setText(freemarkers.processTemplateIntoString(MAIL_OAUTH_BIND, variables), true);
			}
		}, true);

		UserCode code = new UserCode(randomCode, user, UserCodeType.OAUTH_BIND);
		userCodeDao.insert(code);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OauthUser> findOauthBinds(User user) {
		return oauth2Dao.selectByUser(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void unbind(User user, OauthType type) throws LogicException {
		OauthUser db = oauth2Dao.selectByUserAndType(user, type);
		if (db == null) {
			throw new LogicException("error.oauth.unbind");
		}
		oauth2Dao.deleteById(db.getId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public User bind(OauthPrincipal principal, String code, String email, OauthUser oauthUser) throws LogicException {
		User user = userServer.getUserByNameOrEmail(email);

		OauthUser db = oauth2Dao.selectByUserAndType(user, oauthUser.getType());
		if (db != null) {
			throw new LogicException("error.oauth.bind");
		}

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.OAUTH_BIND);
		userCodeCheck(userCode, code, oauthCodeLiveTime);

		oauthUser.setUser(user);
		oauthUser.setCreateDate(new Date());
		oauth2Dao.insert(oauthUser);

		userCode.setAlive(false);
		userCodeDao.update(userCode);

		return user;
	}

	private void insertUserRole(User user, RoleEnum role) {
		Role dbRole = roleDao.selectByRoleName(role);
		userDao.insertUserRole(user, dbRole);
	}

	private void deleteUserRole(User user, RoleEnum role) {
		Role dbRole = roleDao.selectByRoleName(role);
		userDao.deleteUserRole(user, dbRole);
	}

	protected void userCodeCheck(UserCode userCode, String code, long liveTime) throws LogicException {
		if (userCode == null) {
			throw new LogicException("error.user.invalid");
		}
		if (!userCode.getCode().equals(code)) {
			throw new LogicException("error.code.error");
		}
		if (!userCode.getAlive() || Times.getMinute(userCode.getCreateDate(), new Date()) > liveTime) {
			throw new LogicException("error.code.overdue");
		}
	}

	protected boolean validNickname(String nickname) {
		boolean valid = !(Validators.isEmptyOrNull(nickname, true) || nickname.trim().length() < minNicknameLength
				|| nickname.length() > maxNicknameLength || !Jsoup.isValid(nickname, Whitelist.none()));

		if (valid) {
			try {
				userNameChecker.check(nickname);
			} catch (LogicException e) {
				valid = false;
			}
		}

		return valid;
	}

	private User randomUser() {
		User user = new User();
		user.setPassword(UNUSEDPASSWORD);
		user.setEmail(Strings.uuid());
		user.setUsername(Strings.uuid());
		user.setRegisterDate(new Date());

		return user;
	}

	private void checkOauthMailFrequency(UserCode userCode) throws LogicException {
		double second = Times.getSecond(userCode.getCreateDate(), new Date());
		if (second < mailFrequency) {
			throw new LogicException("error.frequenceOperation", mailFrequency - (int) second);
		}
	}

	private String generateRandomCode() {
		return RandomStringUtils.random(randomCodeLength, true, true);
	}

	private void checkOauthRole(User user) throws LogicException {
		boolean hasOauthRole = false;
		if (user.hasRole(RoleEnum.ROLE_OAUTH)) {
			List<Role> currentRoles = roleDao.selectByUser(user);
			user.setRoles(currentRoles);
			if (user.hasRole(RoleEnum.ROLE_OAUTH)) {
				hasOauthRole = true;
			}
		}
		if (!hasOauthRole) {
			throw new LogicException("error.oauth.invalidUser");
		}
	}

	public void setRandomCodeLength(int randomCodeLength) {
		this.randomCodeLength = randomCodeLength;
	}

	public void setUserNameChecker(UserNameChecker userNameChecker) {
		this.userNameChecker = userNameChecker;
	}
}
