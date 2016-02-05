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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.support.android.designlibdemo";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
   // public static final String PATH_WEATHER = "weather";
   // public static final String PATH_LOCATION = "location";

    public static final String PATH_MOVIE_NUMBER = "movietable";
   // public static final String PATH_MOVIE_DETAIL = "moviedetail";

    public static final String PATH_IMDB_NUMBER = "imdbtable";


    public static final String PATH_SERIES_NUMBER = "seriestable";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.


    /* Inner class that defines the table contents of the location table */
    public static final class MovieNumberEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_NUMBER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_NUMBER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_NUMBER;

        // Table name
        public static final String TABLE_NAME = "movietableupdated";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_MOVIE_SETTING = "movie_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_MOVIE_NAME = "movie_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_REL_DATE = "rel_date";
        public static final String COLUMN_MOVIE_DES = "movie_des";


        //public static final String COLUMN_COORD_LONG = "coord_long";

        public static final String COLUMN_MOVIE_TYPE = "movie_type";

        public static final String COLUMN_MOVIE_REL_YEAR = "movie_rel_year";

        public static final String COLUMN_MOVIE_CATEGORY = "movie_category";

        public static final String COLUMN_MOVIE_IMAGEURL = "movie_imageurl";

        public static final String COLUMN_MOVIE_WATCHLIST = "movie_watchlist";

        public static final String COLUMN_MOVIE_WATCHED = "movie_watched";

        public static Uri buildMovieUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieType(String movieSetting) {
            return CONTENT_URI.buildUpon().appendPath(movieSetting).build();
        }

        public static String getMovieTypeFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieTypeWithMovieId(
                String movieSetting, String movieid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(movieSetting)
                    .appendQueryParameter(COLUMN_MOVIE_SETTING, movieid).build();
        }
        public static Uri buildMovieTypeWithMovieIdwithPath(
                String movieSetting, String movieid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(movieSetting)
                    .appendPath(movieid).build();
        }



        public static long getMovieIdFromUri(Uri uri) {
            String movieid = uri.getQueryParameter(COLUMN_MOVIE_SETTING);
            if (null != movieid && movieid.length() > 0)
                return Long.parseLong(movieid);
            else
                return 0;
        }


        public static long getMovieIdFromUriUsingPath(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }




    }

    public static final class SeriesNumberEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SERIES_NUMBER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES_NUMBER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES_NUMBER;

        // Table name
        public static final String TABLE_NAME = "seriesupdated";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_SERIES_ID = "series_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_SERIES_NAME = "series_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_SERIES_CATEGORY = "series_category";
        public static final String COLUMN_SERIES_FOLLOWING = "series_follow";

        public static final String COLUMN_SERIES_POPULARITY = "series_popularity";

        public static final String COLUMN_SERIES_IMAGEURL = "series_url";


        public static Uri buildSeriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSeriesType(String seriescategory) {
            return CONTENT_URI.buildUpon().appendPath(seriescategory).build();
        }

        public static String getSeriesTypeFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildSeriesTypeWithSeriesId(
                String seriescat, String seriesid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(seriescat)
                    .appendQueryParameter(COLUMN_SERIES_ID, seriesid).build();
        }
        public static Uri buildSeriesTypeWithSeriesIdwithPath(
                String seriestype, String seriesid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(seriestype)
                    .appendPath(seriesid).build();
        }



        public static long getSeriesIdFromUri(Uri uri) {
            String seriesid = uri.getQueryParameter(COLUMN_SERIES_ID);
            if (null != seriesid && seriesid.length() > 0)
                return Long.parseLong(seriesid);
            else
                return 0;
        }


        public static long getSeriesIdFromUriUsingPath(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }




    }



    public static final class MovieImdbEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMDB_NUMBER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMDB_NUMBER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMDB_NUMBER;

        // Table name
        public static final String TABLE_NAME = "imdbtableupdated";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_TMDB_ID = "tmdb_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_IMDB_ID = "imdb_id";

        public static final String COLUMN_TEMP = "temp";

        public static final String COLUMN_IMDB_RATING = "imdbrating";
        public static final String COLUMN_DELETED = "deleted";

        public static final String COLUMN_MOVIE_NAME = "movie_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_REL_DATE = "rel_date";
        public static final String COLUMN_MOVIE_DES = "movie_des";


        public static final String COLUMN_MOVIE_REL_MONTH = "movie_rel_month";


        public static final String COLUMN_MOVIE_IMAGEURL = "movie_imageurl";

        public static final String COLUMN_MOVIE_WATCHLIST = "movie_watchlist";

        public static final String COLUMN_MOVIE_WATCHED = "movie_watched";

        public static final String COLUMN_PRO_COUNTRY = "prod_ctry";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
       // public static final String COLUMN_REL_DATE = "rel_date";
       // public static final String COLUMN_MOVIE_DES = "movie_des";


        //public static final String COLUMN_COORD_LONG = "coord_long";

       // public static final String COLUMN_MOVIE_TYPE = "movie_type";

       // public static final String COLUMN_MOVIE_REL_YEAR = "movie_rel_year";

       // public static final String COLUMN_MOVIE_CATEGORY = "movie_category";

      //  public static final String COLUMN_MOVIE_IMAGEURL = "movie_imageurl";

      //  public static final String COLUMN_MOVIE_WATCHLIST = "movie_watchlist";

    //        public static final String COLUMN_MOVIE_WATCHED = "movie_watched";

        public static Uri buildTmdbMovieUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTmdbType(String tmdbid) {
            return CONTENT_URI.buildUpon().appendPath(tmdbid).build();
        }

        public static String getTmdbMovieFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildTmdbIdWithImdbId(
                String tmdbid, String imdbid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(tmdbid)
                    .appendQueryParameter(COLUMN_IMDB_ID, imdbid).build();
        }
        public static Uri buildTmdbIdWithImdbIdwithPath(
                String tmdbid, String imdbid) {
            //long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(tmdbid)
                    .appendPath(imdbid).build();
        }



        public static String getImdbIdFromUri(Uri uri) {
            String imdbid = uri.getQueryParameter(COLUMN_IMDB_ID);
            if (null != imdbid && imdbid.length() > 0)
                return (imdbid);
            else
                return "hello";
        }


        public static String getImdbIdFromUriUsingPath(Uri uri) {
            return (uri.getPathSegments().get(2));
        }




    }



}
