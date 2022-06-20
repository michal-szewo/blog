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

import pl.edu.pw.blog.data.AjaxDBChange;
import pl.edu.pw.blog.data.Article;
import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.User;
import pl.edu.pw.blog.data.UserRepository;

/**
 * Controller handling articles' entities (insertions, modifications, removals).
 * <p>
 * Views involved: 
 * <ul>
 * <li>fragments/general.html</li>
 * <li>fragments/newlcard.html</li>
 * <li>articles.html</li>
 * <li>edit.html</li>
 * </ul>
 * <p>
 * Repositories involved:
 * <ul>                  
 * <li>UserRepository</li>
 * <li>ArticleRepository</li>    
 * </ul>                 
 * @author Michal
 *
 */
@Controller
@SessionAttributes("user")
public class ArticlesController{
	
	
	Logger log = LoggerFactory.getLogger(ArticlesController.class);

	@Autowired
	ServletContext context;
	
	/**
	 * Injecting UserRepository bean into the controller.
	 */
	@Autowired
	UserRepository userRepo;
	
	/**
	 * Injecting ArticleRepository bean into the controller.
	 */
	@Autowired
	ArticleRepository articleRepo;
	
	
	
	/**
	 * @param user Authenticated user
	 * @return Model attribute of the authenticated user.
	 */
	@ModelAttribute("user")
	public User readUser(@AuthenticationPrincipal User user) {
		return user;
	}
	
	
	
	
	/**
	 * Method used for displaying main page.
	 * 
	 * @param sort Request param defining sorting order of the displayed list of articles.
	 * @param authorFilter Request param defining method for filtering the displayed list of articles.
	 * @param article Model attribute for bean validation while adding a new article.
	 * @param refine Model attribute for displaying sorting options.
	 * @param model Model object providing access to all the model attributes.
	 * @param session HttpSession object providing access to all the session attributes.
	 * @return Main page including the list of articles, sorting and filtering options and form for adding new articles.
	 * 
	 * @author Michal
	 */
	@GetMapping("/")
	public String showArticles(
			@RequestParam(name="sortBy") Optional<String> sort,
			@RequestParam(name="authorFilter") Optional<String> authorFilter,
			@ModelAttribute(name="newArticle") Article article,
			@ModelAttribute(name="refine") RefineResults refine,
			Model model, HttpSession session){
		
			
			
			String sortBy= null,authorName = null;
			
			
			
			model.addAttribute("optionsList",refine.getOptionsList());
			model.addAttribute("authorsList",userRepo.findAllAuthors());
			
			if (sort.isPresent()) {
				session.setAttribute("sortBy", sort.get());
				sortBy = sort.get();
				
				
				
				 
				 
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
				
				model.addAttribute("articles", articleRepo.findAll(sortFinal));
			else {
				model.addAttribute("articles", articleRepo.findByAuthorUsername(authorName,sortFinal));
			}
			
			
			
			model.addAttribute("maxModifiedDate",!articleRepo.findMaxModifiedDate().isPresent() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime());
		
			return "articles";
	}
	
	/**
	 * Method used in conjunction with {@link #articlesChanges() articlesChanges()} method and Ajax calls from articles.html page
	 * 
	 * @param model Model object providing access to all the model attributes
	 * @param session HttpSession object providing access to all the session attributes
	 * @return Fragment of the main page containing the refreshed list of articles after database changes
	 * 
	 * @author Michal
	 */
	@RequestMapping(value="/refreshArticles", method=RequestMethod.GET)
	public String refreshArticles(
			Model model, HttpSession session) {
		
		
		
			String sortBy = (String) session.getAttribute("sortBy");
			
			
			String authorName = (String) session.getAttribute("authorName");
			
			String[] sortTransformed = sortBy.split(",");
			String sortDir = sortTransformed[1];
			Sort sortFinal = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortTransformed[0]).ascending() : Sort.by(sortTransformed[0]).descending();
			
		
			if (authorName.equals(""))
				
				model.addAttribute("articles", articleRepo.findAll(sortFinal));
			else {
				model.addAttribute("articles", articleRepo.findByAuthorUsername(authorName,sortFinal));
			}
			
			
			model.addAttribute("maxModifiedDate",!articleRepo.findMaxModifiedDate().isPresent() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime());
			
			model.addAttribute("authorsList",userRepo.findAllAuthors());
		
			return "fragments/general :: article_list";
	}

	/**
	 * Method aimed at processing an attempt to add a new article into database.
	 * 
	 * @param article Model attribute for bean validation while adding a new article.
	 * @param errors Errors that occur during the validation of a new article
	 * @param model Model object providing access to all the model attributes.
	 * @param redirectAttributes Attributes available after post redirection to another page
	 * @return Main page after an attempt to add a new article
	 * 
	 * @author Michal
	 */
	@PostMapping("/addArticle")
	public String addArticle(@ModelAttribute("newArticle") @Valid Article article, BindingResult errors,
			Model model,
			RedirectAttributes redirectAttributes
			) {

		if (errors.hasErrors()) {
			 redirectAttributes.addFlashAttribute("errorMessage","Nie udało się dodać artykułu:");
			 redirectAttributes.addFlashAttribute("errors",errors.getAllErrors()); 
			 redirectAttributes.addFlashAttribute("newArticle",article);
			return "redirect:/";
		  }
		
		article.setAuthor((User) model.getAttribute("user"));
		articleRepo.save(article);
		redirectAttributes.addFlashAttribute("message","Dodano artykuł o id: "+ article.getId());
		return "redirect:/";
	}

	/**
	 * Method used by authors to delete their articles
	 * 
	 * @param id ID of deleted article.
	 * @param model Model object providing access to all the model attributes.
	 * @param redirectAttributes Attributes available after post redirection to another page.
	 * @return Main page after an attempt do delete article.
	 * 
	 * @author Michal
	 */
	@PostMapping(value = "/articles/delete/{id}")
	public String deleteArticle(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes) {
		
		
		if (isAuthor((User) model.getAttribute("user"),id)){
			articleRepo.deleteById(id);
			redirectAttributes.addFlashAttribute("message","Usunięto artykuł o id: " + id);
		} else {
			redirectAttributes.addFlashAttribute("errorMessage","Nie jesteś autorem artykułu o id: " + id);
			}
		
		
		return "redirect:/";
	}
	
	/**
	 * 
	 * Method displaying edit page of a chosen article.
	 * @param id ID of edited article.
	 * @param model Model object providing access to all the model attributes.
	 * @param redirectAttributes Attributes available after post redirection to another page.
	 * @return Edit page if authenticated user is an author of a chosen article or main page otherwise.
	 * 
	 * @author Michal
	 */
	@GetMapping(value = "/articles/edit/{id}")
	public String editArticle(@PathVariable Long id, Model model,  RedirectAttributes redirectAttributes
			
			) {
		if (isAuthor((User) model.getAttribute("user"),id)){
			
			Article article = articleRepo.findById(id)
					.orElseThrow(()-> new IllegalArgumentException("Brak artykułu o id: " + id));
			
			model.addAttribute("article", article);
			
			return "edit";
		} else {
			
			redirectAttributes.addFlashAttribute("errorMessage","Nie masz uprawnień do edycji artykułu id: " + id);
			return "redirect:/";
			
			}
		

		
	}
	
	/**
	 * Method invoked as a result of submission of edit form (edit.html).
	 * 
	 * @param article Article object used for bean validation.
	 * @param errors Errors that occur during the validation of an article.
	 * @param id ID of edited article.
	 * @param model Model object providing access to all the model attributes.
	 * @param redirectAttributes Attributes available after post redirection to another page.
	 * @return Edit page if user is authorize to modify an article or main page otherwise.
	 * 
	 * @author Michal
	 */
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "modify")
	public String modifyArticle(@ModelAttribute("article") @Valid Article article, BindingResult errors, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		
		if (!isAuthor((User) model.getAttribute("user"),id)){
			redirectAttributes.addFlashAttribute("errorMessage","Nie masz uprawnień do edycji artykułu id: " + id);
			return "redirect:/";
		}
		
		Article modifiedArticle = articleRepo.findById(id).get();
		model.addAttribute("mArticle",modifiedArticle);
		if (errors.hasErrors()) {
			
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
	
	
	/**
	 * Method invoked from edit page when user attempts to delete article. 
	 * 
	 * @param id Id of deleted article.
	 * @return Forward reference to {@link #deleteArticle(Long, Model,RedirectAttributes) deleteArticle()} method.
	 * 
	 * @author Michal
	 */
	@RequestMapping(value="/modifyArticle/{id}", method = RequestMethod.POST, params = "delete")
	public String modifyDeleteArticle(@PathVariable Long id) {
		return "forward:/articles/delete/"+id;
	}
	
	
	
	/**
	 * Method designed to refresh "likes" pane within article card on the main page after pressing like/dislike button.
	 * Used in conjunction with ajax call from articles.html page.
	 * 
	 * @param model Model object providing access to all the model attributes.
	 * @param id Id of liked/disliked article.
	 * @param isLiked Boolean value indicating whether article is liked or not by an authenticated user.
	 * @return Fragment of article card within main page that displays "likes" pane.
	 * 
	 * @author Michal
	 */
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
	
	/**
	 * Rest method used in conjunction with {@link #refreshArticles(Model, HttpSession) refreshArticles()} method to detect changes in the database and dynamically reload fragment of articles page, if a change occurs.
	 * 
	 * @return {@link pl.edu.pw.blog.data.AjaxDBChange AjaxDBChange} object containing refreshed sizes of articles and likes table along with the newest modificationAt date and refreshed authors names list.  
	 * 
	 * @author Michal
	 */
	@RequestMapping(value="/dbChanges", method=RequestMethod.GET,
            produces="application/json")
	public @ResponseBody AjaxDBChange articlesChanges() {
		
		Iterable<Article> articles = articleRepo.findAll();
		Set<User> authorsList = userRepo.findAllAuthors();
		
		List<String> authorsNamesList = authorsList.stream().map(User::getUsername).collect(Collectors.toList());
		
		Long countLikes = 0L;
		for (Article article : articles) {
			countLikes += article.likeCount();
		}
		Long maxModifiedAt = !articleRepo.findMaxModifiedDate().isPresent() ? new Date().getTime(): articleRepo.findMaxModifiedDate().get().getTime();
		
		
		return new AjaxDBChange(articleRepo.count(), countLikes, maxModifiedAt, authorsNamesList);
	}
	
	/**
	 * Auxiliary method used to check if an authenticated user is authorized to perform post action such as deleting, modifying article.  
	 * 
	 * @param user Authenticated user.
	 * @param article_id Id of a chosen article.
	 * @return True if user is an author of a chosen article or false otherwise.
	 * 
	 * @author Michal
	 */
	private boolean isAuthor(@AuthenticationPrincipal User user, Long article_id) {
		User authUser = user;
		User author = articleRepo.findById(article_id).get().getAuthor();
		if (authUser.equals(author)) return true;
		
		return false;
	}
	
	
	
	
}
