package romulofranc0.movie_tracker.application.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import romulofranc0.movie_tracker.application.models.responses.MovieResponse;
import romulofranc0.movie_tracker.application.models.responses.SearchMovieResponse;
import romulofranc0.movie_tracker.domain.services.OmdbService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {

    private final OmdbService omdbService;

    @GetMapping("/search")
    public ResponseEntity<List<SearchMovieResponse>> searchMovie(@RequestParam String title) {
        return ResponseEntity.ok(omdbService.search(title));
    }

    @GetMapping()
    public ResponseEntity<MovieResponse> getMovie(@RequestParam String imdbId) {
        MovieResponse movieResponse = omdbService.getMovie(imdbId);
        return ResponseEntity.ok(movieResponse);
    }

}
