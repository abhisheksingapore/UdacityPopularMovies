package com.tatsme.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by abhishek on 19/7/17.
 */

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieAdapterViewHolder> {
    /*//// parsedMovieData is a 2 dimensional array called parsedMovieData. Each element of
    parsedMovieData array consists of 5 sub-elements
                 1. movie poster URL,
                 2. title
                 3. release date
                 4. vote average, and
                 5. plot synopsis
        ////*/

    private String[][] parsedMovieData;
    Context context;

    /* An onClick handler for the Activity to interface with the Recyclerview */
    private final MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    /* The interface that receives onClick messages*/
    public interface MovieAdapterOnClickHandler {
        void onClick (String moviePosterPath, String movieTitle, String releaseDate, String voteAvg,
                      String synopsis);
    }

    /* Creates a  MovieAdapter */
    public MovieAdapter (MovieAdapterOnClickHandler clickHandler) {
        movieAdapterOnClickHandler = clickHandler;
    }

    /* Cache of the children views for the forecast item*/
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView movieImageView;

        public MovieAdapterViewHolder (View view) {
            super(view);
            movieImageView = (ImageView) view.findViewById(R.id.imageViewPoster);
            view.setOnClickListener(this);
        }

        // This gets called by the child views during a click

        @Override
        public void onClick (View view) {
            int adapterPosition = getAdapterPosition();
            String moviePosterPath = parsedMovieData[adapterPosition][0];
            String movieTitle = parsedMovieData[adapterPosition][1];
            String releaseDate = parsedMovieData[adapterPosition][2];
            String voteAvg = parsedMovieData[adapterPosition][3];
            String synopsis = parsedMovieData[adapterPosition][4];
            movieAdapterOnClickHandler.onClick(moviePosterPath, movieTitle, releaseDate, voteAvg, synopsis);
        }
    }

    /* This gets created when a new Viewholder is created */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder (ViewGroup viewGroup, int viewType) {
        Context viewContext;
        viewContext = viewGroup.getContext();
        int layoutIDforListItem = R.layout.movie_list_item;

        LayoutInflater inflater = LayoutInflater.from(viewContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIDforListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    // OnBindViewHolder is called by RecyclerView to display the data at the specified location
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String moviePosterPath = parsedMovieData [position][0]; //This element will correspond to poster URL path
        Picasso.with(context).load(moviePosterPath).into(movieAdapterViewHolder.movieImageView);
    }

    //This method returns the number of items to display in the RecyclerView
    @Override
    public int getItemCount () {
        if (parsedMovieData == null) {
            return 0;
        }
        else {
            return parsedMovieData.length;
        }
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it. */

    public void setParsedMovieData (String [][] movieData, Context mainContext) {
        context = mainContext;
        parsedMovieData = movieData;
        notifyDataSetChanged();
    }

}
