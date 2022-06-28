package pl.edu.pw.blog.data;


import java.io.Serializable;
import java.util.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A JPA entity class having the ability to represent Comments' objects in the database.
 *
 * @author Viaceslav
 *
 */
@Getter
@Setter
@Entity
@Table(name="COMMENTS")
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comments implements Serializable{


    // Definicja zmiennych globalnych


    
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat (pattern="yyyy-MM-dd HH:mm:ss")
    @Column(nullable=false,updatable=false) // updatable=false -> gwarantuje niezmiennosc Daty i zmiennych w Hibernate
    private Date publishedAt;

    @NonNull
    @Column(length=65000,nullable=false)
    private String body;


    @ToString.Exclude
    @NonNull
    @ManyToOne
    private User user;


    @ToString.Exclude
    @NonNull
    @ManyToOne
    private Article article;



    @PrePersist
    void publishedAt() {
        this.publishedAt = new Date();  // budowana przez Hibernate data obecna utowrzenia komejtarza iona jest niezmienna

    }




}
