package com.example.android.popularmovieproject1;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        FetchMovieData fetchMovieData = new FetchMovieData();
        fetchMovieData.execute();
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

    //Async 하위의 background 스레드를 실행하고 해당 데이터를 가져올 내부클래스 생성

    //for using internet connection, use AsyncTask
    //this class will get the data from JSON on main thread and get internet connection on background thread
    public class FetchMovieData extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        /*사용자 편의에 맞게 데이터가 읽기 쉽게 변환할 메소드(필요할경우)
        e.g: 화씨 썹씨로 바꾸는 Math.round 고려해봐야함
        private void changeFormat(){}*/


        //get an argument from api and use the parameter to extract the data that need.
        private String[] getMovieDataFromJson(String movieInfoStr)
                throws JSONException{
            //the name of the JSON object that need to be extracted.
            final String RESULT_LIST = "results";
            final String SEARCH_POSTER_PATH = "poster_path";
            final String SEARCH_OVERVIEW = "overview";
            final String SEARCH_RELEASE_DATE = "release_date";
            final String SEARCH_TITLE = "title";
            final String SEARCH_VOTE = "vote_average";

            JSONObject movieJson = new JSONObject(movieInfoStr);
            //JSON을 살펴보면 results와 genre만 JSONArray이고 나머지는 JSONObject상태라고 보면된다.
            //result의 모든 값을 JSONArray에 담음

            //get a JSON Array
            JSONArray movieArray = movieJson.getJSONArray(RESULT_LIST);

            //just checking for how many list from JSONArray
            int howlong = movieArray.length();
            Log.v(LOG_TAG, "how long" + howlong);

            //in this project, 20 lists are enough.
            // don't need to consider the endless scrollview which is quite interesting.
            //about EndlessScrollview
            // : https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
            String[] resultStrs = new String[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {
                String poster_path;
                String overview;
                String release_date;
                String title;
                String vote_average;


                //get a JSON abject data from movieArray(JSONArray)
                JSONObject movieInfo = movieArray.getJSONObject(i);

                //get a string value from JSONobject
                poster_path = movieInfo.getString(SEARCH_POSTER_PATH);
                resultStrs[i] = "http://image.tmdb.org/t/p/w185" + poster_path;
            }

            for (String  s : resultStrs) {
                Log.v(LOG_TAG, "Movie Info entry" + s);
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params) {
            //나중에 fetchMovieData에서 받을 param weather 앱에서는 위치값은 q의 값을 받았었음 필요하면 구현
            /*if (params.length ==0) {
                return null;
            }*/

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //a instance to put a data from JSON
            String movieInfoStr = null;
            String sortingType = "popularity.desc";


            try{
                //Construct the URL for the OpenWeatherMap query
                final String MOVIE_ORG_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORTING_PARAM = "sort_by";
                final String APPID_PRAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_ORG_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTING_PARAM,sortingType)
                        .appendQueryParameter(APPID_PRAM,BuildConfig.API_KEY_FOR_MOVIE_ORG)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, " Built URI " + builtUri.toString());

                //Create the request to the movie.org and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    //do nothing
                    return  null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));


                String line;
                while((line = reader.readLine()) != null) {
                    //For easy reading, make a new line while getting data from JSON
                    buffer.append(line + "\n");

                    //there is data length limit when you see the ADM manager
                    //in this case even though the data sent successfully
                    //you can't check the full string from Log.v which is alright.
                    Log.v(LOG_TAG, "let's see : " + buffer);
                }

                if (buffer.length() == 0) {
                    //stop to parsing
                    return null;
                }
                movieInfoStr = buffer.toString();
                Log.v(LOG_TAG, " movieInfo string " + movieInfoStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                //ended the parsing
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closeing stream", e);
                    }
                }
            }

            try {
                return  getMovieDataFromJson(movieInfoStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            //if there is error getting movieInfo or parsing return null
            return null;
        }

        /*@Override
        protected void onPostExcute(String[] result) {

            }

        }*/
    }

}
