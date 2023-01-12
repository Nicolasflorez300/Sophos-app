package com.example.sophos.userslogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object db_maps {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())  //json a yison
        .build()

    val service_map = retrofit.create(maps_get::class.java) //hace referencia a la peticion que voy a usar
}