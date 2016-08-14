package net.konyan.popularmoviesapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by zeta on 8/14/16.
 */
public class PopularMoviesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.newInitializerBuilder(this);
    }
}
