package com.companyname.simplepad

import android.app.Application

import com.companyname.simplepad.data.DataStore

class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
    }
}
