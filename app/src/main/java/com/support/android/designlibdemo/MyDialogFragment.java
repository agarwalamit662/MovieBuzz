package com.support.android.designlibdemo;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.support.android.designlibdemo.data.UserProvider;

import org.json.JSONObject;
import java.sql.Timestamp;
//import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyDialogFragment extends DialogFragment {
  //  public DateTimeFormatter formatter;
    private String title;
    private String url;
    private String mname;
    private String watcher;

    public static final MyDialogFragment newInstance(String mid,String name, String url,String watcher)
    {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString("mid", mid);
        bundle.putString("murl", url);
        bundle.putString("mname",name);
        bundle.putString("mwatcher",watcher);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        title = getArguments().getString("mid");
        url = getArguments().getString("murl");
        mname = getArguments().getString("mname");
        //mContexts = getActivity();
        watcher = getArguments().getString("mwatcher");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review_dialog, container, false);
        final EditText review = (EditText) rootView.findViewById(R.id.editreview);
        final Button savereview = (Button) rootView.findViewById(R.id.savereview);
        final RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBar);
        final TextView mnameText = (TextView) rootView.findViewById(R.id.mnamereview);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mnameText.setText(mname);
        final Button cancelreview = (Button) rootView.findViewById(R.id.cancelreview);
        cancelreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });
        if(review.getText().toString() == null && rating.getRating() != 0.0)
        {
            savereview.setClickable(false);
        }
        else
        {
            savereview.setClickable(true);
        }

        savereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseUser user = ParseUser.getCurrentUser();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("movielist");
                query.whereEqualTo("watcher", user.get("fbid").toString());
                query.whereEqualTo("mid", title);
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, com.parse.ParseException e) {

                        if (list != null) {
                            if (list.size() == 1) {

                                ParseObject obj = list.get(0);
                                obj.put("isreviewed", "true");
                                obj.put("rating", Float.toString(rating.getRating()));
                                obj.put("review", review.getText().toString());
                                obj.put("iswatchlist","false");

                                Date now = new Date();
                                String datetoday = String.valueOf(now);

                                  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                 Date date = new Date();
                                  String today = dateFormat.format(date);


                                // String datetoday = String.valueOf(now);
                                // Tue Oct 27 18:21:16 GMT+05:30 2015
                                //  String string = "Sun Oct 05 20:59:57 BRT 2014";
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Timestamp datetime = null;
                                try {
                                    datetime = new Timestamp(formatter.parse(datetoday).getTime());
                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                }


                                if(datetime != null)
                                obj.put("moviereviewtime",today);
                                else
                                    obj.put("moviereviewtime", today);

                                //java.util.Date juDate = new Date();
                               // DateTime dt = new DateTime(juDate);

                                obj.pinInBackground();
                                obj.saveEventually();

                                Log.e("in mydialogfragment", "in my dialog fragment");
                                Log.e("in mydialogfragment","in my dialog fragment");
                                Log.e("in mydialogfragment","in my dialog fragment");



                            }


                        }
                    }
        });

                String sMovieTypeWithMovieID =
                        UserProvider.USER_MOVIES_TABLE_NAME +
                                "." +
                                UserProvider._MOVIE_ID + " = ? AND "
                        +UserProvider._MOVIE_WATCHER + " = ? ";


                ContentValues values = new ContentValues();

                values.put(UserProvider._MOVIE_WATCHER, user.get("fbid").toString());
                values.put(UserProvider._MOVIE_RATING, Float.toString(rating.getRating()));
                values.put(UserProvider._MOVIE_ISREVIEWED, "true");
                values.put(UserProvider._MOVIE_REVIEW, review.getText().toString());
                Date now = new Date();
                String datetoday = String.valueOf(now);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = new Date();
                String today = dateFormat.format(date);


                // String datetoday = String.valueOf(now);
                // Tue Oct 27 18:21:16 GMT+05:30 2015
              //  String string = "Sun Oct 05 20:59:57 BRT 2014";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp datetime = null;
                try {
                    datetime = new Timestamp(formatter.parse(datetoday).getTime());
                } catch (java.text.ParseException e1) {
                    e1.printStackTrace();
                }

                if(datetime != null)
                   // obj.put("moviereviewtime",datetime.toString());
                    values.put(UserProvider._MOVIE_REVIEW_TIME,today);
                else
                    values.put(UserProvider._MOVIE_REVIEW_TIME,today);


                // obj.put("moviereviewtime",datetoday);
                //values.put(UserProvider._MOVIE_REVIEW_TIME,datetoday);
                String where = UserProvider.USER_MOVIES_TABLE_NAME + "." + UserProvider._MOVIE_ID + " = ? AND "
                        +UserProvider._MOVIE_WATCHER + " = ? ";

                if(getActivity() != null) {
                    int i = getActivity().getContentResolver().update(UserProvider.CONTENT_URI_MOVIES, values, where, new String[]{title,user.get("fbid").toString()});
                    // locationCursor1.close();

                    Toast.makeText(getActivity(), "Movie review Added", Toast.LENGTH_SHORT);
                    MovieWatchedFragment.mwf.onResume();
                    ParseUser users = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("FollowMovieBuzz");
                    query1.fromLocalDatastore();
                    query1.whereEqualTo("follower", users.get("fbid").toString());
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

                                    jsonObject = new JSONObject("{\"data\": {\"message\": \"Review Added\",\"title\": \"" + ParseUser.getCurrentUser().get("fbid").toString() + "\"},\"is_background\": true}");
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


                    getDialog().dismiss();
                }






        }
    });
    return rootView;
}
}