package pl.edu.pw.blog;


import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.
                                          SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.edu.pw.blog.security.validation.PasswordMatches;

@Entity
@Table(name="USERS")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE,force=true)
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;
	
	@NonNull
	private String fullname;
	
	/*
	 * @OneToMany(mappedBy="author",fetch = FetchType.EAGER) private Set<Article>
	 * articles;
	 */
	
	  
	  @Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	  }
	
	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }
	
	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }
	
	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }
	
	  @Override
	  public boolean isEnabled() {
	    return true;
	  }
	  
}
