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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.ui.FetchProfilePicture;
import com.support.android.designlibdemo.data.UserProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {

    private static final String[] FORECAST_FOLLOWERS_COLUMNS = {

            UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider._ID_FOLLOWER,
            UserProvider.FOLLOWER,
            UserProvider.FOLLOWING,
            UserProvider.NAME_FOLLOWING,
            UserProvider.IS_FOLLOWING_FOLLOWER,
            UserProvider.FOLLOWING_URL


    };
    public boolean checkbool;
    public String isFoll;


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

    public SharedPreferences.Editor editormainactivity;
    private LocalWordService s;
    public  String aboutme_friend;
    public  String genres_friend;
    public String rating_friend;
    public String picture_friend;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context context;
    Adapter adapter;// = new Adapter(getSupportFragmentManager());
    private ArrayList<Followers> setfollowlist;
    private List<String> friendsList = new ArrayList<String>();
    private ParseUser currentUser;
    public MainActivity ma;
    private ProgressDialog proDialog;
    protected void startLoading() {
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("Loading Data");
        proDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    public void onDataFetch()
    {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager, "Movies");


    }

    @Override
    protected void onResume() {
        super.onResume();
       // viewPager = (ViewPager) findViewById(R.id.viewpager);
       // setupViewPager(viewPager,"Movies");
        Intent intent= new Intent(this, LocalWordService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
        if(s != null) {
            Toast.makeText(MainActivity.this, Double.toString(s.getRandom()), Toast.LENGTH_LONG);
        }
        else if(s == null)
        {
            Toast.makeText(MainActivity.this, "nhfhg", Toast.LENGTH_LONG);
        }

        CircleImageView myimage = (CircleImageView) findViewById(R.id.image_login);

        TextView myname = (TextView) findViewById(R.id.username_login);
        if(ParseUser.getCurrentUser() != null) {
            myname.setText(ParseUser.getCurrentUser().get("name").toString());
            Glide.with(myimage.getContext())
                    .load(ParseUser.getCurrentUser().get("picture").toString())
                    .fitCenter()
                    .into(myimage);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            LocalWordService.MyBinder b = (LocalWordService.MyBinder) binder;
            s = b.getService();
           // Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
            //        .show();
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ma = this;
        currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            AlarmManager service = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this, MyStartServiceReceiver.class);
            PendingIntent pending = PendingIntent.getBroadcast(this, 1234567, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar cal = Calendar.getInstance();
            // start 30 seconds after boot completed
            cal.add(Calendar.SECOND, 60 * 60);
            // fetch every 30 seconds
            // InexactRepeating allows Android to optimize the energy consumption
            //  Toast.makeText(this, "jksndkjsd", Toast.LENGTH_LONG);
            service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), 1000 * 60 * 60, pending);
        }
       // currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {




            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("user", currentUser.get("fbid").toString());
            installation.saveInBackground();

        }

        final SharedPreferences refreshMainActivity = this.getSharedPreferences("refreshMainActivity", Context.MODE_PRIVATE);

        editormainactivity = refreshMainActivity.edit();

        if(refreshMainActivity.getBoolean("mainActivity"+currentUser.get("fbid").toString(),false) ==  false)
        {


            //startLoading();
            Log.e("in main activity ","in main activity");
            Log.e("in main activity ", "in main activity");
            editormainactivity.putBoolean("mainActivity"+currentUser.get("fbid").toString(), true);
            editormainactivity.commit();

            startLoading();
            AllDataSync fetch = new AllDataSync(this);
            fetch.execute("AllDataFetch");
            addUpdateMyFriendDetailsIntoDb();
            stopLoading();


        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        //setfollowlist = findfriends_main();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager,"Movies");
        }

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

       tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        /*ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();*/
        /*
        Bitmap image = ...
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
                    shareButton.setShareContent(content);
                */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupViewPager(ViewPager viewPager, String caseNumber) {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new Adapter(getSupportFragmentManager());

        if(caseNumber.equals("Movies")){
           // Friends Reviews

            adapter.addFragment(MovieLastFragment.newInstance(1, "Mystery"), "Last Month", 0);

            adapter.addFragment(MovieUpcomingFragment.newInstance(1, "Romance"), "Upcoming", 1);

        }
        else if(caseNumber.equals("My Reviews")){

           adapter.addFragment(new MyReviewsFragment().newInstance(1, "My Reviews"), "My Reviews", 0);

        }
        else if(caseNumber.equals("Friends Reviews")){

            adapter.addFragment(new MyFriendsReviewsFragment().newInstance(1, "My Reviews"), "Friends Reviews", 0);

        }
        else if(caseNumber.equals("Watched/Watchlist")){

            adapter.addFragment(new MovieWatchedFragment().newInstance(1,"Mystery"), "Watched",0);
            adapter.addFragment(new MovieWatchlistFragment().newInstance(1,"Romance"), "Watchlist",1);
           // adapter.addFragment(new CheeseListFragment(), "Add Review",2);

        }

        else if(caseNumber.equals("LogOut")){

            adapter.addFragment(new LogOutFragment(), "Log Out",0);

        }
        else if(caseNumber.equals("FacebookFriends")){

            addUpdateMyFriendDetailsIntoDbRefresh();
            adapter.addFragment(new FacebookFriendsFragment(), "Facebook Friends",0);

        }
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
                                               // followers.saveEventually();

                                                String follows =
                                                        UserProvider.USER_FOLLOWING_TABLE_NAME +
                                                                "." +
                                                                UserProvider.FOLLOWING + " = ?  ";

                                                Cursor locationCursor4 = getApplicationContext().getContentResolver().query(
                                                        UserProvider.CONTENT_URI_FOLLOWER,
                                                        FORECAST_FOLLOWERS_COLUMNS,
                                                        follows,
                                                        new String[]{followingid1},
                                                        null);
                                                int inserted4 = 0;

                                                if(locationCursor4.getCount() == 0 ) {


                                                    ContentValues values1 = new ContentValues();


                                                    values1.put(UserProvider.FOLLOWER, user1.get("fbid").toString());
                                                    values1.put(UserProvider.FOLLOWING, followingid1);
                                                    values1.put(UserProvider.FOLLOWING_URL, output);
                                                    values1.put(UserProvider.NAME_FOLLOWING, followingname1);
                                                    values1.put(UserProvider.IS_FOLLOWING_FOLLOWER, "false");
                                                    //String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                    Uri uri1 = getApplicationContext().getContentResolver().insert(
                                                            UserProvider.CONTENT_URI_FOLLOWER, values1);

                                                }

                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                                // ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                //query.fromLocalDatastore();

                                                query.whereEqualTo("fbid", followingid1);

                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> list, ParseException e) {


                                                        if (list != null) {

                                                            if(list.size() > 0) {
                                                                Log.e("list size check ","list size check in list size 0");
                                                                Log.e("list size check ","list size check in list size 0 ");
                                                                Log.e("list size check ","list size check in list size 0 ");
                                                                aboutme_friend = list.get(0).get("aboutme").toString();
                                                                genres_friend = list.get(0).get("genres").toString();
                                                                picture_friend = list.get(0).get("picture").toString();
                                                                rating_friend = list.get(0).get("rating").toString();

                                                                String fbid_friend = followingid1;
                                                                String name_friend = followingname1;
                                                                String isfollowing_friend = "false";

                                                                ContentValues values = new ContentValues();


                                                                values.put(UserProvider.FBID_FRIEND, fbid_friend);
                                                                values.put(UserProvider.NAME_FRIEND, name_friend);
                                                                if(aboutme_friend != null)
                                                                    values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                                else
                                                                    values.put(UserProvider.ABOUTME_FRIEND, "Describe Yourself");
                                                                if(genres_friend != null)
                                                                    values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                                else
                                                                    values.put(UserProvider.GENRES_FRIEND, "Fav Genres");

                                                                if(rating_friend != null )
                                                                    values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                                else
                                                                    values.put(UserProvider.RATING_FRIEND, "0");
                                                                if(picture_friend != null)
                                                                    values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                                else
                                                                    values.put(UserProvider.URLPICTURE_FRIEND, " ");
                                                                values.put(UserProvider.IS_FOLLOWING, isfollowing_friend);
                                                                String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                                Uri uri = getApplicationContext().getContentResolver().insert(
                                                                        UserProvider.CONTENT_URI_FRIENDS, values);

                                                            }
                                                        }
                                                        //stopLoading();

                                                    }


                                                });




                                                //int i  = mContext.getContentResolver().update(UserProvider.CONTENT_URI,values,where, new String[]{fbid});


                                            }
                                            else if (list.size() == 1) {

                                                Log.e("List Size is 1", "list size is 1");
                                                Log.e("List Size is 1", "list size is 1");
                                                ParseObject obj = list.get(0);

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

                                                String follow =
                                                        UserProvider.USER_FOLLOWING_TABLE_NAME +
                                                                "." +
                                                                UserProvider.FOLLOWING + " = ?  ";

                                                Cursor locationCursor2 = getApplicationContext().getContentResolver().query(
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
                                                    values1.put(UserProvider.FOLLOWING_URL, output);
                                                    values1.put(UserProvider.NAME_FOLLOWING, followingname1);
                                                    boolean bools = obj.getBoolean("isfollowing");
                                                    String boolval;
                                                    if(bools)
                                                        boolval = "true";
                                                    else
                                                        boolval = "false";
                                                    values1.put(UserProvider.IS_FOLLOWING_FOLLOWER, boolval);
                                                    //String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                    Uri uri3 = getApplicationContext().getContentResolver().insert(
                                                            UserProvider.CONTENT_URI_FOLLOWER, values1);
                                                    locationCursor2.close();
                                                }



                                                obj.pinInBackground();
                                                boolean bool = obj.getBoolean("isfollowing");

                                                String isfollowing_friend = "false";
                                                if(bool)
                                                    isfollowing_friend = "true";
                                                obj.put("isfollowing", bool);

                                                checkbool = bool;
                                                isFoll = isfollowing_friend;
                                                // obj.saveEventually();

                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                                // ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                //query.fromLocalDatastore();

                                                query.whereEqualTo("fbid", followingid1);

                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> list, ParseException e) {


                                                        if (list != null) {
                                                            Log.e("Size of list",String.valueOf(list.size()));
                                                            Log.e("Size of list",String.valueOf(list.size()));
                                                            Log.e("Size of list",String.valueOf(list.size()));
                                                            if(list.size() > 0 ) {
                                                                aboutme_friend = list.get(0).get("aboutme").toString();
                                                                genres_friend = list.get(0).get("genres").toString();
                                                                picture_friend = list.get(0).get("picture").toString();
                                                                rating_friend = list.get(0).get("rating").toString();

                                                                String friendId =
                                                                        UserProvider.USER_FRIENDS_TABLE_NAME +
                                                                                "." +
                                                                                UserProvider.FBID_FRIEND + " = ?  ";

                                                                Cursor locationCursor1 = getApplicationContext().getContentResolver().query(
                                                                        UserProvider.CONTENT_URI_FRIENDS,
                                                                        FORECAST_FRIENDS_COLUMNS,
                                                                        friendId,
                                                                        new String[]{followingid1},
                                                                        null);
                                                                int inserted = 0;

                                                                if(locationCursor1.getCount() != 0 ) {


                                                                    ContentValues values = new ContentValues();

                                                                    //    values.put(UserProvider.FBID_FRIEND, fbid_friend);
                                                                    //    values.put(UserProvider.NAME_FRIEND, name_friend);
                                                                    if(aboutme_friend != null)
                                                                        values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                                    else
                                                                        values.put(UserProvider.ABOUTME_FRIEND, "Describe Yourself");
                                                                    if(genres_friend != null)
                                                                        values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                                    else
                                                                        values.put(UserProvider.GENRES_FRIEND, "Fav Genres");

                                                                    if(rating_friend != null )
                                                                        values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                                    else
                                                                        values.put(UserProvider.RATING_FRIEND, "0");
                                                                    if(picture_friend != null)
                                                                        values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                                    else
                                                                        values.put(UserProvider.URLPICTURE_FRIEND, " ");


                                                                    values.put(UserProvider.IS_FOLLOWING, isFoll);

                                                                    String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                                    int i = getApplicationContext().getContentResolver().update(UserProvider.CONTENT_URI_FRIENDS, values, where, new String[]{followingid1});
                                                                    locationCursor1.close();
                                                                }
                                                                else if(locationCursor1.getCount() == 0)
                                                                {

                                                                    ContentValues values = new ContentValues();


                                                                    values.put(UserProvider.FBID_FRIEND, followingid1);
                                                                    values.put(UserProvider.NAME_FRIEND, followingname1);
                                                                    // values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                                    // values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                                    //  values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                                    //  values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);

                                                                    //values.put(UserProvider.FBID_FRIEND, fbid_friend);
                                                                    //  values.put(UserProvider.NAME_FRIEND, name_friend);
                                                                    if(aboutme_friend != null)
                                                                        values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                                    else
                                                                        values.put(UserProvider.ABOUTME_FRIEND, "Describe Yourself");
                                                                    if(genres_friend != null)
                                                                        values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                                    else
                                                                        values.put(UserProvider.GENRES_FRIEND, "Fav Genres");

                                                                    if(rating_friend != null )
                                                                        values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                                    else
                                                                        values.put(UserProvider.RATING_FRIEND, "0");
                                                                    if(picture_friend != null)
                                                                        values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                                    else
                                                                        values.put(UserProvider.URLPICTURE_FRIEND, " ");


                                                                    values.put(UserProvider.IS_FOLLOWING, isFoll);
                                                                    // String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                                    Uri uri = getApplicationContext().getContentResolver().insert(
                                                                            UserProvider.CONTENT_URI_FRIENDS, values);

                                                                    locationCursor1.close();
                                                                }

                                                            }
                                                            }
                                                        //stopLoading();

                                                    }


                                                });



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



    public void addUpdateMyFriendDetailsIntoDbRefresh()
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
                                    final JSONObject c = jsonArray.getJSONObject(i);
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
                                                    Log.e("list size what","list");
                                                    Log.e("list size what","list");
                                                    if(list.size() == 0) {
                                                        Log.e("in if list size 0","0");
                                                        Log.e("in if list size 0","0");
                                                        String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FOLLOWING + " = ? AND "
                                                                + UserProvider.FOLLOWER + " = ? ";
                                                        String ans[] = {c.optString("id"), user1.get("fbid").toString()};
                                                        Cursor c = getApplicationContext().getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER, FORECAST_FOLLOWERS_COLUMNS, where, ans, null);
                                                                        Log.e("count of cursor",String.valueOf(c.getCount()));
                                                                        Log.e("count of cursor",String.valueOf(c.getCount()));
                                                                        if (c.getCount() == 0) {
                                                                            Log.e("count = 0 ","count 0");
                                                                            Log.e("count = 0 ","count 0");
                                                                           // while (c.moveToNext()) {

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
                                                                                // followers.saveEventually();

                                                                                String follows =
                                                                                        UserProvider.USER_FOLLOWING_TABLE_NAME +
                                                                                                "." +
                                                                                                UserProvider.FOLLOWING + " = ?  ";

                                                                                Cursor locationCursor4 = getApplicationContext().getContentResolver().query(
                                                                                        UserProvider.CONTENT_URI_FOLLOWER,
                                                                                        FORECAST_FOLLOWERS_COLUMNS,
                                                                                        follows,
                                                                                        new String[]{followingid1},
                                                                                        null);
                                                                                int inserted4 = 0;

                                                                                if(locationCursor4.getCount() == 0 ) {


                                                                                    ContentValues values1 = new ContentValues();


                                                                                    values1.put(UserProvider.FOLLOWER, user1.get("fbid").toString());
                                                                                    values1.put(UserProvider.FOLLOWING, followingid1);
                                                                                    values1.put(UserProvider.FOLLOWING_URL, output);
                                                                                    values1.put(UserProvider.NAME_FOLLOWING, followingname1);
                                                                                    values1.put(UserProvider.IS_FOLLOWING_FOLLOWER, "false");
                                                                                    //String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                                                    Uri uri1 = getApplicationContext().getContentResolver().insert(
                                                                                            UserProvider.CONTENT_URI_FOLLOWER, values1);

                                                                                }

                                                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                                                                // ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                                                //query.fromLocalDatastore();

                                                                                query.whereEqualTo("fbid", followingid1);

                                                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                                                    @Override
                                                                                    public void done(List<ParseObject> list, ParseException e) {


                                                                                        if (list != null) {

                                                                                            if(list.size() > 0) {
                                                                                                Log.e("list size check ","list size check in list size 0");
                                                                                                Log.e("list size check ","list size check in list size 0 ");
                                                                                                Log.e("list size check ","list size check in list size 0 ");
                                                                                                aboutme_friend = list.get(0).get("aboutme").toString();
                                                                                                genres_friend = list.get(0).get("genres").toString();
                                                                                                picture_friend = list.get(0).get("picture").toString();
                                                                                                rating_friend = list.get(0).get("rating").toString();

                                                                                                String fbid_friend = followingid1;
                                                                                                String name_friend = followingname1;
                                                                                                String isfollowing_friend = "false";

                                                                                                ContentValues values = new ContentValues();


                                                                                                values.put(UserProvider.FBID_FRIEND, fbid_friend);
                                                                                                values.put(UserProvider.NAME_FRIEND, name_friend);
                                                                                                if(aboutme_friend != null)
                                                                                                    values.put(UserProvider.ABOUTME_FRIEND, aboutme_friend);
                                                                                                else
                                                                                                    values.put(UserProvider.ABOUTME_FRIEND, "Describe Yourself");
                                                                                                if(genres_friend != null)
                                                                                                    values.put(UserProvider.GENRES_FRIEND, genres_friend);
                                                                                                else
                                                                                                    values.put(UserProvider.GENRES_FRIEND, "Fav Genres");

                                                                                                if(rating_friend != null )
                                                                                                    values.put(UserProvider.RATING_FRIEND, rating_friend);
                                                                                                else
                                                                                                    values.put(UserProvider.RATING_FRIEND, "0");
                                                                                                if(picture_friend != null)
                                                                                                    values.put(UserProvider.URLPICTURE_FRIEND, picture_friend);
                                                                                                else
                                                                                                    values.put(UserProvider.URLPICTURE_FRIEND, " ");
                                                                                                values.put(UserProvider.IS_FOLLOWING, isfollowing_friend);
                                                                                                String where = UserProvider.USER_FRIENDS_TABLE_NAME + "." + UserProvider.FBID_FRIEND + " = ? ";

                                                                                                Uri uri = getApplicationContext().getContentResolver().insert(
                                                                                                        UserProvider.CONTENT_URI_FRIENDS, values);

                                                                                            }
                                                                                        }
                                                                                        //stopLoading();

                                                                                    }


                                                                                });

                                                                           // }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                    );

                                }
                            }
                        }
                        catch (JSONException e) {
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

    private ArrayList<Followers> frndlist;
    private boolean bool ;


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                CircleImageView myimage = (CircleImageView) findViewById(R.id.image_login);

                TextView myname = (TextView) findViewById(R.id.username_login);
                if(ParseUser.getCurrentUser() != null) {
                    myname.setText(ParseUser.getCurrentUser().get("name").toString());
                    Glide.with(myimage.getContext())
                            .load(ParseUser.getCurrentUser().get("picture").toString())
                            .fitCenter()
                            .into(myimage);
                }


                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_about_me:
                    {
                        Intent intent = new Intent(MainActivity.this, MyDetailActivity.class);
                        //intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.nav_movies:
                    {
                        setupViewPager(viewPager, "Movies");

                        return true;

                    }
                    case R.id.nav_logout: {
                        //Snackbar.make(mContentFrame, "Item Two", Snackbar.LENGTH_SHORT).show();
                        //mCurrentSelectedPosition = 1;
                        setupViewPager(viewPager, "LogOut");

                        return true;
                    }
                    case R.id.nav_friends: {

                        setupViewPager(viewPager, "FacebookFriends");

                        return true;
                    }
                    case R.id.nav_watched: {

                        setupViewPager(viewPager, "Watched/Watchlist");

                        return true;
                    }
                    case R.id.nav_my_reviews: {

                        setupViewPager(viewPager, "My Reviews");

                        return true;
                    }
                    case R.id.nav_my_friends_posts: {

                        setupViewPager(viewPager, "Friends Reviews");

                        return true;
                    }
                    default:
                        return true;
                    // return true;
                }
                //return true;
        };
    });
    }

    static class Adapter extends FragmentStatePagerAdapter {
        private  final List<Fragment> mFragments = new ArrayList<>();
        private  final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);

          //  mFragments = new ArrayList<>();
          //  mFragmentTitles = new ArrayList<>();

        }

        public void removeAllFragments()
        {
            //if(mFragments != null)
            //    mFragments = null;
            mFragments.clear();
           // mFragments.removeAll(mFragments);
        }
        public void addFragment(Fragment fragment, String title,int index) {
            //mFragments.add(index);
            if(mFragments.size() == 0 )
            {
                mFragments.add(index, fragment);
                mFragmentTitles.add(index, title);
            }
            else if( index > (mFragments.size()-1)  ) {
                mFragments.add(index, fragment);
                mFragmentTitles.add(index, title);
            }
            else if(index <= (mFragments.size()-1)){
                mFragments.set(index, fragment);
                mFragmentTitles.set(index, title);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}

// Async Task Class



