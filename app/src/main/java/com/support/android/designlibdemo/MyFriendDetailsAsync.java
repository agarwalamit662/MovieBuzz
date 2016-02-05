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
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

//import com.example.amit.tellymoviebuzzz.data.MovieContract;


public class MyFriendDetailsAsync extends AsyncTask<String, Void, Void> {
   // public final SharedPreferences.Editor mydetailseditor;
    public SharedPreferences.Editor myfriendseditor;
    public final SharedPreferences mydetails;
    private final String LOG_TAG = MyFriendDetailsAsync.class.getSimpleName();

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


    private final Context mContext;
    public  String aboutme_friend;
    public  String genres_friend;
    public String rating_friend;
    public String picture_friend;

    public MyFriendDetailsAsync(Context context) {
        mContext = context;
        mydetails = mContext.getSharedPreferences("oncevisited", Context.MODE_PRIVATE);
        //mydetailseditor = mydetails.edit();
        boolean bool = false;
        //mydetails.getBoolean("oncevisited",false);
    }

    private boolean DEBUG = true;


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
                                   // Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));
                                   // Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));
                                   // Log.e("Json array ka length", "json array ka length is " + Integer.toString(jsonArray.length()));

                                    final ParseObject followers = new ParseObject("FollowMovieBuzz");

                                    final ParseUser user1 = ParseUser.getCurrentUser();
                                    final String followingid1 = c.optString("id");
                                    final String followingname1 = c.optString("name");
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("FollowMovieBuzz");
                                    query1.whereEqualTo("follower", user1.get("fbid").toString());
                                    query1.whereEqualTo("following", c.optString("id"));
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {


                                            if (list.size() == 0) {

                                               /* Log.e("List Size is 0", "list size is 0");
                                                Log.e("List Size is 0", "list size is 0");
                                                Log.e("List Size is 0", "list size is 0");
                                                */
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


                                            }
                                            else if (list.size() == 1) {

                                             //   Log.e("List Size is 1", "list size is 1");
                                             //   Log.e("List Size is 1", "list size is 1");
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

                                                String friendId =
                                                        UserProvider.USER_FRIENDS_TABLE_NAME +
                                                                "." +
                                                                UserProvider.FBID_FRIEND + " = ?  ";

                                                Cursor locationCursor1 = mContext.getContentResolver().query(
                                                        UserProvider.CONTENT_URI_MOVIES,
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


    @Override
    protected Void doInBackground(String... params) {


       // Log.e("Into doinBack", "into doinback");


        //addUpdateMyDetailsIntoDb();
        addUpdateMyFriendDetailsIntoDb();

        return null;
    }
}