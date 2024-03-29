package pl.edu.pw.blog.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


import pl.edu.pw.blog.security.RegistrationForm;

/**
 * Custom validator verifying whether the passwords typed by user during registration match.
 * 
 * @author Michal
 *
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatches, RegistrationForm>{
	@Override
    public void initialize(PasswordMatches p) {
        
    }
    
    public boolean isValid(RegistrationForm user, ConstraintValidatorContext c) {
        String password = user.getPassword();
        String repeatPassword = user.getMatchingPassword();
        
        if(password == null || !password.equals(repeatPassword)) {
            return false;
        }
        
        return true;
    }
}
