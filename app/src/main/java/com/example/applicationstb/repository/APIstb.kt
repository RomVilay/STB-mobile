package com.example.applicationstb.repository

import com.example.applicationstb.model.Fiche
import com.example.applicationstb.model.User
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*


interface APIstb  {
    @POST("/users/login")
    fun loginUser(@Body body: BodyLogin): Call<LoginResponse>

    @GET("/fiches")
    fun getFiches( @Header("auth-token") token:String ): Call<FichesResponse>
}