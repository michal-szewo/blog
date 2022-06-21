package pl.edu.pw.blog.web;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.edu.pw.blog.data.Article;
import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.Comments;


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
 * @author Viacleslav
 *
 */


@Controller
@SessionAttributes("user")
public class CommentsController{
	
	@Autowired
	private ArticleRepository artRepo;

	Logger log = LoggerFactory.getLogger(ArticlesController.class);
	
    @PostMapping("/addComment")
    public String addComment(@ModelAttribute("newComment") Comments comment,
                             Model model,
                             RedirectAttributes redirectAttributes
    ) {

        
    	Article commentedArticle = comment.getArticle(); 
    	List<Comments> commentsList = commentedArticle.getComments();
    	
    	commentsList.add(comment);
    	
    	artRepo.save(commentedArticle);
    	
    	model.addAttribute("article",commentedArticle);
    	
        log.warn("article: " + comment.getArticle());
        log.warn("author: " + comment.getUser());
       
      return "redirect:/";
    }





}
