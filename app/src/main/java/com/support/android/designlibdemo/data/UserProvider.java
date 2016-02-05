package com.support.android.designlibdemo.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class UserProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.support.android.designlibdemo.data";
    static final String URL = "content://" + PROVIDER_NAME + "/user";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    static final String URL_FRIENDS = "content://" + PROVIDER_NAME + "/userfriends";
    public static final Uri CONTENT_URI_FRIENDS = Uri.parse(URL_FRIENDS);

    static final String URL_FOLLOWER = "content://" + PROVIDER_NAME + "/userfollower";
    public static final Uri CONTENT_URI_FOLLOWER = Uri.parse(URL_FOLLOWER);

    static final String URL_MOVIES = "content://" + PROVIDER_NAME + "/movies";
    public static final Uri CONTENT_URI_MOVIES = Uri.parse(URL_MOVIES);



    public static final String _ID = "_id";
    public static final String FBID = "fbid";
    public static final String NAME = "name";
    public static final String ABOUTME = "aboutme";
    public static final String GENRES = "genres";
    public static final String RATING = "rating";
    public static final String URLPICTURE = "picture";

    public static final String _ID_FRIEND = "_id_friend";
    public static final String FBID_FRIEND = "fbid_friend";
    public static final String NAME_FRIEND = "name_friend";
    public static final String ABOUTME_FRIEND = "aboutme_friend";
    public static final String GENRES_FRIEND = "genres_friend";
    public static final String RATING_FRIEND = "rating_friend";
    public static final String URLPICTURE_FRIEND = "picture_friend";
    public static final String IS_FOLLOWING = "isfollowing_friend";

    public static final String _ID_FOLLOWER = "_id_follower";
    public static final String FOLLOWER = "follower";
    public static final String FOLLOWING = "following";
    public static final String NAME_FOLLOWING = "namefollowing";
    public static final String IS_FOLLOWING_FOLLOWER = "isfollowing";
    public static final String FOLLOWING_URL = "picture";



    public static final String _ID_MOVIE = "_id_movie";
    public static final String _MOVIE_ID = "mid";
    public static final String _MOVIE_NAME = "mname";
    public static final String _MOVIE_RD = "mrdate";
    public static final String _MOVIE_WATCHER = "watcher";
    public static final String _MOVIE_DES = "mdes";
    public static final String _MOVIE_URL = "murl";
    public static final String _MOVIE_ISWATCHED = "iswatched";
    public static final String _MOVIE_LIKES = "likes";
    public static final String _MOVIE_ISWATCHLIST = "iswatchlist";
    public static final String _MOVIE_CATEGORY = "mcategory";
    public static final String _MOVIE_RATING = "rating";
    public static final String _MOVIE_ISREVIEWED = "isreviewed";
    public static final String _MOVIE_REVIEW = "review";
    public static final String _MOVIE_ON_PARSE = "movieonparse";
    public static final String _MOVIE_REVIEW_TIME = "moviereviewtime";

    private static HashMap<String, String> USER_PROJECTION_MAP;

    static final int USER  = 1;
    static final int USER_FBID = 2;

    private static HashMap<String, String> USER_FRIENDS_PROJECTION_MAP;

    static final int USER_FRIEND  = 3;
    static final int USER_FRIEND_FBID = 4;

    private static HashMap<String, String> USER_FOLLOWER_PROJECTION_MAP;
    static final int USER_FOLLOWER  = 7;
    static final int USER_FOLLOWER_FBID = 8;

    private static HashMap<String, String> USER_MOVIES_PROJECTION_MAP;

    static final int USER_MOVIE  = 5;
    static final int USER_MOVIE_ID = 6;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "user", USER);
        uriMatcher.addURI(PROVIDER_NAME, "user/#", USER_FBID);
        uriMatcher.addURI(PROVIDER_NAME, "userfriends", USER_FRIEND);
        uriMatcher.addURI(PROVIDER_NAME, "userfriends/#", USER_FRIEND_FBID);



        uriMatcher.addURI(PROVIDER_NAME, "movies", USER_MOVIE);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", USER_MOVIE_ID);

        uriMatcher.addURI(PROVIDER_NAME, "userfollower", USER_FOLLOWER);
        uriMatcher.addURI(PROVIDER_NAME, "userfollower/#", USER_FOLLOWER_FBID);


    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "UserMovieBuzz";
    public static final String USER_TABLE_NAME = "user";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + USER_TABLE_NAME +
                    " ( _id  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " fbid TEXT NOT NULL, " +
                    " name TEXT NOT NULL, " +
                    " aboutme TEXT NOT NULL, " +
                    " genres TEXT NOT NULL, " +
                    " rating TEXT NOT NULL, " +
                    " picture TEXT NOT NULL);";


    static final String DATABASE_NAME_FRIENDS = "UserMovieBuzzFriends";
    public static final String USER_FRIENDS_TABLE_NAME = "userfriends";
   // static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE_FRIENDS =
            " CREATE TABLE " + USER_FRIENDS_TABLE_NAME +
                    " ( _id_friend INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " fbid_friend TEXT NOT NULL, " +
                    " name_friend TEXT NOT NULL, " +
                    " aboutme_friend TEXT NOT NULL, " +
                    " genres_friend TEXT NOT NULL, " +
                    " rating_friend TEXT NOT NULL, " +
                    " picture_friend TEXT NOT NULL, " +
                    " isfollowing_friend TEXT NOT NULL);";



    public static final String USER_FOLLOWING_TABLE_NAME = "userfollower";
    // static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE_FOLLOWING =
            " CREATE TABLE " + USER_FOLLOWING_TABLE_NAME +
                    " ( _id_follower INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " follower TEXT NOT NULL, " +
                    " following TEXT NOT NULL, " +
                    " namefollowing TEXT NOT NULL, " +
                    " isfollowing TEXT NOT NULL, " +
                    " picture TEXT NOT NULL ); ";



    public static final String USER_MOVIES_TABLE_NAME = "movies";
    // static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE_MOVIES =
            " CREATE TABLE " + USER_MOVIES_TABLE_NAME +
                    " ( _id_movie  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " mid TEXT NOT NULL, " +
                    " mname TEXT NOT NULL, " +
                    " mrdate TEXT NOT NULL, " +
                    " watcher TEXT NOT NULL, " +
                    " mdes TEXT NOT NULL, " +
                    " murl TEXT, " +
                    " iswatched TEXT NOT NULL, " +
                    " likes TEXT NOT NULL, " +
                    " iswatchlist TEXT NOT NULL, " +
                    " mcategory TEXT NOT NULL, " +
                    " rating TEXT NOT NULL, " +
                    " isreviewed TEXT NOT NULL, " +
                    " review TEXT NOT NULL, " +
                    " movieonparse TEXT NOT NULL, "+
                    " moviereviewtime TEXT NOT NULL);";



    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
            db.execSQL(CREATE_DB_TABLE_FRIENDS);
            db.execSQL(CREATE_DB_TABLE_MOVIES);
            db.execSQL(CREATE_DB_TABLE_FOLLOWING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  USER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " +  USER_FRIENDS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " +  USER_MOVIES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + USER_FOLLOWING_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {
            case USER: {
                long rowID = db.insert(USER_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */

                if (rowID > 0) {
                  /*  Log.e("In user if Instered", Long.toString(rowID));
                    Log.e("In user if Instered", Long.toString(rowID));
                    Log.e("In user if Instered", Long.toString(rowID));
                    Log.e("In user if Instered", Long.toString(rowID));
                    */
                    _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                /*
                Log.e("User Record Instered", Long.toString(rowID));
                Log.e("User Record Instered", Long.toString(rowID));
                Log.e("User Record Instered", Long.toString(rowID));
                Log.e("User Record Instered", Long.toString(rowID));
                */
                throw new SQLException("Failed to add a record into " + uri);

               // break;
            }
            case USER_FRIEND: {
                long rowID = db.insert(USER_FRIENDS_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */

                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_FRIENDS, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                throw new SQLException("Failed to add a record into " + uri);
                //break;
            }
            case USER_FOLLOWER: {
                long rowID = db.insert(USER_FOLLOWING_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */

                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_FOLLOWER, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                throw new SQLException("Failed to add a record into " + uri);
                //break;
            }
            case USER_MOVIE: {
                long rowID = db.insert(USER_MOVIES_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */

                if (rowID > 0) {
                  /*  Log.e("In movie if Instered", Long.toString(rowID));
                    Log.e("In movie if Instered", Long.toString(rowID));
                    Log.e("In movie if Instered", Long.toString(rowID));
                    Log.e("In movie if Instered", Long.toString(rowID));
                    */
                    _uri = ContentUris.withAppendedId(CONTENT_URI_MOVIES, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
               /*
                Log.e("Movie Record Instered", Long.toString(rowID));
                Log.e("Movie Record Instered", Long.toString(rowID));
                Log.e("Movie Record Instered", Long.toString(rowID));
                Log.e("Movie Record Instered", Long.toString(rowID));
                */
                throw new SQLException("Failed to add a record into " + uri);
                //break;
            }
            default:{
               // return _uri;
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }

       // return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //qb.setTables(USER_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case USER:
                qb.setTables(USER_TABLE_NAME);
                qb.setProjectionMap(USER_PROJECTION_MAP);
                break;

            case USER_FBID:
                qb.setTables(USER_TABLE_NAME);
                qb.appendWhere( FBID + "=" + uri.getPathSegments().get(1));
                break;

            case USER_MOVIE:
                qb.setTables(USER_MOVIES_TABLE_NAME);
                qb.setProjectionMap(USER_MOVIES_PROJECTION_MAP);
                break;

            case USER_MOVIE_ID:
                qb.setTables(USER_MOVIES_TABLE_NAME);
                qb.appendWhere( _MOVIE_ID + "=" + uri.getPathSegments().get(1));
                break;

            case USER_FRIEND:
                qb.setTables(USER_FRIENDS_TABLE_NAME);
                qb.setProjectionMap(USER_FRIENDS_PROJECTION_MAP);
                sortOrder = NAME_FRIEND;
                break;

            case USER_FRIEND_FBID:
                qb.setTables(USER_FRIENDS_TABLE_NAME);
                qb.appendWhere(FBID_FRIEND + "=" + uri.getPathSegments().get(1));
                sortOrder = NAME_FRIEND;
                break;

            case USER_FOLLOWER:
                qb.setTables(USER_FOLLOWING_TABLE_NAME);
                qb.setProjectionMap(USER_FOLLOWER_PROJECTION_MAP);
               // sortOrder = NAME_FRIEND;
                break;

            case USER_FOLLOWER_FBID:
                qb.setTables(USER_FOLLOWING_TABLE_NAME);
                qb.appendWhere(FOLLOWER + "=" + uri.getPathSegments().get(1));
               // sortOrder = NAME_FRIEND;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /*
        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
        //    sortOrder = NAME;
       // }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case USER:
                count = db.delete(USER_TABLE_NAME, selection, selectionArgs);
                break;

            case USER_FBID:
                String fbid = uri.getPathSegments().get(1);
                count = db.delete( USER_TABLE_NAME, FBID +  " = " + fbid +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            case USER_MOVIE:
                count = db.delete(USER_MOVIES_TABLE_NAME, selection, selectionArgs);
                break;

            case USER_MOVIE_ID:
                String mid = uri.getPathSegments().get(1);
                count = db.delete( USER_MOVIES_TABLE_NAME, _MOVIE_ID +  " = " + mid +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            case USER_FRIEND:
                count = db.delete(USER_FRIENDS_TABLE_NAME, selection, selectionArgs);
                break;

            case USER_FRIEND_FBID:
                String fbid_friend = uri.getPathSegments().get(1);
                count = db.delete( USER_FRIENDS_TABLE_NAME, FBID +  " = " + fbid_friend +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            case USER_FOLLOWER:
                count = db.delete(USER_FOLLOWING_TABLE_NAME, selection, selectionArgs);
                break;

            case USER_FOLLOWER_FBID:
                String follower = uri.getPathSegments().get(1);
                count = db.delete( USER_FOLLOWING_TABLE_NAME, FOLLOWER +  " = " + follower +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case USER: {
                count = db.update(USER_TABLE_NAME, values, selection, selectionArgs);
                /*
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                */
                break;
                }
            case USER_FBID:
                count = db.update(USER_TABLE_NAME, values, FBID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            case USER_MOVIE: {
                count = db.update(USER_MOVIES_TABLE_NAME, values, selection, selectionArgs);
                /*

                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                */
                break;
            }
            case USER_MOVIE_ID:
                count = db.update(USER_MOVIES_TABLE_NAME, values, _MOVIE_ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            case USER_FRIEND:
                count = db.update(USER_FRIENDS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case USER_FRIEND_FBID:
                count = db.update(USER_FRIENDS_TABLE_NAME, values, FBID_FRIEND + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            case USER_FOLLOWER:
                count = db.update(USER_FOLLOWING_TABLE_NAME, values, selection, selectionArgs);
                break;

            case USER_FOLLOWER_FBID:
                count = db.update(USER_FOLLOWING_TABLE_NAME, values, FOLLOWER + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case USER:
               // return "vnd.android.cursor.dir/vnd.example.students";
               return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "user";
            /**
             * Get a particular student
             */
            case USER_FBID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "user";



            case USER_MOVIE:
                // return "vnd.android.cursor.dir/vnd.example.students";
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "movies";
            /**
             * Get a particular student
             */
            case USER_MOVIE_ID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "movies";


            case USER_FRIEND:
                // return "vnd.android.cursor.dir/vnd.example.students";
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "userfriends";
            /**
             * Get a particular student
             */
            case USER_FRIEND_FBID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "userfriends";


            case USER_FOLLOWER:
                // return "vnd.android.cursor.dir/vnd.example.students";
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "userfollower";
            /**
             * Get a particular student
             */
            case USER_FOLLOWER_FBID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "userfollower";



            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}