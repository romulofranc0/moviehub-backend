package romulofranc0.movie_tracker.domain.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import romulofranc0.movie_tracker.application.mappers.MovieMapper;
import romulofranc0.movie_tracker.application.mappers.ReviewMapper;
import romulofranc0.movie_tracker.application.models.requests.CommentRequest;
import romulofranc0.movie_tracker.application.models.requests.ReviewRequest;
import romulofranc0.movie_tracker.application.models.responses.MovieResponse;
import romulofranc0.movie_tracker.application.models.responses.ReviewResponse;
import romulofranc0.movie_tracker.domain.entities.Movie;
import romulofranc0.movie_tracker.domain.entities.Review;
import romulofranc0.movie_tracker.domain.entities.ReviewComment;
import romulofranc0.movie_tracker.domain.entities.User;
import romulofranc0.movie_tracker.domain.exceptions.MovieNotFoundException;
import romulofranc0.movie_tracker.domain.exceptions.ReviewAlreadyExistsException;
import romulofranc0.movie_tracker.domain.exceptions.ReviewNotFoundException;
import romulofranc0.movie_tracker.domain.exceptions.UserNotFoundException;
import romulofranc0.movie_tracker.infra.repositories.MovieRepository;
import romulofranc0.movie_tracker.infra.repositories.ReviewCommentRepository;
import romulofranc0.movie_tracker.infra.repositories.ReviewRepository;
import romulofranc0.movie_tracker.infra.repositories.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final OmdbService omdbService;
    private final ReviewMapper reviewMapper;
    private final MovieMapper movieMapper;

    @Transactional
    public Review createReview(ReviewRequest reviewRequest) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        final int MAX_COMMENT_LENGTH = 500;

        if (reviewRequest.rating() > 10 || reviewRequest.rating() < 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        if (reviewRequest.reviewText() != null && reviewRequest.reviewText().length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("Comment cannot exceed " + MAX_COMMENT_LENGTH + " characters");
        }
        if (!reviewRepository.existsReviewByMovieImdbIDAndUserId(reviewRequest.imdbId(), user.getId())) {

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
                    .watchDate(reviewRequest.watchDate() != null ? reviewRequest.watchDate() : null)
                    .build();

            reviewRepository.save(review);

            return review;
        } else {
            throw new ReviewAlreadyExistsException("review already exists");
        }
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("review not found"));
        Movie movie = movieRepository.findByImdbId(review.getMovie().getImdbID())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        MovieResponse movieResponse = movieMapper.toMovieResponse(movie);
        return new ReviewResponse(
                review.getId(),
                review.getMovie().getImdbID(),
                review.getRating(),
                review.getReviewText(),
                review.getWatchDate(),
                movieResponse);
    }

    public ReviewResponse getReviewByImdbId(String imdbId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Review review = reviewRepository.findByMovieImdbIDAndUserId(imdbId, user.getId())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        return reviewMapper.toReviewResponse(review);
    }

    public Boolean reviewsExists(String imdbId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var username = auth.getName();

        Long userId = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();

        return reviewRepository.existsByMovieImdbIDAndUserId(imdbId, userId);
    }

    @Transactional
    public void updateReview(ReviewRequest reviewRequest) {
        Review review = reviewRepository
                .findById(reviewRequest.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (reviewRequest.reviewText() != null)
            review.setReviewText(reviewRequest.reviewText());
        if (reviewRequest.rating() != null)
            review.setRating(reviewRequest.rating());
        if (reviewRequest.watchDate() != null)
            review.setWatchDate(reviewRequest.watchDate());
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewResponse> getAllReviews() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return reviewRepository.findAllByUserId(user.getId()).stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getMovie().getImdbID(),
                        review.getRating(),
                        review.getReviewText(),
                        review.getWatchDate(),
                        movieMapper.toMovieResponse(movieRepository
                                .findByImdbId(review
                                        .getMovie()
                                        .getImdbID())
                                .orElseThrow(() -> new MovieNotFoundException("Movie not found")))

                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createComment(CommentRequest commentRequest) {
        String commenterUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(commenterUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Review review = reviewRepository.findById(commentRequest.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException("review not found"));

        ReviewComment reviewComment = new ReviewComment();

        reviewComment.setCommentText(commentRequest.comment());
        reviewComment.setUser(user);
        reviewComment.setReview(review);

        reviewCommentRepository.save(reviewComment);

    }
}
