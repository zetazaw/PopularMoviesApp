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
import net.konyan.popularmoviesapp.adapter.VideoAdapter;
import net.konyan.popularmoviesapp.data.MovieProvider;
import net.konyan.popularmoviesapp.data.VideoColumn;
import net.konyan.popularmoviesapp.data_model.Video;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeta on 8/14/16.
 */
public class VideoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final String LOG_TAG = VideoFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 2;
    int type;
    int movieID;

    VideoAdapter videoAdapter;

    public static VideoFragment newInstance(Bundle args) {
        VideoFragment fragment = new VideoFragment();
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

        RecyclerView recyclerMovies = (RecyclerView) rootView.findViewById(R.id.rv_movie_extra);
        videoAdapter = new VideoAdapter(getActivity(), null, new VideoAdapter.TrailerClickListener() {
            @Override
            public void onItemClick(String videoKey) {
                startActivity(new Intent(Intent.ACTION_VIEW, WebConfig.getVideoUrl(videoKey)));

            }
        });
        recyclerMovies.setAdapter(videoAdapter);
        recyclerMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        showTrailer(movieID);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //showTrailer();
    }

    private void showTrailer(final int movieId){
        Call<Video> call = WebConfig.getTrailers(movieId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                Log.d(LOG_TAG, response.message());
                if (response.isSuccessful()){
                    insertVideo(response.body().results, movieId);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    private void insertVideo(List<Video.VideoModel> videoList, int movieId){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(videoList.size());

        for (Video.VideoModel video : videoList){

            String mSelectionClause = VideoColumn.VIDEO_ID+  " ='"+ video.id+"'";

            ContentValues mUpdateValues = new ContentValues();
            mUpdateValues.put(VideoColumn.VIDEO_ID, video.id);
            mUpdateValues.put(VideoColumn.VIDEO_NAME, video.name);
            mUpdateValues.put(VideoColumn.VIDEO_KEY, video.key);
            mUpdateValues.put(VideoColumn.VIDEO_SITE, video.site);
            mUpdateValues.put(VideoColumn.VIDEO_QTY, video.size);
            mUpdateValues.put(VideoColumn.VIDEO_TYPE, video.type);

            int mRowsUpdated = getContext().getContentResolver().update(
                    MovieProvider.Videos.CONTENT_URI,
                    mUpdateValues,
                    mSelectionClause,
                    null
            );

            if (mRowsUpdated < 1){
                ContentProviderOperation.Builder builder =
                        ContentProviderOperation.newInsert(MovieProvider.Videos.CONTENT_URI);
                builder.withValue(VideoColumn.MOVIE_ID, movieId);
                builder.withValue(VideoColumn.VIDEO_ID, video.id);
                builder.withValue(VideoColumn.VIDEO_KEY, video.key);
                builder.withValue(VideoColumn.VIDEO_NAME, video.name);
                builder.withValue(VideoColumn.VIDEO_SITE, video.site);
                builder.withValue(VideoColumn.VIDEO_QTY, video.size);
                builder.withValue(VideoColumn.VIDEO_TYPE, video.type);
                batchOperations.add(builder.build());
            }

        }

        try{
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
            getActivity().getContentResolver().notifyChange(MovieProvider.Videos.CONTENT_URI, null);

        } catch(RemoteException | OperationApplicationException e){
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {VideoColumn._ID,
                VideoColumn.MOVIE_ID,
                VideoColumn.VIDEO_KEY,
                VideoColumn.VIDEO_NAME,
                VideoColumn.VIDEO_ID};
        String selection = VideoColumn.MOVIE_ID + " LIKE ? ";
        String[] selArgs = {String.valueOf(movieID)};

        return new CursorLoader(getActivity(), MovieProvider.Videos.CONTENT_URI,
                projection,
                selection,
                selArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        videoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        videoAdapter.swapCursor(null);
    }
}
