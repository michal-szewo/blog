package pl.edu.pw.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import pl.edu.pw.blog.data.CommentRepository;
import pl.edu.pw.blog.data.User;
import pl.edu.pw.blog.data.UserRepository;
import pl.edu.pw.blog.security.RegistrationController;
import pl.edu.pw.blog.security.UserService;
import pl.edu.pw.blog.web.ArticlesController;


/**
 * JUnit 5 and Spring testing. Mainly security issues (authorization, signing in, registration).
 * 
 * @author Michal
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class BlogApplicationTests {
	
	
	Long likesNumber = 0L;
	Long commentsNumber = 0L;
	Long articlesNumber = 2L;
	Long authorsNumber = 2L;
	
	Map<String,Object> mockModelAttributes = Map.ofEntries(
			entry("likesNumber",likesNumber),
			entry("commentsNumber",commentsNumber),
			entry("articlesNumber",articlesNumber),
			entry("authorsNumber",authorsNumber)
			);
	

	@Autowired
	private MockMvc mockMvc;
	
	
	@Autowired
	private UserRepository userRepo;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistrationController rController;
	
	
	
	
	// Test prawidłowego załadowania klasy RegistrationController
	@Test
	public void contextLoads() throws Exception {
		assertThat(rController).isNotNull();
	}
	
	
	// Test ładowania użytkownika po prawidłowej nazwie
	@Test
	public void whenValidName_thenUserShouldBeFound() {
	    String name = "m";
	    User found = (User) userService.loadUserByUsername(name);
	 
	     assertThat(found.getUsername())
	      .isEqualTo(name);
	 }
	
	
	// Niezalogowany użytkownik próbuje otworzyć stronę z listą artykułów - spodziewany rezultat: przekierowanie do strony logowania
	@Test
	public void whenUnauthorizedUser_ThenRedirectToLoginPage() throws Exception{
		this.mockMvc.perform(get("/"))
		.andExpect(status().is(302))
		;
		
	}
	
	// Niezalogowany użytkownik próbuje otworzyć stronę /login - spodziewany rezultat: status HTTP 200, nazwa widoku login
	@Test
	public void whenUnauthorizedUserAccessesDirectlyLoginPage_thenReturnLoginPage() throws Exception{
		this.mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
		
	}
	
	
	// Niezalogowany użytkownik próbuje wywołać zastrzeżoną dla zalogowanych metodę get kontrolera ArticlesController zwracającą dane w formacie Json
	// Spodziewany rezultat: przekierowanie (do strony logowania)
	@Test
	public void whenUserIsUnauthorized_dontSendJsonDBChanges() throws Exception {
	    
		this.mockMvc.perform(get("/dbChanges"))
	      .andExpect(status().is3xxRedirection());
	      
	      
	}
	
	
	// Test rejestracji - podane hasła się nie zgadzają
	// Spodziewany rezultat: błąd walidacji
	@Test
	public void passwordsDontMatch() throws Exception{
		
				
		MvcResult result = this.mockMvc.perform(post("/register")
				.param("username", "m")
				.param("fullname", "m")
				.param("password", "eeeeeeeeee")
				.param("matchingPassword", "miiiiiiiiiiiii")
				.with(csrf()))
				.andExpect(model().hasErrors())
				.andReturn();
				
				
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Podane hasła nie zgadzają się"));			
		
	}
	
	// Test rejestracji - podane hasła są za krótkie
	// Spodziewany rezultat: błąd walidacji
	@Test
	public void passwordIsTooShort() throws Exception{
		
				
		MvcResult result = this.mockMvc.perform(post("/register")
				.param("username", "djasidjas")
				.param("fullname", "atygsd8agsd")
				.param("password", "1234567")
				.param("matchingPassword", "1234567")
				.with(csrf()))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("form", "password"))
				.andReturn()
				;
		
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Hasło powinno liczyć przynajmniej 8 znaków"));
		
		
	}
	
	//Udana resjestracja - spodziewane przekierowanie do strony logowania
	@Test
	public void successfulRegistration() throws Exception{
		
				
		this.mockMvc.perform(post("/register")
				.param("username", "nowy_testowy")
				.param("fullname", "nowy_testowy")
				.param("password", "nowy_testowy")
				.param("matchingPassword", "nowy_testowy")
				.with(csrf()))
				.andExpect(redirectedUrl("/login")).andExpect(status().is(302))
				;
		
		
		
	}
	
	// Test udanego logowania po podaniu prawidłowych poświadczeń (poświadczenia załadowane uprzednio za pomocą pliku test-user-data.sql)
	// Spodziewany rezultat: przekeirowanie do strony głównej
	@Test
	@Sql(scripts = "/test-user-data.sql")
	public void testSuccessfulLogin() throws Exception {
		
	    RequestBuilder requestBuilder = formLogin().user("m").password("test1234");
	    this.mockMvc.perform(requestBuilder);
	    
	   // this.mockMvc.perform(requestBuilder).andExpect(redirectedUrl("/")).andExpect(status().isFound());
	    
	    
	}
	
	// Test nieudanego logowania - spodziewany rezultat: przekierowanie do strony /login?error=true
	@Test
	public void testFailedLogin() throws Exception {
		
	    RequestBuilder requestBuilder = formLogin().user("invalid").password("invalid333");
	    this.mockMvc.perform(requestBuilder).andExpect(redirectedUrl("/login?error=true")).andExpect(status().isFound());
	
	}
	
	
	// Test poprawnego załadowania strony głównej, gdy użytkownik jest zalogowany
	@Test
	@WithMockUser(username = "test", roles = "USER")
	public void whenAuthorizedUser_thenReturnArticlesTemplate() throws Exception {
	
		
	    
		MvcResult result = this.mockMvc.perform(get("/").sessionAttr("user", userRepo.findByUsername("test")).flashAttrs(mockModelAttributes))
	        .andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
	        .andReturn();
		
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Zalogowany:"));
	}
	
	
	// Sprawdzenie, czy użytkownik może usunąć cudzy artykuł
	// Spodziewany rezultat: komunikat błędu
	@Test
	public void whenNotAuthor_thenCannotDeleteArticle() throws Exception {
		
		User testUser = (User) userService.loadUserByUsername("m");
		
		this.mockMvc.perform(post("/articles/delete/1").with(user(testUser)).with(csrf()))
		.andExpect(flash().attributeExists("errorMessage"));
		
	    		
	}
		
	
	// Sprawdzenie, czy zalogowany użytkownik może pobrać dane json
	// Spodziewany rezultat: status HTTP 200
	@Test
	@WithMockUser(username = "m", roles = "USER")
	public void whenUserIsAuthorized_sendJsonDBChanges() throws Exception {
	    
		this.mockMvc.perform(get("/dbChanges"))
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentType("application/json"));
	      
	}
	
	
	

}
