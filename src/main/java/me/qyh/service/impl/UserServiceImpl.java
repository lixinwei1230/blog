package me.qyh.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;

import me.qyh.bean.Crop;
import me.qyh.dao.FileDao;
import me.qyh.dao.LoginInfoDao;
import me.qyh.dao.RoleDao;
import me.qyh.dao.UserCodeDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.LoginInfo;
import me.qyh.entity.MyFile;
import me.qyh.entity.Role;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.entity.UserCode;
import me.qyh.entity.UserCodeType;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.file.BadImageException;
import me.qyh.helper.file.ImageInfo;
import me.qyh.helper.file.ImageProcessing;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.helper.mail.Mailer;
import me.qyh.helper.mail.MimeMessageHelperHandler;
import me.qyh.pageparam.LoginInfoPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.UserServer;
import me.qyh.service.UserService;
import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Files;
import me.qyh.utils.Times;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService, InitializingBean {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserCodeDao userCodeDao;
	@Value("${config.mail.frequency}")
	private int mailFrequency;
	@Value("${config.activateCode.livetime}")
	private int activateCodeLiveTime;
	@Value("${config.findPasswordCode.liveTime}")
	private int findPasswordCodeLiveTime;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ActivateSuccessHandler activateSuccessHandler;
	@Autowired
	private WebFreemarkers freemarkers;
	@Autowired
	private Mailer mailer;
	@Autowired
	private UserServer userServer;
	private UserNameChecker userNameChecker = new SafeUserNameChecker();
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private FileServer fileServer;
	@Autowired
	private ImageProcessing im4javas;
	@Autowired
	private LoginInfoDao loginInfoDao;
	@Value("${config.avatar.filePrefix}")
	private String avatarPrefix;

	private static final String MAIL_ACTIVATE_TPL_PATH = "mail/activate.ftl";
	private static final String MAIL_FINDPASSWORD_TPL_PATH = "mail/findPassword.ftl";
	private static final String MAIL_REGISTER_SUCCESS_TPL_PATH = "mail/registerSuccess.ftl";

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void register(User user) throws LogicException {
		userNameChecker.check(user.getUsername());
		User db = userDao.selectByName(user.getUsername());
		if (db != null) {
			throw new LogicException("error.user.isExists", user.getUsername());
		}
		User db2 = userDao.selectByEmail(user.getEmail());
		if (db2 != null) {
			throw new LogicException("error.user.isExists", user.getEmail());
		}
		encryptPassword(user);

		user.setNickname(user.getUsername());

		userDao.insert(user);
		String activateCode = UUID.randomUUID().toString();
		userCodeDao.insert(new UserCode(activateCode, user, UserCodeType.ACTIVATE));
		sendRegisterMail(user, activateCode);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void reactive(String name, String mail) throws LogicException {
		User user = userDao.selectByName(name);
		if (user == null || !user.getEmail().equals(mail)) {
			throw new LogicException("error.user.notexists");
		}
		if (user.getActivate()) {
			throw new LogicException("error.user.activated");
		}

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.ACTIVATE);
		if (userCode != null) {
			checkMailFrequency(userCode);
			userCode.setAlive(false);
			userCodeDao.update(userCode);
		}

		String activateCode = UUID.randomUUID().toString();
		userCodeDao.insert(new UserCode(activateCode, user, UserCodeType.ACTIVATE));
		sendRegisterMail(user, activateCode);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void activate(Integer userId, String activateCode) throws LogicException {
		User user = userDao.selectById(userId);
		if (user == null) {
			throw new LogicException("error.user.notexists");
		}
		if (user.getActivate()) {
			throw new LogicException("error.user.activated");
		}
		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.ACTIVATE);

		userCodeCheck(userCode, activateCode, activateCodeLiveTime);

		user.setActivate(true);
		user.setActivateDate(new Date());
		userDao.update(user);
		userCode.setAlive(false);
		userCodeDao.update(userCode);
		insertUserRole(user, RoleEnum.ROLE_USER);

		activateSuccessHandler.activateSuccess(user);

		sendRegisterSuccessMail(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void forgetPassword(String name, String email) throws LogicException {
		User user = userServer.getUserByNameOrEmail(name);
		if (!user.getEmail().equals(email)) {
			throw new LogicException("error.user.notexists");
		}

		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.FORGETPASSWORD);
		if (userCode != null) {
			checkMailFrequency(userCode);
			userCode.setAlive(false);
			userCodeDao.update(userCode);
		}

		String code = UUID.randomUUID().toString();
		userCodeDao.insert(new UserCode(code, user, UserCodeType.FORGETPASSWORD));
		sendFindPasswordMail(user, code);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void resetPasswordCheck(String code, Integer userId) throws LogicException {
		User user = userServer.getUserById(userId);
		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.FORGETPASSWORD);

		userCodeCheck(userCode, code, findPasswordCodeLiveTime);
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

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void resetPassword(String code, String password, Integer userId) throws LogicException {
		User user = userServer.getUserById(userId);
		UserCode userCode = userCodeDao.selectByUserAndType(user, UserCodeType.FORGETPASSWORD);

		userCodeCheck(userCode, code, findPasswordCodeLiveTime);

		userCode.setAlive(false);
		userCodeDao.update(userCode);
		user.setPassword(password);

		encryptPassword(user);
		userDao.update(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void changePassword(String oldPassword, String newPassword) throws LogicException {
		User db = userServer.getUserById(UserContext.getUser().getId());

		String dbPassword = db.getPassword();
		if (!passwordEncoder.matches(oldPassword, dbPassword)) {
			throw new LogicException("error.invalidOldPassword");
		}

		db.setPassword(newPassword);
		encryptPassword(db);
		userDao.update(db);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void changeNickname(String nickname) throws LogicException {
		userNameChecker.check(nickname);
		User user = UserContext.getUser();
		user.setNickname(nickname);
		userDao.update(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateAvatar(Crop crop) throws LogicException {
		User user = UserContext.getUser();
		File file = crop.getFile();
		if (file == null || !file.exists()) {
			throw new LogicException("error.avatar.fileNotFound");
		}
		boolean croped = false;
		ImageInfo info = null;
		try {
			info = im4javas.read(file);
			croped = (info.getWidth() != crop.getW() || info.getHeight() != crop.getH());
		} catch (BadImageException e) {
			throw new LogicException("error.upload.badImage");
		}
		File dest = file;
		if (croped) {
			try {
				crop.setFile(file);
				File tmp = new File(file.getParentFile(), Files.appendFilename(file.getName(), "$"));
				im4javas.crop(crop, tmp);
				dest = tmp;
			} catch (Exception e) {
				throw new LogicException("error.avatar.badCrop");
			}
		}
		File rename = new File(dest.getParent() , Files.prependFilename(dest.getName(), avatarPrefix));
		if(!dest.renameTo(rename)){
			throw new SystemException(
					String.format("头像文件%s更名为%失败",dest.getAbsolutePath(),rename.getAbsolutePath()));
		}
		MyFile avatar = new MyFile(user, dest.length(), rename.getName(), new Date(), file.getName());
		FileStorage avatarStore = fileServer.getStore(avatar);
		try {
			avatar.setRelativePath(avatarStore.store(avatar, rename));
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}

		avatar.setWidth(info.getWidth());
		avatar.setHeight(info.getHeight());
		avatar.setStore(avatarStore);
		fileDao.insert(avatar);

		user.setAvatar(avatar.getUrl());
		userDao.update(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LoginInfo> findLoginInfos(LoginInfoPageParam param) {
		return new Page<LoginInfo>(param, loginInfoDao.selectCount(param), loginInfoDao.selectPage(param));
	}

	private void sendRegisterMail(final User user, String activateCode) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", user);
		variables.put("activateCode", activateCode);

		final String subject = messageSource.getMessage("mail.subject.register", new Object[] { user.getUsername() },
				LocaleContextHolder.getLocale());
		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setSubject(subject);
				helper.setTo(user.getEmail());
				helper.setText(freemarkers.processTemplateIntoString(MAIL_ACTIVATE_TPL_PATH, variables), true);

			}
		}, true);
	}

	private void sendFindPasswordMail(final User user, String code) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", user);
		variables.put("code", code);

		final String subject = messageSource.getMessage("mail.subject.findPassword", new Object[] {},
				LocaleContextHolder.getLocale());
		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setSubject(subject);
				helper.setTo(user.getEmail());
				helper.setText(freemarkers.processTemplateIntoString(MAIL_FINDPASSWORD_TPL_PATH, variables), true);

			}
		}, true);
	}

	private void sendRegisterSuccessMail(final User user) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", user);

		final String subject = messageSource.getMessage("mail.subject.register.success", new Object[] {},
				LocaleContextHolder.getLocale());
		mailer.sendEmail(new MimeMessageHelperHandler() {

			@Override
			public void handle(MimeMessageHelper helper) throws MessagingException {
				helper.setSubject(subject);
				helper.setTo(user.getEmail());
				helper.setText(freemarkers.processTemplateIntoString(MAIL_REGISTER_SUCCESS_TPL_PATH, variables), true);

			}
		}, true);
	}

	private void encryptPassword(User user) {
		String oldpassword = user.getPassword();
		String newPassword = passwordEncoder.encode(oldpassword);
		user.setPassword(newPassword);
	}

	private void insertUserRole(User user, RoleEnum role) {
		Role dbRole = roleDao.selectByRoleName(role);
		userDao.insertUserRole(user, dbRole);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (userDao.selectByRole(RoleEnum.ROLE_MESSAGER).isEmpty()
				|| userDao.selectByRole(RoleEnum.ROLE_SUPERVISOR).isEmpty()) {
			throw new Exception("系统初始化时需要指定一个管理角色和信息发送角色");
		}
	}

	public void setUserNameChecker(UserNameChecker userNameChecker) {
		this.userNameChecker = userNameChecker;
	}

	private void checkMailFrequency(UserCode userCode) throws LogicException {
		double second = Times.getSecond(userCode.getCreateDate(), new Date());
		if (second < mailFrequency) {
			throw new LogicException("error.frequenceOperation", mailFrequency - (int) second);
		}
	}

}
