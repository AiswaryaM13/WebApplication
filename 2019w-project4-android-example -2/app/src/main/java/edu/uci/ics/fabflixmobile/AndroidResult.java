package edu.uci.ics.fabflixmobile;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AndroidResult {
    private String title;
    private String year;
    private String director;
    private String rating;
    private ArrayList<String> genres= new ArrayList<>();
    private ArrayList<String> stars= new ArrayList<>();

    public AndroidResult(String title, String year, String director, String rating, ArrayList<String> genres, ArrayList<String> stars) {
        this.title = title;
        this.year = year;
        this.director=director;
        this.rating=rating;
        this.genres=genres;
        this.stars=stars;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getRating() {
        return rating;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }
    public ArrayList<String> getStars() {
        return stars;
    }
}
