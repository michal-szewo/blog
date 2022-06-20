package pl.edu.pw.blog;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A JPA entity class having the ability to represent Articles' objects in the database.
 * 
 * @author Michal
 *
 */
@Data
@Entity
@Table(name="ARTICLES")
@RequiredArgsConstructor
@NoArgsConstructor
public class Article implements Serializable{
	
	

	// ------------ Parametry do Article----------------//

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@NotBlank(message="Podanie tytułu jest obowiązkowe")
	@Column(nullable=false)
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
	@Column(nullable=false,updatable=false)
	private Date publishedAt;
	
	@NonNull
	@NotBlank(message="Podanie treści jest obowiązkowe")
	@Column(length=65000,nullable=false)
	private String body;


	// ------------ Parametry do Article----------------//




	@ToString.Exclude
	@ManyToMany(fetch=FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable( name="LIKES",
	            joinColumns = @JoinColumn( name="article_id"),
	            inverseJoinColumns = @JoinColumn( name="user_id",unique=false)
	        )
    private Set<User> likers = new TreeSet<>();


	// Z poziomu klasy Article łączymy sie do wielu klas Comments
	// czyli tabela Article w Bazie Danych jest łączona do wielu komentarzy na swoj temat



	@OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
	private List<Comments> comments = new ArrayList<>();



	@ToString.Exclude
	@NonNull
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", nullable=false)
	private User author;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
	private Date modifiedAt;
	
	@PrePersist
	void publishedAt() {
		this.publishedAt = new Date();
		this.modifiedAt = publishedAt;
	}
	@PreUpdate
	void modifiedAt() {
		this.modifiedAt = new Date();
	}
	
	
	public int likeCount(){
	    return getLikers().size();
	}
	
	public void addLike(User u){
	    getLikers().add(u);
	}
	public void removeLike(User u){
	    getLikers().remove(u);
	}
	
	public boolean isLiked() {
		return getLikers().size() > 0;
	}
	
	public boolean isLikedByUser(User u) {
		
		if (likers.contains(u)) return true;
		return false;
	}
	
	
	
}
