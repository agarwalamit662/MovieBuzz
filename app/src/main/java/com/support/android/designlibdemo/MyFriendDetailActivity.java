/*
 * Copyright (C) 2015 The Android Open Source Project
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

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.support.android.designlibdemo.data.UserProvider;

import java.util.ArrayList;
import java.util.List;

public class MyFriendDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "friend_name";
    private ProgressDialog proDialog;

    private static final String[] FORECAST_FRIENDS_COLUMNS = {

            UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider._ID_FRIEND,
            UserProvider.FBID_FRIEND,
            UserProvider.NAME_FRIEND,
            UserProvider.ABOUTME_FRIEND,
            UserProvider.GENRES_FRIEND,
            UserProvider.RATING_FRIEND,
            UserProvider.URLPICTURE_FRIEND,
            UserProvider.IS_FOLLOWING


    };


    public String backdrop = "random";
    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Loading Information");
        proDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean state = false;

        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        state = true;

                    }else {
                        // state = false;
                    }


                    if(state)
                    {

                        //this.pd = ProgressDialog.show(this, "Working..", "Downloading Data...", true, false);
                        setContentView(R.layout.activity_friends_detail);

                        Intent intent = getIntent();
                        final String frndid = intent.getStringExtra(EXTRA_NAME);


                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        final CollapsingToolbarLayout collapsingToolbar =
                                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_friends);



                        final TextView aboutme = (TextView) findViewById(R.id.aboutme_friend);
                        final TextView genres = (TextView) findViewById(R.id.favgenres_friends);
                        aboutme.setVisibility(View.GONE);
                        genres.setVisibility(View.GONE);
                       // TextView rating = (TextView) findViewById(R.id.rating_friends);
                       // rating.setText("10");
                        final String name = "name";
                        final String aboutmestr = "aboutme";
                        final String picture = "picture";
                        //ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
                        final ArrayList<String> elements = new ArrayList<>();
                        // ParseQuery query = ParseUser.getQuery();
                        // List<ParseObject> objects = query.find(); // Online ParseQuery results
                        // ParseObject.pinAllInBackground(objects);

                       //ParseObject myfriends = new ParseObject("Myfriends");
                       // startLoading();
                        String where = UserProvider.FBID_FRIEND + " = ? ";
                        String[] args = {frndid};

                        Cursor c = getApplicationContext().getContentResolver().query(
                                UserProvider.CONTENT_URI_FRIENDS,FORECAST_FRIENDS_COLUMNS,where,args,null
                        );

                        if(c.getCount() != 0)
                        {
                            while(c.moveToNext())
                            {

                                aboutme.setVisibility(View.VISIBLE);
                                genres.setVisibility(View.VISIBLE);
                                aboutme.setText(c.getString(3).toString());

                                genres.setText(c.getString(4).toString());
                                collapsingToolbar.setTitle(c.getString(2).toString());
                                //backdrop = list.get(0).get("picture").toString();
                                loadBackdrop(c.getString(6).toString());

                            }

                        }
                        /*
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                        // ParseQuery<ParseUser> query = ParseUser.getQuery();
                        //query.fromLocalDatastore();

                        query.whereEqualTo("fbid", frndid);

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {


                                if (list != null) {
                                    aboutme.setVisibility(View.VISIBLE);
                                    genres.setVisibility(View.VISIBLE);
                                    aboutme.setText(list.get(0).get("aboutme").toString());

                                    genres.setText(list.get(0).get("genres").toString());
                                    collapsingToolbar.setTitle(list.get(0).get("name").toString());
                                    backdrop = list.get(0).get("picture").toString();
                                    //loadBackdrop(list.get(0).get("picture").toString());

                                }
                                stopLoading();

                            }


                        });
                        loadBackdrop(backdrop);
                        */
                       // this.pd.dismiss();

                    }
                    else
                    {

                        setContentView(R.layout.activity_friends_detail);

                        Intent intent = getIntent();
                        final String frndid = intent.getStringExtra(EXTRA_NAME);


                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        final CollapsingToolbarLayout collapsingToolbar =
                                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_friends);



                        final TextView aboutme = (TextView) findViewById(R.id.aboutme_friend);
                        final TextView genres = (TextView) findViewById(R.id.favgenres_friends);
                        aboutme.setVisibility(View.GONE);
                        genres.setVisibility(View.GONE);
                       // TextView rating = (TextView) findViewById(R.id.rating_friends);
                       // rating.setText("10");
                        final String name = "name";
                        final String aboutmestr = "aboutme";
                        final String picture = "picture";
                        //ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
                        final ArrayList<String> elements = new ArrayList<>();
                        // ParseQuery query = ParseUser.getQuery();
                        // List<ParseObject> objects = query.find(); // Online ParseQuery results
                        // ParseObject.pinAllInBackground(objects);

                        //ParseObject myfriends = new ParseObject("Myfriends");
                        // startLoading();
                        String where = UserProvider.FBID_FRIEND + " = ? ";
                        String[] args = {frndid};

                        Cursor c = getApplicationContext().getContentResolver().query(
                                UserProvider.CONTENT_URI_FRIENDS,FORECAST_FRIENDS_COLUMNS,where,args,null
                        );

                        if(c.getCount() != 0)
                        {
                            while(c.moveToNext())
                            {

                                aboutme.setVisibility(View.VISIBLE);
                                genres.setVisibility(View.VISIBLE);
                                aboutme.setText(c.getString(3).toString());

                                genres.setText(c.getString(4).toString());
                                collapsingToolbar.setTitle(c.getString(2).toString());
                                //backdrop = list.get(0).get("picture").toString();
                                loadBackdrop(c.getString(6).toString());

                            }

                        }

                    }
        }

        else
        {
           // Toast.makeText(MyFriendDetailActivity.this,"Network Error" ,Toast.LENGTH_LONG);
        }
    }


    private void loadBackdrop(String url) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop_friends);
        Glide.with(this).load(url).centerCrop().into(imageView);
        //Glide.with(this).load()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
