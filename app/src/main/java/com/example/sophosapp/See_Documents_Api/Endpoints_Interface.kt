package com.example.sophos.userslogin

import com.example.sophosapp.Delivery_Documents.Post_doc
import com.example.sophosapp.See_Documents_Api.Item
import com.example.sophosapp.See_Documents_Api.Values
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface Endpoints_Interface {
    @GET(Constants.DOCS)
    fun DocumentGet(@Query("correo") correo: String): Call<Values>


}