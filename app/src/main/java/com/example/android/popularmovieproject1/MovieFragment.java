package com.example.android.popularmovieproject1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by MD on 2016-03-31.
 */
public class MovieFragment extends Fragment {
    //the Adapter for storing moviePosterList
    private  PostAdapter postAdapter;

    //the ArrayList for storing moviePoster
    private ArrayList<MoviePoster> moviePosterList;

    //dummy data
    MoviePoster[] moviePosters = {
            new MoviePoster("bus657", R.drawable.bus657),
            new MoviePoster("cicvilwar", R.drawable.civilwar),
            new MoviePoster("hungergame2", R.drawable.hungergame2),
            new MoviePoster("interstellar", R.drawable.interstellar),
            new MoviePoster("madmax", R.drawable.madmax),
            new MoviePoster("minions", R.drawable.minions),
            new MoviePoster("revernant", R.drawable.revernant),
            new MoviePoster("spector", R.drawable.spector),
            new MoviePoster("terminator", R.drawable.terminator)
    };





    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //preventing data loss from device rotation.
        if(savedInstanceState == null || !savedInstanceState.containsKey("saveposter")) {
            //if ArrayList<MoviePoster> is not exist, make new object for it.
            moviePosterList = new ArrayList<MoviePoster>(Arrays.asList(moviePosters));
        } else {
            moviePosterList = savedInstanceState.getParcelableArrayList("saveposter");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //the preserved value will be put on based on its key.
        outState.putParcelableArrayList("saveposter", moviePosterList);
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.moviefragment_main,container, false );
        postAdapter = new PostAdapter(getActivity(), moviePosterList);

        //inflate data based on preferred Adapter
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_moviefragment);
        gridView.setAdapter(postAdapter);

        return rootView;
    }

    /*AsyncTask를 실행할 메소드 필요
    * background에서 picasso가 이미지를 처리할 수 있는 코드 작성*/

}
