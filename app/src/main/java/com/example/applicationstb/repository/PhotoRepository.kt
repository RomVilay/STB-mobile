package com.example.applicationstb.repository

import android.app.Application
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.AndroidRuntimeException
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import id.zelory.compressor.decodeSampledBitmapFromFile
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
import java.lang.Long.min
import java.nio.file.Files
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.typeOf
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
    // definition du repository photo
    fun servicePhoto(): APIstb {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
            .build()
            .create(APIstb::class.java)
    }
    // url des photos pour l'upload
    suspend fun getURLPhoto(token: String, photoName: String) = servicePhoto().getURLPhoto(token, photoName)
    // url d'affichage des photos
    suspend fun getPhoto(address: String) = servicePhoto().getPhoto(address)
    suspend fun getURL(token: String,name:String) = servicePhoto().getURLToUploadPhoto(token,name)
    // upload des photos
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun sendPhoto(token:String, photo:String, context: Context) {
       CoroutineScope(Dispatchers.IO).launch {
                val url = async {getURL(token,photo)}
                url.await()
                try {
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/test_pictures" ), photo)
                    if (file.exists() && file.readBytes().isNotEmpty()){
                        // compression pour une taille de fichier inférieure à 1mb = 1048576
                        val targetQuality = (1048576.toFloat() / file.length() * 100)+5 // maximum de compression à 95%
                        val minQuality = min(targetQuality.toLong(), 80L)
                        var compress = async { Compressor.compress(context, file) {
                           // resolution(1280,720)
                            //quality( 80)
                            //size(1048576)
                            quality(minQuality.toInt())
                            format(Bitmap.CompressFormat.JPEG)
                        } }
                        compress.await()
                        if (compress.isCompleted) {
                            var body = RequestBody.create(MediaType.parse("image/jpeg"), compress.getCompleted())
                            var upload = async {
                                servicePhoto().uploadPhoto2(url.await().body()!!.url!!, token , body)
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
                    }
                } catch (e:Exception){
                    Log.e("error",e.message!!)
                }
                catch (e:AndroidRuntimeException){
                    Log.e("error",e.message!!)
                }
        }
    }
    // upload des signatures
    suspend fun sendSignature(token:String, photo:String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = getURL(token,photo)
            Log.i("info", "url  ${url.body()!!.url}")
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/test_signatures" ), photo)
            try {
                if (file.exists() && file.readBytes().isNotEmpty()){
                var compress = async { Compressor.compress(context, file) }
                compress.join()
                if (compress.isCompleted) {
                    var body =
                        RequestBody.create(MediaType.parse("image/jpeg"), compress.getCompleted())
                    var upload = async {
                        servicePhoto().uploadPhoto2(url.body()!!.url!!, token, body)
                    }
                    withContext(Dispatchers.Main) {
                        return@withContext ServerResponse(
                            upload.await().code(),
                            upload.await().message()
                        )
                    }
                    Log.i("info", "photo ${photo} uploaded ")
                } else {
                    withContext(Dispatchers.Main) {
                        return@withContext ServerResponse(400, "Transfert échoué.")
                    }
                }
            }
            } catch (e:Exception){
                Log.e("error",e.message!!)
            }
        }
    }

}