package romulofranc0.movie_tracker.application.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import romulofranc0.movie_tracker.application.models.responses.ClientMovieResponse;
import romulofranc0.movie_tracker.application.models.responses.SearchResponseWrapper;

@FeignClient(url = "http://www.omdbapi.com/", name = "omdb")
public interface OmdbClient {

    @GetMapping("/")
    SearchResponseWrapper searchMovie(@RequestParam("s") String title, @RequestParam("apikey") String apiKey,
            @RequestParam("type") String type);

    @GetMapping("/")
    ClientMovieResponse getMovie(@RequestParam("i") String imdbId, @RequestParam("apikey") String apiKey,
            @RequestParam("plot") String plot);

}