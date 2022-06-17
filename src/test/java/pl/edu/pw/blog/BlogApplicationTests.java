package pl.edu.pw.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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


import pl.edu.pw.blog.data.UserRepository;
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

	@Autowired
	private MockMvc mockMvc;
	
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticlesController aController;
	

	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(aController).isNotNull();
	}
	
	@Test
	public void whenUnauthorized_ThenRedirectToLoginPage() throws Exception{
		this.mockMvc.perform(get("/"))
		.andExpect(status().is(302))
		;
		
	}
	
	@Test
	public void whenUnauthorizedAccessesDirectlyLoginPage_thenReturnLoginPage() throws Exception{
		this.mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
		
	}
	
	@Test
	@Sql(scripts = "/test-user-data.sql")
	public void testSuccessfulLogin() throws Exception {
		
	    RequestBuilder requestBuilder = formLogin().user("test").password("test1234");
	    this.mockMvc.perform(requestBuilder).andExpect(redirectedUrl("/")).andExpect(status().isFound());
	
	}
	
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
				;
				
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Podane hasła nie zgadzają się"));			
		
	}
	
	@Test
	public void passwordIsTooShort() throws Exception{
		
				
		MvcResult result = this.mockMvc.perform(post("/register")
				.param("username", "djasidjas")
				.param("fullname", "atygsd8agsd")
				.param("password", "m")
				.param("matchingPassword", "m")
				.with(csrf()))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("form", "password"))
				.andReturn()
				;
		
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Hasło powinno liczyć przynajmniej 8 znaków"));
		
		
	}	
	
	
	@Test
	public void testFailedLogin() throws Exception {
		
	    RequestBuilder requestBuilder = formLogin().user("").password("");
	    this.mockMvc.perform(requestBuilder).andExpect(redirectedUrl("/login?error=true")).andExpect(status().isFound());
	
	}
	
	
	
	@Test
	@WithMockUser(username = "test", roles = "USER")
	public void whenAuthorizedUser_thenReturnArticlesTemplate() throws Exception {
	
		
	    
		MvcResult result = this.mockMvc.perform(get("/").sessionAttr("user", userRepo.findByUsername("test")))
	        .andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
	        .andReturn();
		
		String content = result.getResponse().getContentAsString();
	    assertNotNull(content);
	    assertTrue(content.contains("Zalogowany:"));
	}
	
	
	
	
	@Test
	public void whenValidName_thenUserShouldBeFound() {
	    String name = "m";
	    User found = (User) userService.loadUserByUsername(name);
	 
	     assertThat(found.getUsername())
	      .isEqualTo(name);
	 }
	
	
	@Test
	@WithMockUser(username = "m", roles = "USER")
	public void sendJsonDBChanges_whenUserIsAuthorized() throws Exception {
	    
		this.mockMvc.perform(get("/dbChanges")).andDo(print())
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentType("application/json"));
	      
	}
	
	
	@Test
	
	public void dontSendJsonDBChanges_whenUserIsUnAuthorized() throws Exception {
	    
		this.mockMvc.perform(get("/dbChanges"))
	      .andExpect(status().is3xxRedirection());
	      
	      
	}
	
	
	
	

}
