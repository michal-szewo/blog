package pl.edu.pw.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.UserRepository;
import pl.edu.pw.blog.web.ArticlesController;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ArticleRepository artRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ArticlesController controller;
	
	
	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void redirectMainPageWhenNoAuthorisation() throws Exception{
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection());
	}
	
	
	
	

}
