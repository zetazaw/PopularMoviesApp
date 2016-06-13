package net.konyan.popularmoviesapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import net.konyan.popularmoviesapp.MovieDetailActivity;
import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.adapter.MoviesAdapter;
import net.konyan.popularmoviesapp.data.ParcelMoviesResult;
import net.konyan.popularmoviesapp.utils.WebConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;



public class MainFragment extends Fragment {

    final String LOG_TAG = MainFragment.class.getSimpleName();

    private String currentOrder = WebConfig.PATH_POPULAR;

    public MainFragment() {}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    List<ParcelMoviesResult> moviesResults = new ArrayList<>();
    MoviesAdapter moviesAdapter;

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_main_fragment);

        moviesAdapter = new MoviesAdapter(getActivity(), moviesResults);

        GridView gridView = (GridView)view.findViewById(R.id.grid_movie_posters);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_SUBJECT, moviesResults.get(position));
                startActivity(detailIntent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchPopularMovies().execute(currentOrder);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        progressBar.setVisibility(View.VISIBLE);
        switch (id){
            case R.id.action_popular:{
                if (currentOrder.equalsIgnoreCase(WebConfig.PATH_TOP_RATED)){

                    new FetchPopularMovies().execute(WebConfig.PATH_POPULAR);
                    currentOrder = WebConfig.PATH_POPULAR;
                }
                return true;
            }
            case R.id.action_top_rated:{
                if (currentOrder.equalsIgnoreCase(WebConfig.PATH_POPULAR)){
                    new FetchPopularMovies().execute(WebConfig.PATH_TOP_RATED);
                    currentOrder = WebConfig.PATH_TOP_RATED;
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchPopularMovies extends AsyncTask<String, Void, List<ParcelMoviesResult>>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<ParcelMoviesResult> doInBackground(String... params) {
            return fetchMovies(params[0]);
        }

        List<ParcelMoviesResult> fetchMovies(String sort){
            String jsonStr = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Log.d(LOG_TAG, "fetch_url: "+WebConfig.getMoviesUrl(sort).toString());

                connection = (HttpURLConnection) WebConfig.getMoviesUrl(sort).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    return null;
                }

                jsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "connection_error: ", e);
            } finally {
                if (connection != null){
                    connection.disconnect();
                }

                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "closing_steam_error: ", e);
                    }
                }
            }

            //parse from json and return parcel list
            try {
                return parseJson(jsonStr);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "json_error: ", e);
            }
            return null;
        }

        public List<ParcelMoviesResult> parseJson(String jsonStr) throws JSONException {
            List<ParcelMoviesResult> moviesList = new ArrayList<>();

            if (jsonStr != null){
                JSONObject rootObj = new JSONObject(jsonStr);
                JSONArray moviesArray = rootObj.getJSONArray(ParcelMoviesResult.RESULTS);

                for (int i = 0; i<moviesArray.length(); i++){

                    String poster_path = moviesArray.getJSONObject(i).getString(ParcelMoviesResult.POSTER_PATH);
                    String original_title = moviesArray.getJSONObject(i).getString(ParcelMoviesResult.ORIGINAL_TITLE);
                    String backdrop_path = moviesArray.getJSONObject(i).getString(ParcelMoviesResult.BACKDROP_PATH);
                    String overview = moviesArray.getJSONObject(i).getString(ParcelMoviesResult.OVERVIEW);
                    double vote_average = moviesArray.getJSONObject(i).getDouble(ParcelMoviesResult.VOTE_AVERAGE);
                    String release_date = moviesArray.getJSONObject(i).getString(ParcelMoviesResult.RELEASE_DATE);

                    ParcelMoviesResult result =
                            new ParcelMoviesResult(poster_path, original_title,
                                    backdrop_path, overview,
                                    vote_average, release_date);
                    moviesList.add(result);
                }
            }

            return moviesList;
        }

        @Override
        protected void onPostExecute(List<ParcelMoviesResult> parcelMoviesResults) {
            progressBar.setVisibility(View.GONE);
            if (parcelMoviesResults != null){
                moviesResults.clear();
                moviesResults.addAll(parcelMoviesResults);
                moviesAdapter.notifyDataSetChanged();
            }
        }
    }
}
