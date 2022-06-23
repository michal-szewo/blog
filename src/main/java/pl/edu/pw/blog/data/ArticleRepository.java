package pl.edu.pw.blog.data;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * Repository bean providing sophisticated CRUD functionality for managing Article entities. PagingAndSortingRepository interface makes it possible to
 * retrieve entities using the sorting abstraction.
 * 
 * @author Michal
 *
 */
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

	  Article findByTitle(String title);
	  
	  Set<Article> findAll();
	  
	  Set<Article> findAllByOrderByPublishedAtDesc();
	  Set<Article> findAllByOrderByPublishedAt();
	  
	  
	  Set<Article> findAllByOrderByAuthorAsc();
	  Set<Article> findAllByOrderByAuthor();
	  
	  Set<Article> findByAuthorId(Long id);

	  Set<Article> findByAuthorId(Long authorId, Sort by);

	  Set<Article> findByAuthorUsername(String authorName, Sort by);
	  
	  @Query("select max(a.modifiedAt) from Article a")
	  Optional<Date> findMaxModifiedDate();
	  
	  
	 
	  
	}