package net.konyan.popularmoviesapp.fragment;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.konyan.popularmoviesapp.DetailActivity;
import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.adapter.MoviesAdapter;
import net.konyan.popularmoviesapp.data.MovieColumn;
import net.konyan.popularmoviesapp.data.MovieProvider;
import net.konyan.popularmoviesapp.data_model.Movie;
import net.konyan.popularmoviesapp.utils.Util;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    final String LOG_TAG = MainFragment.class.getSimpleName();

    private String currentOrder = WebConfig.PATH_POPULAR;

    private static final int CURSOR_LOADER_ID = 0;

    MovieClickCallback clickCallback;

    public MainFragment() {}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, null, null, null);
        //Log.i(LOG_TAG, "cursor count: " + c.getCount());
        //cursor loading fetch logic here!
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieClickCallback){
            clickCallback = (MovieClickCallback) context;
        }else {
            throw new RuntimeException(context + " need to implement "+MovieClickCallback.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //List<ParcelMoviesResult> moviesResults = new ArrayList<>();
    MoviesAdapter moviesAdapter;

    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_main_fragment);

        moviesAdapter = new MoviesAdapter(getActivity(), null, new MoviesAdapter.ItemClickListener() {
            @Override
            public void onClick(Uri uri) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.setData(uri);
                startActivity(detailIntent);
                clickCallback.onClick(uri);
            }
        });

        RecyclerView recyclerMovies = (RecyclerView) view.findViewById(R.id.rv_movie_posters);
        recyclerMovies.setAdapter(moviesAdapter);
        recyclerMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchAndUpdateMovies(currentOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "main frag on resume");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_popular:{
                if (currentOrder.equalsIgnoreCase(WebConfig.PATH_TOP_RATED)){
                    currentOrder = WebConfig.PATH_POPULAR;
                    selectionArgs[0] = currentOrder;
                    selection = MovieColumn.USER_SORT + " LIKE ? ";
                    fetchAndUpdateMovies(currentOrder);
                }
                break;
                //return true;
            }
            case R.id.action_top_rated:{
                if (currentOrder.equalsIgnoreCase(WebConfig.PATH_POPULAR)){
                    currentOrder = WebConfig.PATH_TOP_RATED;
                    selectionArgs[0] = currentOrder;
                    selection = MovieColumn.USER_SORT + " LIKE ? ";
                    fetchAndUpdateMovies(currentOrder);
                }
                break;
                //return true;
            }
            case R.id.action_my_favorite:{
                selectionArgs[0] = WebConfig.PATH_FAVORITE;
                selection = MovieColumn.USER_FAVORITE + " LIKE ? ";
                break;
            }
            default:break;

        }

        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        return super.onOptionsItemSelected(item);
    }


    private void fetchAndUpdateMovies(final String sort){

        long lastUpdated = Util.getUpdateTime(getActivity(), sort);
        long diff = System.currentTimeMillis() - lastUpdated;
        if ( diff > (1000*60*3) ){ //3 min
            progressBar.setVisibility(View.VISIBLE);
            Call<Movie> call = WebConfig.getMovies(currentOrder);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Log.d(LOG_TAG, "retro:"+ response.message());
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        insertData(response.body().movies, sort);
                        Util.saveUpdateTime(getActivity(), sort);
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.d(LOG_TAG, "retro error:"+ t.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            Log.d(LOG_TAG, "updated in three");
        }

    }

    /*try to find same movies to update but taking care for sort category*/
    private void insertData(List<Movie.MovieModel> movieList, String sort){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movieList.size());

        for (Movie.MovieModel movie : movieList){

            String mSelectionClause = MovieColumn.USER_SORT +" ='"+sort +"' AND "+MovieColumn.MOVIE_ID +  " ='"+ movie.id +"'";

            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(MovieColumn.ORIGINAL_TITLE, movie.original_title);
            mUpdateValues.put(MovieColumn.POSTER_PATH, movie.poster_path);

            mUpdateValues.put(MovieColumn.BACK_DROP_URL, movie.backdrop_path);
            mUpdateValues.put(MovieColumn.PLOT, movie.overview);
            mUpdateValues.put(MovieColumn.RATING, movie.vote_average);
            mUpdateValues.put(MovieColumn.RELEASE_DATE, movie.release_date);

            int mRowsUpdated = getContext().getContentResolver().update(
                    MovieProvider.Movies.CONTENT_URI,
                    mUpdateValues,
                    mSelectionClause,
                    null
            );

            if (mRowsUpdated < 1){
                ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(MovieProvider.Movies.CONTENT_URI);
                builder.withValue(MovieColumn.MOVIE_ID, movie.id);
                builder.withValue(MovieColumn.ORIGINAL_TITLE, movie.original_title);
                builder.withValue(MovieColumn.POSTER_PATH, movie.poster_path);
                builder.withValue(MovieColumn.BACK_DROP_URL, movie.backdrop_path);
                builder.withValue(MovieColumn.PLOT, movie.overview);
                builder.withValue(MovieColumn.RATING, movie.vote_average);
                builder.withValue(MovieColumn.RELEASE_DATE, movie.release_date);
                builder.withValue(MovieColumn.USER_SORT, sort);
                batchOperations.add(builder.build());
            }

        }

        try{
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
            getActivity().getContentResolver().notifyChange(MovieProvider.Movies.CONTENT_URI, null);
        } catch(RemoteException | OperationApplicationException e){
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    String[] projection = {MovieColumn._ID,
            MovieColumn.MOVIE_ID,
            MovieColumn.ORIGINAL_TITLE,
            MovieColumn.POSTER_PATH};
    String selection = MovieColumn.USER_SORT + " LIKE ? ";
    String[] selectionArgs = {currentOrder};
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }

    public interface MovieClickCallback{
        void onClick(Uri id);
    }
}
