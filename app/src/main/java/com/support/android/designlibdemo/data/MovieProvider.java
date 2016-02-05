/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.support.android.designlibdemo.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private com.support.android.designlibdemo.data.MovieDbHelper mOpenHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int LOCATION = 300;
    static final int MOVIE = 103;
    static final int MOVIE_WITH_TYPE = 104;
    static final int MOVIE_WITH_TYPE_AND_ID = 105;
    static final int SERIES = 106;
    static final int SERIES_WITH_TYPE = 107;
    static final int SERIES_WITH_TYPE_AND_ID = 108;

    static final int TMDB = 109;
    static final int TMDB_WITH_ID = 110;
    static final int TMDB_WITH_ID_AND_IMDB = 111;

    private static final SQLiteQueryBuilder sMovieByMovieSettingQueryBuilder;


    static{
        sMovieByMovieSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sMovieByMovieSettingQueryBuilder.setTables(
                MovieContract.MovieNumberEntry.TABLE_NAME );
    }

    private static final SQLiteQueryBuilder sSeriesBySeriesIDQueryBuilder;

    static{
        sSeriesBySeriesIDQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sSeriesBySeriesIDQueryBuilder.setTables(
                MovieContract.SeriesNumberEntry.TABLE_NAME);
    }


    private static final SQLiteQueryBuilder sTmdbByImdbIDQueryBuilder;

    static{
        sTmdbByImdbIDQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sTmdbByImdbIDQueryBuilder.setTables(
                MovieContract.MovieImdbEntry.TABLE_NAME);
    }

    //location.location_setting = ?
    private static final String sMovieTypeSelection =
            MovieContract.MovieNumberEntry.TABLE_NAME+
                    "." + MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE + " = ? ";

    //location.location_setting = ? AND date >= ?

    private static final String sMovieTypeWithMovieID =
            MovieContract.MovieNumberEntry.TABLE_NAME+
                    "." + MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE + " = ? AND " +
                    MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";


    private static final String sSeriesTypeSelection =
            MovieContract.SeriesNumberEntry.TABLE_NAME+
                    "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY + " = ? ";

    //location.location_setting = ? AND date >= ?

    private static final String sSeriesTypeWithSeriesID =
            MovieContract.SeriesNumberEntry.TABLE_NAME+
                    "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY + " = ? AND " +
                    MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ? ";

    //location.location_setting = ? AND date = ?


    private Cursor getMovieByMovieType(Uri uri, String[] projection, String sortOrder) {
        String movieType = MovieContract.MovieNumberEntry.getMovieTypeFromUri(uri);
        long movieid = MovieContract.MovieNumberEntry.getMovieIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        if(movieid==0) {
            selection = sMovieTypeSelection;
            selectionArgs = new String[]{movieType};
        }
        else {
            selectionArgs = new String[]{movieType, Long.toString(movieid)};
            selection = sMovieTypeWithMovieID;
        }


        return sMovieByMovieSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieByMovieTypeAndMovieID(
            Uri uri, String[] projection, String sortOrder) {
        String movieType = MovieContract.MovieNumberEntry.getMovieTypeFromUri(uri);
        long movieid = MovieContract.MovieNumberEntry.getMovieIdFromUri(uri);

        return sMovieByMovieSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieTypeWithMovieID,
                new String[]{movieType, Long.toString(movieid)},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getTmdbMovieByTmdbId(Uri uri, String[] projection, String sortOrder) {
        //String movieType = MovieContract.MovieNumberEntry.getMovieTypeFromUri(uri);
        String tmdb = MovieContract.MovieImdbEntry.getTmdbMovieFromUri(uri);

        String[] selectionArgs = {tmdb};
        String selection = MovieContract.MovieImdbEntry.TABLE_NAME+"."+MovieContract.MovieImdbEntry.COLUMN_TMDB_ID+" = ?";






        return sTmdbByImdbIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getImdbidMovieByTMDBID(
            Uri uri, String[] projection, String sortOrder) {
        String imdbid = MovieContract.MovieImdbEntry.getImdbIdFromUri(uri);
        String tmdbid = MovieContract.MovieImdbEntry.getTmdbMovieFromUri(uri);

        String selection = MovieContract.MovieImdbEntry.TABLE_NAME+"."+MovieContract.MovieImdbEntry.COLUMN_TMDB_ID+" = ? AND "+
                MovieContract.MovieImdbEntry.COLUMN_IMDB_ID+" = ?";

        return sTmdbByImdbIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                new String[]{tmdbid, imdbid},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getSeriesBySeriesType(Uri uri, String[] projection, String sortOrder) {
        String seriesType = MovieContract.SeriesNumberEntry.getSeriesTypeFromUri(uri);
        long seriesid = MovieContract.SeriesNumberEntry.getSeriesIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        if(seriesid==0) {
            selection = sSeriesTypeSelection;
            selectionArgs = new String[]{seriesType};
        }
        else {
            selectionArgs = new String[]{seriesType, Long.toString(seriesid)};
            selection = sSeriesTypeWithSeriesID;
        }


        return sSeriesBySeriesIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSeriesBySeriesTypeAndSeriesID(
            Uri uri, String[] projection, String sortOrder) {
        String seriesType = MovieContract.SeriesNumberEntry.getSeriesTypeFromUri(uri);
        long seriesid = MovieContract.SeriesNumberEntry.getSeriesIdFromUri(uri);

        return sSeriesBySeriesIDQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sSeriesTypeWithSeriesID,
                new String[]{seriesType, Long.toString(seriesid)},
                null,
                null,
                sortOrder
        );
    }



    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE_NUMBER, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_NUMBER + "/*", MOVIE_WITH_TYPE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_NUMBER + "/*/#", MOVIE_WITH_TYPE_AND_ID);

        matcher.addURI(authority, MovieContract.PATH_SERIES_NUMBER, SERIES);
        matcher.addURI(authority, MovieContract.PATH_SERIES_NUMBER + "/*", SERIES_WITH_TYPE);
        matcher.addURI(authority, MovieContract.PATH_SERIES_NUMBER + "/*/#", SERIES_WITH_TYPE_AND_ID);

        matcher.addURI(authority, MovieContract.PATH_IMDB_NUMBER, TMDB);
        matcher.addURI(authority, MovieContract.PATH_IMDB_NUMBER + "/*", TMDB_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_IMDB_NUMBER + "/*/#", TMDB_WITH_ID_AND_IMDB);


        //matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE_WITH_TYPE:
                return MovieContract.MovieNumberEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieNumberEntry.CONTENT_TYPE;
            case MOVIE_WITH_TYPE_AND_ID:
                return MovieContract.MovieNumberEntry.CONTENT_ITEM_TYPE;


            case SERIES_WITH_TYPE:
                return MovieContract.SeriesNumberEntry.CONTENT_TYPE;
            case SERIES:
                return MovieContract.SeriesNumberEntry.CONTENT_TYPE;
            case SERIES_WITH_TYPE_AND_ID:
                return MovieContract.SeriesNumberEntry.CONTENT_ITEM_TYPE;



            case TMDB_WITH_ID:
                return MovieContract.MovieImdbEntry.CONTENT_TYPE;
            case TMDB:
                return MovieContract.MovieImdbEntry.CONTENT_TYPE;
            case TMDB_WITH_ID_AND_IMDB:
                return MovieContract.MovieImdbEntry.CONTENT_ITEM_TYPE;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"

            case MOVIE_WITH_TYPE:
            {
                retCursor = getMovieByMovieType(uri,projection,sortOrder);
                break;
            }

            case MOVIE_WITH_TYPE_AND_ID:
            {
                retCursor = getMovieByMovieTypeAndMovieID(uri,projection,sortOrder);
                break;
            }

            // "weather"
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieNumberEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case TMDB_WITH_ID:
            {
                retCursor = getTmdbMovieByTmdbId(uri,projection,sortOrder);
                break;
            }

            case TMDB_WITH_ID_AND_IMDB:
            {
                retCursor = getImdbidMovieByTMDBID(uri, projection, sortOrder);
                break;
            }

            // "weather"
            case TMDB: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieImdbEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }





            case SERIES_WITH_TYPE:
            {
                retCursor = getSeriesBySeriesType(uri,projection,sortOrder);
                break;
            }

            case SERIES_WITH_TYPE_AND_ID:
            {
                retCursor = getSeriesBySeriesTypeAndSeriesID(uri, projection, sortOrder);
                break;
            }

            // "weather"
            case SERIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.SeriesNumberEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case MOVIE: {
                long _id = db.insert(MovieContract.MovieNumberEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieNumberEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case SERIES: {
                long _id = db.insert(MovieContract.SeriesNumberEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.SeriesNumberEntry.buildSeriesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TMDB: {
                long _id = db.insert(MovieContract.MovieImdbEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieImdbEntry.buildTmdbMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE:{
                rowsDeleted = db.delete(
                        MovieContract.MovieNumberEntry.TABLE_NAME, selection, selectionArgs);
                break;}

            case SERIES:{
                rowsDeleted = db.delete(
                        MovieContract.SeriesNumberEntry.TABLE_NAME, selection, selectionArgs);
                break;}

            case TMDB:{
                rowsDeleted = db.delete(
                        MovieContract.MovieImdbEntry.TABLE_NAME, selection, selectionArgs);
                break;}

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }



    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {

            case MOVIE:{
                rowsUpdated = db.update(MovieContract.MovieNumberEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;}

            case SERIES:{
                rowsUpdated = db.update(MovieContract.SeriesNumberEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;}

            case TMDB:{
                rowsUpdated = db.update(MovieContract.MovieImdbEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;}

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieNumberEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }


                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case SERIES: {
                db.beginTransaction();
               int  returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.SeriesNumberEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case TMDB: {
                db.beginTransaction();
                int  returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieImdbEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}