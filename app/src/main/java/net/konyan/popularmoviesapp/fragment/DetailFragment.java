package net.konyan.popularmoviesapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.konyan.popularmoviesapp.R;
import net.konyan.popularmoviesapp.data.ParcelMoviesResult;
import net.konyan.popularmoviesapp.utils.WebConfig;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailFragment extends Fragment {

    private static final String KEY_MOVIE_DATA = "key_movie_data";

    private static ParcelMoviesResult movieData;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(ParcelMoviesResult movieData) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MOVIE_DATA, movieData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieData = getArguments().getParcelable(KEY_MOVIE_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        String backdropUrl = null;
        if (movieData != null){
            try {
                backdropUrl = WebConfig.getPosterUrl(movieData.getPoster_path()).toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        ImageView ivPoster = (ImageView) view.findViewById(R.id.iv_detail_movie_poster);
        Glide.with(this).load(backdropUrl)
                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.cold_drink))
                .error(ContextCompat.getDrawable(getContext(), R.drawable.hot_drink))
                .crossFade()
                .into(ivPoster);


        TextView tvTitle, tvPlot, tvRating, tvRelease;
        tvTitle = (TextView)view.findViewById(R.id.tv_detail_movie_title);
        tvPlot = (TextView)view.findViewById(R.id.tv_detail_movie_plot);
        tvRating = (TextView)view.findViewById(R.id.tv_detail_movie_rating);
        tvRelease = (TextView)view.findViewById(R.id.tv_detail_movie_release_date);

        tvTitle.setText(movieData.getOriginal_title());
        tvPlot.setText(movieData.getOverview());
        tvRating.setText(String.format("%.2f/10", movieData.getVote_average()));
        tvRelease.setText(dateFormatter(movieData.getRelease_date()));


        return view;
    }

    private String dateFormatter(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("yyyy").format(date);

    }
}
