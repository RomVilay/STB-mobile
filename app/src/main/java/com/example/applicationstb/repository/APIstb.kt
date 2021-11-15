package com.example.applicationstb.repository

import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Fiche
import com.example.applicationstb.model.User
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface APIstb  {
    @POST("/users/login")
    fun loginUser(@Body body: BodyLogin): Call<LoginResponse>

    @GET("/fiches")
    fun getFiches( @Header("auth-token") token:String ): Call<FichesResponse>

    @GET("/fiches?type=1&type=2&type=3&type=4&status=1&status=2")
    fun getFichesUser( @Header("auth-token") token:String, @Query("filter") userid:String ): Call<FichesResponse>

    @GET("/fiches?type=2&status=3")
    fun getFichesForRemontage( @Header("auth-token") token: String, @Query("filter") numDevis: String): Call<DemontagesResponse>

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


    @PATCH("/fiches/{ficheId}")
    fun patchRemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontage ): Call<RemontageResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageResponse>

    @GET("/fiches/{ficheId}")
    fun getDemoPompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontagePompeResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontagePompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemoPompe ): Call<DemontagePompeResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontageAlternateur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageAlternateur ): Call<DemontageAlternateurResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageAlternateur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageAlternateurResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontageMonophase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageMonophase ): Call<DemontageMonophaseResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageMonophase(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageMonophaseResponse>

    @PATCH("/fiches/{ficheId}")
    fun patchDemontageRotorBobine(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageRotorBobine ): Call<DemontageRotorBobineResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageRotorBobine(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageRotorBobineResponse>
    @GET("/vehicules")
    fun getAllVehicules(@Header("auth-token") token:String): Call<VehiculesResponse>
    @GET("/vehicules/{id}")
    fun getVehiculeById(@Header("auth-token") token:String, @Path("id") id:String ): Call<VehiculesResponse>
    @GET("/clients")
    fun getAllClients(@Header("auth-token") token:String): Call<ClientsResponse>
    @GET("/clients/{id}")
    fun getClientsById(@Header("auth-token") token:String, @Path("id") id:String ): Call<ClientsResponse>
    @PUT("{address}")
    fun uploadPhoto( @Header("auth-token") token:String, @Path("address") address:String, @Body body: RequestBody )
    @GET("/images/put")
    fun getURLToUploadPhoto( @Header("auth-token") token:String) : Call<URLPhotoResponse2>
    @GET("/images/get/{photoName}")
    fun getURLPhoto(@Header("auth-token") token:String, photoName: String): Call<URLPhotoResponse>
    @GET("{address}")
    fun getPhoto(@Header("auth-token") token:String, @Path("address") address: String): Call<PhotoResponse>
}