package net.konyan.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.konyan.popularmoviesapp.adapter.DetailPagerAdapter;
import net.konyan.popularmoviesapp.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity implements DetailFragment.MovieLoadFinishListener{

    public final String LOG_TAG = DetailActivity.class.getSimpleName();

    DetailPagerAdapter mPagerAdapter;
    ViewPager mPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.container_detail) != null){

            if (savedInstanceState != null){
                return;
            }

            if (getIntent().getData() == null){
                return;
            }
            Bundle args = new Bundle();
            args.putParcelable(Intent.EXTRA_STREAM, getIntent().getData());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, DetailFragment.newInstance(args),
                            DetailFragment.class.getSimpleName()).commit();

            //view pager and fragment for trailer videos and reviews
            mPager = (ViewPager) findViewById(R.id.movie_detail_pager);
            tabLayout = (TabLayout) findViewById(R.id.tab_extra);

        }
    }

    @Override
    public void onLoaded(int movieId) {
        mPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), movieId);
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mPager);
    }
}
