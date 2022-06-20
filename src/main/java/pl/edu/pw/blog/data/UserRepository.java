package pl.edu.pw.blog.data;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository bean providing sophisticated CRUD functionality for managing User entities.
 * 
 * @author Michal
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

	  User findByUsername(Optional<String> name);
	  User findByUsername(String name);

	  User findById(Optional<Long> id);
	  
	  @Query("select u from User u join u.articles a order by u.username")
	    Set<User> findAllAuthors();
	  
	  
	}