package com.javaunit3.springmvc;

import com.javaunit3.springmvc.model.MovieEntity;
import com.javaunit3.springmvc.model.VoteEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Annotate so Spring recognizes it as a controller
@Controller
public class MovieController {

    // create field object
    @Autowired   // takes care of server side. when called, it's ready to go already initialized on line 32.
    private BestMovieService bestMovieService;


    // use field injection to set a private SessionFactory variable.
    // This variable will be set by the bean we defined earlier for SessionFactory in HibernateConfig.java file
    @Autowired
    private SessionFactory sessionFactory;


    // Annotate as requestMapping so Spring knows which page to load
    @RequestMapping("/")
    public String getIndexPage() {

        System.out.println("testing print line in console");

        // return index.HTML page
        return "index";
    }

    // Annotate as requestMapping so Spring knows which page to load
    // use Spring Model parameter -- model is used for data related logic/activity to the html page
    @RequestMapping("/bestMovie")   // path used
    public String getBestMoviePage(Model model){

        // OLD CODE
        // In getBestMovie(), add an attribute to the model named “BestMovie”,
        // parameters: ( attribute name, attribute value)
            // and use the bestMovieService object to set it to the best movie’s title.
        // model.addAttribute("BestMovie", bestMovieService.getBestMovie().getTitle());


        // NEW CODE
        // Modify the getBestMoviePage() method in the MovieController
        // to get the movie with the most votes from the database.
        // Populate attributes in the model for the best movie title and voters.
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // get movieEntity objects out from DB
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        // sort the movieEntityList by votes size
        movieEntityList.sort(Comparator.comparing(movieEntity -> movieEntity.getVotes().size()));

        // use array index to get the last movieEntity in the list (the one with most votes)
        MovieEntity movieWithMostVotes = movieEntityList.get(movieEntityList.size()-1);

        System.out.println(movieEntityList);
        System.out.println(movieWithMostVotes);

        // create new array list
        List<String> voterNames = new ArrayList<>();

        // loop to add voter name
        for (VoteEntity vote: movieWithMostVotes.getVotes()) {
            voterNames.add(vote.getVoterName());
        }

        // join the voter names
        String voterNameList = String.join(", " , voterNames);


        System.out.println(voterNameList);


        // add model attributes to show on HTML page
        model.addAttribute("bestMovie" , movieWithMostVotes.getTitle());
        model.addAttribute("bestMovieVoters" , voterNameList );

        // commit the session
        session.getTransaction().commit();

        // return bestMovie.HTML page
        return "bestMovie";
    }

    // create a new method with the request mapping of “voteForBestMovieForm”.
    // This method will simply return the String “voteForBestMovie”, allowing
    // us to LOAD PAGE http://localhost:8080/voteForBestMovie when we run
    // our server.
    @RequestMapping("/voteForBestMovieForm")
    public String voteForBestMoviePageForm(Model model) {

        // get a list of all the movie entities in the database -- use Session
        // then populate a “movies” attribute in the model with the list.
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
            // get data from database, add to a List. Data saved was a MovieEntity objects
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();

        session.getTransaction().commit();

        model.addAttribute("movies" , movieEntityList);   // attribute name, attr. value

        // return voteForTheBestMovie.HTML page
        return "voteForTheBestMovie";
    }

    // create a new method with the request mapping of “/voteForBestMovie”.
    // This method will HANDLE THE FORM DATA submitted by users.
        // Form ACTION = "voteForBestMovie"
        // Form METHOD = GET
    @RequestMapping("/voteForBestMovie")  // Form Action path
    public String voteForBestMovie( HttpServletRequest request, Model model ){
                                    // read data from HTML

        // OLD CODE
        // Get the submitted movie title from the request, and add it to the model.
            // Form-input NAME = "movieTitle"
        // String movieTitle = request.getParameter("movieTitle");

        // add request to the model -- attribute name is BestMovieVote which is used in HTML
        // model.addAttribute("BestMovieVote" , movieTitle );


        // NEW CODE
//        Modify voteForBestMovie() in the MovieController class to:
//        Get the voter name and movie id from the request
        String movieId = request.getParameter("movieId");
        String voterName = request.getParameter("voterName");

        // start hibernate session
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

//      Using the movie id grabbed from the parameter, get the existing movie object from the database
        MovieEntity movieEntity = (MovieEntity) session.get(MovieEntity.class , Integer.parseInt(movieId));

//      Add a new vote to the movie
        VoteEntity newVote = new VoteEntity(); // create new VoteEntity Object
        newVote.setVoterName(voterName);       // set voterName in VoteEntity object just created
        movieEntity.addVote(newVote);          // add the vote to the movieEntity that we took from the database using addVote (which adds to the vote List)

//      Save the changes
        session.update(movieEntity);            // update the movie entity object just updated with the new vote
        session.getTransaction().commit();

        // Return “voteForBestMovie” so that view is loaded when the form is submitted.
        return  "voteForTheBestMovie";
    }

    // create a method addMovieForm with the request mapping “/addMovieForm”.
    @RequestMapping("/addMovieForm")
    public String addMovieForm(){
        return "addMovie";  // return “addMovie” to direct to the addMovie.html view.
    }

    // create a method addMovie with the request mapping “addMovie”.
            // Form action = "addMovie" path and Form method = "POST"
    @RequestMapping("/addMovie")
    public String addMovie(HttpServletRequest request){

        // In this method, get the movie title, maturity rating,
        // and genre from the request and assign them to local variables.
        String movieTitle = request.getParameter("movieTitle");
        String maturityRating = request.getParameter("maturityRating");
        String genre = request.getParameter("genre");

        // Create new MovieEntity object, and set all the values appropriately.
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(movieTitle);
        movieEntity.setGenre(genre);
        movieEntity.setMaturityRating(maturityRating);

        // Using the session factory we injected, get a session object.
            // Use it to begin a transaction, save the MovieEntity object,
            // and then commit the transaction.
            // save user info to database
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(movieEntity);
        session.getTransaction().commit();

        return "addMovie";
    }

}
