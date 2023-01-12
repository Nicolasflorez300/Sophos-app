package com.example.sophos.userslogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object db_service {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())  //json a yison
        .build()

    val service = retrofit.create(Get_users::class.java) //hace referencia a la peticion que voy a usar
}