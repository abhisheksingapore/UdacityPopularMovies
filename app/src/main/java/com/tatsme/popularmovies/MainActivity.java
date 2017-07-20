package com.tatsme.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tatsme.popularmovies.utilities.NetworkUtils;
import com.tatsme.popularmovies.utilities.OpenMovieDataJsonUtils;
import com.tatsme.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private RecyclerView recyclerViewImageList;
    int numberOfColumns = 2; //number of columns for the grid layout of the recyclerview
    private MovieAdapter movieAdapter;

    //Following variables will be passed from the Main movie listing activity to movie details Activity
    public static final String MOVIE_POSTER_PATH="";
    public static final String MOVIE_TITLE="";
    public static final String MOVIE_RELEASE_DATE="";
    public static final String MOVIE_VOTE_AVERAGE="";
    public static final String MOVIE_SYNOPSIS="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        //imageView = (ImageView) findViewById(R.id.imageViewPoster);
        recyclerViewImageList = (RecyclerView) findViewById(R.id.recylerViewMovieList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,numberOfColumns);

        recyclerViewImageList.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this);

        recyclerViewImageList.setAdapter(movieAdapter);

        new FetchMovieData().execute();
    }

    @Override
    public void onClick(String moviePosterPath, String movieTitle, String releaseDate, String voteAvg,
                        String synopsis){
        /*Toast.makeText(this, movieData, Toast.LENGTH_SHORT)
                .show();
                TODO: to be deleted soon*/

        Context context = this;
        Intent intent = new Intent(this, MovieDetails.class);

        Bundle extras = new Bundle();
        extras.putString("MOVIE_POSTER_PATH", moviePosterPath);
        extras.putString("MOVIE_TITLE", movieTitle);
        extras.putString("MOVIE_RELEASE_DATE", releaseDate);
        extras.putString("MOVIE_VOTE_AVERAGE", voteAvg);
        extras.putString("MOVIE_SYNOPSIS", synopsis);
        intent.putExtras(extras);

        startActivity(intent);
    }

    public class FetchMovieData extends AsyncTask <String, Void, String[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground (String... params) {
            URL movieRequestURL = null;

            if (params.length == 0) {
                movieRequestURL = NetworkUtils.buildURL();
            } else {
                movieRequestURL = NetworkUtils.buildURL(params);
            }

            try {
                String jSonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                String [][] parsedMovieData = OpenMovieDataJsonUtils.getMoviesStringsFromJson
                        (MainActivity.this, jSonMovieResponse);
                return parsedMovieData;
                /*//// parsedMovieData is a 2 dimensional array. Each element of
                parsedMovieData array consists of 5 sub-elements
                 1. movie poster URL,
                 2. title
                 3. release date
                 4. vote average, and
                 5. plot synopsis
                                    ////*/
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData!=null) {
                movieAdapter.setParsedMovieData(movieData, MainActivity.this);
            } else
            {
                Toast.makeText(MainActivity.this, "No movie data found", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    //This method is to create the menu for different sort option settings
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.most_popular) {
            movieAdapter.setParsedMovieData(null, this);
            new FetchMovieData().execute();
            return true;
        }

        if (id == R.id.top_rated) {
            movieAdapter.setParsedMovieData(null, this);
            new FetchMovieData().execute("topRated");
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
