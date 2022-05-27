package pl.edu.pw.blog.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@ModelAttribute
	public void readUser(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("user", user);
	}
	

	@GetMapping("/")
	public String showArticles(Model model
			//,@AuthenticationPrincipal User user
			) {

		model.addAttribute("articles", articleRepo.findAllByOrderByPublishedAtDesc());
		//model.addAttribute("user", user);

		return "articles";
	}

	@PostMapping("/addArticle")
	public String addArticle(@ModelAttribute("newArticle") @Valid Article article, BindingResult errors,
			//@AuthenticationPrincipal User user,
			Model model,
			RedirectAttributes redirectAttributes
			) {

		if (errors.hasErrors()) {
			 redirectAttributes.addFlashAttribute("errorMessage","Nie udało się dodać artykułu");
			 redirectAttributes.addFlashAttribute("errors",errors.getAllErrors()); 
			 redirectAttributes.addFlashAttribute("newArticle",article);
			return "redirect:/";
		  }
		
		article.setAuthor((User) model.getAttribute("user"));
		articleRepo.save(article);
		redirectAttributes.addFlashAttribute("message","Dodano artykuł o id: "+ article.getId());
		return "redirect:/";
	}

	@PostMapping(value = "/articles/delete/{id}")
	public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		articleRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message","Usunięto artykuł o id: " + id);
		return "redirect:/";
	}
	
	@GetMapping(value = "/articles/edit/{id}")
	public String editArticle(@PathVariable Long id, Model model
			//,@AuthenticationPrincipal User user
			) {
		Article article = articleRepo.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("Brak artykułu o id: " + id));
		
		model.addAttribute("article", article);
		//model.addAttribute("user", user);
		return "edit";
		
	}
	
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "modify")
	public String modifyArticle(@ModelAttribute("article") @Valid Article article, BindingResult errors, @PathVariable Long id, RedirectAttributes redirectAttributes) {
		
		
		if (errors.hasErrors()) {

			return "edit";
		  }

			Article modifiedArticle = articleRepo.findById(id).get();
			modifiedArticle.setTitle(article.getTitle());
			modifiedArticle.setBody(article.getBody());
			articleRepo.save(modifiedArticle);
			redirectAttributes.addFlashAttribute("message","Artykuł zapisano");
			return "redirect:/articles/edit/"+modifiedArticle.getId();
			
		
		
	}
	
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "delete")
	public String modifyDeleteArticle(@PathVariable Long id) {
		return "forward:/articles/delete/"+id;
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
