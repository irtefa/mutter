package com.mutter.mirtefa.mutter;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by mirtefa on 4/1/15.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "VqvOisyMYbbJKrHHcRq1f9dOSAZgrytPOrrTWDqG", "FowxasIYgCGXjMu34jNj58Rg7jLlmIbJ5UCVyi4q");
    }
}

