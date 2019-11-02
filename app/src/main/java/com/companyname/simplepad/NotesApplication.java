package com.companyname.simplepad;

import android.app.Application;

import com.companyname.simplepad.data.DataStore;

public class NotesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataStore.INSTANCE.init(this);
    }
}
