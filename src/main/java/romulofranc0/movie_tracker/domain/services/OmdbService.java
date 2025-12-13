package romulofranc0.movie_tracker.domain.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import romulofranc0.movie_tracker.application.clients.OmdbClient;
import romulofranc0.movie_tracker.application.models.responses.MovieResponse;
import romulofranc0.movie_tracker.application.models.responses.SearchMovieResponse;

@RequiredArgsConstructor
@Service
public class OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;

    private final OmdbClient omdbClient;

    public List<SearchMovieResponse> search(String title) {
        String type = "movie";
        var searchResult = omdbClient.searchMovie(title, apiKey, type);

        List<SearchMovieResponse> searchResponse = new ArrayList<>();

        searchResult.Search().forEach(movie -> {
            SearchMovieResponse searchMovieResponse = new SearchMovieResponse(
                    movie.Title(),
                    movie.Year(),
                    movie.Type(),
                    movie.imdbID(),
                    movie.Poster());
            searchResponse.add(searchMovieResponse);
        });

        return searchResponse;

    }

    public MovieResponse getMovie(String imdbId) {
        var movieResult = omdbClient.getMovie(imdbId, apiKey, "full");

        MovieResponse movieResponse = new MovieResponse(
                movieResult.Title(),
                movieResult.Director(),
                movieResult.Plot(),
                movieResult.Year(),
                movieResult.Genre(),
                movieResult.imdbID(),
                movieResult.Poster(),
                movieResult.imdbRating());
        return movieResponse;
    }
}
