package com.example.android.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.android.spotifystreamer.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
