package com.example.applicationstb.repository

import android.content.Context
import com.squareup.moshi.Moshi
import okhttp3.ConnectionSpec
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class URLPhotoResponse(
    var url: String?
)

class URLPhotoResponse2(
    var url: String?,
    var name: String?
)

class PhotoRepository(context: Context) {
    private val moshiBuilder = Moshi.Builder().add(CustomDateAdapter()).add(CustomDateAdapter2())
    var okHttpClient = OkHttpClient.Builder()
        .callTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        .build()
    fun servicePhoto(): APIstb {
        return Retrofit.Builder()
            .baseUrl(minioUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
            .build()
            .create(APIstb::class.java)
    }

    fun uploadPhoto(
        token: String,
        name: String,
        address: List<String>,
        photo: File,
        param: Callback<URLPhotoResponse>
    ) {
        var body = RequestBody.create(MediaType.parse("image/jpeg"), photo)
        var call = servicePhoto().uploadPhoto(
            token,
            name,
            address[0],
            address[1].removePrefix("X-Amz-Credential="),
            address[2].removePrefix("X-Amz-Date="),
            address[3].removePrefix("X-Amz-Expires="),
            address[4].removePrefix("X-Amz-SignedHeaders="),
            address[5].removePrefix("X-Amz-Signature="),
            body
        )
        var photo: String? = null
        call.enqueue(param)
    }
}