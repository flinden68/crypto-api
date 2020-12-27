package nl.elstarit.crypto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer implements UserDetails {

	@Id
	protected String id;

	@CreatedDate
	@Builder.Default
	protected LocalDateTime created = LocalDateTime.now();

	@LastModifiedDate
	@Builder.Default
	protected LocalDateTime modified = LocalDateTime.now();

	String name;
	String username;
	String password;

	private boolean active = true;
	private Set<GrantedAuthority> roles = new HashSet<>();

	public Customer(){}

	@Builder
	public Customer(String username, String password){
		this.username = username;
		this.password = password;
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return active;
	}

	@Override
	public boolean isAccountNonLocked() {
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return active;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}
}
