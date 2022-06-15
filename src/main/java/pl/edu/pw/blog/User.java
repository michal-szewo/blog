package pl.edu.pw.blog;


import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
import lombok.ToString;

@Entity
@Table(name="USERS")
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED,force=true)
public class User implements Comparable<User>, UserDetails{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@Column(nullable=false, unique=true)
	private String username;
	
	@NonNull
	@ToString.Exclude
	@Column(nullable=false)
	private String password;
	
	@NonNull
	private String fullname;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "author")
	private Set<Article> articles = new HashSet<>();
	
	@ToString.Exclude
	@ManyToMany(mappedBy = "likers",fetch=FetchType.EAGER)
	private Set<Article> likedArticles = new HashSet<>();
	
	
	
	@Override
	public boolean equals(Object o) {
		if (this ==o) return true;
		if (o == null || getClass() !=o.getClass()) return false;
		User other = (User) o;
		if (!id.equals(other.getId())) return false;
		if (!username.equals(other.getUsername())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31*result + username.hashCode();	
		return result;
	}
	
	@Override
	public int compareTo(User o) {
		return this.username.compareTo(o.getUsername());
	}
	
	  
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
