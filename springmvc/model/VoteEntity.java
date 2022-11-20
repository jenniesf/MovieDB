package com.javaunit3.springmvc.model;
import javax.persistence.*;

// Define it as a Hibernate entity that uses the “votes” table in database
    // this data from the user will connect to the heroku SQL database

@Entity
@Table(name = "votes")
public class VoteEntity {

    // Create a new VoteEntity class in the model folder with a
    // generated id field and a field for the voter’s name.
    // Make sure to create all of the necessary getters and setters.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "voter_name")
    private String voterName;


    // getters and setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getVoterName() {
        return voterName;
    }
    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }
}
