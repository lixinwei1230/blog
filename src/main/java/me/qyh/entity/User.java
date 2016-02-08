package me.qyh.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.qyh.helper.html.JsonHtmlXssSerializer;
import me.qyh.security.RoleGrantedAuthority;
import me.qyh.utils.Validators;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 系统注册用户
 * 
 * @author henry.qian
 *
 */
@JsonIgnoreProperties(value = { "password", "email", "activateDate", "activate", "registerDate", "authorities",
		"enabled", "accountNonExpired", "roles", "accountNonLocked", "credentialsNonExpired" })
public class User extends Id implements UserDetails, CredentialsContainer, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String username;
	private String password;
	private String email;
	private Date registerDate;
	private Date activateDate;
	private Boolean activate;
	private Boolean enabled;
	private Boolean accountNonExpired;
	private Boolean credentialsNonExpired;
	private Boolean accountNonLocked;
	private List<Role> roles = new ArrayList<Role>();
	private String avatar;
	private Space space;
	private String nickname;

	public User() {
		super();
	}

	public User(Integer id) {
		super(id);
	}

	public User(Integer id, String nameOrEmail) {
		super(id);
		if (nameOrEmail.indexOf('@') != -1) {
			this.email = nameOrEmail;
		} else {
			this.username = nameOrEmail;
		}
	}

	public User(Integer id, String username, String email) {
		super(id);
		this.username = username;
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(Date activateDate) {
		this.activateDate = activateDate;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getActivate() {
		return activate;
	}

	public void setActivate(Boolean activate) {
		this.activate = activate;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public boolean hasRole(RoleEnum role) {
		if (Validators.isEmptyOrNull(roles) || role == null) {
			return false;
		}
		for (Role _role : roles) {
			if (role.equals(_role.getRole())) {
				return true;
			}
		}
		return false;
	}

	public void removeRole(RoleEnum role) {
		if (!(roles.isEmpty() || role == null)) {
			int index = -1;
			for (int i = 0; i < roles.size(); i++) {
				Role _role = roles.get(i);
				if (role.equals(_role.getRole())) {
					index = i;
				}
			}
			if (index != -1) {
				roles.remove(index);
			}
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authorities.add(new RoleGrantedAuthority(role.getRole(), role.getId()));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof User) {
			User _rhs = (User) rhs;
			if (getId() != null && _rhs.getId() != null) {
				return getId().equals(_rhs.getId());
			}
			if (username != null && _rhs.username != null) {
				return username.equals(_rhs.username);
			}
			if (email != null && _rhs.email != null) {
				return email.endsWith(_rhs.email);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode()) + username == null ? 0
				: username.hashCode();
		return result;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + " ,username=" + username + ", space=" + space + "]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String printRoles(){
		if(Validators.isEmptyOrNull(roles)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(Role role : roles){
			sb.append(role.getRole()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
