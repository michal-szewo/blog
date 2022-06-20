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
import pl.edu.pw.blog.Comments;
import pl.edu.pw.blog.User;
import pl.edu.pw.blog.data.ArticleRepository;
import pl.edu.pw.blog.data.CommentRepository;
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
public class CommentsController{

    @PostMapping("/addComment")
    public String addComment(@ModelAttribute("newComment") Comments comment,
                             Model model,
                             RedirectAttributes redirectAttributes
    ) {

        comment.getArticle().getId();


        // id_artykulu , id_user
        // body
        // article.getComments
        //add(new Comments(id_artykulu,id_user,body))

//        comment.setAuthor((User) model.getAttribute("user"));
//        articleRepo.save(article);
//        redirectAttributes.addFlashAttribute("message","Dodano artykuł o id: "+ article.getId());
      return "redirect:/";
    }





}
