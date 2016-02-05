/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.support.android.designlibdemo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.FetchProfilePicture;
import com.support.android.designlibdemo.data.UserProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

//import com.example.amit.tellymoviebuzzz.data.MovieContract;


public class AllDataSync extends AsyncTask<String, Void, Void> {
   // public final SharedPreferences.Editor mydetailseditor;
    public SharedPreferences.Editor myfriendseditor;
    public final SharedPreferences mydetails;
    private final String LOG_TAG = AllDataSync.class.getSimpleName();
    public MainActivity ma;

    private static final String[] FORECAST_FOLLOWERS_COLUMNS = {

            UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider._ID_FOLLOWER,
            UserProvider.FOLLOWER,
            UserProvider.FOLLOWING,
            UserProvider.NAME_FOLLOWING,
            UserProvider.IS_FOLLOWING_FOLLOWER,
            UserProvider.FOLLOWING_URL


    };


    private static final String[] FORECAST_FRIENDS_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider._ID_FRIEND,
            UserProvider.FBID_FRIEND,
            UserProvider.NAME_FRIEND,
            UserProvider.ABOUTME_FRIEND,
            UserProvider.GENRES_FRIEND,
            UserProvider.RATING_FRIEND,
            UserProvider.URLPICTURE_FRIEND,
            UserProvider.IS_FOLLOWING


    };

    private static final String[] FORECAST_MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._ID_MOVIE,
            UserProvider._MOVIE_ID,
            UserProvider._MOVIE_NAME,
            UserProvider._MOVIE_RD,
            UserProvider._MOVIE_WATCHER,
            UserProvider._MOVIE_DES,
            UserProvider._MOVIE_URL,
            UserProvider._MOVIE_ISWATCHED,
            UserProvider._MOVIE_LIKES,
            UserProvider._MOVIE_ISWATCHLIST,
            UserProvider._MOVIE_CATEGORY,
            UserProvider._MOVIE_RATING,
            UserProvider._MOVIE_ISREVIEWED,
            UserProvider._MOVIE_REVIEW,
            UserProvider._MOVIE_ON_PARSE,
            UserProvider._MOVIE_REVIEW_TIME

    };
    private final Context mContext;
    public  String aboutme_friend;
    public  String genres_friend;
    public String rating_friend;
    public String picture_friend;

    public AllDataSync(Context context) {
        mContext = context;
        ma = (MainActivity) mContext;
        //ma = mf;
        mydetails = mContext.getSharedPreferences("oncevisited", Context.MODE_PRIVATE);
        //mydetailseditor = mydetails.edit();
        boolean bool = false;
        //mydetails.getBoolean("oncevisited",false);
    }

    private boolean DEBUG = true;

    public void addUpdateMyDetailsIntoDb()
    {

        ParseUser user = ParseUser.getCurrentUser();
        if(!mydetails.getBoolean("oncevisited",false) ) {
            if (user != null) {
                //mydetailseditor.putBoolean("oncevisited", false);
                // viewHolder.follow.setClickable(true);

                SharedPreferences.Editor editor = mydetails.edit();
                editor.putBoolean("oncevisited",true);
               // editor.putString("password", ""+passtext);
                editor.commit();

                Log.e("in if", "in if");
                Log.e("in if", "in if");
                Log.e("in if", "in if");
                Log.e("in if", "in if");

                //editorfriends.putString(frndid + "hell", "Following");
                //mydetailseditor.commit();
                String fbid = user.get("fbid").toString();
                String aboutme = user.get("aboutme").toString();
                String name = user.get("name").toString();
                String genres = user.get("genres").toString();
                String rating = user.get("rating").toString();
                String picture = user.get("picture").toString();

                ContentValues values = new ContentValues();


                values.put(UserProvider.FBID, fbid);
                values.put(UserProvider.NAME, name);
                values.put(UserProvider.ABOUTME, name);
                values.put(UserProvider.GENRES, genres);
                values.put(UserProvider.RATING, rating);
                values.put(UserProvider.URLPICTURE, picture);

                Uri uri = mContext.getContentResolver().insert(
                        UserProvider.CONTENT_URI, values);

                Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");

                //
                //  Toast.makeText(this,
                //           uri.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if(user != null ) {
                // mydetailseditor.putBoolean("oncevisited", true);
                // viewHolder.follow.setClickable(true);

             /*   Log.e("in else","in if");
                Log.e("in else","in if");
                Log.e("in else","in if");
                Log.e("in else","in if");
                */
                //editorfriends.putString(frndid + "hell", "Following");
                //mydetailseditor.commit();
                String fbid = user.get("fbid").toString();
                String aboutme = user.get("aboutme").toString();
                String name = user.get("name").toString();
                String genres = user.get("genres").toString();
                String rating = user.get("rating").toString();
                String picture = user.get("picture").toString();

                ContentValues values = new ContentValues();


                values.put(UserProvider.FBID, fbid);
                values.put(UserProvider.NAME,name);
                values.put(UserProvider.ABOUTME,aboutme);
                values.put(UserProvider.GENRES,genres);
                values.put(UserProvider.RATING,rating);
                values.put(UserProvider.URLPICTURE,picture);

                String where = UserProvider.USER_TABLE_NAME+"."+UserProvider.FBID + " = ? ";

                int i  = mContext.getContentResolver().update(UserProvider.CONTENT_URI,values,where, new String[]{fbid});
                // Uri uri = mContext.getContentResolver().insert(
                //       UserProvider.CONTENT_URI, values);

              /*  Log.e("Upated database ", "updated database for myself");
                Log.e("Upated database ", "updated database for myself");
                Log.e("Upated database ", "updated database for myself");
                Log.e("Upated database ", "updated database for myself");
                */
                //   Toast.makeText(this,
                //           uri.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void addUpdateMyFriendDetailsIntoDb()
    {

        GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse response) {

                        try {
                            // ArrayList<Friend_List_Load> item_details = new ArrayList<Friend_List_Load>();
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                     Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));
                                     Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));
                                     Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));

                                    final ParseObject followers = new ParseObject("FollowMovieBuzz");

                                    final ParseUser user1 = ParseUser.getCurrentUser();
                                    final String followingid1 = c.optString("id");
                                    final String followingname1 = c.optString("name");
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("FollowMovieBuzz");
                                    query1.whereEqualTo("follower", user1.get("fbid").toString());
                                    query1.whereEqualTo("following", c.optString("id"));
                                   // query1.fromLocalDatastore();
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {


                                            if (list.size() == 0) {

                                                Log.e("List Size is 0", "list size is 0");
                                                Log.e("List Size is 0", "list size is 0");
                                                Log.e("List Size is 0", "list size is 0");

                                                followers.put("follower", user1.get("fbid").toString());
                                                followers.put("following", followingid1);
                                                followers.put("isfollowing", false);
                                                followers.put("namefollowing", followingname1);

                                                // String url = "http://graph.facebook.com/"+followingid1+"/picture?type=large";
                                                String url = "http://graph.facebook.com" + File.separator
                                                        + String.valueOf(followingid1) + File.separator + "picture?type=large&redirect=false";

                                                String output = "hello";
                                                try {
                                                    output = new FetchProfilePicture().execute(url).get();
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                } catch (ExecutionException e1) {
                                                    e1.printStackTrace();
                                                }

                                                followers.put("picture", output);
                                                followers.pinInBackground();
                                                followers.saveEventually();


                                                ContentValues values1 = new ContentValues();


                                                values1.put(UserProvider.FOLLOWER, user1.get("fbid").toString());
                                                values1.put(UserProvider.FOLLOWING, followingid1);
                                                values1.put(UserProvider.FOLLOWING_URL, output);
                                                values1.put(UserProvider.NAME_FOLLOWING, followingname1);
                                                values1.put(UserProvider.IS_FOLLOWING_FOLLOWER, "false");
                                                //String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                Uri uri1 = mContext.getContentResolver().insert(
                                                        UserProvider.CONTENT_URI_FOLLOWER, values1);


                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                                // ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                //query.fromLocalDatastore();

                                                query.whereEqualTo("fbid", followingid1);

                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> list, ParseException e) {


                                                        if (list != null) {

                                                            aboutme_friend = list.get(0).get("aboutme").toString();
                                                            genres_friend = list.get(0).get("genres").toString();
                                                            picture_friend = list.get(0).get("picture").toString();
                                                            rating_friend = list.get(0).get("rating").toString();
                                                        }
                                                        //stopLoading();

                                                    }


                                                });

                                                String fbid_friend = followingid1;
                                                String name_friend = followingname1;
                                                String isfollowing_friend = "false";

                                                ContentValues values = new ContentValues();


                                                values.put(UserProvider.FBID_FRIEND, fbid_friend);
                                                values.put(UserProvider.NAME_FRIEND, name_friend);
                                                values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                values.put(UserProvider.IS_FOLLOWING, isfollowing_friend);
                                                String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                Uri uri = mContext.getContentResolver().insert(
                                                        UserProvider.CONTENT_URI_FRIENDS, values);


                                                //int i  = mContext.getContentResolver().update(UserProvider.CONTENT_URI,values,where, new String[]{fbid});


                                            }
                                            else if (list.size() == 1) {

                                                   Log.e("List Size is 1", "list size is 1");
                                                   Log.e("List Size is 1", "list size is 1");
                                                ParseObject obj = list.get(0);


                                                String follow =
                                                        UserProvider.USER_FOLLOWING_TABLE_NAME +
                                                                "." +
                                                                UserProvider.FOLLOWING + " = ?  ";

                                                Cursor locationCursor2 = mContext.getContentResolver().query(
                                                        UserProvider.CONTENT_URI_FOLLOWER,
                                                        FORECAST_FOLLOWERS_COLUMNS,
                                                        follow,
                                                        new String[]{followingid1},
                                                        null);
                                                int inserted1 = 0;

                                                if(locationCursor2.getCount() == 0 ) {


                                                    ContentValues values1 = new ContentValues();


                                                    values1.put(UserProvider.FOLLOWER, user1.get("fbid").toString());
                                                    values1.put(UserProvider.FOLLOWING, followingid1);
                                                    values1.put(UserProvider.FOLLOWING_URL, obj.get("picture").toString());
                                                    values1.put(UserProvider.NAME_FOLLOWING, followingname1);
                                                    boolean bools = obj.getBoolean("isfollowing");
                                                    String boolval;
                                                    if(bools)
                                                        boolval = "true";
                                                    else
                                                        boolval = "false";
                                                    values1.put(UserProvider.IS_FOLLOWING_FOLLOWER, boolval);
                                                    //String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                    Uri uri3 = mContext.getContentResolver().insert(
                                                            UserProvider.CONTENT_URI_FOLLOWER, values1);
                                                    locationCursor2.close();
                                                }



                                                obj.pinInBackground();
                                                boolean bool = obj.getBoolean("isfollowing");

                                                String isfollowing_friend = "false";
                                                if(bool)
                                                    isfollowing_friend = "true";
                                                obj.put("isfollowing", bool);
                                                // obj.saveEventually();

                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                                // ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                //query.fromLocalDatastore();

                                                query.whereEqualTo("fbid", followingid1);

                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> list, ParseException e) {


                                                        if (list != null) {

                                                            aboutme_friend = list.get(0).get("aboutme").toString();
                                                            genres_friend = list.get(0).get("genres").toString();
                                                            picture_friend = list.get(0).get("picture").toString();
                                                            rating_friend = list.get(0).get("rating").toString();
                                                        }
                                                        //stopLoading();

                                                    }


                                                });

                                                String friendId =
                                                        UserProvider.USER_FRIENDS_TABLE_NAME +
                                                                "." +
                                                                UserProvider.FBID_FRIEND + " = ?  ";

                                                Cursor locationCursor1 = mContext.getContentResolver().query(
                                                        UserProvider.CONTENT_URI_FRIENDS,
                                                        FORECAST_FRIENDS_COLUMNS,
                                                        friendId,
                                                        new String[]{followingid1},
                                                        null);
                                                int inserted = 0;

                                                if(locationCursor1.getCount() != 0 ) {


                                                    ContentValues values = new ContentValues();
                                                    values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                    values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                    values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                    values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                    values.put(UserProvider.IS_FOLLOWING, isfollowing_friend);

                                                    String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                    int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_FRIENDS, values, where, new String[]{followingid1});
                                                    locationCursor1.close();
                                                }
                                                else if(locationCursor1.getCount() == 0)
                                                {

                                                    ContentValues values = new ContentValues();


                                                    values.put(UserProvider.FBID_FRIEND, followingid1);
                                                    values.put(UserProvider.NAME_FRIEND, followingname1);
                                                    values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                    values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                    values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                    values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                    values.put(UserProvider.IS_FOLLOWING, isfollowing_friend);
                                                    // String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                    Uri uri = mContext.getContentResolver().insert(
                                                            UserProvider.CONTENT_URI_FRIENDS, values);

                                                    locationCursor1.close();
                                                }

                                            }
                                        }
                                    });


                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }),
                GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        System.out.println("meJSONObject: "+object);
                        System.out.println("meGraphResponse: "+response);

                    }
                })
        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                //Log.i(TAG, "onCompleted: graphRequests "+ graphRequests);
            }
        });
        batch.executeAsync();


    }


    public void findMoviesFromParse(){

        final ParseUser pu = ParseUser.getCurrentUser();
        ParseObject obj = new ParseObject("movielist");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
        query.whereEqualTo("watcher", pu.get("fbid").toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (list != null && list.size() != 0) {
                    Log.e("Movie Found ", "Movie Found");
                    Log.e("Movie Found ", "Movie Found");
                    Log.e("Movie Found ", "Movie Found");
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject obj = list.get(i);
                        obj.pinInBackground();
                        String mid = obj.get("mid").toString();
                        String _MOVIE_NAME = obj.get("mname").toString();
                        String _MOVIE_RD = obj.get("mrdate").toString();
                        String _MOVIE_WATCHER = obj.get("watcher").toString();
                        String _MOVIE_DES = obj.get("mdes").toString();
                        String _MOVIE_URL = obj.get("murl").toString();
                        String _MOVIE_ISWATCHED = obj.get("iswatched").toString();
                        String _MOVIE_LIKES = obj.get("likes").toString();
                        String _MOVIE_ISWATCHLIST = obj.get("iswatchlist").toString();
                        String _MOVIE_CATEGORY = obj.get("mcategory").toString();
                        String _MOVIE_RATING = obj.get("rating").toString();
                        String _MOVIE_ISREVIEWED = obj.get("isreviewed").toString();
                        String _MOVIE_REVIEW = obj.get("review").toString();
                        String _MOVIE_ON_PARSE = obj.get("movieonparse").toString();
                        String _MOVIE_REVIEW_TIME = obj.get("moviereviewtime").toString();
                        String bool = "true";
                        String sMovieTypeWithMovieID =
                                UserProvider.USER_MOVIES_TABLE_NAME +
                                        "." +
                                        UserProvider._MOVIE_ID + " = ? AND  "
                                        + UserProvider._MOVIE_ON_PARSE + " = ? AND "
                                        + UserProvider._MOVIE_WATCHER + " = ? ";

                        Cursor locationCursor1 = mContext.getContentResolver().query(
                                UserProvider.CONTENT_URI_MOVIES,
                                FORECAST_MOVIE_COLUMNS,
                                sMovieTypeWithMovieID,
                                new String[]{mid, bool, pu.get("fbid").toString()},
                                null);
                        int inserted = 0;

                        if (locationCursor1.getCount() != 0) {
                            locationCursor1.close();
                        } else {
                            Log.e("Movie Found in else ", "Movie Found");
                            Log.e("Movie Found in else ", "Movie Found");
                            Log.e("Movie Found in else ", "Movie Found");
                            ContentValues movieValues = new ContentValues();
                            movieValues.put(UserProvider._MOVIE_ID, mid);
                            movieValues.put(UserProvider._MOVIE_NAME, _MOVIE_NAME);
                            movieValues.put(UserProvider._MOVIE_RD, _MOVIE_RD);
                            movieValues.put(UserProvider._MOVIE_WATCHER, _MOVIE_WATCHER);
                            movieValues.put(UserProvider._MOVIE_DES, _MOVIE_DES);
                            movieValues.put(UserProvider._MOVIE_URL, _MOVIE_URL);
                            movieValues.put(UserProvider._MOVIE_ISWATCHED, _MOVIE_ISWATCHED);
                            movieValues.put(UserProvider._MOVIE_LIKES, _MOVIE_LIKES);
                            movieValues.put(UserProvider._MOVIE_ISWATCHLIST, _MOVIE_ISWATCHLIST);
                            movieValues.put(UserProvider._MOVIE_CATEGORY, _MOVIE_CATEGORY);
                            movieValues.put(UserProvider._MOVIE_RATING, _MOVIE_RATING);
                            movieValues.put(UserProvider._MOVIE_ISREVIEWED, _MOVIE_ISREVIEWED);
                            movieValues.put(UserProvider._MOVIE_REVIEW, _MOVIE_REVIEW);
                            movieValues.put(UserProvider._MOVIE_ON_PARSE, _MOVIE_ON_PARSE);
                            movieValues.put(UserProvider._MOVIE_REVIEW_TIME,_MOVIE_REVIEW_TIME);

                            Uri uri = mContext.getContentResolver().insert(
                                    UserProvider.CONTENT_URI_MOVIES, movieValues);
                            locationCursor1.close();

                            removeDuplicates();
                        }

                    }
                }


            }
        });


    }

    public void findFriendsMoviesFromParse(){

        final ParseUser pu = ParseUser.getCurrentUser();
        ParseObject obj = new ParseObject("movielist");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FollowMovieBuzz");
        query.whereEqualTo("follower",pu.get("fbid").toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                for(int i = 0; i< list.size(); i++) {

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                    query.whereEqualTo("watcher", list.get(i).get("following").toString());
                    query.whereEqualTo("isreviewed","true");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {

                            if (list != null && list.size() != 0) {
                                Log.e("Movie Found ", "Movie Found");
                                Log.e("Movie Found ", "Movie Found");
                                Log.e("Movie Found ", "Movie Found");
                                for (int i = 0; i < list.size(); i++) {
                                    ParseObject obj = list.get(i);
                                    obj.pinInBackground();
                                    String mid = obj.get("mid").toString();
                                    String _MOVIE_NAME = obj.get("mname").toString();
                                    String _MOVIE_RD = obj.get("mrdate").toString();
                                    String _MOVIE_WATCHER = obj.get("watcher").toString();
                                    String _MOVIE_DES = obj.get("mdes").toString();
                                    String _MOVIE_URL = obj.get("murl").toString();
                                    String _MOVIE_ISWATCHED = obj.get("iswatched").toString();
                                    String _MOVIE_LIKES = obj.get("likes").toString();
                                    String _MOVIE_ISWATCHLIST = obj.get("iswatchlist").toString();
                                    String _MOVIE_CATEGORY = obj.get("mcategory").toString();
                                    String _MOVIE_RATING = obj.get("rating").toString();
                                    String _MOVIE_ISREVIEWED = obj.get("isreviewed").toString();
                                    String _MOVIE_REVIEW = obj.get("review").toString();
                                    String _MOVIE_ON_PARSE = obj.get("movieonparse").toString();
                                    String _MOVIE_REVIEW_TIME = obj.get("moviereviewtime").toString();
                                    String bool = "true";
                                    String sMovieTypeWithMovieID =
                                            UserProvider.USER_MOVIES_TABLE_NAME +
                                                    "." +
                                                    UserProvider._MOVIE_ID + " = ? AND  "
                                                    + UserProvider._MOVIE_ON_PARSE + " = ? AND "
                                                    + UserProvider._MOVIE_WATCHER + " = ? ";

                                    Cursor locationCursor1 = mContext.getContentResolver().query(
                                            UserProvider.CONTENT_URI_MOVIES,
                                            FORECAST_MOVIE_COLUMNS,
                                            sMovieTypeWithMovieID,
                                            new String[]{mid, bool, _MOVIE_WATCHER},
                                            null);
                                    int inserted = 0;

                                    if (locationCursor1.getCount() != 0) {
                                        locationCursor1.close();
                                    } else {
                                        Log.e("Movie Found in else ", "Movie Found");
                                        Log.e("Movie Found in else ", "Movie Found");
                                        Log.e("Movie Found in else ", "Movie Found");
                                        ContentValues movieValues = new ContentValues();
                                        movieValues.put(UserProvider._MOVIE_ID, mid);
                                        movieValues.put(UserProvider._MOVIE_NAME, _MOVIE_NAME);
                                        movieValues.put(UserProvider._MOVIE_RD, _MOVIE_RD);
                                        movieValues.put(UserProvider._MOVIE_WATCHER, _MOVIE_WATCHER);
                                        movieValues.put(UserProvider._MOVIE_DES, _MOVIE_DES);
                                        movieValues.put(UserProvider._MOVIE_URL, _MOVIE_URL);
                                        movieValues.put(UserProvider._MOVIE_ISWATCHED, _MOVIE_ISWATCHED);
                                        movieValues.put(UserProvider._MOVIE_LIKES, _MOVIE_LIKES);
                                        movieValues.put(UserProvider._MOVIE_ISWATCHLIST, _MOVIE_ISWATCHLIST);
                                        movieValues.put(UserProvider._MOVIE_CATEGORY, _MOVIE_CATEGORY);
                                        movieValues.put(UserProvider._MOVIE_RATING, _MOVIE_RATING);
                                        movieValues.put(UserProvider._MOVIE_ISREVIEWED, _MOVIE_ISREVIEWED);
                                        movieValues.put(UserProvider._MOVIE_REVIEW, _MOVIE_REVIEW);
                                        movieValues.put(UserProvider._MOVIE_ON_PARSE, _MOVIE_ON_PARSE);
                                        movieValues.put(UserProvider._MOVIE_REVIEW_TIME,_MOVIE_REVIEW_TIME);

                                        Uri uri = mContext.getContentResolver().insert(
                                                UserProvider.CONTENT_URI_MOVIES, movieValues);
                                        locationCursor1.close();

                                        removeDuplicates();
                                    }

                                }
                            }


                        }
                    });
                }




            }
        });




    }


    private void getMovieDataFromJson(String forecastJsonStr)


            throws JSONException {

        Map<Integer, String> dictionary = new HashMap<Integer, String>();


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




        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_DES = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_MOV_ID = "id";

        final String OWN_MOV_POSTER = "poster_path";
        final String OWN_PRODUCTION_COUNTRY = "genre_ids";



        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);


            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());


            for(int i = 0; i < movieArray.length(); i++) {
                // These are the values that will be collected.

                String moviename;
                String movieid;
                String moviedes;
                String reldate;
                String prod_country = "";
                String url;


                // Get the JSON object representing the day
                JSONObject movieForecast = movieArray.getJSONObject(i);


                JSONArray prod = movieForecast.getJSONArray(OWN_PRODUCTION_COUNTRY);
                int x = 0;
                String temp;
                for (int j = 0; j < prod.length(); j++) {
                    //JSONObject valueprod = prod.getJSONObject(j);
                    x = prod.getInt(j);
                    //temp = valueprod.getString("name");
                    temp = dictionary.get(x);
                    //temp = cat.dicelements(x);
                    prod_country = prod_country.concat(temp + " , ");
                }
                if (prod.length() == 0) {
                    prod_country = "Others";
                }

                // Cheating to convert this to UTC time, which is what we want anyhow
                movieid = movieForecast.getString(OWM_MOV_ID);
                moviename = movieForecast.getString(OWM_TITLE);
                moviedes = movieForecast.getString(OWM_DES);
                reldate = movieForecast.getString(OWM_REL_DATE);
                //  String reldateupdate = reldate.substring(0, 4);
                Date now = new Date();
                String today = new SimpleDateFormat("yyyy").format(now);
                String month = new SimpleDateFormat("MM").format(now);


                url = movieForecast.getString(OWN_MOV_POSTER);

                String imageurl = "http://image.tmdb.org/t/p/w45" + url;

                if(url == null )
                {
                    imageurl = "notavailable";
                }
                ParseUser users = ParseUser.getCurrentUser();

                String sMovieTypeWithMovieID =
                        UserProvider.USER_MOVIES_TABLE_NAME +
                                "." +
                                UserProvider._MOVIE_ID + " = ?  AND "+
                UserProvider._MOVIE_WATCHER + " =  ? ";

                Cursor locationCursor1 = mContext.getContentResolver().query(
                        UserProvider.CONTENT_URI_MOVIES,
                        FORECAST_MOVIE_COLUMNS,
                        sMovieTypeWithMovieID,
                        new String[]{movieid, users.get("fbid").toString()},
                        null);
                int inserted = 0;

                if(locationCursor1.getCount() != 0 )
                {
                    locationCursor1.close();
                }

                else if (locationCursor1.getCount() == 0 ) {


                    ParseUser user = ParseUser.getCurrentUser();
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(UserProvider._MOVIE_ID,movieid);
                    movieValues.put(UserProvider._MOVIE_NAME,moviename);
                    movieValues.put(UserProvider._MOVIE_RD,reldate);
                    movieValues.put(UserProvider._MOVIE_WATCHER,user.get("fbid").toString());
                    movieValues.put(UserProvider._MOVIE_DES,moviedes);
                    movieValues.put(UserProvider._MOVIE_URL,imageurl);
                    movieValues.put(UserProvider._MOVIE_ISWATCHED,"false");
                    movieValues.put(UserProvider._MOVIE_LIKES,"0");
                    movieValues.put(UserProvider._MOVIE_ISWATCHLIST,"false");
                    movieValues.put(UserProvider._MOVIE_CATEGORY,prod_country);
                    movieValues.put(UserProvider._MOVIE_RATING,"0");
                    movieValues.put(UserProvider._MOVIE_ISREVIEWED,"false");
                    movieValues.put(UserProvider._MOVIE_REVIEW,"Add your review");
                    movieValues.put(UserProvider._MOVIE_ON_PARSE,"false");
                    String datetoday = String.valueOf(now);
                    movieValues.put(UserProvider._MOVIE_REVIEW_TIME, datetoday);
                    Uri uri = mContext.getContentResolver().insert(
                            UserProvider.CONTENT_URI_MOVIES, movieValues);
                    locationCursor1.close();
                }


            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private void getMovieDataFromJsonup(String forecastJsonStr)


            throws JSONException {

        Map<Integer, String> dictionary = new HashMap<Integer, String>();


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




        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_DES = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_MOV_ID = "id";

        final String OWN_MOV_POSTER = "poster_path";
        final String OWN_PRODUCTION_COUNTRY = "genre_ids";



        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);


            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());


            for(int i = 0; i < movieArray.length(); i++) {
                // These are the values that will be collected.

                String moviename;
                String movieid;
                String moviedes;
                String reldate;
                String prod_country = "";
                String url;


                // Get the JSON object representing the day
                JSONObject movieForecast = movieArray.getJSONObject(i);


                JSONArray prod = movieForecast.getJSONArray(OWN_PRODUCTION_COUNTRY);
                int x = 0;
                String temp;
                for (int j = 0; j < prod.length(); j++) {
                    //JSONObject valueprod = prod.getJSONObject(j);
                    x = prod.getInt(j);
                    //temp = valueprod.getString("name");
                    temp = dictionary.get(x);
                    //temp = cat.dicelements(x);
                    prod_country = prod_country.concat(temp + " , ");
                }
                if (prod.length() == 0) {
                    prod_country = "Others";
                }

                // Cheating to convert this to UTC time, which is what we want anyhow
                movieid = movieForecast.getString(OWM_MOV_ID);
                moviename = movieForecast.getString(OWM_TITLE);
                moviedes = movieForecast.getString(OWM_DES);
                reldate = movieForecast.getString(OWM_REL_DATE);
                // String reldateupdate = reldate.substring(0, 4);
                Date now = new Date();
                String today = new SimpleDateFormat("yyyy").format(now);
                String month = new SimpleDateFormat("MM").format(now);


                url = movieForecast.getString(OWN_MOV_POSTER);

                String imageurl = "http://image.tmdb.org/t/p/w45" + url;

                if(url == null )
                {
                    imageurl = "notavailable";
                }

                ParseUser users = ParseUser.getCurrentUser();
                String sMovieTypeWithMovieID =
                        UserProvider.USER_MOVIES_TABLE_NAME +
                                "." +
                                UserProvider._MOVIE_ID + " = ? AND "+
                                UserProvider._MOVIE_WATCHER + " =  ? ";

                Cursor locationCursor1 = mContext.getContentResolver().query(
                        UserProvider.CONTENT_URI_MOVIES,
                        FORECAST_MOVIE_COLUMNS,
                        sMovieTypeWithMovieID,
                        new String[]{movieid,users.get("fbid").toString()},
                        null);
                int inserted = 0;


                if(locationCursor1.getCount() != 0 )
                {

                }

                else if (locationCursor1.getCount() == 0 ) {

                    ParseUser user = ParseUser.getCurrentUser();
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(UserProvider._MOVIE_ID,movieid);
                    movieValues.put(UserProvider._MOVIE_NAME,moviename);
                    movieValues.put(UserProvider._MOVIE_RD,reldate);
                    movieValues.put(UserProvider._MOVIE_WATCHER,user.get("fbid").toString());
                    movieValues.put(UserProvider._MOVIE_DES,moviedes);
                    movieValues.put(UserProvider._MOVIE_URL,imageurl);
                    movieValues.put(UserProvider._MOVIE_ISWATCHED,"false");
                    movieValues.put(UserProvider._MOVIE_LIKES,"0");
                    movieValues.put(UserProvider._MOVIE_ISWATCHLIST,"false");
                    movieValues.put(UserProvider._MOVIE_CATEGORY,prod_country);
                    movieValues.put(UserProvider._MOVIE_RATING,"0");
                    movieValues.put(UserProvider._MOVIE_ISREVIEWED,"false");
                    movieValues.put(UserProvider._MOVIE_REVIEW,"Add your review");
                    movieValues.put(UserProvider._MOVIE_ON_PARSE, "false");

                    String datetoday = String.valueOf(now);
                    movieValues.put(UserProvider._MOVIE_REVIEW_TIME,datetoday);

                    Uri uri = mContext.getContentResolver().insert(
                            UserProvider.CONTENT_URI_MOVIES, movieValues);

                }


            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }



    @Override
    protected Void doInBackground(String... params) {

      //  startLoading();
        Log.e("Into doinBack", "into doinback");

        //final SharedPreferences mydetails = mContext.getSharedPreferences("mydetails", Context.MODE_PRIVATE);
        /*
        Log.e("mydetails.getBoolean", Boolean.toString(mydetails.getBoolean("oncevisited", false)));
        Log.e("mydetails.getBoolean",Boolean.toString(mydetails.getBoolean("oncevisited",false)));
        Log.e("mydetails.getBoolean",Boolean.toString(mydetails.getBoolean("oncevisited",false)));
        Log.e("mydetails.getBoolean",Boolean.toString(mydetails.getBoolean("oncevisited",false)));
        Log.e("mydetails.getBoolean",Boolean.toString(mydetails.getBoolean("oncevisited",false)));

        */
        addUpdateMyDetailsIntoDb();
      //  addUpdateMyFriendDetailsIntoDb();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;

        for(int i=1; i<=13; i++) {
            try {

                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                //http://api.themoviedb.org/3/movie/upcoming?;
                // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

                final String QUERY_PARAM = "api_key";
                final String PRI_REL_GRT = "primary_release_date.gte";
                final String PRI_REL_LESS = "primary_release_date.lte";
                final String PAGE = "page";

                //  Date date = new Date();
                //  String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                //  String year = new SimpleDateFormat("yyyy").format(date);
                //  String month = new SimpleDateFormat("MM").format(date);
                //  String date = new SimpleDateFormat("dd").format(date);
                //  String thisyeardate = year + "-"+month+"-01";

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();
                String today = dateFormat.format(date);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -29);
                Date before = cal.getTime();
                String sevendays = dateFormat.format(before);


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, "b2375fe3eeeb29e0f67d7afe13649fd2")
                        .appendQueryParameter(PRI_REL_GRT, sevendays)
                        .appendQueryParameter(PRI_REL_LESS, today)
                        .appendQueryParameter(PAGE, String.valueOf(i))
                        .build();


                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                getMovieDataFromJson(forecastJsonStr);
                //getWeatherDataFromJson(forecastJsonStr, locationQuery);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        HttpURLConnection urlConnectionup = null;
        BufferedReader readerup = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStrup = null;

        String formatup = "json";
        String unitsup = "metric";
        int numDaysup = 14;

        for(int i=1; i<=13; i++) {
            try {

                final String FORECAST_BASE_URL_UP =
                        "http://api.themoviedb.org/3/discover/movie?";
                //http://api.themoviedb.org/3/movie/upcoming?;
                // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

                final String QUERY_PARAM_UP = "api_key";
                final String PRI_REL_GRT_UP = "primary_release_date.gte";
                final String PRI_REL_LESS_UP = "primary_release_date.lte";
                final String PAGE_UP = "page";

                //  Date date = new Date();
                //  String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

                //  String year = new SimpleDateFormat("yyyy").format(date);
                //  String month = new SimpleDateFormat("MM").format(date);
                //  String date = new SimpleDateFormat("dd").format(date);
                //  String thisyeardate = year + "-"+month+"-01";

                DateFormat dateFormatup = new SimpleDateFormat("yyyy-MM-dd");

                Date dateup = new Date();
                String todayup = dateFormatup.format(dateup);

                Calendar calup = Calendar.getInstance();
                calup.add(Calendar.DATE, +29);
                Date afterup = calup.getTime();
                String sevendaysup = dateFormatup.format(afterup);


                Uri builtUriup = Uri.parse(FORECAST_BASE_URL_UP).buildUpon()
                        .appendQueryParameter(QUERY_PARAM_UP, "b2375fe3eeeb29e0f67d7afe13649fd2")
                        .appendQueryParameter(PRI_REL_GRT_UP, todayup)
                        .appendQueryParameter(PRI_REL_LESS_UP, sevendaysup)
                        .appendQueryParameter(PAGE_UP, String.valueOf(i))
                        .build();


                URL urlup = new URL(builtUriup.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnectionup = (HttpURLConnection) urlup.openConnection();
                urlConnectionup.setRequestMethod("GET");
                urlConnectionup.connect();

                // Read the input stream into a String
                InputStream inputStreamup = urlConnectionup.getInputStream();
                StringBuffer bufferup = new StringBuffer();
                if (inputStreamup == null) {
                    // Nothing to do.
                    return null;
                }
                readerup = new BufferedReader(new InputStreamReader(inputStreamup));

                String lineup;
                while ((lineup = readerup.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    bufferup.append(lineup + "\n");
                }

                if (bufferup.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStrup = bufferup.toString();
                getMovieDataFromJsonup(forecastJsonStrup);
                //getWeatherDataFromJson(forecastJsonStr, locationQuery);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnectionup != null) {
                    urlConnectionup.disconnect();
                }
                if (readerup != null) {
                    try {
                        readerup.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }


        findMoviesFromParse();
        findFriendsMoviesFromParse();






        return null;
    }

    public void removeDuplicates()
    {

        ParseUser user = ParseUser.getCurrentUser();
        String sMovieTypeWithMovieID =
                UserProvider._MOVIE_WATCHER + " = "+user.get("fbid").toString() + " AND " +
                UserProvider._MOVIE_ID + " is not null )  GROUP BY "+
                        UserProvider._MOVIE_ID + " HAVING COUNT( "+
                        UserProvider._MOVIE_ID + " ) >= (2  ";

        Cursor locationCursor1 = mContext.getContentResolver().query(
                UserProvider.CONTENT_URI_MOVIES,
                new String[]{UserProvider._MOVIE_ID},
                sMovieTypeWithMovieID,
                null,
                null);
        int inserted = 0;


        if(locationCursor1.getCount() != 0 )
        {
            while(locationCursor1.moveToNext()) {
                //locationCursor1.moveToFirst();
                Log.e("here", "here");
                Log.e("here", "here");
                Log.e("here", "here");

                Log.e("Movie id is: ", locationCursor1.getString(0));
                Log.e("Movie id is: ", locationCursor1.getString(0));
                Log.e("Movie id is: ", locationCursor1.getString(0));

                String sMovieTypeWithMovieIDRe =
                        UserProvider._MOVIE_WATCHER + " = " + user.get("fbid").toString() + " AND " +
                                UserProvider._MOVIE_ID + " = ? AND " +
                                UserProvider._MOVIE_ON_PARSE + " = ? ";

                Cursor locationCursor2 = mContext.getContentResolver().query(
                        UserProvider.CONTENT_URI_MOVIES,
                        new String[]{UserProvider._MOVIE_ID},
                        sMovieTypeWithMovieIDRe,
                        new String[]{locationCursor1.getString(0), "false"},
                        null);

                if (locationCursor2.getCount() != 0) {

                    while(locationCursor2.moveToNext()) {
                        //locationCursor2.moveToFirst();
                        Log.e("value", locationCursor2.getString(0));
                        Log.e("value", locationCursor2.getString(0));
                        mContext.getContentResolver().delete(UserProvider.CONTENT_URI_MOVIES, UserProvider._MOVIE_ID + " = ? AND " + UserProvider._MOVIE_ON_PARSE + " = ? AND " + UserProvider._MOVIE_WATCHER + " = ? ", new String[]{locationCursor2.getString(0), "false", ParseUser.getCurrentUser().get("fbid").toString()});

                    }
                } else {
                    Log.e("in else count 0", "in else count 0");
                    Log.e("in else count 0", "in else count 0");
                }
            }


        }

        else if (locationCursor1.getCount() == 0 ) {



        }


    }

    private ProgressDialog proDialog;
    protected void startLoading() {
        proDialog = new ProgressDialog(mContext);

        proDialog.setMessage("Loading Data.. Fetching Friends, Movies and Reviews");
        proDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        startLoading();
        //stopLoading();

    }

    @Override
    protected void onPostExecute(Void v)
    {
        super.onPostExecute(null);
        stopLoading();
       // Bundle tempBundle = new Bundle();
       // ma.onCreate(tempBundle);
       // ma.onResume();
        ma.onDataFetch();
        //ma.onCreate();
        //MainActivity.ma.setupViewPager();

    }
}