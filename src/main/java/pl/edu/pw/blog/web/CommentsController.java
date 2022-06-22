package pl.edu.pw.blog.web;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.edu.pw.blog.data.Article;
import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.CommentRepository;
import pl.edu.pw.blog.data.Comments;
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
 * @author Viacleslav
 *
 */


@Controller
@SessionAttributes("user")
public class CommentsController{
	
	
	
	@Autowired
	private CommentRepository commRepo;
	
	@Autowired
	private ArticleRepository artRepo;
	
	@Autowired
	private UserRepository userRepo;

	Logger log = LoggerFactory.getLogger(ArticlesController.class);
	
    @PostMapping(value="/addComment")
    public String addComment(Model model,
    						@RequestParam(name="body") String body,
    						@RequestParam(name="article") Long article_id,
    						@AuthenticationPrincipal User user
    						
    ) {
    	
    	Article article = artRepo.findById(article_id).get();
    	
    	log.info("body: " + body + ", article: " + article);
    	
    	List<Comments> comments_list = article.getComments();
    	comments_list.add(new Comments(body,user,article));
    	
    	artRepo.save(article);
    	
    	model.addAttribute("article",article);
    	
      return "fragments/newcomments :: commentCard";
    }





}
