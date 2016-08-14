package net.konyan.popularmoviesapp.fragment;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.adapter.ReviewAdapter;
import net.konyan.popularmoviesapp.data.MovieProvider;
import net.konyan.popularmoviesapp.data.ReviewColumn;
import net.konyan.popularmoviesapp.data_model.Review;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeta on 8/14/16.
 */
public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final String LOG_TAG = ReviewFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 3;
    int type;
    int movieID;

    ReviewAdapter reviewAdapter;

    public static ReviewFragment newInstance(Bundle args) {
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(Intent.EXTRA_SUBJECT);
            movieID = getArguments().getInt(Intent.EXTRA_ASSIST_UID);
            Log.d(LOG_TAG, "loaded movie "+movieID);
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_pager_recycler, container, false);

        RecyclerView recyclerReview = (RecyclerView) rootView.findViewById(R.id.rv_movie_extra);
        reviewAdapter = new ReviewAdapter(getActivity(), null);
        recyclerReview.setAdapter(reviewAdapter);
        recyclerReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        showReview(movieID);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //showReview();
    }

    private void showReview(final int movieId){
        Call<Review> call = WebConfig.getReviews(movieId);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Log.d(LOG_TAG, response.message());
                if (response.isSuccessful()){
                    insertReview(response.body().results, movieId);
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    private void insertReview(List<Review.ReviewModel> reviewList, int movieId){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(reviewList.size());

        for (Review.ReviewModel review : reviewList){

            String mSelectionClause = ReviewColumn.REVIEW_ID+  " ='"+ review.id+"'";

            ContentValues mUpdateValues = new ContentValues();

            mUpdateValues.put(ReviewColumn.REVIEW_ID, review.id);
            mUpdateValues.put(ReviewColumn.AUTHOR, review.author);
            mUpdateValues.put(ReviewColumn.CONTENT, review.content);
            mUpdateValues.put(ReviewColumn.URL, review.url);

            int mRowsUpdated = getContext().getContentResolver().update(
                    MovieProvider.Reviews.CONTENT_URI,
                    mUpdateValues,
                    mSelectionClause,
                    null
            );

            if (mRowsUpdated < 1){
                ContentProviderOperation.Builder builder =
                        ContentProviderOperation.newInsert(MovieProvider.Reviews.CONTENT_URI);
                builder.withValue(ReviewColumn.MOVIE_ID, movieId);
                builder.withValue(ReviewColumn.REVIEW_ID, review.id);
                builder.withValue(ReviewColumn.AUTHOR, review.author);
                builder.withValue(ReviewColumn.CONTENT, review.content);
                builder.withValue(ReviewColumn.URL, review.url);
                batchOperations.add(builder.build());
            }

        }

        try{
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
            getActivity().getContentResolver().notifyChange(MovieProvider.Reviews.CONTENT_URI, null);

        } catch(RemoteException | OperationApplicationException e){
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ReviewColumn._ID,
                ReviewColumn.MOVIE_ID,
                ReviewColumn.AUTHOR,
                ReviewColumn.CONTENT};
        String selection = ReviewColumn.MOVIE_ID + " LIKE ? ";
        String[] selArgs = {String.valueOf(movieID)};

        return new CursorLoader(getActivity(), MovieProvider.Reviews.CONTENT_URI,
                projection,
                selection,
                selArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        reviewAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        reviewAdapter.swapCursor(null);
    }
}
