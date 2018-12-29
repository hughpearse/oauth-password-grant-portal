package usrportal.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "User")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Size(max = 256)
	@Column(length = 256, unique = false, nullable = false)
	String password;
	
	@NotNull
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
	String username;
	
	@Size(max = 128)
	@Column(length = 128, unique = false)
	String regActivationToken;
	
	@Size(max = 128)
	@Column(length = 128, unique = false)
	String passActivationToken;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;
	
	@NotNull
	Boolean isAccountNonExpired;
	
	@NotNull
	Boolean isAccountNonLocked;
	
	@NotNull
	Boolean isCredentialsNonExpired;
	
	@NotNull
	Boolean isEnabled;
		
	public User() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		for(String role: roles) {
			auths.add(new SimpleGrantedAuthority(role.toUpperCase()));
		}
		return auths;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public String setPassword(String password) {
		return this.password = password ;
	}

	public String setUsername(String username) {
		return this.username = username;
	}
		
	public boolean setIsAccountNonExpired(Boolean isAccountNonExpired) {
		return this.isAccountNonExpired = isAccountNonExpired;
	}

	public boolean setIsAccountNonLocked(Boolean isAccountNonLocked) {
		return this.isAccountNonLocked = isAccountNonLocked;
	}

	public boolean setIsCredentialsNonExpired(Boolean isCredentialsNonExpired) {
		return this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	
	public boolean setIsEnabled(Boolean isEnabled) {
		return this.isEnabled= isEnabled;
	}

	public String getRegActivationToken() {
		return regActivationToken;
	}

	public void setRegActivationToken(String regActivationToken) {
		this.regActivationToken = regActivationToken;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
