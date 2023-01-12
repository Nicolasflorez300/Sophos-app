package com.example.sophos.userslogin

import com.example.sophosapp.See_Documents_Api.Values_Image
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Image_Interface {
    @GET(Constants.DOCS)
    fun DocumentGet(@Query("idRegistro") idRegistro: String): Call<Values_Image>


}