package pl.edu.pw.blog.data;

import java.io.Serial;
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
 * A JPA entity class having the ability to represent Articles' objects in the database.
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


    // Definicja zmiennych globalnych


    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
    @Column(nullable=false,updatable=false) // updatable=false -> gwarantuje niezmienność Daty i zmiennych w Hibernate
    private Date publishedAt;

    @NonNull
    @Column(length=65000,nullable=false)
    private String body;

    // Łączenie tabeli Comments z Tabelą Users

    /**
    *adnotacja @JoinColumn(name="author_id", nullable=false) w tym przypadku połączy t
    *
    */

    //

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


//
//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
//    private Date modifiedAt;

    @PrePersist
    void publishedAt() {
        this.publishedAt = new Date();  // budowana przez Hibernate data obecna utowrzenia komejtarza iona jest niezmienna
//        this.modifiedAt = publishedAt;
    }
//    @PreUpdate
//    void modifiedAt() {
//        this.modifiedAt = new Date();
//    }

//
//    public int commentsCount(){
//        return getComments().size();
//    }

//    public void addComment(Comments comments){
//        getBody().add(comments);
//    }
//
//    public void removeComment(Comments comments){
//        getComments().remove(comments);
//    }
//
//    public boolean howManyComments() {
//        return getComments().size() > 0;
//    }
//
//    public boolean isCommentedByUser(Comments comment) {
//
//        return comments.contains(comment);
//    }



}
