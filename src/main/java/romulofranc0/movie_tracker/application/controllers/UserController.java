package romulofranc0.movie_tracker.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import romulofranc0.movie_tracker.domain.services.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/follow/{id}")
    public ResponseEntity<?> follow(@PathVariable Long id) {
        userService.follow(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow/{id}")
    public ResponseEntity<?> unfollow(@PathVariable Long id) {
        userService.unfollow(id);
        return ResponseEntity.ok().build();
    }
}
