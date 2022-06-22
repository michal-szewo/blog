package pl.edu.pw.blog.data;


import java.io.Serializable;
import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A JPA entity class having the ability to represent Comments' objects in the database.
 *
 * @author Viaceslav
 *
 */
@Data
@Entity
@Table(name="COMMENTS")
@RequiredArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable{


    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
    @Column(nullable=false,updatable=false)
    private Date publishedAt;

    @NonNull
    @Column(length=10000,nullable=false)
    private String body;


    @ToString.Exclude
    @NonNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="author_id", nullable=false)
    private User user;


    @ToString.Exclude
    @NonNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="article_id", nullable=false)
    private Article article;



    @PrePersist
    void publishedAt() {
        this.publishedAt = new Date(); 
    }



}
