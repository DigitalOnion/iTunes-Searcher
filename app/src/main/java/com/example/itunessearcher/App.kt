package com.example.itunessearcher

import android.app.Application
import android.content.Context

//This class allows accessing the App's context anywhere
class App : Application() {
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}