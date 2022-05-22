package pl.edu.pw.blog;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;



import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name="ARTICLES")
public class Article implements Serializable{
	
	public Article() {	
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NonNull
	@NotBlank(message="Podanie tytułu jest obowiązkowe")
	private String title;
	
	
	private Date publishedAt;
	
	@NonNull
	@NotBlank(message="Podanie treści jest obowiązkowe")
	@Column(length=65000)
	private String body;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="author_id", nullable=false)
	private User author;
	
	
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

	
	
	
}
