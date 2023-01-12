package com.example.sophos.userslogin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Get_users {
    @GET(Constants.USERS)
    fun listusers(@Query("idUsuario") idUsuario: String, @Query("clave") clave: String): Call<db_result>
}