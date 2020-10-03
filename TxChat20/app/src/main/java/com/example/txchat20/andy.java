package com.example.txchat20;

import android.app.Application;

import com.parse.Parse;

public class andy extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("86XnwBxx6O5BdKa5a9rjT2fJumFvuy5qcofEYT10")
                .clientKey("fs4oR9aObSRapwNVg1WN3lN2Fns0s3ZKHqQyEKXf")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
