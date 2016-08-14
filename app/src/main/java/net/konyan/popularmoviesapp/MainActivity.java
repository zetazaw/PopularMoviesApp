package net.konyan.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.konyan.popularmoviesapp.fragment.DetailFragment;
import net.konyan.popularmoviesapp.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.MovieClickCallback {
    final String MAIN_TAG = "main";
    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_main, MainFragment.newInstance(), MainFragment.class.getSimpleName())
                    .commit();

            if (findViewById(R.id.container_detail) != null) {
                twoPane = true;
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_detail, DetailFragment.newInstance(null),
                                DetailFragment.class.getSimpleName()).commit();
            } else {
                twoPane = false;
            }
        }
    }

    @Override
    public void onClick(Uri uri) {
        if (twoPane){
            Bundle bundle = new Bundle();
            bundle.putParcelable(Intent.ACTION_ATTACH_DATA, uri);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, DetailFragment.newInstance(bundle),
                            DetailFragment.class.getSimpleName()).commit();
        }
    }
}
