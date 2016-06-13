package net.konyan.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.ParcelMoviesResult;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.net.MalformedURLException;
import java.util.List;

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

public class MoviesAdapter extends ArrayAdapter<ParcelMoviesResult> {
    final String LOG_TAG = MoviesAdapter.class.getSimpleName();


    public MoviesAdapter(Context context, List<ParcelMoviesResult> moviesResults) {
        super(context, R.layout.item_movie, moviesResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParcelMoviesResult moviesResult = getItem(position);
        String posterUrl = null;

        try {
             posterUrl = WebConfig.getPosterUrl(moviesResult.getPoster_path()).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }

        ImageView ivMoviePoster = (ImageView)convertView.findViewById(R.id.iv_movie_poster);
        Glide.with(getContext())
                .load(posterUrl)
                .override(185, 278)
                .fitCenter()
                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.cold_drink))
                .error(ContextCompat.getDrawable(getContext(), R.drawable.hot_drink))
                .crossFade()
                .into(ivMoviePoster);

        return convertView;
    }
}
