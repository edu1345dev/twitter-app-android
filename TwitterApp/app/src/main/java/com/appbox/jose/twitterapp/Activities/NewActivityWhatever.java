package com.appbox.jose.twitterapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Jose Santos on 20/09/2017.
 */

public class NewActivityWhatever extends AppCompatActivity {

    private static final String TAG = NewActivityWhatever.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: teste");

    }
}