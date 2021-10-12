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

    @GET("/fiches/{ficheId}")
    fun getDemontageTriphase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageTriphaseResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontageTriphase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageTriphase ): Call<DemontageTriphaseResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageCC(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageCCResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontageCC(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageCC ): Call<DemontageCCResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontageTriphase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageTriphaseResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchRemontageTriphase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontageTriphase ): Call<RemontageTriphaseResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontageCC(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageCCResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchRemontageCC(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontageCC ): Call<RemontageCCResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageResponse>

    @GET("/fiches/{ficheId}")
    fun getDemoPompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontagePompeResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontagePompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemoPompe ): Call<DemontagePompeResponse>


}