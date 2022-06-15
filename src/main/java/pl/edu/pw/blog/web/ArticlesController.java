package pl.edu.pw.blog.web;


import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import pl.edu.pw.blog.AjaxDBChange;
import pl.edu.pw.blog.Article;
import pl.edu.pw.blog.User;
import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.RefineResults;
import pl.edu.pw.blog.data.UserRepository;

/**
 * Controller for the home page
 * 
 * @author michal
 *
 */

@Controller
@SessionAttributes("user")
public class ArticlesController{
	Logger log = LoggerFactory.getLogger(ArticlesController.class);

	@Autowired
	ServletContext context;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ArticleRepository articleRepo;
	
	
	@ModelAttribute(name = "newArticle")
	public Article article() {
		return new Article();
	}
	@ModelAttribute("user")
	public User readUser(@AuthenticationPrincipal User user) {
		return user;
	}
	@ModelAttribute("refine")
	public RefineResults refineResults() {
		return new RefineResults();
	}

	@GetMapping("/")
	public String showArticles(
			@RequestParam(name="sortBy") Optional<String> sort,
			@RequestParam(name="authorFilter") Optional<String> authorFilter,
			@ModelAttribute(name="refine") RefineResults refine,
			Model model, HttpSession session){
		
			//User defaultAuthor = (User) model.getAttribute("user");
			
			String sortBy= null,authorName = null;
			
			
			
			model.addAttribute("optionsList",refine.getOptionsList());
			model.addAttribute("authorsList",userRepo.findAllAuthors());
			
			if (sort.isPresent()) {
				session.setAttribute("sortBy", sort.get());
				sortBy = sort.get();
				
				
				//String[] transformed = sortBy.split(",");
				//log.info("transformed" + transformed[0] + ", " + transformed[1]);
				// String sortDir = "desc";
				// Sort sort1 = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
				// Iterable<Article> sortedArticles = articleRepo.findAll(sort1);
				 
				 
			} else {
				if(session.getAttribute("sortBy")==null) {
					session.setAttribute("sortBy", "publishedAt,desc"); 
					sortBy = "publishedAt,desc";
				} else {
					sortBy = (String) session.getAttribute("sortBy");
					
				}
			}
			model.addAttribute("selectedSort",sortBy);
			
			
			
			if (authorFilter.isPresent()) {
				if (!authorFilter.get().equals("")) {
				
				session.setAttribute("authorName", userRepo.findByUsername(authorFilter.get()).getUsername());
				authorName = userRepo.findByUsername(authorFilter.get()).getUsername();
				} else {
					session.setAttribute("authorName", "");
					authorName="";
				}
			} else {
				if(session.getAttribute("authorName")==null) {
					session.setAttribute("authorName", ""); 
					authorName = "";
				} else {
					authorName = (String) session.getAttribute("authorName");
				}
			}
			model.addAttribute("selectedAuthor",authorName);
			
			String[] sortTransformed = sortBy.split(",");
			
			
			String sortDir = sortTransformed[1];
			Sort sortFinal = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortTransformed[0]).ascending() : Sort.by(sortTransformed[0]).descending();
		
			if (authorName.equals(""))
				//model.addAttribute("articles", articleRepo.findAll(Sort.by(Sort.Direction.DESC,sortBy)));
				model.addAttribute("articles", articleRepo.findAll(sortFinal));
			else {
				model.addAttribute("articles", articleRepo.findByAuthorUsername(authorName,sortFinal));
			}
			
			
			
			model.addAttribute("maxModifiedDate",articleRepo.findMaxModifiedDate().isEmpty() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime());
		
			return "articles";
	}
	
	@RequestMapping(value="/refreshArticles", method=RequestMethod.GET)
	public String refreshArticles(
			Model model, HttpSession session) {
		
		
		
			String sortBy = (String) session.getAttribute("sortBy");
			
			
			String authorName = (String) session.getAttribute("authorName");
			
			String[] sortTransformed = sortBy.split(",");
			String sortDir = sortTransformed[1];
			Sort sortFinal = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortTransformed[0]).ascending() : Sort.by(sortTransformed[0]).descending();
			
		
			if (authorName.equals(""))
				//model.addAttribute("articles", articleRepo.findAll(Sort.by(Sort.Direction.DESC,sortBy)));
				model.addAttribute("articles", articleRepo.findAll(sortFinal));
			else {
				model.addAttribute("articles", articleRepo.findByAuthorUsername(authorName,sortFinal));
			}
			
			
			model.addAttribute("maxModifiedDate",articleRepo.findMaxModifiedDate().isEmpty() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime());
			
			model.addAttribute("authorsList",userRepo.findAllAuthors());
		
			return "fragments/general :: article_list";
	}

	@PostMapping("/addArticle")
	public String addArticle(@ModelAttribute("newArticle") @Valid Article article, BindingResult errors,
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
	public String deleteArticle(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes) {
		String message;
		
		if (isAuthor((User) model.getAttribute("user"),id)){
			articleRepo.deleteById(id);
			message = "Usunięto artykuł o id: ";
		} else {
			message = "Nie jesteś autorem artykułu o id: ";
			}
		
		redirectAttributes.addFlashAttribute("message",message + id);
		return "redirect:/";
	}
	
	@GetMapping(value = "/articles/edit/{id}")
	public String editArticle(@PathVariable Long id, Model model,  RedirectAttributes redirectAttributes
			
			) {
		if (isAuthor((User) model.getAttribute("user"),id)){
			
			Article article = articleRepo.findById(id)
					.orElseThrow(()-> new IllegalArgumentException("Brak artykułu o id: " + id));
			
			model.addAttribute("article", article);
			
			return "edit";
		} else {
			
			redirectAttributes.addFlashAttribute("message","Nie masz uprawnień do edycji artykułu id: " + id);
			return "redirect:/";
			
			}
		

		
	}
	
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "modify")
	public String modifyArticle(@ModelAttribute("article") @Valid Article article, BindingResult errors, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		
		if (!isAuthor((User) model.getAttribute("user"),id)){
			redirectAttributes.addFlashAttribute("message","Nie masz uprawnień do edycji artykułu id: " + id);
			return "redirect:/";
		}
		
		Article modifiedArticle = articleRepo.findById(id).get();
		model.addAttribute("mArticle",modifiedArticle);
		if (errors.hasErrors()) {
			/*
			 * redirectAttributes.addFlashAttribute(
			 * "errorMessage","Nie udało się zmodyfikować artykułu:");
			 * redirectAttributes.addFlashAttribute("errors",errors.getAllErrors());
			 */
			model.addAttribute("errorMessage","Nie udało się zmodyfikować artykułu:");
			
			model.addAttribute("errors",errors.getAllErrors());
			
			
			//return "redirect:/articles/edit/"+modifiedArticle.getId();
			return "edit";
		  }

			
			modifiedArticle.setTitle(article.getTitle());
			modifiedArticle.setBody(article.getBody());
			articleRepo.save(modifiedArticle);
			redirectAttributes.addFlashAttribute("message","Artykuł zmodyfikowano.");
			return "redirect:/articles/edit/"+modifiedArticle.getId();
			
		
		
	}
	
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "delete")
	public String modifyDeleteArticle(@PathVariable Long id) {
		return "forward:/articles/delete/"+id;
	}
	
	
	
	//Ajax Call
	@PostMapping(value="/likes_update")
	public String sendNewLCard(Model model, @RequestParam(name = "id") Long id, @RequestParam(name = "liked") boolean isLiked) {
	    
		Article article;
		try {
		article = articleRepo.findById(id)
				.orElseThrow(()-> new Exception("Brak artykułu o id: " + id));
			
		} catch (Exception e) {
			log.error("Brak artykułu o id: " + id);
			return "fragments/newlcard :: lcard";
		}
		User user = (User) model.getAttribute("user");
		if(!isLiked) {
			article.addLike(user);
			} else {
			article.removeLike(user);	
			}
		articleRepo.save(article);
		
		model.addAttribute("article",article);
		model.addAttribute("user",user);
		model.addAttribute("isLiked",!isLiked);
		
	    return "fragments/newlcard :: lcard";
	}
	
	@RequestMapping(value="/dbChanges", method=RequestMethod.GET,
            produces="application/json")
	public @ResponseBody AjaxDBChange ArticlesChanges() {
		
		Iterable<Article> articles = articleRepo.findAll();
		Set<User> authorsList = userRepo.findAllAuthors();
		
		List<String> authorsNamesList = authorsList.stream().map(User::getUsername).collect(Collectors.toList());
		
		Long countLikes = 0L;
		for (Article article : articles) {
			countLikes += article.likeCount();
		}
		Long maxModifiedAt = articleRepo.findMaxModifiedDate().isEmpty() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime();
		
		
		return new AjaxDBChange(articleRepo.count(), countLikes, maxModifiedAt, authorsNamesList);
	}
	
	private boolean isAuthor(@AuthenticationPrincipal User user, Long article_id) {
		User authUser = user;
		User author = articleRepo.findById(article_id).get().getAuthor();
		if (authUser.equals(author)) return true;
		
		return false;
	}
	
	/*
	 * @GetMapping(value="resetRefineResults") public String
	 * resetResults(HttpSession session) { session.removeAttribute("sortBy");
	 * session.removeAttribute("authorName"); return "/"; }
	 */
	
	
	
	
	
	

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
