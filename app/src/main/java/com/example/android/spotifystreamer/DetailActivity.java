package com.example.android.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show selected artist in ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        String artist = getIntent().getExtras().getString("Artist");
        actionBar.setSubtitle(artist);
    }
}
