package com.example.sophos.userslogin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object doc_service {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL)
        .addConverterFactory(GsonConverterFactory.create())  //json a yison
        .build()

    val service_doc = retrofit.create(document_endpoint::class.java) //hace referencia a la peticion que voy a usar
}