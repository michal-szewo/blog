package pl.edu.pw.blog.data;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.edu.pw.blog.Article;


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
	  
	}