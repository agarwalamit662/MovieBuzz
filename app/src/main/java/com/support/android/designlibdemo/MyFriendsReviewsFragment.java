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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.codec.binary.StringUtils;
import com.support.android.designlibdemo.data.UserProvider;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyFriendsReviewsFragment extends Fragment implements SearchView.OnQueryTextListener{

    public static MyFriendsReviewsFragment mwf;
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

    private ArrayList<Followers> frndlist; // = null;
    private ArrayList<Followers> frndlist_new = null;
    private ArrayList<Movies> movies = null;
    private LayoutInflater inflater;
    private ViewGroup cont;
    private String mCategory;
    static public Context mContexts;
    private RecyclerView rv;// = (RecyclerView) inflater.inflate(
    //   R.layout.fragment_facebook_friends, container, false);
    public String message;


    public static final MyFriendsReviewsFragment newInstance(int title, String message)
    {
        MyFriendsReviewsFragment fragment = new MyFriendsReviewsFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("title", title);
        bundle.putString("mcategory", message);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        int title = getArguments().getInt("title");
        message = getArguments().getString("mcategory");
        mContexts = getActivity();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mwf = this;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.e("in on resume", "in on resume");
        Log.e("in on resume", "in on resume");
        rv =(RecyclerView) getActivity().findViewById(R.id.recyclerview_myfriendsreviews);
        findMovies(rv, "Action");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);


        final MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
       // searchView.setOnQueryTextListener(this);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {

        //mov = findMoviesLast;
        List<Movies> sample = findMovies(rv,"ghjg");
        Log.e("Sample Ki Size",String.valueOf(sample.size()));
        Log.e("Sample Ki Size",String.valueOf(sample.size()));
        Log.e("Sample Ki Size",String.valueOf(sample.size()));
        final List<Movies> filteredModelList = filter(sample, query);

        SimpleStringRecyclerViewAdapter adap = new SimpleStringRecyclerViewAdapter(getActivity(),
                sample);
        Log.e("Size of findMoviesLast",String.valueOf(filteredModelList.size()));
        Log.e("Size of findMoviesLast",String.valueOf(filteredModelList.size()));


        adap.animateTo(filteredModelList);
        rv.setAdapter(adap);
        rv.scrollToPosition(0);
        return true;


        //  return false;
    }

    private List<Movies> filter(List<Movies> models, String query) {
        query = query.toLowerCase();

        final List<Movies> filteredModelList = new ArrayList<>();
        for (Movies model : models) {
            final String text = model.getMname().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        Log.e("filteredModelList size",String.valueOf(filteredModelList.size()));
        Log.e("filteredModelList size", String.valueOf(filteredModelList.size()));
        Log.e("filteredModelList size", String.valueOf(filteredModelList.size()));
        return filteredModelList;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_myfriendsreviews, container, false);


        rv =(RecyclerView) rootView.findViewById(R.id.recyclerview_myfriendsreviews);

        //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
        findMovies(rv, "Action");


        return rootView;
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

    private ArrayList<Movies> findMovies(RecyclerView rv,String s)
    {

        movies = new ArrayList<>();
        final RecyclerView recyclerView =  rv;

        ParseUser pu = ParseUser.getCurrentUser();

        //Cursor locationquery1 =
        String sMovieTypeWithMovieID =
                UserProvider.USER_MOVIES_TABLE_NAME +
                        "." +
                        UserProvider._MOVIE_ISREVIEWED + " = ? AND "
                        + UserProvider._MOVIE_WATCHER + " != ? ";
                        //") ORDER BY DATETIME("+UserProvider._MOVIE_REVIEW_TIME;

        String sortOrder = "DATETIME("+ UserProvider._MOVIE_REVIEW_TIME + " ) DESC";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String today = dateFormat.format(date);

        Cursor locationCursor1 = getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI_MOVIES,
                FORECAST_MOVIE_COLUMNS,
                sMovieTypeWithMovieID,
                new String[]{"true",pu.get("fbid").toString()},
                sortOrder);
        int inserted = 0;

        if(locationCursor1.getCount() != 0 )
        {
            while (locationCursor1.moveToNext()) {


                String mid = locationCursor1.getString(1);
                String mname = locationCursor1.getString(2);
                String reldate = locationCursor1.getString(3);
                String mdes = locationCursor1.getString(5);
                String watcher = locationCursor1.getString(4);
                //String des = locationCursor1.getString(5);
                String url = locationCursor1.getString(6);
                String iswatched = locationCursor1.getString(7);
                String likes = locationCursor1.getString(8);
                String iswatchlist = locationCursor1.getString(9);
                String category = locationCursor1.getString(10);
                String rating = locationCursor1.getString(11);
                String isreviewed = locationCursor1.getString(12);
                String review = locationCursor1.getString(13);
                String movieonparse = locationCursor1.getString(14);
                String moviereviewtime = locationCursor1.getString(15);
                Movies mov = new Movies(mid,mname,reldate,watcher,mdes,url,iswatched,likes,iswatchlist,category,rating,isreviewed,review,movieonparse,moviereviewtime);
                movies.add(mov);

                if (getActivity() != null) {

                    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

                    recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                            movies));
                }

            }
        }
        return movies;
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        public SharedPreferences.Editor editorslikedwatched;
        public SharedPreferences.Editor editorlikedfriends;
        public SharedPreferences.Editor editorlikedcountfriends;
        private boolean isfollowing;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Movies> mValues;
        private Context mContext;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mMovieName;
            public final TextView mReview;
            public final RatingBar mRating;
            public final CardView card;
            public final TextView mReviewerName;
            public final TextView mReviewTime;
            public final CheckableImageView mLikesImage;
            public final TextView mLikes;
            public final TextView likersName;
            public final LinearLayout layoutLikes;
            //public final Button mAddReview;
            //public Switch follow;
            // public final ImageButton followbutton;
            // public final CheckableImageButton cib;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar_reviewer);
                mMovieName = (TextView) view.findViewById(R.id.mnamemyreviewsfriends);
                mReview = (TextView) view.findViewById(R.id.mnamereviewfriends);
                mRating = (RatingBar) view.findViewById(R.id.ratingBarMyReviewsfriends);
                mReviewerName = (TextView) view.findViewById(R.id.reviewername);
              //  mAddReview = (Button) view.findViewById(R.id.addreviewbutton);
                card = (CardView) view.findViewById(R.id.myfriendsreviewscardview);
                mReviewTime = (TextView) view.findViewById(R.id.mreviewtime);
                mLikesImage = (CheckableImageView) view.findViewById(R.id.likeimageview);
                mLikes = (TextView)view.findViewById(R.id.likes);
                likersName = (TextView) view.findViewById(R.id.likesNamesFriends);
                layoutLikes = (LinearLayout) view.findViewById(R.id.likerslayoutFriends);

                //cib = (CheckableImageButton) view.findViewById(R.id.followmovieimage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMovieName.getText();
            }
        }

        public Movies getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Movies> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_myfriendsreviews, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }
        private String username;
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            //rootView.setBackgroundColor(Color.rgb(192,192,192));
            //holder.card.setCardBackgroundColor(Color.rgb(192,192,192));
            holder.mBoundString = mValues.get(position).getMname().toString();
            holder.mMovieName.setText(mValues.get(position).getMname().toString());
            holder.mReview.setText(mValues.get(position).getReview().toString());
            holder.mReviewTime.setText(mValues.get(position).getMoviereviewtime().toString());
            boolean bool;
            final String likesInitial = mValues.get(position).getLikes().toString();
           // int count = StringUtils.countMatches("a.b.c.d", ".");
            final int count = mValues.get(position).getLikes().toString().length() - mValues.get(position).getLikes().toString().replace(",", "").length();
            Log.e("count ki val ",String.valueOf(count));
            Log.e("count ki val ",String.valueOf(count));
            final SharedPreferences countPref = mContext.getSharedPreferences("countPref", Context.MODE_PRIVATE);

            editorlikedcountfriends = countPref.edit();

            if(count != countPref.getInt("Count"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count))
            {

                Log.e("count ki val in if",String.valueOf(count)+" " +String.valueOf(countPref.getInt("Count"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count)));
                Log.e("count ki val in if",String.valueOf(count)+" " +String.valueOf(countPref.getInt("Count"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count)));
                Log.e("count ki val in if",String.valueOf(count));
                editorlikedcountfriends.putString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), mValues.get(position).getLikes().toString());
                editorlikedcountfriends.commit();
                editorlikedcountfriends.putInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), count);
                editorlikedcountfriends.commit();
                holder.mLikes.setText("Likes +" + String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), count)));

                holder.mLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.layoutLikes.setVisibility(View.VISIBLE);
                        holder.likersName.setVisibility(View.VISIBLE);
                        String[] arr = likesInitial.split(",");
                        String likers = "";
                        if (arr.length == 1) {
                            likers = "None";
                        }
                        for (int i = 0; i < arr.length; i++) {

                            if (i > 0 && i != arr.length - 1) {

                                //likers = likers.concat(arr[i] + " , ");
                                String where = UserProvider.FOLLOWING + " = ? ";
                                String wheres[] = {arr[i]};
                                Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER, new String[]{UserProvider.NAME_FOLLOWING}, where, wheres, null);
                                if (c.getCount() > 0) {
                                    while (c.moveToNext()) {
                                        likers = likers.concat(c.getString(0) + " , ");
                                    }
                                }
                            } else if (i != 0 && i == arr.length - 1) {
                                // likers = likers.concat(arr[i]);
                                String where = UserProvider.FOLLOWING + " = ? ";
                                String wheres[] = {arr[i]};
                                Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER, new String[]{UserProvider.NAME_FOLLOWING}, where, wheres, null);
                                if (c.getCount() > 0) {
                                    while (c.moveToNext()) {
                                        likers = likers.concat(c.getString(0));
                                    }
                                }
                            }
                        }
                        holder.likersName.setText(likers);

                    }
                });


            }

            else
            {

                countPref.getString("LikeString"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),mValues.get(position).getLikes().toString());
                holder.mLikes.setText("Likes +" + String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), count)));
                Log.e("count ki val in else", String.valueOf(count) + " " + String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), count)));
                Log.e("count ki val in else", String.valueOf(count) + " " + String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString() + ParseUser.getCurrentUser().get("fbid").toString(), count)));
                Log.e("count ki val in else", String.valueOf(count));

                holder.mLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.layoutLikes.setVisibility(View.VISIBLE);
                        holder.likersName.setVisibility(View.VISIBLE);
                        String[] arr = likesInitial.split(",");
                        String likers = "";
                        if (arr.length == 1) {
                            likers = "None";
                        }
                        for (int i = 0; i < arr.length; i++) {

                            if (i > 0 && i != arr.length - 1) {

                                //likers = likers.concat(arr[i] + " , ");
                                String where = UserProvider.FOLLOWING + " = ? ";
                                String wheres[] = {arr[i]};
                                Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER, new String[]{UserProvider.NAME_FOLLOWING}, where, wheres, null);
                                if (c.getCount() > 0) {
                                    while (c.moveToNext()) {
                                        likers = likers.concat(c.getString(0) + " , ");
                                    }
                                }
                            } else if (i != 0 && i == arr.length - 1) {
                                // likers = likers.concat(arr[i]);
                                String where = UserProvider.FOLLOWING + " = ? ";
                                String wheres[] = {arr[i]};
                                Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER, new String[]{UserProvider.NAME_FOLLOWING}, where, wheres, null);
                                if (c.getCount() > 0) {
                                    while (c.moveToNext()) {
                                        likers = likers.concat(c.getString(0));
                                    }
                                }
                            }
                        }
                        holder.likersName.setText(likers);

                    }
                });

            }

         //   editorlikedcountfriends.putString("LikeString"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),mValues.get(position).getLikes().toString());
         //   editorlikedcountfriends.commit();
           // countPref.getString("LikeString"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),mValues.get(position).getLikes().toString());
          //  holder.mLikes.setText(String.valueOf(countPref.getInt("Count"+ mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count)));


            if(mValues.get(position).getLikes().toString().contains(ParseUser.getCurrentUser().get("fbid").toString()))
            {
                Log.e("contains user","contains");
                Log.e("contains user","contains");
                bool = true;

            }
            else
            {
                Log.e("doesn't contains user","contains");
                Log.e("doesn't contains user","contains");
                bool = false;
            }

            final SharedPreferences likedPref = mContext.getSharedPreferences("likedPref", Context.MODE_PRIVATE);

            editorlikedfriends = likedPref.edit();


            username = "hello";
             final Resources resources = mContext.getResources();
            holder.mLikesImage.setChecked(likedPref.getBoolean(username + mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), bool));

            if (likedPref.getBoolean(username + mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), bool)) {
                // 154-205-50
                //holder.mLikesImage.setBackgroundColor(Color.rgb(154, 205, 50));
                Log.e("in if","in if");
                Log.e("in if","in if");

                holder.mLikesImage.setImageDrawable(resources.getDrawable(R.drawable.ic_likeorange));
                //holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_watched));
            } else {
                //holder.cib.setBackgroundColor(Color.rgb(59, 89, 152));
                //holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_follow));
                holder.mLikesImage.setImageDrawable(resources.getDrawable(R.drawable.ic_like));
                Log.e("in else","in else");
                Log.e("in else", "in else");
            }

            holder.mLikesImage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckableImageView) v).isChecked()) {
                        editorlikedfriends.putBoolean(username + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), true);
                        editorlikedfriends.commit();
                        holder.mLikesImage.setImageDrawable(resources.getDrawable(R.drawable.ic_likeorange));

                        editorlikedcountfriends.putString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), mValues.get(position).getLikes().toString() + "," + ParseUser.getCurrentUser().get("fbid").toString());
                        editorlikedcountfriends.commit();
                        ContentValues values = new ContentValues();
                        final String likes = countPref.getString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),"skej");
                                //mValues.get(position).getLikes().toString()+","+mValues.get(position).getWatcher().toString();
                        values.put(UserProvider._MOVIE_LIKES, likes);
                        //holder.mLikes.setText(String.valueOf(count + 1));


                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                       // query.fromLocalDatastore();
                        query.whereEqualTo("watcher", mValues.get(position).getWatcher().toString());
                        query.whereEqualTo("mid", mValues.get(position).getMovieid().toString());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if(list != null && list.size() > 0)
                                {
                                    String addToLikes = "," + ParseUser.getCurrentUser().get("fbid").toString();
                                    String oldLikes = list.get(0).get("likes").toString();
                                    String newLikes = oldLikes+addToLikes;
                                    list.get(0).put("likes",newLikes);
                                    list.get(0).pinInBackground();
                                    list.get(0).saveEventually();

                                }

                            }
                        });


                        ParseUser users = ParseUser.getCurrentUser();
                        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("FollowMovieBuzz");
                        query1.fromLocalDatastore();
                        query1.whereEqualTo("follower", users.get("fbid").toString());
                        query1.whereEqualTo("following",mValues.get(position).getWatcher().toString());
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if (list == null) {

                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");

                                }
                                else if(list.size() == 0)
                                {

                                    Log.e("size 0","size 0 ");
                                    Log.e("size 0","size 0");
                                }

                                if(list != null && list.size() > 0)
                                    for (int i = 0; i < list.size(); i++) {
                                        JSONObject jsonObject;
                                        try {


                                            Log.e("No of pushes", "No of pushes");
                                            Log.e("No of pushes", "No of pushes");


                                            //"{\"data\": {\"message\": \"Review Added\",\"title\": \""+ParseUser.getCurrentUser().get("fbid").toString()+"\"},\"is_background\": true}"

                                            jsonObject = new JSONObject("{\"data\": {\"message\": \"Likes Added\",\"title\": \"" + ParseUser.getCurrentUser().get("fbid").toString() + "\"},\"is_background\": true}");
                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();
                                            query.whereNotEqualTo("user", ParseUser.getCurrentUser().get("fbid").toString());
                                            query.whereEqualTo("user", list.get(i).get("following").toString());
                                            query.whereEqualTo("deviceType", "android");
                                            push.setQuery(query);
                                            push.setData(jsonObject);
                                            push.sendInBackground();
                                        } catch (Exception e1) {
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                        }

                                    }


                            }
                        });

                        ParseUser usersall = ParseUser.getCurrentUser();
                        ParseQuery<ParseObject> query1all = new ParseQuery<ParseObject>("FollowMovieBuzz");
                       // query1all.fromLocalDatastore();
                       // query1all.whereEqualTo("follower", users.get("fbid").toString());
                        query1all.whereEqualTo("follower",mValues.get(position).getWatcher().toString());
                        query1all.whereNotEqualTo("following",users.get("fbid").toString());
                        query1all.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if (list == null) {

                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");

                                }
                                else if(list.size() == 0)
                                {

                                    Log.e("size 0","size 0 ");
                                    Log.e("size 0","size 0");
                                }

                                if(list != null && list.size() > 0)
                                    for (int i = 0; i < list.size(); i++) {
                                        JSONObject jsonObject;
                                        try {


                                            Log.e("No of pushes", "No of pushes");
                                            Log.e("No of pushes", "No of pushes");


                                            //"{\"data\": {\"message\": \"Review Added\",\"title\": \""+ParseUser.getCurrentUser().get("fbid").toString()+"\"},\"is_background\": true}"

                                            jsonObject = new JSONObject("{\"data\": {\"message\": \"Friends Added\",\"title\": \"" + mValues.get(position).getWatcher().toString() + "\"},\"is_background\": true}");
                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();
                                            query.whereNotEqualTo("user", ParseUser.getCurrentUser().get("fbid").toString());
                                            query.whereEqualTo("user", list.get(i).get("following").toString());
                                            query.whereEqualTo("deviceType", "android");
                                            push.setQuery(query);
                                            push.setData(jsonObject);
                                            push.sendInBackground();
                                        } catch (Exception e1) {
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                        }

                                    }


                            }
                        });



                        final int counts = likes.length() - likes.replace(",", "").length();
                        editorlikedcountfriends.putInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), counts);
                        editorlikedcountfriends.commit();
                        holder.mLikes.setText(String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count)));
                       // final int counts = mValues.get(position).getLikes().toString().length() - mValues.get(position).getLikes().toString().replace(",", "").length();
                        String where = UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._MOVIE_ID + " = ? AND "+
                                UserProvider._MOVIE_WATCHER +" =  "+mValues.get(position).getWatcher().toString();

                        int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_MOVIES, values, where, new String[]{mValues.get(position).getMovieid().toString()});
                        holder.mLikes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.layoutLikes.setVisibility(View.VISIBLE);
                                holder.likersName.setVisibility(View.VISIBLE);
                                String[] arr = likes.split(",");
                                String likers = "";
                                if(arr.length == 1)
                                {
                                    likers = "None";
                                }
                                for(int i = 0 ; i< arr.length; i++)
                                {

                                    if(i > 0 && i != arr.length-1) {

                                        //likers = likers.concat(arr[i] + " , ");
                                        String where = UserProvider.FOLLOWING + " = ? ";
                                        String wheres[] = {arr[i]};
                                        Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER,new String[]{UserProvider.NAME_FOLLOWING},where,wheres,null);
                                        if(c.getCount() > 0)
                                        {
                                            while(c.moveToNext())
                                            {
                                                likers = likers.concat(c.getString(0)+ " , ");
                                            }
                                        }
                                    }
                                    else if(i != 0 && i == arr.length-1)
                                    {
                                        // likers = likers.concat(arr[i]);
                                        String where = UserProvider.FOLLOWING + " = ? ";
                                        String wheres[] = {arr[i]};
                                        Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER,new String[]{UserProvider.NAME_FOLLOWING},where,wheres,null);
                                        if(c.getCount() > 0)
                                        {
                                            while(c.moveToNext()){
                                                likers = likers.concat(c.getString(0));
                                            }
                                        }
                                    }
                                }
                                holder.likersName.setText(likers);

                            }
                        });


                    }
                    else
                    {

                        editorlikedfriends.putBoolean(username + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), false);
                        editorlikedfriends.commit();


                        holder.mLikesImage.setImageDrawable(resources.getDrawable(R.drawable.ic_like));

                        //ContentValues values = new ContentValues();
                        String like = mValues.get(position).getLikes().toString()+","+mValues.get(position).getWatcher().toString();
                        final String likes = countPref.getString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),"sdj").replace(","+ParseUser.getCurrentUser().get("fbid").toString(),"");


                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                       // query.fromLocalDatastore();
                        query.whereEqualTo("watcher", mValues.get(position).getWatcher().toString());
                        query.whereEqualTo("mid", mValues.get(position).getMovieid().toString());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if(list != null && list.size() > 0)
                                {
                                   // String addToLikes = "," + ParseUser.getCurrentUser().get("fbid").toString();
                                    String oldLikes = list.get(0).get("likes").toString().replace("," + ParseUser.getCurrentUser().get("fbid").toString(), "");
                                    String newLikes = oldLikes;
                                    list.get(0).put("likes",oldLikes);

                                    // /list.get(0).put("likes",likes);
                                    list.get(0).pinInBackground();
                                    list.get(0).saveEventually();

                                }

                            }
                        });


                        ParseUser users = ParseUser.getCurrentUser();
                        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("FollowMovieBuzz");
                        query1.fromLocalDatastore();
                        query1.whereEqualTo("follower", users.get("fbid").toString());
                        query1.whereEqualTo("following",mValues.get(position).getWatcher().toString());
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if (list == null) {

                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");

                                }
                                else if(list.size() == 0)
                                {

                                    Log.e("size 0","size 0 ");
                                    Log.e("size 0","size 0");
                                }

                                if(list != null && list.size() > 0)
                                    for (int i = 0; i < list.size(); i++) {
                                        JSONObject jsonObject;
                                        try {


                                            Log.e("No of pushes", "No of pushes");
                                            Log.e("No of pushes", "No of pushes");


                                            //"{\"data\": {\"message\": \"Review Added\",\"title\": \""+ParseUser.getCurrentUser().get("fbid").toString()+"\"},\"is_background\": true}"

                                            jsonObject = new JSONObject("{\"data\": {\"message\": \"Likes Added\",\"title\": \"" + ParseUser.getCurrentUser().get("fbid").toString() + "\"},\"is_background\": true}");
                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();
                                            query.whereNotEqualTo("user", ParseUser.getCurrentUser().get("fbid").toString());
                                            query.whereEqualTo("user", list.get(i).get("following").toString());
                                            query.whereEqualTo("deviceType", "android");
                                            push.setQuery(query);
                                            push.setData(jsonObject);
                                            push.sendInBackground();
                                        } catch (Exception e1) {
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                        }

                                    }


                            }
                        });


                        ParseUser usersall = ParseUser.getCurrentUser();
                        ParseQuery<ParseObject> query1all = new ParseQuery<ParseObject>("FollowMovieBuzz");
                        //query1.fromLocalDatastore();
                        //query1all.whereEqualTo("follower", users.get("fbid").toString());
                        query1all.whereEqualTo("follower",mValues.get(position).getWatcher().toString());
                        query1all.whereNotEqualTo("following",users.get("fbid").toString());
                        query1all.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                if (list == null) {

                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");
                                    Log.e("list is null","list null");

                                }
                                else if(list.size() == 0)
                                {

                                    Log.e("size 0","size 0 ");
                                    Log.e("size 0","size 0");
                                }

                                if(list != null && list.size() > 0)
                                    for (int i = 0; i < list.size(); i++) {
                                        JSONObject jsonObject;
                                        try {


                                            Log.e("No of pushes", "No of pushes");
                                            Log.e("No of pushes", "No of pushes");


                                            //"{\"data\": {\"message\": \"Review Added\",\"title\": \""+ParseUser.getCurrentUser().get("fbid").toString()+"\"},\"is_background\": true}"

                                            jsonObject = new JSONObject("{\"data\": {\"message\": \"Friends Added\",\"title\": \"" + mValues.get(position).getWatcher().toString() + "\"},\"is_background\": true}");
                                            ParsePush push = new ParsePush();
                                            ParseQuery query = ParseInstallation.getQuery();
                                            query.whereNotEqualTo("user", ParseUser.getCurrentUser().get("fbid").toString());
                                            query.whereEqualTo("user", list.get(i).get("following").toString());
                                            query.whereEqualTo("deviceType", "android");
                                            push.setQuery(query);
                                            push.setData(jsonObject);
                                            push.sendInBackground();
                                        } catch (Exception e1) {
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                            Log.e("Parse", "An exception has occured!");
                                        }

                                    }


                            }
                        });


                        editorlikedcountfriends.putString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), likes);
                        editorlikedcountfriends.commit();
                        ContentValues values = new ContentValues();
                        //String likes = countPref.getString("LikeString" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString(),"skej");
                        //mValues.get(position).getLikes().toString()+","+mValues.get(position).getWatcher().toString();
                        values.put(UserProvider._MOVIE_LIKES, likes);
                        //holder.mLikes.setText(String.valueOf(count + 1));
                        final int counts = likes.length() - likes.replace(",", "").length();
                        editorlikedcountfriends.putInt("Count" + mValues.get(position).getWatcher().toString() + mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(), counts);
                        editorlikedcountfriends.commit();
                        holder.mLikes.setText("Likes +"+String.valueOf(countPref.getInt("Count" + mValues.get(position).getWatcher().toString()+mValues.get(position).getMovieid().toString()+ParseUser.getCurrentUser().get("fbid").toString(),count)));


                        String where = UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._MOVIE_ID + " = ? AND "+
                                UserProvider._MOVIE_WATCHER +" =  "+mValues.get(position).getWatcher().toString();

                        int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_MOVIES, values, where, new String[]{mValues.get(position).getMovieid().toString()});

                        holder.mLikes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.layoutLikes.setVisibility(View.VISIBLE);
                                holder.likersName.setVisibility(View.VISIBLE);
                                String[] arr = likes.split(",");
                                String likers = "";
                                if(arr.length == 1)
                                {
                                    likers = "None";
                                }
                                for(int i = 0 ; i< arr.length; i++)
                                {

                                    if(i > 0 && i != arr.length-1) {

                                        //likers = likers.concat(arr[i] + " , ");
                                        String where = UserProvider.FOLLOWING + " = ? ";
                                        String wheres[] = {arr[i]};
                                        Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER,new String[]{UserProvider.NAME_FOLLOWING},where,wheres,null);
                                        if(c.getCount() > 0)
                                        {
                                            while(c.moveToNext())
                                            {
                                                likers = likers.concat(c.getString(0)+ " , ");
                                            }
                                        }
                                    }
                                    else if(i != 0 && i == arr.length-1)
                                    {
                                        // likers = likers.concat(arr[i]);
                                        String where = UserProvider.FOLLOWING + " = ? ";
                                        String wheres[] = {arr[i]};
                                        Cursor c = mContext.getContentResolver().query(UserProvider.CONTENT_URI_FOLLOWER,new String[]{UserProvider.NAME_FOLLOWING},where,wheres,null);
                                        if(c.getCount() > 0)
                                        {
                                            while(c.moveToNext()){
                                                likers = likers.concat(c.getString(0));
                                            }
                                        }
                                    }
                                }
                                holder.likersName.setText(likers);

                            }
                        });

                    }
                }
            });


            String sMovieTypeWithMovieID = UserProvider.FBID_FRIEND + " = ? ";

            Cursor locationCursor1 = mContext.getContentResolver().query(
                    UserProvider.CONTENT_URI_FRIENDS,
                    new String[]{UserProvider.NAME_FRIEND,UserProvider.URLPICTURE_FRIEND},
                    sMovieTypeWithMovieID,
                    new String[]{mValues.get(position).getWatcher().toString()},
                    null);
            int inserted = 0;

            if(locationCursor1.getCount() != 0 )
            {
                while (locationCursor1.moveToNext()) {

                    holder.mReviewerName.setText(locationCursor1.getString(0));
                    Glide.with(holder.mImageView.getContext())
                            .load(locationCursor1.getString(1).toString())
                            .fitCenter()
                            .into(holder.mImageView);

                    break;
                }
            }

                    //holder.mReviewerName.setText(mValues.get(position).getWatcher().toString());
            //final String imageurl = mValues.get(position).getUrl().toString();
            holder.mRating.setRating(Float.parseFloat(mValues.get(position).getRating().toString()));



            /*
            if(!imageurl.equals("notavailable")) {
                Glide.with(holder.mImageView.getContext())
                        .load(mValues.get(position).getUrl().toString())
                        .fitCenter()
                        .into(holder.mImageView);
            }
            else
            {
                Glide.with(holder.mImageView.getContext())
                        .load(R.drawable.com_facebook_close)
                        .fitCenter()
                        .into(holder.mImageView);

            }

            */

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    // Intent intent = new Intent(context, MyFriendDetailActivity.class);
                    // intent.putExtra(MyFriendDetailActivity.EXTRA_NAME, frndid);

                    // context.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void animateTo(List<Movies> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateAdditions(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<Movies> newModels) {
            for (int i = mValues.size() - 1; i >= 0; i--) {
                final Movies model = mValues.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<Movies> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final Movies model = newModels.get(i);
                if (!mValues.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<Movies> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final Movies model = newModels.get(toPosition);
                final int fromPosition = mValues.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public  Movies removeItem(int position) {
            final Movies model = mValues.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addItem(int position, Movies model) {
            mValues.add(position, model);
            notifyItemInserted(position);
        }

        public void moveItem(int fromPosition, int toPosition) {
            final Movies model = mValues.remove(fromPosition);
            mValues.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }
    }
}
