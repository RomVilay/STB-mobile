package com.example.applicationstb.repository

import com.example.applicationstb.model.*
import com.squareup.moshi.Moshi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    @GET("/vehicles")
    fun getAllVehicules(@Header("auth-token") token:String): Call<VehiculesResponse>
    @GET("/vehicles/{id}")
    fun getVehiculeById(@Header("auth-token") token:String, @Path("id") id:String ): Call<VehiculesResponse>
    @GET("/clients")
    fun getAllClients(@Header("auth-token") token:String): Call<ClientsResponse>
    @GET("/clients/{id}")
    fun getClientsById(@Header("auth-token") token:String, @Path("id") id:String ): Call<ClientsResponse>
    @PUT("/images/{name}")
    fun uploadPhoto( @Header("auth-token") token:String, @Path("name", encoded = true) name:String,@Query("X-Amz-Algorithm") algo:String ,@Query(value = "X-Amz-Credential", encoded = true ) cred:String ,@Query("X-Amz-Date") date:String ,@Query("X-Amz-Expires") Expires:String ,@Query("X-Amz-SignedHeaders") SignedHeaders:String ,@Query("X-Amz-Signature") Signature:String,@Body body: RequestBody ): Call<URLPhotoResponse>
    @GET("/images/put")
    suspend fun getURLToUploadPhoto( @Header("auth-token") token:String) : Response<URLPhotoResponse2>
    @GET("/images/get/{photoName}")
    suspend fun getURLPhoto(@Header("auth-token") token:String, @Path("photoName")photoName: String ): Response <URLPhotoResponse>
    @Streaming
    @GET
    suspend fun getPhoto(@Url address: String): Response<ResponseBody>

    @GET("/fiches/{ficheId}")
    fun getDemontageMotopompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageMotopompeResponse>
    @PATCH("/fiches/{ficheId}")
    fun patchDemontageMotopompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemoMotoPompe ): Call<DemontageMotopompeResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontageMotopompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageMotopompeResponse>
    @PATCH("/fiches/{ficheId}")
    fun patchRemontageMotopompe(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontageMotopompe ): Call<RemontageMotopompeResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageMotoreducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageMotoreducteurResponse>
    @PATCH("/fiches/{ficheId}")
    fun patchDemontageMotoreducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemoMotoReducteur ): Call<DemontageMotoreducteurResponse>

    @GET("/fiches/{ficheId}")
    fun getRemontageMotoreducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageMotoreducteurResponse>
    @PATCH("/fiches/{ficheId}")
    fun patchRemontageMotoreducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontageMotoreducteur ): Call<RemontageMotoreducteurResponse>

    @GET("/fiches/{ficheId}")
    fun getDemontageReducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<DemontageReducteurResponse>
    @PATCH("/fiches/{ficheId}")
    fun patchDemontageReducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyDemontageReducteur ): Call<DemontageReducteurResponse>
    @GET("/fiches/{ficheId}")
    fun getRemontageReducteur(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Call<RemontageReducteurResponse>


}