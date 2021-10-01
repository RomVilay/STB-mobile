package com.example.applicationstb.repository

import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Fiche
import com.example.applicationstb.model.User
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIstb  {
    @POST("/users/login")
    fun loginUser(@Body body: BodyLogin): Call<LoginResponse>

    @GET("/fiches")
    fun getFiches( @Header("auth-token") token:String ): Call<FichesResponse>

    @GET("/fiches/user/{userid}")
    fun getFichesUser( @Header("auth-token") token:String, @Path("userid") userid:String ): Call<FichesResponse>

    @GET("/vehicules/{vehiculesId}")
    fun getVehiculeById(@Header("auth-token") token:String, @Path("vehiculesId") userid:String): Call<VehiculesResponse>

    @GET("/fiches/{ficheId}")
    fun getChantier(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<ChantierResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchChantier(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyChantier ): Call<ChantierResponse>

    @GET("/fiches/{ficheId}")
    fun getBobinage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<BobinageResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchBobinage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyBobinage ): Call<BobinageResponse>

}