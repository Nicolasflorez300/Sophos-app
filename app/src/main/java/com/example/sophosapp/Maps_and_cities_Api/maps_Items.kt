package com.example.sophosapp.Maps_and_cities_Api

import com.google.gson.annotations.SerializedName

data class maps_Items(
    @SerializedName("Count") var Count:Int,
    @SerializedName("ScannedCount") var ScannedCount: Int,
    @SerializedName("Items") var Items:List<maps_data>
    )