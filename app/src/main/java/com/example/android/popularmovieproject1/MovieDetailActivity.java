package com.example.android.popularmovieproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_detail_container, new MovieDetailFragment())
                    .commit();

        }
    }



    public static class MovieDetailFragment extends Fragment {
        private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("title") && intent.hasExtra("poster")
                    && intent.hasExtra("overView") && intent.hasExtra("releaseDate")
                    && intent.hasExtra("voteAverage")) {

                String title = intent.getStringExtra("title");
                String poster = intent.getStringExtra("poster");
                String overView = intent.getStringExtra("overView");
                String releaseDate = intent.getStringExtra("releaseDate");
                String voteAverage = intent.getStringExtra("voteAverage");

                Log.v(LOG_TAG, "what's in there? : " + title);

                ImageView view = (ImageView) rootView.findViewById(R.id.detail_poster);
                Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + poster).into(view);

                 ((TextView) rootView.findViewById(R.id.movie_title))
                        .setText(title);

                ((TextView) rootView.findViewById(R.id.movie_release_date))
                        .setText(releaseDate);
                ((TextView) rootView.findViewById(R.id.movie_vote_average))
                        .setText(voteAverage);
                ((TextView) rootView.findViewById(R.id.movie_over_view))
                        .setText(overView);



            }
            return rootView;
        }
    }
}

