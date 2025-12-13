package romulofranc0.movie_tracker.domain.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Integer rating;
    @Column(nullable = false, length = 500)
    private String reviewText;
    private LocalDate watchDate;
    private LocalDate reviewDate;

    @OneToMany(mappedBy = "review")
    private Set<ReviewComment> reviewComments;

    @Builder
    public Review(Movie movie, User user, Integer rating, String comment, LocalDate watchDate, LocalDate reviewDate) {
        this.movie = movie;
        this.user = user;
        this.rating = rating;
        this.reviewText = comment;
        this.watchDate = watchDate;
        this.reviewDate = reviewDate;
    }
}