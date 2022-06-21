package pl.edu.pw.blog.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.
                                              UserDetailsService;
import org.springframework.security.core.userdetails.
                                       UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.pw.blog.data.User;
import pl.edu.pw.blog.data.UserRepository;


/**
 * Auxiliary service methods for RegistrationController.
 * 
 * @author Michal
 *
 */
@Service
@Transactional
public class UserService 
        implements UserDetailsService {


private UserRepository userRepo;


  
  @Autowired
  public UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
    
  }
  
  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user != null) {
      return user;
    }
    throw new UsernameNotFoundException(
                    "Użytkownik '" + username + "' nie został znaleziony.");
  }
  

  public boolean userExists(String username) {
      return userRepo.findByUsername(username) != null;
  }

}
