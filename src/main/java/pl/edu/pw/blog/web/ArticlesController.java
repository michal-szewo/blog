package pl.edu.pw.blog.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import pl.edu.pw.blog.Article;
import pl.edu.pw.blog.User;
import pl.edu.pw.blog.data.ArticleRepository;


/**
 * Controller for the home page
 * @author michal
 *
 */

@Controller
public class ArticlesController {
	Logger log = LoggerFactory.getLogger(ArticlesController.class);
	
	private ArticleRepository articleRepo;
	
	public ArticlesController(ArticleRepository articleRepo) {
		this.articleRepo = articleRepo;
	}
	
	@ModelAttribute(name = "newArticle")
	  public Article article() {
	    return new Article();
	  }
	
	@GetMapping("/")
	public String showArticles(WebRequest request, Model model ,@AuthenticationPrincipal User user) {
		
		model.addAttribute("articles",articleRepo.findAllByOrderByPublishedAtDesc());
		model.addAttribute("user",user);
		return "articles";
	}
	
	@PostMapping("/addArticle")
	public String addArticle(@ModelAttribute("newArticle") @Valid Article article, Errors errors, @AuthenticationPrincipal User user, Model model) {
		
		log.info(user.getUsername());
		
		article.setAuthor(user);
		articleRepo.save(article);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/articles/delete/{id}", method = RequestMethod.POST)
	public String deleteBook(@PathVariable Long id) {
	    articleRepo.deleteById(id);
	    return "redirect:/";
	}
}
