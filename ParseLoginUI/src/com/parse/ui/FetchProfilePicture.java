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
package com.parse.ui;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchProfilePicture extends AsyncTask<String, Integer, String> {


   // private final String LOG_TAG = FetchImdbTask.class.getSimpleName();



    private String getMovieDataFromJson(String forecastJsonStr,
                                      String locationSetting)



            throws JSONException {

        String movieurl= "hello";

        final String OWM_LIST = "data";

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
           // JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);

            JSONObject movieForecast = forecastJson.getJSONObject(OWM_LIST);
            movieurl = movieForecast.getString("url");

        } catch (JSONException e) {
           // Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return movieurl;
    }

    @Override
    protected String doInBackground(String... params) {

        String returnurl=null;

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String locationQuery = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;


            try {

                URL url = new URL(locationQuery);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                returnurl = getMovieDataFromJson(forecastJsonStr, locationQuery);
                //getWeatherDataFromJson(forecastJsonStr, locationQuery);
            } catch (IOException e) {
             //   Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
              //  Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                     //   Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

        }

        return returnurl;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}