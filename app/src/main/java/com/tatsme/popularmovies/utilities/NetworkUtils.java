package com.tatsme.popularmovies.utilities;

import android.net.Uri;
import android.os.Parcel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by abhishek on 13/7/17.
 */

public final class NetworkUtils {
    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie";

    private static final String BASE_MOVIE_POSTER_URL="https://image.tmdb.org/t/p/";

    private static final String API_KEY = "To be inserted"; //Replace with your own API key

    private static final String DEFAULT_API_CALL_FOR_MOVIES_SORT = "popular";

    private static final String API_CALL_FOR_MOVIES_SORTED_TOP_RATED = "top_rated";

    private static final String DEFAULT_API_CALL_FOR_MOVIES_RESULTS = "1"; //return results to fill 1 page

    //This method is for loading movies with default sort criteria which is sortby "Most Popular"
    public static URL buildURL() {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(DEFAULT_API_CALL_FOR_MOVIES_SORT)
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("page",DEFAULT_API_CALL_FOR_MOVIES_RESULTS)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException mURLe) {
            mURLe.printStackTrace();
        }
        return url;
    }

    //This method is for loading movies with  sort criteria "Top Rated"
    public static URL buildURL(String [] topRated) {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(API_CALL_FOR_MOVIES_SORTED_TOP_RATED)
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("page",DEFAULT_API_CALL_FOR_MOVIES_RESULTS)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException mURLe) {
            mURLe.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException {
        HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//A");
            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static URL buildPosterURL (String imageName, String posterSize) {
        String posterURLName = "";
        URL posterURL = null;

        switch (posterSize) {
            case "big":
                posterURLName = BASE_MOVIE_POSTER_URL + "w1280" + imageName;
            case "thumbnail": //For my phone the best thumbnail layout view was with w342
                posterURLName = BASE_MOVIE_POSTER_URL + "w342" + imageName;
        }

        try {
            posterURL = new URL(posterURLName);
        } catch (MalformedURLException urlException) {
            urlException.printStackTrace();
        }
        return posterURL;
    }

}
