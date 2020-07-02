package com.udacity.gradle.builditbigger.backend;

import java.util.ArrayList;
import java.util.List;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;

    private ArrayList<String> jokesList;

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public ArrayList<String> getJokesList() {
        return jokesList;
    }

    public void setJokesList(ArrayList<String> jokesList) {
        this.jokesList = jokesList;
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "jokesList=" + jokesList +
                '}';
    }
}