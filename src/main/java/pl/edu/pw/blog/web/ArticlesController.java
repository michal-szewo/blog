package pl.edu.pw.blog.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import pl.edu.pw.blog.Article;
import pl.edu.pw.blog.User;
import pl.edu.pw.blog.data.ArticleRepository;

/**
 * Controller for the home page
 * 
 * @author michal
 *
 */

@Controller
public class ArticlesController {
	Logger log = LoggerFactory.getLogger(ArticlesController.class);

	@Autowired
	ServletContext context;

	private ArticleRepository articleRepo;

	public ArticlesController(ArticleRepository articleRepo) {
		this.articleRepo = articleRepo;

	}

	@ModelAttribute(name = "newArticle")
	public Article article() {
		return new Article();
	}

	@GetMapping("/")
	public String showArticles(WebRequest request, Model model, @AuthenticationPrincipal User user) {

		model.addAttribute("articles", articleRepo.findAllByOrderByPublishedAtDesc());
		model.addAttribute("user", user);

		return "articles";
	}

	@PostMapping("/addArticle")
	public String addArticle(@ModelAttribute("newArticle") @Valid Article article, Errors errors,
			@AuthenticationPrincipal User user, Model model) {

		if (errors.hasErrors()) {
			  
			  return "redirect:/";
		  }

		article.setAuthor(user);
		articleRepo.save(article);

		return "redirect:/";
	}

	@PostMapping(value = "/articles/delete/{id}")
	public String deleteArticle(@PathVariable Long id) {
		articleRepo.deleteById(id);
		return "redirect:/";
	}
	
	@GetMapping(value = "/articles/edit/{id}")
	public String editArticle(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
		Article article = articleRepo.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("Brak artykułu o id: " + id));
		
		model.addAttribute("article", article);
		model.addAttribute("user", user);
		return "edit";
		
		
		
	}

	/*
	 * @RequestMapping(value="/uploadImage", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String handleTinyMCEUpload(@RequestParam("file")
	 * MultipartFile file) throws IOException{
	 * log.info("uploading______________________________________MultipartFile " +
	 * file);
	 * 
	 * 
	 * String filePath = "images/" + file.getOriginalFilename();
	 * 
	 * String result = uploadFilesFromTinyMCE(file, filePath); log.info(result);
	 * return "{\"location\":\"" + filePath + "\"}";
	 * 
	 * }
	 * 
	 * 
	 * private String uploadFilesFromTinyMCE(MultipartFile file, String path) {
	 * 
	 * try { String folder = context.getRealPath("images/");
	 * log.info("uploading______________________________________" + folder);
	 * StringBuffer result = new StringBuffer(); byte[] bytes = file.getBytes();
	 * Path destination = Paths.get("/images/"+file.getOriginalFilename());
	 * result.append("Uploading of File ");
	 * 
	 * 
	 * if (!file.isEmpty()) {
	 * 
	 * try {
	 * 
	 * 
	 * String path = ""; path = folder + file.getOriginalFilename(); File
	 * destination = new File(path); log.info("--> " + destination);
	 * file.transferTo(destination);
	 * 
	 * log.info(destination.toString()); Files.write(destination, bytes);
	 * 
	 * result.append(file.getOriginalFilename() + " Success. "); } catch (Exception
	 * e) { throw new RuntimeException("Product Image saving failed", e); }
	 * 
	 * } else result.append(file.getOriginalFilename() + " Failed. ");
	 * 
	 * 
	 * 
	 * return result.toString();
	 * 
	 * } catch (Exception e) { return "Error Occured while uploading files." +
	 * " => " + e.getMessage(); } }
	 */

}
