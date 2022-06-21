package pl.edu.pw.blog.web;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
