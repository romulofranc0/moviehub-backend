package romulofranc0.movie_tracker.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(username + "not found");
    }
}
