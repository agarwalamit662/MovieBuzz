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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.Random;

public class MyDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "cheese_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String cheeseName = "amit agarwal";//intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        final ParseUser user1 = ParseUser.getCurrentUser();
        String username = user1.get("fbid").toString();
        String name = user1.get("name").toString();
        String url = user1.get("picture").toString();

        final String aboutmestr = user1.get("aboutme").toString();
        final String favgenresstr = user1.get("genres").toString();
        String ratingstr = (user1.get("rating").toString());
        collapsingToolbar.setTitle(name);

        loadBackdrop(url);
        final TextView aboutme = (TextView)findViewById(R.id.aboutme);
        aboutme.setText(aboutmestr);
        final TextView genres = (TextView)findViewById(R.id.favgenres);
        genres.setText(favgenresstr);
       // final TextView rating = (TextView)findViewById(R.id.rating);
       // rating.setText(ratingstr);
        final EditText aboutmeedit = (EditText) findViewById(R.id.aboutme_edit);
        final EditText genresedit = (EditText) findViewById(R.id.favgenres_edit);

        aboutmeedit.setText(user1.get("aboutme").toString());
        genresedit.setText(user1.get("genres").toString());
        FloatingActionButton fabedit = (FloatingActionButton) findViewById(R.id.aboutmeeditbutton);
        FloatingActionButton saveedit = (FloatingActionButton) findViewById(R.id.aboutmesavebutton);

        fabedit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(MainActivity.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();

                        aboutmeedit.setVisibility(View.VISIBLE);
                        genresedit.setVisibility(View.VISIBLE);

                        aboutmeedit.setText(user1.get("aboutme").toString());
                        genresedit.setText(user1.get("genres").toString());
                        aboutme.setVisibility(View.GONE);
                        genres.setVisibility(View.GONE);
                    }
                });

        saveedit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();




                        aboutme.setVisibility(View.VISIBLE);
                        genres.setVisibility(View.VISIBLE);

                        if(aboutmeedit.getText().length() !=0 ) {
                            aboutme.setText(aboutmeedit.getText());
                             user1.put("aboutme", aboutmeedit.getText().toString());
                             user1.saveEventually();
                            aboutmeedit.setText(user1.get("aboutme").toString());

                            aboutmeedit.setVisibility(View.GONE);
                        }
                        if(genresedit.getText().length() != 0) {
                            genres.setText(genresedit.getText());
                            user1.put("genres", genresedit.getText().toString());
                            //user1.saveInBackground();
                            user1.saveEventually();
                            genresedit.setText(user1.get("genres").toString());
                            genresedit.setVisibility(View.GONE);
                        }
                    }
                });


    }




    private void loadBackdrop(String url) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(url).centerCrop().into(imageView);
        //Glide.with(this).load()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        return true;
    }
}
