package net.konyan.popularmoviesapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.konyan.popularmoviesapp.fragment.ReviewFragment;
import net.konyan.popularmoviesapp.fragment.VideoFragment;

/**
 * Created by zeta on 8/14/16.
 */
public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_REVIEW = 1;
    final int movieId;

    public DetailPagerAdapter(FragmentManager fm, int movieId) {
        super(fm);
        this.movieId = movieId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        switch (position){
            case 0:{
                args.putInt(Intent.EXTRA_SUBJECT, TYPE_VIDEO);
                args.putInt(Intent.EXTRA_ASSIST_UID, movieId);
                return VideoFragment.newInstance(args);
            }
            case 1:{
                args.putInt(Intent.EXTRA_SUBJECT, TYPE_REVIEW);
                args.putInt(Intent.EXTRA_ASSIST_UID, movieId);
                return ReviewFragment.newInstance(args);
            }
            default:return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Trailers";
            case 1: return "Reviews";
            default:return  null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
