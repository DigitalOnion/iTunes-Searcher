package com.example.itunessearcher

import android.app.Application
import android.content.Context

//This class allows accessing the App's context anywhere
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: Context? = null
            private set
    }
}