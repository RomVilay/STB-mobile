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
    @POST("users/login")
    fun loginUser(@Body body: BodyLogin): Call<LoginResponse>
    @GET("fiches")
    fun getFiches( @Header("auth-token") token:String ): Call<FichesResponse>
    @GET("fiches?type=1&type=2&type=3&type=4&status=1&status=2")
    fun getFichesUser( @Header("auth-token") token:String, @Query("filter") userid:String ): Call<FichesResponse>
    @GET ("fiches?type=2&status=2&status=3&status=4")
    suspend fun getFichesDemontages( @Header("auth-token") token:String, @Query("filter") numdevis:String ): Response<ListFicheDemontageResponse>
    @GET("fiches?type=1")
    fun getAllChantier(@Header("auth-token") token:String, @Path("filter") username:String ): Call<ChantierResponse>
    @GET("fiches/{ficheId}")
    suspend fun getChantier(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Response <ChantierResponse>
    @PATCH("fiches/{ficheId}")
    fun patchChantier(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyChantier ): Call<ChantierResponse>
    @GET("fiches/{ficheId}")
    suspend fun getBobinage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Response<BobinageResponse>
    @PATCH("fiches/{ficheId}")
    fun patchBobinage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyBobinage ): Call<BobinageResponse>
    @GET("fiches/{ficheId}")
    suspend fun getFicheDemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Response<FicheDemontageResponse>
    @PATCH("fiches/{ficheId}")
    fun patchFicheDemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyFicheDemontage ): Call<FicheDemontageResponse>
    @PATCH("fiches/{ficheId}")
    fun patchRemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String, @Body fiche: BodyRemontage ): Call<RemontageResponse>
    @GET("fiches/{ficheId}")
    suspend fun getRemontage(@Header("auth-token") token:String, @Path("ficheId") ficheId:String ): Response<RemontageResponse>
    @GET("vehicles")
    fun getAllVehicules(@Header("auth-token") token:String): Call<VehiculesResponse>
    @GET("vehicles/{id}")
    fun getVehiculeById(@Header("auth-token") token:String, @Path("id") id:String ): Call<VehiculesResponse>
    @GET("clients")
    fun getAllClients(@Header("auth-token") token:String): Call<ClientsResponse>
    @GET("clients/{id}")
    fun getClientsById(@Header("auth-token") token:String, @Path("id") id:String ): Call<ClientsResponse>
    @PUT("images/{name}")
    suspend fun uploadPhoto( @Header("auth-token") token:String, @Path("name", encoded = true) name:String,@Query("X-Amz-Algorithm") algo:String ,@Query(value = "X-Amz-Credential", encoded = true ) cred:String ,@Query("X-Amz-Date") date:String ,@Query("X-Amz-Expires") Expires:String ,@Query("X-Amz-SignedHeaders") SignedHeaders:String ,@Query("X-Amz-Signature") Signature:String,@Body body: RequestBody ): Response<URLPhotoResponse>
    @PUT
    suspend fun uploadPhoto2( @Url url:String , @Header("auth-token") token:String, @Body body: RequestBody ): Response<Unit>
    @GET("images/put/")
    suspend fun getURLToUploadPhoto( @Header("auth-token") token:String,@Query("name") name:String) : Response<URLPhotoResponse2>
    @GET("images/get/{photoName}")
    suspend fun getURLPhoto(@Header("auth-token") token:String, @Path("photoName")photoName: String ): Response <URLPhotoResponse>
    @Streaming
    @GET
    suspend fun getPhoto(@Url url: String): Response<ResponseBody>
    @POST("pointages")
    suspend fun postPointages(@Header("auth-token") token:String, @Body body: BodyPointage) : Response<PointageResponse>

    @DELETE("pointages/{ficheId}")
    suspend fun deletePointage(@Header("auth-token") token:String, @Path("ficheId") pointage:String ) : Response<PointageResponse>

    @GET("pointages")
    suspend fun getPointages2(@Header("auth-token")token:String, @Query("limit") limit: String, @Query("offset") offset: Int, @Query("user") user: String, @Query("minDate") minDate : String, @Query("maxDate") maxDate : String ) : Response<PointagesResponse>

    @GET("pointages")
    fun getPointages(@Header("auth-token")token:String, @Query("limit") limit: String, @Query("offset") offset: Int, @Query("user") user: String, @Query("minDate") minDate : String, @Query("maxDate") maxDate : String ) : Call <PointagesResponse>
}