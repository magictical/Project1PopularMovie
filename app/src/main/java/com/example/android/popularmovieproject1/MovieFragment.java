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
import java.util.List;

/**
 * Created by MD on 2016-03-31.
 */
public class MovieFragment extends Fragment {

    //the Adapter for storing moviePosterList
    private MovieAdapter movieAdapter;

    //the ArrayList for storing movieInfo
    List<MovieInfo> movieInfoList = new ArrayList<>();

    //Instance for saving Bundle
    ArrayList<MovieInfo> saveMovieInfoList;


    //dummy data
    /*MovieInfo[] moviePosters = {
            *//*new MovieInfo("bus657", R.drawable.bus657),
            new MovieInfo("cicvilwar", R.drawable.civilwar),
            new MovieInfo("hungergame2", R.drawable.hungergame2),
            new MovieInfo("interstellar", R.drawable.interstellar),
            new MovieInfo("madmax", R.drawable.madmax),
            new MovieInfo("minions", R.drawable.minions),
            new MovieInfo("revernant", R.drawable.revernant),
            new MovieInfo("spector", R.drawable.spector),
            new MovieInfo("terminator", R.drawable.terminator)*//*
    };*/




    //종류별로 저장해야될것 같은데 확인해야함!!
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //preventing data loss from device rotation.
        if(savedInstanceState == null || !savedInstanceState.containsKey(getString(R.string.key_save_data))) {
            //if ArrayList<MovieInfo> is not exist, make new object for it.
            saveMovieInfoList = new ArrayList<MovieInfo>((movieInfoList));
        } else {
            saveMovieInfoList = savedInstanceState.getParcelableArrayList(getString(R.string.key_save_data));
        }

        FetchMovieData fetchMovieData = new FetchMovieData();
        //this will launch the doInBackground()
        fetchMovieData.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //the preserved value will be put on based on its key.
        //ParcelableArrayList에 맞게 유형을 맞춰서 사용해야함 e.g : saveMovieInfoList는 arraylist 임
        outState.putParcelableArrayList(getString(R.string.key_save_data), saveMovieInfoList);
        super.onSaveInstanceState(outState);
    }
    //every argument LayoutInflater, ViewGroup, Bundle are coming from Context
    // when system Overrride method. Overrride triggers taking automatic tools supplied by SDK and
    //modify components to our specific application.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.moviefragment_main,container, false );
        movieAdapter = new MovieAdapter(getActivity(), movieInfoList);

        //inflate data based on preferred Adapter
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_moviefragment);
        gridView.setAdapter(movieAdapter);

        return rootView;
    }

    //for using internet connection, use AsyncTask
    //this class will get  url connection and  the JSON string from background thread and
    // JSON string pass to UI thread which is getMovieDataFromJson
    public class FetchMovieData extends AsyncTask<String, Void, List<MovieInfo>> {

        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        //get an argument from api and use the parameter to extract the data that is needed.
        //this will callback by doInBackground at first catch block
        private List<MovieInfo> getMovieDataFromJson(String movieInfoStr)
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
            int howLong = movieArray.length();
            Log.v(LOG_TAG, "how long" + howLong);

            //in this project, 20 lists are enough.
            //don't need to consider the endless scrollview which is quite interesting.
            //about EndlessScrollview
            // : https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews



            //create MovieInfo Object to save string in JSONArray
            movieInfoList = new ArrayList<>();

            for(int i = 0; i < movieArray.length(); i++) {

                //get a JSON abject data from movieArray(JSONArray)
                JSONObject movieUrlPath = movieArray.getJSONObject(i);
                JSONObject movieOverView = movieArray.getJSONObject(i);
                JSONObject movieReleaseDate = movieArray.getJSONObject(i);
                JSONObject movieTitle = movieArray.getJSONObject(i);
                JSONObject movieVoteAverage = movieArray.getJSONObject(i);



                MovieInfo movieInfo = new MovieInfo();

                //get a string value from JSONobject
                movieInfo.setPosterPath(movieUrlPath.getString(SEARCH_POSTER_PATH));
                movieInfo.setOverView(movieOverView.getString(SEARCH_OVERVIEW));
                movieInfo.setReleaseDate(movieReleaseDate.getString(SEARCH_RELEASE_DATE));
                movieInfo.setTitle(movieTitle.getString(SEARCH_TITLE));
                movieInfo.setVoteAverage(movieVoteAverage.getString(SEARCH_VOTE));

                Log.v(LOG_TAG, "path is : " + movieInfo.getPosterPath()
                        + "overview is : " + movieInfo.getOverView()
                        + "release date is " + movieInfo.getReleaseDate()
                        + " title is " + movieInfo.getTitle()
                        + "vote is "  + movieInfo.getVoteAverage()
                );
                movieInfoList.add(movieInfo);
            }

            return movieInfoList;
        }

        @Override
        protected List<MovieInfo> doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //a instance put the string from JSON
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
                /*Log.v(LOG_TAG, " movieInfo string " + movieInfoStr);*/

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

        @Override
        protected void onPostExecute(List<MovieInfo> result) {

            super.onPostExecute(result);

            if(result != null) {
                movieAdapter.clear();
                movieAdapter.addAll(result);
            }

        }
    }

}
