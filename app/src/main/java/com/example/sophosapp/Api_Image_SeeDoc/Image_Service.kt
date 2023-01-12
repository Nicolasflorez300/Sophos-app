package com.example.sophos.userslogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Image_Service {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())  //json a yison
        .build()

    val service_Image = retrofit.create(Image_Interface::class.java) //hace referencia a la peticion que voy a usar
}