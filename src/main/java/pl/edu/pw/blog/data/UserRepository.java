package pl.edu.pw.blog.data;

import org.springframework.data.repository.CrudRepository;

import pl.edu.pw.blog.User;

public interface UserRepository extends CrudRepository<User, Long> {

	  User findByUsername(String username);
	  
	  
	}