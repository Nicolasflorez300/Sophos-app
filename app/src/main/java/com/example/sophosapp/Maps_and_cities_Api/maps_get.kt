package com.example.sophos.userslogin

import com.example.sophosapp.Maps_and_cities_Api.maps_Items
import retrofit2.Call
import retrofit2.http.GET

interface maps_get {
    @GET(Constants.MAPS)
    fun listcity(): Call<maps_Items>
}