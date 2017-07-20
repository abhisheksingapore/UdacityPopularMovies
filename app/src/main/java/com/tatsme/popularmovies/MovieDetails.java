package com.tatsme.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.tatsme.popularmovies.MainActivity.MOVIE_POSTER_PATH;
import static com.tatsme.popularmovies.MainActivity.MOVIE_RELEASE_DATE;
import static com.tatsme.popularmovies.MainActivity.MOVIE_SYNOPSIS;
import static com.tatsme.popularmovies.MainActivity.MOVIE_TITLE;
import static com.tatsme.popularmovies.MainActivity.MOVIE_VOTE_AVERAGE;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String moviePosterPath = extras.getString("MOVIE_POSTER_PATH");
        String movieTitle = extras.getString("MOVIE_TITLE");
        String releaseDate = extras.getString("MOVIE_RELEASE_DATE");
        String voteAvg = extras.getString("MOVIE_VOTE_AVERAGE");
        String synopsis = extras.getString("MOVIE_SYNOPSIS");

        ImageView imageViewMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        TextView textViewMovieTitle = (TextView) findViewById(R.id.movie_title);
        TextView textViewMovieReleaseDate = (TextView)findViewById(R.id.movie_release_date);
        TextView textViewMovieVoteAvg = (TextView) findViewById(R.id.movie_vote_average);
        TextView textViewSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis);

        Picasso.with(this).load(moviePosterPath).into(imageViewMoviePoster);
        textViewMovieTitle.setText(movieTitle);
        textViewMovieReleaseDate.setText("Release date:" + releaseDate);
        textViewMovieVoteAvg.setText("Vote Average:" + voteAvg);
        textViewSynopsis.setText("Plot Synopsis: \n" + synopsis);
    }
}
