package romulofranc0.movie_tracker.domain.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import romulofranc0.movie_tracker.domain.entities.User;
import romulofranc0.movie_tracker.domain.exceptions.UserAlreadyFollowedException;
import romulofranc0.movie_tracker.infra.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void follow(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();


        User userFollowing = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
        User userFollowed = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));

        if(userFollowing.getFollowing().contains(userFollowed)){
            throw new UserAlreadyFollowedException("User already following");
        }

        userFollowing.getFollowing().add(userFollowed);
        userFollowed.getFollowers().add(userFollowing);

        userRepository.save(userFollowed);
        userRepository.save(userFollowing);
    }

    @Transactional
    public void unfollow(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User userUnfollowing = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        User userUnfollowed = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));

        userUnfollowing.getFollowing().remove(userUnfollowed);
        userUnfollowed.getFollowers().remove(userUnfollowing);

        userRepository.save(userUnfollowing);
        userRepository.save(userUnfollowed);

    }
}
