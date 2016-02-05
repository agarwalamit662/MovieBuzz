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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.parse.ParseUser;
import com.support.android.designlibdemo.data.UserProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyReviewsFragment extends Fragment implements SearchView.OnQueryTextListener{

    public static MyReviewsFragment mwf;
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


    public static final MyReviewsFragment newInstance(int title, String message)
    {
        MyReviewsFragment fragment = new MyReviewsFragment();
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
        rv =(RecyclerView) getActivity().findViewById(R.id.recyclerview_myreviews);
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
        View rootView = inflater.inflate(R.layout.fragment_myreviews, container, false);


        rv =(RecyclerView) rootView.findViewById(R.id.recyclerview_myreviews);

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
        /*String sMovieTypeWithMovieID =
                UserProvider.USER_MOVIES_TABLE_NAME +
                        "." +
                        UserProvider._MOVIE_ISREVIEWED + " = ? AND "
                        + UserProvider._MOVIE_WATCHER + " = ? ) ORDER BY DATETIME("+UserProvider._MOVIE_REVIEW_TIME;

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
        */
        String sMovieTypeWithMovieID =
                UserProvider.USER_MOVIES_TABLE_NAME +
                        "." +
                        UserProvider._MOVIE_ISREVIEWED + " = ? AND "
                        + UserProvider._MOVIE_WATCHER + " = ? ";
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
          //  public final ImageView mImageView;
            public final TextView mMovieName;
            public final TextView mReview;
            public final RatingBar mRating;
            public final CardView card;
            public final TextView mReviewTime;
            public final TextView mMyLikes;
            public final ShareButton sharereviewButton;
            public final TextView likersName;
            public final LinearLayout layoutLikes;
            //public final Button mAddReview;
            //public Switch follow;
            // public final ImageButton followbutton;
            // public final CheckableImageButton cib;
            public ViewHolder(View view) {
                super(view);
                mView = view;
               // mImageView = (ImageView) view.findViewById(R.id.avatar_movies_reviewed);
                mMovieName = (TextView) view.findViewById(R.id.mnamemyreviews);
                mReview = (TextView) view.findViewById(R.id.mnamereview);
                mRating = (RatingBar) view.findViewById(R.id.ratingBarMyReviews);
              //  mAddReview = (Button) view.findViewById(R.id.addreviewbutton);
                card = (CardView) view.findViewById(R.id.myreviewscardview);
                mReviewTime = (TextView) view.findViewById(R.id.mreviewtimemine);
                mMyLikes = (TextView) view.findViewById(R.id.likesmy);
                sharereviewButton = (ShareButton) view.findViewById(R.id.sharereview);
                likersName = (TextView) view.findViewById(R.id.likesNames);
                layoutLikes = (LinearLayout) view.findViewById(R.id.likerslayout);
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
                    .inflate(R.layout.list_item_myreviews, parent, false);
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
            final String imageurl = mValues.get(position).getUrl().toString();
            holder.mRating.setRating(Float.parseFloat(mValues.get(position).getRating().toString()));
            holder.mReviewTime.setText(mValues.get(position).getMoviereviewtime().toString());
            holder.likersName.setVisibility(View.INVISIBLE);
            final String likesCount = mValues.get(position).getLikes().toString();
            final int likes = mValues.get(position).getLikes().toString().length() - mValues.get(position).getLikes().toString().replace(",", "").length();
            holder.mMyLikes.setText("Likes +" + String.valueOf(likes));
            holder.mMyLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.layoutLikes.setVisibility(View.VISIBLE);
                    holder.likersName.setVisibility(View.VISIBLE);
                    String[] arr = likesCount.split(",");
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
            /*
            Bitmap dest = getBitmapFromURL(imageurl);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temporary_file.jpg");

            if(f.exists())
                f.delete();
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                dest.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            final ShareDialog shareDialog = new ShareDialog(MyReviewsFragment.mwf);

            final ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("www.facebook.com"))
                    .setContentTitle(mValues.get(position).getMname().toString())
                    .setContentDescription(mValues.get(position).getReview().toString())

                    .setImageUrl(Uri.fromFile(f))
                    .setRef("MovieBuzz")
                    .build();

           // holder.sharereviewButton.setShareContent(content);
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

        public static Bitmap getBitmapFromURL(String src) {

                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Bitmap doInBackground(Object[] params) {
                        try {
                        URL url = new URL(params[0].toString());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        return myBitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                       // return null;
                    }
                };


            return null;
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
