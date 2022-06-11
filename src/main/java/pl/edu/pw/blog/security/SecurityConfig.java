package pl.edu.pw.blog.security;

import org.springframework.context.annotation.Bean;
//end::securityConfigOuterClass[]
//tag::baseBonesImports[]
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web
                      .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
                      .configuration.WebSecurityConfigurerAdapter;
//end::baseBonesImports[]

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation
           .authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web
           .builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


@Autowired
private UserDetailsService userDetailsService;


@Override
protected void configure(HttpSecurity http) throws Exception {
  http
  .authorizeRequests()
			
			.antMatchers("/").access("hasRole('ROLE_USER')").antMatchers("/h2-console","/registration").access("permitAll")
			
			
			.and()
			
      .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true")
        
      
    .and()
      .logout()
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
    .and()  
        .headers()
        .frameOptions()
         .sameOrigin()
 
    .and()
      .csrf()
      .ignoringAntMatchers("/h2-console/**")

    
   
    ;
}

@Bean
public PasswordEncoder encoder() {
  return new BCryptPasswordEncoder();
}


@Override
protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {

  auth
    .userDetailsService(userDetailsService)
    .passwordEncoder(encoder());
  
}


}
