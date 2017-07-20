package com.tatsme.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by abhishek on 14/7/17.
 */

public class OpenMovieDataJsonUtils {


    public static String [][] getMoviesStringsFromJson (Context context, String jSonSingleString)
            throws JSONException {

        String [][] parsedMovieData = null; //String 2-dimensional array to hold movie data
        JSONObject jsonMovieData = new JSONObject(jSonSingleString); //JSON of the movie data

        final String TMDB_MESSAGE_CODE = "cod"; //Check for status message

        final String TMDB_RESULTS = "results";

        final String TMDB_MOVIE_POSTER = "poster_path";
        final String TMDB_MOVIE_TITLE = "title";
        final String TMDB_MOVIE_RELEASE_DATE = "release_date";
        final String TMDB_MOVIE_VOTE_AVG = "vote_average";
        final String TMDB_MOVIE_SYNOPSIS = "overview";



        //check for any errors first
        //TODO: fix error handling. It is still not working correctly
        if (jsonMovieData.has(TMDB_MESSAGE_CODE)) {
            int statusCode = jsonMovieData.getInt(TMDB_MESSAGE_CODE);
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: //no result
                    return null;
                default: //server probably down
                    return null;
            }
        }


        JSONArray moviesArray = jsonMovieData.getJSONArray(TMDB_RESULTS);

       /*//// In the next few steps,  we will achieve following  things
                1. we will create 2 dimensional array called parsedMovieData
                2. the length of the parsedMovieData will be same as the number of retrieved JSON objects
                3. each element of parsedMovieData array will consist of 5 sub-elements
                 1. movie poster,
                 2. title
                 3. release date
                 4. vote average, and
                 5. plot synopsis
        ////*/

        parsedMovieData = new String[moviesArray.length()][5];

        for (int i = 0; i < moviesArray.length(); i++) {
            //Get the JSON object representing the movie
            JSONObject movieData = moviesArray.getJSONObject(i);

            /*@param 'thumbnail' will fetch the image corresponding to the small size */
            parsedMovieData[i][0] = NetworkUtils.buildPosterURL(movieData.getString(TMDB_MOVIE_POSTER), "thumbnail")
                    .toString();
            parsedMovieData[i][1] = movieData.getString(TMDB_MOVIE_TITLE);
            parsedMovieData[i][2] = movieData.getString(TMDB_MOVIE_RELEASE_DATE);
            parsedMovieData[i][3] = movieData.getString(TMDB_MOVIE_VOTE_AVG);
            parsedMovieData[i][4] = movieData.getString(TMDB_MOVIE_SYNOPSIS);
        }

        return parsedMovieData;
    }

}
