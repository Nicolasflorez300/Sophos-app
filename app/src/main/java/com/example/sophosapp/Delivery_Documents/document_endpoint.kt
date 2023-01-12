package com.example.sophos.userslogin

import com.example.sophosapp.Delivery_Documents.Post_doc
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface document_endpoint {
    @POST(Constants.DOCS)
    fun DocPost(@Body postDoc: Post_doc): Call<Post_doc>
}