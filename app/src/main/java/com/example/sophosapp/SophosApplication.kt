package com.example.sophosapp

import android.app.Application

class SophosApplication: Application() {

    companion object{
        lateinit var prefs:Prefs

    }


    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}