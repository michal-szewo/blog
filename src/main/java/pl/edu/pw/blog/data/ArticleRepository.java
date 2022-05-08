package pl.edu.pw.blog.data;

import java.util.Set;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.edu.pw.blog.Article;



public interface ArticleRepository extends CrudRepository<Article, Long> {

	  Article findByTitle(String title);
	  
	  Set<Article> findAllByOrderByPublishedAtDesc();
	  
	  
	  
	}