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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import android.widget.Button;
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

public class MovieWatchedFragment extends Fragment implements SearchView.OnQueryTextListener{

    public static MovieWatchedFragment mwf;
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


    public static final MovieWatchedFragment newInstance(int title, String message)
    {
        MovieWatchedFragment fragment = new MovieWatchedFragment();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);


        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


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
        rv =(RecyclerView) getActivity().findViewById(R.id.recyclerview_movies_watched);
        findMovies(rv, "Action");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_movies_watched, container, false);

        rv =(RecyclerView) rootView.findViewById(R.id.recyclerview_movies_watched);
        rv.addItemDecoration(new DividerItemDecoration(getActivity()));

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
                        UserProvider._MOVIE_ISWATCHED + " = ? AND "
                        + UserProvider._MOVIE_WATCHER + " = ? ";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String today = dateFormat.format(date);

        Cursor locationCursor1 = getActivity().getContentResolver().query(
                UserProvider.CONTENT_URI_MOVIES,
                FORECAST_MOVIE_COLUMNS,
                sMovieTypeWithMovieID,
                new String[]{"true",pu.get("fbid").toString()},
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
        public SharedPreferences.Editor editorisreviewedwatched;
        public SharedPreferences.Editor editorfriends;
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
            public final Button mAddReview;
            //public Switch follow;
           // public final ImageButton followbutton;
           // public final CheckableImageButton cib;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar_movies_watched);
                mTextView = (TextView) view.findViewById(R.id.text_mname_watched);
                mRelDate = (TextView) view.findViewById(R.id.text_mrdate_watched);
                mAddReview = (Button) view.findViewById(R.id.addreviewbuttonwatched);

                 //cib = (CheckableImageButton) view.findViewById(R.id.followmovieimage);
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
                    .inflate(R.layout.list_item_movie_watched, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }
        private String username;
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mBoundString = mValues.get(position).getMname().toString();
            holder.mTextView.setText(mValues.get(position).getMname().toString());
            holder.mRelDate.setText(mValues.get(position).getReldate().toString());
            final String imageurl = mValues.get(position).getUrl().toString();
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
            String isreviewed = mValues.get(position).getIsreviewed().toString();
            final String watcher = mValues.get(position).getWatcher().toString();

            boolean reviewbool;

            //Log.e("is reviewed", isreviewed);
            //Log.e("is reviewed",)

            Log.e("MovieId",mValues.get(position).getMovieid().toString());
            Log.e("Category",mValues.get(position).getCategory().toString());
            Log.e("IsWatched",mValues.get(position).getIswatched().toString());
            Log.e("Iswatchlist",mValues.get(position).getIswatchlist().toString());
            Log.e("IsReviewed",mValues.get(position).getIsreviewed().toString());
            Log.e("Rating",mValues.get(position).getRating().toString());
           // Log.e("")

            final SharedPreferences isReviewedPrefWatched = mContext.getSharedPreferences("isReviewedPrefWatched", Context.MODE_PRIVATE);

            editorisreviewedwatched = isReviewedPrefWatched.edit();


            username = "hello";
            final Resources resources = mContext.getResources();

            if(isreviewed.equals("true")) {
                reviewbool = true;
                editorisreviewedwatched.putInt("isReviewedWatched" + mid,View.INVISIBLE);
                editorisreviewedwatched.commit();

            }
            else
            {
                reviewbool = false;
                editorisreviewedwatched.putInt("isReviewedWatched" + mid, View.VISIBLE);
                editorisreviewedwatched.commit();

            }

            if(isReviewedPrefWatched.getInt("isReviewedWatched" + mid,4) == 4){
                //holder.cib.setClickable(false);
                //255-165-0

                holder.mAddReview.setText("Reviewed");
                holder.mAddReview.setClickable(false);
                holder.mAddReview.setBackgroundColor(Color.rgb(255, 165, 0));


                //holder.cib.setImageDrawable(resources.getDrawable(R.drawable.ic_reviewed));
                // holder.cib.setVisibility(View.INVISIBLE);

            }
            else
            {
                holder.mAddReview.setText("ADD REVIEW");
                holder.mAddReview.setClickable(true);
                holder.mAddReview.setBackgroundColor(resources.getColor(R.color.com_facebook_blue));
                holder.mAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentActivity activity = (FragmentActivity)v.getContext();
                        FragmentManager manager = activity.getSupportFragmentManager();
                        MyDialogFragment dialogFragment = new MyDialogFragment ();
                        dialogFragment = MyDialogFragment.newInstance(mid,mValues.get(position).getMname().toString(),imageurl,watcher);
                        dialogFragment.show(manager, "Sample Fragment");

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
