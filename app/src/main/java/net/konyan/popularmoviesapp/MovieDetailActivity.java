package net.konyan.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.konyan.popularmoviesapp.data.ParcelMoviesResult;
import net.konyan.popularmoviesapp.fragment.DetailFragment;

public class MovieDetailActivity extends AppCompatActivity {
    final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    ParcelMoviesResult movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieData = getIntent().getExtras().getParcelable(Intent.EXTRA_SUBJECT);

        if (findViewById(R.id.container_detail) != null){

            if (savedInstanceState != null){
                return;
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, DetailFragment.newInstance(movieData),
                            DetailFragment.class.getSimpleName()).commit();

        }



    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
