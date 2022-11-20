package com.javaunit3.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// define as Spring component so its avail. in Spring app
@Component
public class BestMovieService {

    // define a Movie property
    // @Autowired
    private Movie movie;

    // Constructor that takes a movie as a parameter.
        // Annotate the method so that Spring will use
        // the constructor to inject a Movie object.
    @Autowired
    public BestMovieService(@Qualifier("titanicMovie") Movie movie) {
        this.movie = movie;
    }

    // method- return a Movie
    public Movie getBestMovie() {
        return movie;
    }

    // create a setter that takes a Movie object as input
        // annotate to inject the Movie object
//    @Autowired
//    public void setMovie(Movie movie) {
//        this.movie = movie;
//    }

}
