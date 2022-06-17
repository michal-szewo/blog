package pl.edu.pw.blog.security.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom Bean validation annotation which logic relies on PasswordMatchValidator class.
 * It is designed to be used in {@link pl.edu.pw.blog.security.RegistrationForm RegistrationForm} class in order to verify whether the passwords typed by user during registration match.
 * 
 * @author Michal
 *
 */
@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy=PasswordMatchValidator.class)
@Documented
public @interface PasswordMatches {
	String message() default "Podane hasła nie zgadzają się";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
