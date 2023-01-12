package com.example.sophosapp.Delivery_Documents


import com.google.gson.annotations.SerializedName

data class Post_doc(
    @SerializedName("TipoId") val TipoId: String,
    @SerializedName("Identificacion") val Identificacion: String,
    @SerializedName("Nombre") val Nombre: String,
    @SerializedName("Apellido") val Apellido: String,
    @SerializedName("Ciudad") val Ciudad: String,
    @SerializedName("Correo") val Correo: String,
    @SerializedName("TipoAdjunto") val TipoAdjunto: String,
    @SerializedName("Adjunto") val Adjunto: String,


)