package romulofranc0.movie_tracker.domain.services;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import romulofranc0.movie_tracker.application.mappers.MovieListMapper;
import romulofranc0.movie_tracker.application.models.requests.MovieListRequest;
import romulofranc0.movie_tracker.application.models.responses.MovieListResponse;
import romulofranc0.movie_tracker.domain.entities.Movie;
import romulofranc0.movie_tracker.domain.entities.MovieList;
import romulofranc0.movie_tracker.domain.entities.User;
import romulofranc0.movie_tracker.domain.exceptions.MovieAlreadyInListException;
import romulofranc0.movie_tracker.domain.exceptions.MovieListNotFoundException;
import romulofranc0.movie_tracker.domain.exceptions.MovieNotFoundException;
import romulofranc0.movie_tracker.infra.repositories.MovieListRepository;
import romulofranc0.movie_tracker.infra.repositories.MovieRepository;

@RequiredArgsConstructor
@Service
public class MovieListService {

    private final MovieListRepository movieListRepository;
    private final MovieListMapper movieListMapper;
    private final MovieRepository movieRepository;

    public Set<MovieListResponse> getAllMovieListsFromUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<MovieList> movieLists = movieListRepository.findAllByUserId(user.getId());

        return movieListMapper.toMovieListResponses(movieLists);
    }

    public MovieList createMovieList(MovieListRequest request) {

        var movieList = new MovieList();
        movieList.setListName(request.name());
        movieList.setDescription(request.description());

        return movieListRepository.save(movieList);
    }

    public MovieListResponse getMovieList(Long movieListId) {

        return movieListMapper
                .toMovieListResponse(movieListRepository
                        .findById(movieListId)
                        .orElseThrow(() -> new MovieListNotFoundException("List not found")));
    }

    public void deleteMovieListForUser(Long listId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        MovieList movieList = movieListRepository.findById(listId)
                .orElseThrow(() -> new MovieListNotFoundException("MovieList not found with id: " + listId));

        if (!movieList.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this list.");
        }

        movieListRepository.delete(movieList);
    }

    public boolean existsById(Long listId) {
        return movieListRepository.existsById(listId);
    }

    @Transactional
    public MovieListResponse addMovieToList(String imdbId, Long listId) {
        MovieList movieList = movieListRepository
                .findById(listId)
                .orElseThrow(() -> new MovieListNotFoundException("List not found with id: " + listId));
        Movie movie = movieRepository
                .findByImdbId(imdbId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with imdbId: " + imdbId));

        // DEBUG: Print what we're working with
        System.out.println("MovieList ID: " + movieList.getId());
        System.out.println("Movie IMDB ID: " + movie.getImdbID());
        System.out.println("Movies in list before: " + movieList.getMovies().size());

        // Check if movie exists using ID comparison
        boolean exists = movieList.getMovies().stream()
                .anyMatch(m -> m.getImdbID().equals(movie.getImdbID()));

        System.out.println("Movie exists in list: " + exists);

        if (!exists) {
            movieList.getMovies().add(movie);
            System.out.println("Added movie. Movies in list after: " + movieList.getMovies().size());
        } else {
            throw new MovieAlreadyInListException("This movie is already in this list");
        }

        return movieListMapper.toMovieListResponse(movieList);
    }
}
