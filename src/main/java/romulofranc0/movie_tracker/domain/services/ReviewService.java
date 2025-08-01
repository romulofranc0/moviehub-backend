package romulofranc0.movie_tracker.domain.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import romulofranc0.movie_tracker.application.models.requests.CommentRequest;
import romulofranc0.movie_tracker.application.models.requests.ReviewRequest;
import romulofranc0.movie_tracker.application.models.responses.ClientMovieResponse;
import romulofranc0.movie_tracker.application.models.responses.MovieResponse;
import romulofranc0.movie_tracker.application.models.responses.ReviewResponse;
import romulofranc0.movie_tracker.domain.entities.ReviewComment;
import romulofranc0.movie_tracker.domain.entities.User;
import romulofranc0.movie_tracker.domain.entities.Movie;
import romulofranc0.movie_tracker.domain.entities.Review;
import romulofranc0.movie_tracker.domain.exceptions.ReviewAlreadyExistsException;
import romulofranc0.movie_tracker.domain.exceptions.ReviewNotFoundException;
import romulofranc0.movie_tracker.domain.exceptions.UserNotFoundException;
import romulofranc0.movie_tracker.infra.repositories.MovieRepository;
import romulofranc0.movie_tracker.infra.repositories.ReviewCommentRepository;
import romulofranc0.movie_tracker.infra.repositories.ReviewRepository;
import romulofranc0.movie_tracker.infra.repositories.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final OmdbService omdbService;

    @Transactional
    public Review createReview(ReviewRequest reviewRequest){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        final int MAX_COMMENT_LENGTH = 500;

        if(reviewRequest.rating() > 10 || reviewRequest.rating() < 0){
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        if (reviewRequest.reviewText() != null && reviewRequest.reviewText().length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("Comment cannot exceed " + MAX_COMMENT_LENGTH + " characters");
        }
        if(!reviewRepository.existsReviewByMovieImdbIDAndUserId(reviewRequest.imdbId(),user.getId())) {

        MovieResponse omdbResponse = omdbService.getMovie(reviewRequest.imdbId());

        Movie movie = new Movie();
        movie.setDirector(omdbResponse.director());
        movie.setGenre(omdbResponse.genre());
        movie.setTitle(omdbResponse.title());
        movie.setYear(omdbResponse.year());
        movie.setPlot(omdbResponse.plot());
        movie.setPoster(omdbResponse.poster());
        movie.setImdbID(omdbResponse.imdbID());
        movieRepository.save(movie);

        Review review = Review.builder()
                .reviewDate(LocalDate.now())
                .user(user)
                .movie(movie)
                .comment(reviewRequest.reviewText())
                .rating(reviewRequest.rating())
                .watchDate(reviewRequest.watchDate())
                .build();

           reviewRepository.save(review);


    return review;
        }else {
            throw new ReviewAlreadyExistsException("review already exists");
        }
    }

    public ReviewResponse getReviewById(Long reviewId){
       Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException("review not found"));
        return new ReviewResponse(
                 review.getId(),
                 review.getMovie().getImdbID(),
                 review.getRating(),
                 review.getReviewText(),
                 review.getWatchDate());
    }

    @Transactional
    public void updateReview(ReviewRequest reviewRequest){
        Review review = reviewRepository
                .findByImdbIdAndUsername(reviewRequest.imdbId(), SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if(reviewRequest.reviewText() != null)review.setReviewText(reviewRequest.reviewText());
        if(reviewRequest.rating() != null)review.setRating(reviewRequest.rating());
        if(reviewRequest.watchDate() != null)review.setWatchDate(reviewRequest.watchDate());
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteById(Long reviewId){
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> new ReviewResponse(
                        review.getUser().getId(),
                        review.getMovie().getImdbID(),
                        review.getRating(),
                        review.getReviewText(),
                        review.getWatchDate()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(CommentRequest commentRequest){
        String commenterUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(commenterUsername).orElseThrow(() -> new RuntimeException("User not found"));
        Review review = reviewRepository.findById(commentRequest.reviewId()).orElseThrow(() -> new ReviewNotFoundException("review not found"));

        ReviewComment reviewComment = new ReviewComment();

        reviewComment.setCommentText(commentRequest.comment());
        reviewComment.setUser(user);
        reviewComment.setReview(review);

        reviewCommentRepository.save(reviewComment);

    }
}
