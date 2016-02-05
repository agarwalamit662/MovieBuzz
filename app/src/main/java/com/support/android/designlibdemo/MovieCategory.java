package com.support.android.designlibdemo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amitagarwal3 on 10/12/2015.
 */
public class MovieCategory {


    public Map<Integer, String> dictionary = new HashMap<Integer, String>();

    public MovieCategory()
    {
        dictionary.put(9648,"Mystery");
        dictionary.put(10749,"Romance");
        dictionary.put(878,"Science Fiction");
        dictionary.put(10770,"TV Movie");
        dictionary.put(53,"Thriller");
        dictionary.put(10752,"War");
        dictionary.put(37,"Western");


        dictionary.put(18,"Drama");
        dictionary.put(10751,"Family");
        dictionary.put(14,"Fantasy");
        dictionary.put(10769,"Foreign");
        dictionary.put(36,"History");
        dictionary.put(27,"Horror");
        dictionary.put(10402,"Music");


        dictionary.put(28,"Action");

        dictionary.put(12,"Adventure");
        dictionary.put(16,"Animation");
        dictionary.put(35,"Comedy");
        dictionary.put(80,"Crime");
        dictionary.put(99,"Documentary");

    }

    public String dicelements(int x) {

        return dictionary.get(x);

    }
}
