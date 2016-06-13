package net.konyan.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.konyan.popularmoviesapp.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container_main) != null){

            if (savedInstanceState != null){
                return;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_main, MainFragment.newInstance())
                    .commit();
        }

    }
}
