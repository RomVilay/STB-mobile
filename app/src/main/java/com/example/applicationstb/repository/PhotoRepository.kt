package com.example.applicationstb.repository

import android.app.Application
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import okhttp3.ConnectionSpec
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class URLPhotoResponse(
    var url: String?
)

class URLPhotoResponse2(
    var url: String?,
    var name: String?
)
class ServerResponse(
    var code:Int,
    var message: String
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
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
            .build()
            .create(APIstb::class.java)
    }
    suspend fun getURLPhoto(token: String, photoName: String) = servicePhoto().getURLPhoto(token, photoName)
    suspend fun getPhoto(address: String) = servicePhoto().getPhoto(address)
    suspend fun uploadPhoto(
        token: String,
        name: String,
        address: List<String>,
        photo: File
    ) : Response<URLPhotoResponse> {
        var body = RequestBody.create(MediaType.parse("image/jpeg"), photo)
        return servicePhoto().uploadPhoto(
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
    }
    suspend fun getURL(token: String,name:String) = servicePhoto().getURLToUploadPhoto(token,name)
    // list composé uniquement de photos présentes dans test_picture
    suspend fun sendPhotos(token:String, list:MutableList<String>, context: Context) : MutableList<String> {
        var photos = list.filterNot { it == "" }.toMutableList()
        var job = CoroutineScope(Dispatchers.IO).launch {
            var iter = photos.iterator()
            while ( iter.hasNext() ) {
                var photo = iter.next()
                val url = getURL(token,photo)
                Log.i("info", "url  ${url.body()!!.url}")
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/test_pictures" ), photo)
                try {
                    var compress = async { Compressor.compress(context, file)  }
                    compress.join()
                    if (compress.isCompleted) {
                        var body = RequestBody.create(MediaType.parse("image/jpeg"), compress.getCompleted())
                            var upload = async {
                                servicePhoto().uploadPhoto2(url.body()!!.url!!, token , body)
                                /*servicePhoto().uploadPhoto(
                                token,
                                photo,
                                tab[0],
                                tab[1].removePrefix("X-Amz-Credential="),
                                tab[2].removePrefix("X-Amz-Date="),
                                tab[3].removePrefix("X-Amz-Expires="),
                                tab[4].removePrefix("X-Amz-SignedHeaders="),
                                tab[5].removePrefix("X-Amz-Signature="),
                                body
                            )*/ }
                            upload.await()
                        Log.i("info","photo ${photo} uploaded ")
                    }
                } catch (e:Exception){
                    Log.e("error",e.message!!)
                }
            }
        }
        job.join()
        return photos
    }
    suspend fun sendPhoto(token:String, photo:String, context: Context) {
       CoroutineScope(Dispatchers.IO).launch {
                val url = getURL(token,photo)
                Log.i("info", "url  ${url.body()!!.url}")
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/test_pictures" ), photo)
                try {
                    var compress = async { Compressor.compress(context, file)  }
                    compress.join()
                    if (compress.isCompleted) {
                        var body = RequestBody.create(MediaType.parse("image/jpeg"), compress.getCompleted())
                        var upload = async {
                            servicePhoto().uploadPhoto2(url.body()!!.url!!, token , body)
                        }
                        withContext(Dispatchers.Main){
                            return@withContext ServerResponse(upload.await().code(),upload.await().message())
                        }
                        Log.i("info","photo ${photo} uploaded ")
                    } else {
                        withContext(Dispatchers.Main){
                            return@withContext ServerResponse(400,"Transfert échoué.")
                        }
                    }
                } catch (e:Exception){
                    Log.e("error",e.message!!)
                }
        }
    }

}