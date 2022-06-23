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

	
}
