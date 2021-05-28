package com.example.applicationstb.repository

import com.example.applicationstb.model.User
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface APIstb  {
    @POST("/users/login")
    fun loginUser(@Body body: BodyLogin): Call<LoginResponse>
}