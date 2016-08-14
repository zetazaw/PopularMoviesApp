package net.konyan.popularmoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.MovieColumn;
import net.konyan.popularmoviesapp.data.MovieProvider;
import net.konyan.popularmoviesapp.utils.WebConfig;

/**
 * Created by zeta on 6/12/16.
 *
 * Glide api reference from
 * https://github.com/bumptech/glide
 * https://futurestud.io/blog/
 * https://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en
 * -----------------------------------------------
 * The original icon images from
 * Icon vector designed by Freepik
 * http://www.freepik.com/free-photos-vectors/icon
 */

public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.MovieHolder> {

    final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    final Context context;

    final ItemClickListener listener;

    public MoviesAdapter(Context context, Cursor cursor, ItemClickListener listener) {
        super(context, cursor);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(MovieHolder viewHolder, final Cursor cursor) {

        if (cursor != null){

            final int _id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieColumn._ID));
            final int mov_id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieColumn.MOVIE_ID));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(MovieColumn.POSTER_PATH));
            Glide.with(context)
                    .load(WebConfig.getPosterUrl(poster).toString())
                    .override(185, 278)
                    .fitCenter()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.cold_drink))
                    .error(ContextCompat.getDrawable(context, R.drawable.hot_drink))
                    .crossFade()
                    .into(viewHolder.ivMoviePoster);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(MovieProvider.Movies.withId(_id));
                }
            });
        }
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    class MovieHolder extends RecyclerView.ViewHolder{
        ImageView ivMoviePoster;

        public MovieHolder(View itemView) {
            super(itemView);
            ivMoviePoster = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
        }
    }

    public interface ItemClickListener{
        void onClick(Uri uri);
    }
}
