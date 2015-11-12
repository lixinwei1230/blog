package me.qyh.oauth2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import me.qyh.oauth2.dao.OauthUserDao;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.utils.Validators;

public class DefaultOauthDetailsServiceImpl implements OauthDetailsService {

	@Autowired
	private OauthUserDao oauth2Dao;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public UserDetails loadUserByOauthPrincipal(OauthPrincipal oauth2Principal) throws UsernameNotFoundException {

		if (validOauthPrincipal(oauth2Principal)) {
			OauthUser user = oauth2Dao.selectByUserIdAndType(oauth2Principal.getOauthUserId(),
					oauth2Principal.getType());
			if (user == null) {
				throw new UsernameNotFoundException("");
			}

			return userDetailsService.loadUserByUsername(user.getUser().getUsername());
		}

		throw new UsernameNotFoundException("");
	}

	protected boolean validOauthPrincipal(OauthPrincipal oauth2Principal) {
		return !(Validators.isEmptyOrNull(oauth2Principal.getOauthUserId()) || oauth2Principal.getType() == null);
	}

}
