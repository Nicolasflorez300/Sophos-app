package com.example.sophos.userslogin

import com.google.gson.annotations.SerializedName

data class db_result (
    @SerializedName("acceso") val access: Boolean,
    val admin: Boolean,
    @SerializedName("apellido") val lastName: String,
    val id: String,
    @SerializedName("nombre") val name: String

)