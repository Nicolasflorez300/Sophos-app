package com.example.sophos.userslogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object See_service {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())  //json a yison
        .build()

    val service_see = retrofit.create(Endpoints_Interface::class.java) //hace referencia a la peticion que voy a usar
}