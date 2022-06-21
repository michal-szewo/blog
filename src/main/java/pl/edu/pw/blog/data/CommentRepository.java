package pl.edu.pw.blog.data;


import org.springframework.data.repository.CrudRepository;




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
