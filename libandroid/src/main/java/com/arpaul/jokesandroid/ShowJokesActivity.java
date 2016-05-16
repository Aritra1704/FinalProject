package com.arpaul.jokesandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Aritra on 5/16/2016.
 */
public class ShowJokesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.library_jokes);

        TextView tvLibJokes = (TextView) findViewById(R.id.tvLibJokes);

        if(getIntent().hasExtra("jokes")){
            tvLibJokes.setText(getIntent().getStringExtra("jokes"));
        }
    }
}
