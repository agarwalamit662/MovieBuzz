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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class MovieUpcomingFragment extends Fragment implements SearchView.OnQueryTextListener{
    public static MovieUpcomingFragment mlf;
    public String val;
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
    private RecyclerView rv;// = (RecyclerView) inflater.inflate(
    //   R.layout.fragment_facebook_friends, container, false);
    public String message;


    public static final MovieUpcomingFragment newInstance(int title, String message)
    {
        MovieUpcomingFragment fragment = new MovieUpcomingFragment();
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


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {

        //mov = findMoviesLast;
        sp2.setSelection(str1.length-1);
        List<Movies> sample = findMoviesAll(rv,val);
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

    final String[] str1={"Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","Foreign","History"
            ,"Horror","Music","Mystery","Romance","Science Fiction","TV Movie","Thriller","War","Western","Others","All"};

    public Spinner sp2;
    public ArrayAdapter<String> adp2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_movies_upcomingmonth, container, false);
        //rv = (RecyclerView) inflater.inflate(
        //        R.layout.fragment_facebook_friends, container, false);
        rv =(RecyclerView) rootView.findViewById(R.id.recyclerview_movies_upcoming);
        //setupRecyclerView(rv);
        //cont = container;
        rv.addItemDecoration(new DividerItemDecoration(getActivity()));
        findMoviesAll(rv, "All");


        sp2 = (Spinner) rootView.findViewById(R.id.spinnermovieupcoming);
        //final Spinner sp2= (Spinner) findViewById(R.id.spinner2);

        adp2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,str1);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adp2);
        sp2.setSelection(str1.length-1);


        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                // Toast.makeText(getBaseContext(),list.get(position), Toast.LENGTH_SHORT).show();

                 val = str1[position];

                if(!val.equals("All"))
                    findMovies(rv,val);
                else if(val.equals("All"))
                    findMoviesAll(rv,val);



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        return rootView;
    }




    private ProgressDialog proDialog;
    protected void startLoading() {
        proDialog = new ProgressDialog(this.getActivity());
        proDialog.setMessage("Loading Movies");
        proDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);


        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


    }

    private ArrayList<Movies> findMovies(RecyclerView rv,String s)
    {

        movies = new ArrayList<>();
        final RecyclerView recyclerView =  rv;

        //Cursor locationquery1 =
        String sMovieTypeWithMovieID =
                UserProvider.USER_MOVIES_TABLE_NAME +
                        "." +
                        UserProvider._MOVIE_CATEGORY + " LIKE ? AND "
                        + UserProvider._MOVIE_RD + " > ? AND "+
        UserProvider._MOVIE_WATCHER +" =  "+ParseUser.getCurrentUser().get("fbid").toString();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String today = dateFormat.format(date);

        Cursor locationCursor1 = getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI_MOVIES,
                FORECAST_MOVIE_COLUMNS,
                sMovieTypeWithMovieID,
                new String[]{'%'+ s + '%',today},
                null);
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

    private ArrayList<Movies> findMoviesAll(RecyclerView rv,String s)
    {

        movies = new ArrayList<>();
        final RecyclerView recyclerView =  rv;

        //Cursor locationquery1 =
        String sMovieTypeWithMovieID =
                UserProvider.USER_MOVIES_TABLE_NAME +
                        "." +
                         UserProvider._MOVIE_RD + " > ? AND "+
                        UserProvider._MOVIE_WATCHER +" =  "+ParseUser.getCurrentUser().get("fbid").toString();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String today = dateFormat.format(date);

        Cursor locationCursor1 = getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI_MOVIES,
                FORECAST_MOVIE_COLUMNS,
                sMovieTypeWithMovieID,
                new String[]{today},
                null);
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

        public SharedPreferences.Editor editorfriendsUp;
        public SharedPreferences.Editor editorisreviewedUp;
        private boolean isfollowing;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Movies> mValues;
        private Context mContext;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public final TextView mRelDate;

            //public Switch follow;
            // public final ImageButton followbutton;
            public final CheckableImageButton cib;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar_movies_upcoming);
                mTextView = (TextView) view.findViewById(R.id.text_mname_upcoming);
                mRelDate = (TextView) view.findViewById(R.id.text_mrdate_upcoming);
                //  follow = (Switch) view.findViewById(R.id.followbutton);
                //  followmoviebutton = (ImageButton)view.findViewById(R.id.followbuttonimage);
                //followbutton = CheckableImageButton;

                cib = (CheckableImageButton) view.findViewById(R.id.upcomingmovieimage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
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
                    .inflate(R.layout.list_item_movie_upcoming, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }
        private String username;
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mBoundString = mValues.get(position).getMname().toString();
            holder.mTextView.setText(mValues.get(position).getMname().toString());
            holder.mRelDate.setText(mValues.get(position).getReldate().toString());
            String imageurl = mValues.get(position).getUrl().toString();
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


            boolean bool;
            final String mid = mValues.get(position).getMovieid().toString();
            String iswatched = mValues.get(position).getIswatched().toString();
            String iswatchlist = mValues.get(position).getIswatchlist().toString();
            String isreviewed = mValues.get(position).getIsreviewed().toString();
            boolean reviewbool;
            final SharedPreferences isReviewedPrefUp = mContext.getSharedPreferences("isReviewedPrefUp", Context.MODE_PRIVATE);

            editorisreviewedUp = isReviewedPrefUp.edit();


            username = "hello";
            final Resources resources = mContext.getResources();

            if(isreviewed.equals("true")) {
                reviewbool = true;
                editorisreviewedUp.putInt("isReviewed" + mid,View.INVISIBLE);
                editorisreviewedUp.commit();

            }
            else
            {
                reviewbool = false;
                editorisreviewedUp.putInt("isReviewed" + mid,View.VISIBLE);
                editorisreviewedUp.commit();

            }

            if(isReviewedPrefUp.getInt("isReviewed" + mid,4) == 4){
                holder.cib.setClickable(false);
                //255-165-0
                holder.cib.setBackgroundColor(Color.rgb(255, 165, 0));
                holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_reviewed));
                // holder.cib.setVisibility(View.INVISIBLE);

            }
            else {

                holder.cib.setVisibility(View.VISIBLE);
                if (iswatchlist.equals("true")) {
                    bool = true;


                }
                else {
                    bool = false;


                }
                final SharedPreferences moviesPrefUp = mContext.getSharedPreferences("moviesPrefUp", Context.MODE_PRIVATE);

                editorfriendsUp = moviesPrefUp.edit();


                username = "hello";
                // final Resources resources = mContext.getResources();
                holder.cib.setChecked(moviesPrefUp.getBoolean(username + mid, bool));

                if (moviesPrefUp.getBoolean(username + mid, bool)) {
                    // 154-205-50
                    holder.cib.setBackgroundColor(Color.rgb(154, 205, 50));
                    // holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_following__1443588011));
                    holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_watched));
                } else {
                    holder.cib.setBackgroundColor(Color.rgb(59, 89, 152));
                    //holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_follow));
                    holder.cib.setImageDrawable(resources.getDrawable(R.drawable.watch));
                }

                holder.cib.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (((CheckableImageButton) v).isChecked()) {
                            editorfriendsUp.putBoolean(username + mid, true);
                            // viewHolder.follow.setClickable(true);

                            editorfriendsUp.putString(mid + "hell", "Following");
                            editorfriendsUp.commit();

                            final ParseObject movielist = new ParseObject("movielist");

                            final ParseUser user = ParseUser.getCurrentUser();

                            String sMovieTypeWithMovieID =
                                    UserProvider.USER_MOVIES_TABLE_NAME +
                                            "." +
                                            UserProvider._MOVIE_ID + " = ?  ";


                            ContentValues values = new ContentValues();
                            values.put(UserProvider._MOVIE_ISWATCHLIST, "true");
                            values.put(UserProvider._MOVIE_ON_PARSE, "true");
                            values.put(UserProvider._MOVIE_WATCHER, user.get("fbid").toString());

                            String where = UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._MOVIE_ID + " = ? AND "+
                                    UserProvider._MOVIE_WATCHER +" =  "+ParseUser.getCurrentUser().get("fbid").toString();

                            int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_MOVIES, values, where, new String[]{mid});

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("movielist");
                            query.whereEqualTo("watcher", user.get("fbid").toString());
                            query.whereEqualTo("mid", mid);
                            //query.whereEqualTo("iswatchlist","false");
                            query.fromLocalDatastore();
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, com.parse.ParseException e) {

                                    if (list != null) {
                                        if (list.size() == 1) {

                                            ParseObject obj = list.get(0);
                                            obj.put("iswatchlist", "true");
                                            Date now = new Date();
                                            String datetoday = String.valueOf(now);
                                            obj.put("moviereviewtime",datetoday);


                                            obj.pinInBackground();
                                            obj.saveEventually();


                                        }
                                        else if(list.size() == 0)
                                        {
                                            ParseObject obj = new ParseObject("movielist");
                                            String mid = mValues.get(position).getMovieid();
                                            obj.put("mid",mid);
                                            // String mid = obj.get("mid").toString();
                                            String _MOVIE_NAME = mValues.get(position).getMname();
                                            obj.put("mname",_MOVIE_NAME);
                                            //obj.get("mname").toString();
                                            String _MOVIE_RD = mValues.get(position).getReldate();
                                            obj.put("mrdate",_MOVIE_RD);
                                            //obj.get("mrdate").toString();
                                            String _MOVIE_WATCHER = user.get("fbid").toString();
                                            obj.put("watcher",_MOVIE_WATCHER);
                                            //mValues.get(position).getWatcher();
                                            //obj.get("watcher").toString();
                                            String _MOVIE_DES = mValues.get(position).getDes();
                                            obj.put("mdes",_MOVIE_DES);
                                            //obj.get("mdes").toString();
                                            String _MOVIE_URL = mValues.get(position).getUrl();
                                            obj.put("murl",_MOVIE_URL);
                                            //obj.get("murl").toString();
                                            String _MOVIE_ISWATCHED = mValues.get(position).getIswatched();
                                            obj.put("iswatched",_MOVIE_ISWATCHED);
                                            //obj.get("iswatched").toString();
                                            String _MOVIE_LIKES = mValues.get(position).getLikes();
                                            obj.put("likes",_MOVIE_LIKES);
                                            //obj.get("likes").toString();
                                            String _MOVIE_ISWATCHLIST = mValues.get(position).getIswatchlist();
                                            obj.put("iswatchlist","true");
                                            //obj.get("iswatchlist").toString();
                                            String _MOVIE_CATEGORY = mValues.get(position).getCategory();
                                            obj.put("mcategory",_MOVIE_CATEGORY);
                                            //obj.get("mcategory").toString();
                                            String _MOVIE_RATING = mValues.get(position).getRating();
                                            obj.put("rating",_MOVIE_RATING);
                                            //obj.get("rating").toString();
                                            String _MOVIE_ISREVIEWED = mValues.get(position).getIsreviewed();
                                            obj.put("isreviewed",_MOVIE_ISREVIEWED);
                                            //obj.get("isreviewed").toString();
                                            String _MOVIE_REVIEW = mValues.get(position).getReview();
                                            obj.put("review",_MOVIE_REVIEW);
                                            //obj.get("review").toString();
                                            String _MOVIE_ON_PARSE = "true";
                                            obj.put("movieonparse",_MOVIE_ON_PARSE);
                                            //obj.get("movieonparse").toString();
                                            Date now = new Date();
                                            String datetoday = String.valueOf(now);
                                            obj.put("moviereviewtime",datetoday);

                                            String bool = "true";

                                            obj.pinInBackground();
                                            obj.saveEventually();

                                        }

                                    }
                                }
                            });


                            holder.cib.setBackgroundColor(Color.rgb(154, 205, 50));
                            holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_watched));

                        } else {
                            // holder.followbutton.setBackgroundColor(Color.rgb(59,89,152));
                            //holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_follow));
                            holder.cib.setImageDrawable(resources.getDrawable(R.drawable.watch));
                            holder.cib.setBackgroundColor(Color.rgb(59, 89, 152));
                            editorfriendsUp.putBoolean(username + mid, false);
                            editorfriendsUp.putString(mid + "hell", "Follow");
                            editorfriendsUp.commit();

                            final ParseObject movielist = new ParseObject("movielist");

                            final ParseUser user = ParseUser.getCurrentUser();

                            ContentValues values = new ContentValues();
                            values.put(UserProvider._MOVIE_ISWATCHLIST, "false");
                            values.put(UserProvider._MOVIE_ON_PARSE, "false");
                            values.put(UserProvider._MOVIE_WATCHER, ParseUser.getCurrentUser().get("fbid").toString());
                            String where = UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._MOVIE_ID + " = ? AND "+
                                    UserProvider._MOVIE_WATCHER +" =  "+ParseUser.getCurrentUser().get("fbid").toString();

                            int i = mContext.getContentResolver().update(UserProvider.CONTENT_URI_MOVIES, values, where, new String[]{mid});


                            ParseQuery<ParseObject> query = ParseQuery.getQuery("movielist");
                            query.whereEqualTo("watcher", user.get("fbid").toString());
                            query.whereEqualTo("mid", mid);
                            query.fromLocalDatastore();
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, com.parse.ParseException e) {

                                    if (list != null) {
                                        if (list.size() == 1) {

                                            ParseObject obj = list.get(0);
                                            obj.unpinInBackground();
                                            obj.deleteEventually();


                                        } else if (list.size() == 0) {

                                        }

                                    }
                                }
                            });



                        }


                    }


                });

            }


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
