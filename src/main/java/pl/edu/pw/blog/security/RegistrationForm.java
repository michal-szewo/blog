package pl.edu.pw.blog.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import lombok.NonNull;
import pl.edu.pw.blog.User;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import pl.edu.pw.blog.security.validation.PasswordMatches;

@Data
@PasswordMatches
public class RegistrationForm {
  
	
	@NotBlank(message="Podanie loginu jest obowiązkowe")
	private String username;
	
	@NotBlank(message="Podanie hasła jest obowiązkowe")
	@Size(min=8, message="Hasło powinno liczyć przynajmniej 8 znaków")
	private String password;
	
	private String matchingPassword;
	
	@NotBlank(message="Podanie pełnej nazwy jest obowiązkowe")
	private String fullname;
  
  
  public User toUser(PasswordEncoder passwordEncoder) {
    return new User(
	username, passwordEncoder.encode(password), fullname
	);
  }
  
}
