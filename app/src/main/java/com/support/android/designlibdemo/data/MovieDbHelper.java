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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.support.android.designlibdemo.data.MovieContract.MovieNumberEntry;
import com.support.android.designlibdemo.data.MovieContract.SeriesNumberEntry;

/**
 * Manages a local database for weather data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "moviedatabase.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieNumberEntry.TABLE_NAME + " (" +
                MovieNumberEntry._ID + " INTEGER PRIMARY KEY," +
                MovieNumberEntry.COLUMN_MOVIE_SETTING + " TEXT UNIQUE NOT NULL, " +
                MovieNumberEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MovieNumberEntry.COLUMN_MOVIE_DES + " TEXT NOT NULL, " +
                MovieNumberEntry.COLUMN_REL_DATE + " TEXT NOT NULL, " +
                MovieNumberEntry.COLUMN_MOVIE_TYPE + " TEXT NOT NULL, "+
                MovieNumberEntry.COLUMN_MOVIE_REL_YEAR + " TEXT NOT NULL, "+
                MovieNumberEntry.COLUMN_MOVIE_CATEGORY + " TEXT NOT NULL, "+
                MovieNumberEntry.COLUMN_MOVIE_IMAGEURL + " TEXT NOT NULL, "+
                MovieNumberEntry.COLUMN_MOVIE_WATCHLIST + " BOOLEAN NOT NULL, "+
                MovieNumberEntry.COLUMN_MOVIE_WATCHED + " BOOLEAN NOT NULL "+
                " );";

       // public static final String COLUMN_MOVIE_IMAGEURL = "movie_imageurl";

       // public static final String COLUMN_MOVIE_WATCHLIST = "movie_watchlist";

       // public static final String COLUMN_MOVIE_WATCHED = "movie_watched";


        final String SQL_CREATE_SERIES_TABLE = "CREATE TABLE " + SeriesNumberEntry.TABLE_NAME + " (" +
                SeriesNumberEntry._ID + " INTEGER PRIMARY KEY," +
                SeriesNumberEntry.COLUMN_SERIES_ID + " TEXT UNIQUE NOT NULL, " +
                SeriesNumberEntry.COLUMN_SERIES_NAME + " TEXT NOT NULL, " +
                SeriesNumberEntry.COLUMN_SERIES_CATEGORY + " TEXT NOT NULL, " +
                SeriesNumberEntry.COLUMN_SERIES_FOLLOWING + " TEXT NOT NULL, " +
                SeriesNumberEntry.COLUMN_SERIES_POPULARITY + " TEXT NOT NULL, " +
                SeriesNumberEntry.COLUMN_SERIES_IMAGEURL + " TEXT NOT NULL "+
                " );";



        final String SQL_CREATE_IMDB_TABLE = "CREATE TABLE " + MovieContract.MovieImdbEntry.TABLE_NAME + " (" +
                MovieContract.MovieImdbEntry._ID + " INTEGER PRIMARY KEY," +
                MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " TEXT UNIQUE NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_IMDB_ID + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_TEMP + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING + " REAL NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_DELETED + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_REL_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_DES + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_REL_MONTH + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_IMAGEURL + " TEXT NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHLIST + " BOOLEAN NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHED + " BOOLEAN NOT NULL, " +
                MovieContract.MovieImdbEntry.COLUMN_PRO_COUNTRY + " TEXT NOT NULL " +
                " );";






        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SERIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_IMDB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieNumberEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SeriesNumberEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieImdbEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
