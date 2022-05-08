package pl.edu.pw.blog.security;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import pl.edu.pw.blog.User;
import pl.edu.pw.blog.data.UserRepository;
import pl.edu.pw.blog.web.ArticlesController;


@Controller
@RequestMapping("/register")
public class RegistrationController {

  Logger log = LoggerFactory.getLogger(RegistrationController.class);
  
  private UserRepository userRepo;
  private PasswordEncoder passwordEncoder;
  private UserService userService;

  public RegistrationController(
      UserRepository userRepo, PasswordEncoder passwordEncoder, UserService userService) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }
  

  @GetMapping
  public String registerForm(WebRequest request, Model model) {
	  RegistrationForm form = new RegistrationForm();
	  model.addAttribute("form", form);
	  return "register"; 
	  }

  
  @PostMapping
  public String processRegistration(@ModelAttribute("form") @Valid RegistrationForm form, Errors errors, Model model) {
	  log.info("PROCESSING REGISTRATION");
	  if (errors.hasErrors()) {
		  
		  return "register";
	  }
	  try { 
		    
		    if(userService.userExists(form.getUsername())) {
		    	log.info("User exists");
		    	model.addAttribute("message", "Konto dla podanego loginu już istnieje");
		    	return "register";
		    };
		    User registered = userRepo.save(form.toUser(passwordEncoder));
		    log.info("SUCCESSFUL registration of " + registered.getUsername());
	    } catch (Exception e) {
	    	log.info(e.getMessage());
	        model.addAttribute("message", "Failure of input");
	        return "register";
	    } 
	  
	
    return "redirect:/login";
  }

}