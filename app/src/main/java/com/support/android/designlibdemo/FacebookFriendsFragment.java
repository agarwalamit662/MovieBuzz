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

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.support.android.designlibdemo.data.UserProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FacebookFriendsFragment extends Fragment {



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
            UserProvider._MOVIE_ON_PARSE

    };
    private static final String[] FORECAST_FRIENDS_COLUMNS = {

            UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider._ID_FOLLOWER,
            UserProvider.FOLLOWER,
            UserProvider.FOLLOWING,
            UserProvider.NAME_FOLLOWING,
            UserProvider.IS_FOLLOWING_FOLLOWER,
            UserProvider.FOLLOWING_URL


};

    private ArrayList<Followers> frndlist; // = null;
    private ArrayList<Followers> frndlist_new = null;
    private ArrayList<Followers> friends;
    private LayoutInflater inflater;
    private ViewGroup cont;
    private RecyclerView rv;// = (RecyclerView) inflater.inflate(
         //   R.layout.fragment_facebook_friends, container, false);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_facebook_friends, container, false);
        //setupRecyclerView(rv);
        //cont = container;
        rv.addItemDecoration(new DividerItemDecoration(getActivity()));
        findFriends(rv, "freiends");
        //MainActivity ma = new MainActivity();
        //ma.addUpdateMyFriendDetailsIntoDb();

        //findfriendsfast(rv);
        return rv;
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        //inflater = LayoutInflater.from(getActivity());
        //frndlist_new = findfriendsfast();
       // rv = (RecyclerView) inflater.inflate(
       //         R.layout.fragment_facebook_friends, cont, false);
        findfriendsfast(rv);
    }
    */





    private void setupRecyclerView(RecyclerView recyclerView) {


        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
        //        findfriendsfast()));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }
    private ProgressDialog proDialog;
    protected void startLoading() {
        proDialog = new ProgressDialog(this.getActivity());
        proDialog.setMessage("Loading Friends");
        proDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }



    private ArrayList<Followers> findFriends(RecyclerView rv, String s)
    {

        friends = new ArrayList<>();
       // recyclerView =  rv;
        final RecyclerView recyclerView =  rv;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String today = dateFormat.format(date);

        Cursor locationCursor1 = getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI_FOLLOWER,
                FORECAST_FRIENDS_COLUMNS,
                null,
                null,
                null);
        int inserted = 0;

        if(locationCursor1.getCount() != 0 )
        {
            while (locationCursor1.moveToNext()) {




                String followingid = locationCursor1.getString(2);
                String followingname1 = locationCursor1.getString(3);
                String isfollowing = locationCursor1.getString(4);
                boolean bool;
                if(isfollowing.equals("true"))
                    bool = true;
                else
                    bool = false;
                String url = locationCursor1.getString(5);
                Followers f = new Followers(followingid, followingname1, bool, url);
                if (f != null) {


                    friends.add(f);
                    if (getActivity() != null) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

                        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                                friends));
                    }

                }


            }
        }
        return friends;
    }

    private ArrayList<Followers> findfriendsfast(RecyclerView rv){

        frndlist =  new ArrayList<>();
        //final ParseObject followers = new ParseObject("Follow");
        final RecyclerView recyclerView =  rv;
        final ParseUser user1 = ParseUser.getCurrentUser();

        if(user1 != null && user1.get("fbid") != null) {

            startLoading();
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("FollowMovieBuzz");
            query1.whereEqualTo("follower", user1.get("fbid").toString());

            query1.fromLocalDatastore();
            query1.findInBackground(new FindCallback<ParseObject>() {
                List<String> newlist;
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    newlist = new ArrayList<String>();
                    //String));
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                ParseObject obj = list.get(i);

                                String followingid = obj.get("following").toString();
                                if(newlist.contains(followingid) == false) {
                                    newlist.add(followingid);

                                    boolean bool = obj.getBoolean("isfollowing");
                                    String followingname1 = obj.get("namefollowing").toString();
                                    String url = obj.get("picture").toString();

                                    Followers f = new Followers(followingid, followingname1, bool, url);
                                    if (f != null) {


                                        if (frndlist.contains(f.getid()) != true) {

                                            frndlist.add(f);


                                        } else if (frndlist.contains(f.getid()) == true) {

                                        }
                                        if (getActivity() != null) {
                                            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

                                            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                                                    frndlist));
                                        }

                                    }

                                }
                            }
                        }
                        stopLoading();
                    }

                });
            }
        return frndlist;

    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        public String nameFollowing;
        public String urlFriend;
        public SharedPreferences.Editor editorfriends;
        private boolean isfollowing;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Followers> mValues;
        private Context mContext;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            //public Switch follow;
           // public final ImageButton followbutton;
            public final CheckableImageButton cib;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar_fb_frnds);
                mTextView = (TextView) view.findViewById(R.id.text1_fb_frnds);
              //  follow = (Switch) view.findViewById(R.id.followbutton);
               // followbutton = (ImageButton)view.findViewById(R.id.followbuttonimage);
                //followbutton = CheckableImageButton;

                 cib = (CheckableImageButton) view.findViewById(R.id.followbuttonimage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public Followers getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Followers> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_fb_frnds, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }
        private String username;
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position).getname().toString();
            holder.mTextView.setText(mValues.get(position).getname().toString());
            final String frndid = mValues.get(position).getid().toString();
            final boolean bool = mValues.get(position).getFollowing();
            final SharedPreferences friendsPref = mContext.getSharedPreferences("friendsPref", Context.MODE_PRIVATE);
            nameFollowing = mValues.get(position).getname().toString();

            urlFriend = mValues.get(position).getUrlFriend().toString();
            editorfriends = friendsPref.edit();


            username = "hello";
            final Resources resources = mContext.getResources();
            holder.cib.setChecked(friendsPref.getBoolean(username + frndid, bool));

           // holder.followbutton.setActivated(friendsPref.getBoolean(username + frndid, bool));
           // holder.followbutton.s
            if(friendsPref.getBoolean(username + frndid, bool)) {
                //holder.followbutton.setBackgroundColor(Color.rgb(1, 50, 32));
                holder.cib.setBackgroundColor(Color.rgb(1, 50, 32));
                holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_following__1443588011));
            }
            else {
                holder.cib.setBackgroundColor(Color.rgb(59, 89, 152));
                holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_follow));
            }
           // holder.followbutton.setOnClickListener(new View.OnClickListener(){
            holder.cib.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    if (((CheckableImageButton) v).isChecked()) {
                        editorfriends.putBoolean(username + frndid, true);
                        // viewHolder.follow.setClickable(true);

                        editorfriends.putString(frndid + "hell", "Following");
                        editorfriends.commit();

                        final ParseObject followers = new ParseObject("FollowMovieBuzz");
                        //    followers.put("follower", 1337);
                        //    followers.put("playerName", "Sean Plott");


                        String sMovieTypeWithMovieID =
                                UserProvider.USER_FOLLOWING_TABLE_NAME +
                                        "." +
                                        UserProvider.FOLLOWER + " = ?  ";


                        final ParseUser user = ParseUser.getCurrentUser();
                        ContentValues values = new ContentValues();
                        values.put(UserProvider.IS_FOLLOWING_FOLLOWER, "true");
                       // values.put(UserProvider._MOVIE_WATCHER, user.get("fbid").toString());
                       // values.put(UserProvider._MOVIE_ON_PARSE, "true");
                       // values.put(UserProvider._MOVIE_ISWATCHLIST, "false");
                        String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FOLLOWING + " = ? ";

                        int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_FOLLOWER, values, where, new String[]{frndid});
                        // locationCursor1.close();



                       // final ParseUser user = ParseUser.getCurrentUser();
                        final String followingid = frndid;
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("FollowMovieBuzz");
                        query.whereEqualTo("follower", user.get("fbid").toString());
                        query.whereEqualTo("following", frndid);
                        query.fromLocalDatastore();
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, com.parse.ParseException e) {

                                if (list != null) {
                                    if (list.size() == 1) {

                                        Log.e("Hello world","Hello World");
                                        Log.e("Hello world","Hello World");
                                        Log.e("Hello world","Hello World");

                                        ParseObject obj = list.get(0);
                                        obj.put("isfollowing", true);
                                        //obj.unpinInBackground();
                                        obj.pinInBackground();
                                        obj.saveEventually();
                                       // obj.saveInBackground();
                                    }
                                    else if(list.size() == 0)
                                    {
                                        followers.put("follower", ParseUser.getCurrentUser().get("fbid").toString());
                                        followers.put("following", frndid);
                                        followers.put("isfollowing", true);
                                        followers.put("namefollowing", nameFollowing);
                                        followers.put("picture", urlFriend);
                                        followers.pinInBackground();
                                        followers.saveEventually();

                                    }

                                }
                            }
                        });

                        //holder.followbutton.setBackgroundColor(Color.rgb(1, 50, 32));
                        holder.cib.setBackgroundColor(Color.rgb(1, 50, 32));
                        holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_following__1443588011));
                        //resources.getDrawable(R.drawable.ic_following__1443588011);

                        final ParseUser pu = ParseUser.getCurrentUser();
                        ParseObject obj = new ParseObject("movielist");

                        ParseQuery<ParseObject> querys = new ParseQuery<ParseObject>("movielist");
                        querys.whereEqualTo("watcher",frndid);
                        querys.findInBackground(new FindCallback<ParseObject>() {
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

                                          //  removeDuplicates();
                                        }

                                    }
                                }


                            }
                        });




                    } else {
                       // holder.followbutton.setBackgroundColor(Color.rgb(59,89,152));
                        holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_follow));
                        holder.cib.setBackgroundColor(Color.rgb(59,89,152));
                        editorfriends.putBoolean(username + frndid, false);
                        editorfriends.putString(frndid + "hell", "Follow");
                        editorfriends.commit();


                        final ParseUser user = ParseUser.getCurrentUser();
                        ContentValues values = new ContentValues();
                        values.put(UserProvider.IS_FOLLOWING_FOLLOWER, "false");
                        // values.put(UserProvider._MOVIE_WATCHER, user.get("fbid").toString());
                        // values.put(UserProvider._MOVIE_ON_PARSE, "true");
                        // values.put(UserProvider._MOVIE_ISWATCHLIST, "false");
                        String where = UserProvider.USER_FOLLOWING_TABLE_NAME + "." + UserProvider.FOLLOWING + " = ? ";

                        int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_FOLLOWER, values, where, new String[]{frndid});


                        final ParseObject followers = new ParseObject("FollowMovieBuzz");
                        //    followers.put("follower", 1337);
                        //    followers.put("playerName", "Sean Plott");

                       // final ParseUser user = ParseUser.getCurrentUser();
                        final String followingid = frndid;
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("FollowMovieBuzz");
                        query.whereEqualTo("follower", user.get("fbid").toString());
                        query.whereEqualTo("following", frndid);
                        query.fromLocalDatastore();
                       // query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, com.parse.ParseException e) {

                                if (list != null) {
                                    if (list.size() == 1) {

                                        ParseObject obj = list.get(0);
                                        obj.deleteEventually();
                                        //obj.put("isfollowing", false);
                                       // obj.pinInBackground();
                                        //obj.saveInBackground();
                                        //obj.unpinInBackground();
                                       // obj.saveEventually();
                                    }
                                }

                            }
                        });


                        String wheres = UserProvider._MOVIE_WATCHER+ " = ? ";
                        int is = mContext.getContentResolver().delete(UserProvider.CONTENT_URI_MOVIES,wheres,new String[]{frndid});

                        /*
                        String sMovieTypeWithMovieID =
                                UserProvider.USER_MOVIES_TABLE_NAME +
                                        "." +
                             UserProvider._MOVIE_WATCHER + " = ? ";


                        Cursor locationCursor1 = mContext.getContentResolver().query(
                                UserProvider.CONTENT_URI_MOVIES,
                                FORECAST_MOVIE_COLUMNS,
                                sMovieTypeWithMovieID,
                                new String[]{frndid},
                                null);
                        int inserted = 0;

                        if (locationCursor1.getCount() != 0) {

                            while(locationCursor1.moveToNext())
                            {
                                String wheres = UserProvider.FBID_FRIEND+ " = ? ";
                                //

                            }


                            locationCursor1.close();

                        }
                        */


                    }


                }


            });


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MyFriendDetailActivity.class);
                    intent.putExtra(MyFriendDetailActivity.EXTRA_NAME, frndid);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(mValues.get(position).getUrlFriend().toString())
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
