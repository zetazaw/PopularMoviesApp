package net.konyan.popularmoviesapp.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.MovieColumn;
import net.konyan.popularmoviesapp.data.MovieProvider;
import net.konyan.popularmoviesapp.data.ParcelMoviesResult;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String KEY_MOVIE_DATA = "key_movie_data";
    private static final int DETAIL_LOADER = 0;

    private static ParcelMoviesResult movieData;


    private int movieId;
    MovieLoadFinishListener loadFinishListener;

    private static Uri movieUri;
    @BindView(R.id.tv_detail_movie_title)
    TextView tvDetailMovieTitle;
    @BindView(R.id.tv_detail_movie_release_date)
    TextView tvDetailMovieReleaseDate;
    @BindView(R.id.tv_detail_movie_rating)
    TextView tvDetailMovieRating;
    @BindView(R.id.iv_detail_movie_poster)
    ImageView ivDetailMoviePoster;
    @BindView(R.id.tv_detail_movie_plot)
    TextView tvDetailMoviePlot;
    @BindView(R.id.bt_detail_movie_favorite)
    Button btDetailMovieFavorite;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(Bundle args) {
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieUri = getArguments().getParcelable(Intent.EXTRA_STREAM);
        }else {
            getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, null, null, null);
            movieUri = MovieProvider.Movies.withId(0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieLoadFinishListener){
            loadFinishListener = (MovieLoadFinishListener) context;
        }else {
            throw new RuntimeException(context + " require to implement ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    private String dateFormatter(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("yyyy").format(date);

    }

    String[] projection = {MovieColumn._ID,
            MovieColumn.MOVIE_ID,
            MovieColumn.ORIGINAL_TITLE,
            MovieColumn.POSTER_PATH,
            MovieColumn.BACK_DROP_URL,
            MovieColumn.PLOT,
            MovieColumn.RATING,
            MovieColumn.RELEASE_DATE
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                movieUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            movieId = data.getInt(data.getColumnIndexOrThrow(MovieColumn.MOVIE_ID));

            String title = data.getString(data.getColumnIndexOrThrow(MovieColumn.ORIGINAL_TITLE));
            String backdropUrl = data.getString(data.getColumnIndexOrThrow(MovieColumn.POSTER_PATH));
            String plot = data.getString(data.getColumnIndexOrThrow(MovieColumn.PLOT));
            double rating = data.getDouble(data.getColumnIndexOrThrow(MovieColumn.RATING));
            String released = data.getString(data.getColumnIndexOrThrow(MovieColumn.RELEASE_DATE));

            backdropUrl = WebConfig.getPosterUrl(backdropUrl).toString();

            Glide.with(this).load(backdropUrl)
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.cold_drink))
                    .error(ContextCompat.getDrawable(getContext(), R.drawable.hot_drink))
                    .crossFade()
                    .into(ivDetailMoviePoster);
            tvDetailMovieTitle.setText(title);
            tvDetailMoviePlot.setText(plot);
            tvDetailMovieRating.setText(String.format("%.2f/10", rating));
            tvDetailMovieReleaseDate.setText(dateFormatter(released));

            loadFinishListener.onLoaded(movieId);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @OnClick(R.id.bt_detail_movie_favorite)
    public void onClick() {
        if (movieId != 0){
            String mSelectionClause = MovieColumn.MOVIE_ID +  " = "+ movieId;

            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(MovieColumn.USER_FAVORITE, WebConfig.PATH_FAVORITE);

            int mRowsUpdated = getContext().getContentResolver().update(
                    MovieProvider.Movies.CONTENT_URI,
                    mUpdateValues,
                    mSelectionClause,
                    null
            );

            Log.d(LOG_TAG, "fav added:" + mRowsUpdated);
        }

    }

    public interface MovieLoadFinishListener {
        void onLoaded(int movieId);
    }
}
