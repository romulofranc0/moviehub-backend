package romulofranc0.movie_tracker.domain.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import romulofranc0.movie_tracker.application.mappers.ReviewMapper;
import romulofranc0.movie_tracker.application.models.responses.ReviewResponse;
import romulofranc0.movie_tracker.domain.entities.User;
import romulofranc0.movie_tracker.domain.exceptions.UserNotFoundException;
import romulofranc0.movie_tracker.infra.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public Set<ReviewResponse> getUserFeed(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Set<ReviewResponse> followingReviews = new HashSet<>();

        var following = user.getFollowing();

        following.forEach(follow -> {
            followingReviews.addAll(reviewMapper.toReviewResponses(follow.getReviews()));
        });
        return followingReviews;
    }
}
