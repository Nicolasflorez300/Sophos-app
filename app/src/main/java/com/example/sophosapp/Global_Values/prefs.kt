package com.example.sophosapp

import android.content.Context

class Prefs( context: Context) {
    val SHARED_NAME = "mydtb"
    val SHARED_USER_NAME = "username"
    val SHARED_USER_EMAIL = "email"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)
    val storage2 = context.getSharedPreferences(SHARED_USER_EMAIL, 0)

    fun saveName(name: String){
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun getName():String{
        return storage.getString(SHARED_USER_NAME, "")!!
    }

    fun saveEmail(email: String){
        storage2.edit().putString(SHARED_USER_EMAIL, email).apply()
    }

    fun getEmail():String{
        return storage2.getString(SHARED_USER_EMAIL, "")!!
    }



}