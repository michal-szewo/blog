package pl.edu.pw.blog.data;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.pw.blog.Article;
import pl.edu.pw.blog.Comments;
import pl.edu.pw.blog.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository bean providing sophisticated CRUD functionality for managing Comments entities. CrudRepository interface makes it possible to
 * retrieve entities using the sorting abstraction.
 *
 * @author Viaceslav
 *
 */

public interface CommentRepository extends CrudRepository<Comments, Long> {

//    Comments findByUsername(Optional<String> name);
//    Comments findByUsername(String name);
//
//    User findById(Optional<Long> id);
//
//
//    @Query("select u from Comments u join u.articles a order by u.username")
//    Set<User> findAllAuthors();




}
