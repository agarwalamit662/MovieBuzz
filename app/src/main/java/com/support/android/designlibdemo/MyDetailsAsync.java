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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

//import com.example.amit.tellymoviebuzzz.data.MovieContract;
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

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MyDetailsAsync extends AsyncTask<String, Void, Void> {
   // public final SharedPreferences.Editor mydetailseditor;
    public SharedPreferences.Editor myfriendseditor;
    public final SharedPreferences mydetails;
    private final String LOG_TAG = MyDetailsAsync.class.getSimpleName();

    private final Context mContext;
    public  String aboutme_friend;
    public  String genres_friend;
    public String rating_friend;
    public String picture_friend;

    public MyDetailsAsync(Context context) {
        mContext = context;
        mydetails = mContext.getSharedPreferences("oncevisited", Context.MODE_PRIVATE);
        //mydetailseditor = mydetails.edit();
        boolean bool = false;
        //mydetails.getBoolean("oncevisited",false);
    }

    private boolean DEBUG = true;

    public void addUpdateMyDetailsIntoDb()
    {

       // Log.e("add Update", Boolean.toString(mydetails.getBoolean("oncevisited", true)));
       // Log.e("add Update",Boolean.toString(mydetails.getBoolean("oncevisited",true)));
      //  Log.e("add Update",Boolean.toString(mydetails.getBoolean("oncevisited",true)));
      //  Log.e("add Update",Boolean.toString(mydetails.getBoolean("oncevisited",true)));
      //  Log.e("add Update",Boolean.toString(mydetails.getBoolean("oncevisited",true)));
        ParseUser user = ParseUser.getCurrentUser();
        if(!mydetails.getBoolean("oncevisited",false) ) {
            if (user != null) {
                //mydetailseditor.putBoolean("oncevisited", false);
                // viewHolder.follow.setClickable(true);

                SharedPreferences.Editor editor = mydetails.edit();
                editor.putBoolean("oncevisited",true);
               // editor.putString("password", ""+passtext);
                editor.commit();
                /*
                Log.e("in if", "in if");
                Log.e("in if", "in if");
                Log.e("in if", "in if");
                Log.e("in if", "in if");
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
                values.put(UserProvider.NAME, name);
                values.put(UserProvider.ABOUTME, name);
                values.put(UserProvider.GENRES, genres);
                values.put(UserProvider.RATING, rating);
                values.put(UserProvider.URLPICTURE, picture);

                Uri uri = mContext.getContentResolver().insert(
                        UserProvider.CONTENT_URI, values);

              /*  Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");
                Log.e("Insert database ", "updated database for myself");
                */
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
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, com.parse.ParseException e) {


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


                                            } else if (list.size() == 1) {

                                                Log.e("List Size is 1", "list size is 1");
                                                Log.e("List Size is 1", "list size is 1");
                                                ParseObject obj = list.get(0);

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


                                                ContentValues values = new ContentValues();
                                                values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                values.put(UserProvider.IS_FOLLOWING,isfollowing_friend );

                                                String where = UserProvider.USER_FRIENDS_TABLE_NAME+"."+UserProvider.FBID_FRIEND + " = ? ";

                                                int i  = mContext.getContentResolver().update(UserProvider.CONTENT_URI_FRIENDS,values,where, new String[]{followingid1});



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


    @Override
    protected Void doInBackground(String... params) {


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
       // addUpdateMyFriendDetailsIntoDb();

        return null;
    }
}