package com.support.android.designlibdemo;

/**
 * Created by amitagarwal3 on 10/7/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.support.android.designlibdemo.data.UserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//  import info.androidhive.parsenotifications.activity.MainActivity;
      //  import info.androidhive.parsenotifications.helper.NotificationUtils;

/**
 * Created by Ravi on 01/06/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = CustomPushReceiver.class.getSimpleName();

    public Context mContext;
    private NotificationUtils notificationUtils;

    private Intent parseIntent;

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

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        mContext = context;

        if (intent == null)
            return;

        try {

            String data = intent.getExtras().getString("com.parse.Channel");

            if(data != null ){

                Log.e("Channel Data is: ",data);
                Log.e("Channel Data is: ",data);
                Log.e("Channel Data is: ",data);
                Log.e("Channel Data is: ",data);
                Log.e("Channel Data is: ",data);
            }

            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));


            Log.e(TAG, "Push received: " + json);
            Log.e(TAG, "Push received: " + json);
            Log.e(TAG, "Push received: " + json);
            Log.e(TAG, "Push received: " + json);


            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            final String title = data.getString("title");
            Log.e("Title is ",title);
            Log.e("Title is ",title);
            String message = data.getString("message");

            if(message.contains("Review Added")) {
                final ParseUser pu = ParseUser.getCurrentUser();
                ParseObject obj = new ParseObject("movielist");

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FollowMovieBuzz");
                query.whereEqualTo("follower", pu.get("fbid").toString());
                query.whereEqualTo("following", title);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {

                        for (int i = 0; i < list.size(); i++) {

                            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                            query.whereEqualTo("watcher", list.get(i).get("following").toString());
                            query.whereEqualTo("isreviewed", "true");
                            query.findInBackground(new FindCallback<ParseObject>() {
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
                                                movieValues.put(UserProvider._MOVIE_REVIEW_TIME, _MOVIE_REVIEW_TIME);

                                                Uri uri = mContext.getContentResolver().insert(
                                                        UserProvider.CONTENT_URI_MOVIES, movieValues);
                                                locationCursor1.close();

                                            }

                                        }
                                    }


                                }
                            });
                        }


                    }
                });

            }
            else if(message.contains("Likes Added"))
            {

                final ParseUser pu = ParseUser.getCurrentUser();
                ParseObject obj = new ParseObject("movielist");

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                query.whereEqualTo("watcher", pu.get("fbid").toString());
                query.findInBackground(new FindCallback<ParseObject>() {
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
                                        new String[]{mid, bool, pu.get("fbid").toString()},
                                        null);
                                int inserted = 0;

                                if (locationCursor1.getCount() != 0) {

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
                                    movieValues.put(UserProvider._MOVIE_REVIEW_TIME, _MOVIE_REVIEW_TIME);
                                    String where = UserProvider._MOVIE_ID + " = ?  AND "
                                                + UserProvider._MOVIE_WATCHER + " = ? " ;
                                    int update = mContext.getContentResolver().update(
                                            UserProvider.CONTENT_URI_MOVIES, movieValues,where,new String[]{mid,pu.get("fbid").toString()});

                                    locationCursor1.close();

                                    Log.e("Updated ", "updated");
                                    Log.e("Updated ","updated");

                                } else {
                                    /*
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
                                    */
                                    locationCursor1.close();

                                   // removeDuplicates();
                                }

                            }
                        }


                    }
                });

            }
            else if(message.contains("Friends Added"))
            {

                Log.e("In freinds likes added","in friends likes");
                Log.e("In freinds likes added","in friends likes");
                final ParseUser pu = ParseUser.getCurrentUser();
                ParseObject obj = new ParseObject("movielist");

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("movielist");
                query.whereEqualTo("watcher", title);
                query.findInBackground(new FindCallback<ParseObject>() {
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
                                        new String[]{mid, bool, title},
                                        null);
                                int inserted = 0;

                                if (locationCursor1.getCount() != 0) {

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
                                    movieValues.put(UserProvider._MOVIE_REVIEW_TIME, _MOVIE_REVIEW_TIME);
                                    String where = UserProvider._MOVIE_ID + " = ?  AND "
                                            + UserProvider._MOVIE_WATCHER + " = ? " ;
                                    int update = mContext.getContentResolver().update(
                                            UserProvider.CONTENT_URI_MOVIES, movieValues,where,new String[]{mid,title});

                                    locationCursor1.close();

                                    Log.e("Updated ", "updated");
                                    Log.e("Updated ","updated");

                                } else {
                                    /*
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
                                    */
                                    locationCursor1.close();

                                    // removeDuplicates();
                                }

                            }
                        }


                    }
                });

            }

            if (!isBackground) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                showNotificationMessage(context, title, message, resultIntent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}
