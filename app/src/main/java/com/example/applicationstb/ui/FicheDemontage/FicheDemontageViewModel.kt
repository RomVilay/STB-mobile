package com.example.applicationstb.ui.FicheDemontage

import android.app.Application
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.applicationstb.model.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.repository.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.graphics.Bitmap.CompressFormat
import android.os.SystemClock
import com.example.applicationstb.model.FicheDemontage
import id.zelory.compressor.Compressor
import java.io.*


class FicheDemontageViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context)
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    var listeDemontages = MutableLiveData<MutableList<FicheDemontage>>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    var selection = MutableLiveData<FicheDemontage>()
    var start = MutableLiveData<Date>()
    var image = MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()
    val sharedPref =
        getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
            getLocalFiches()
        }
    }

    suspend fun getLocalFiches(){
        listeDemontages.postValue(repository.demontageRepository!!.getAllDemontageLocalDatabase().map { it.toFicheDemontage() }.toMutableList())
    }

    fun back(view: View) {
        val action = FicheDemontageDirections.deDemontageversAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }

    fun afficherFiche(fiche: Fiche) {
        when (fiche) {
            is CourantContinu -> Log.i("INFO", "Type Courant Continu")
            is Triphase -> Log.i("Info", "Type triphasé")
            /*is RotorBobine -> Log.i("INFO","Type Rotor Bobine")
            is Monophase -> Log.i("Info","Type monophasé")
            is Alternateur -> Log.i("INFO","type alternateur")
            is DemontagePompe -> Log.i("Info", "type pompe")*/
        }
    }

    fun setCouplage(type: String) {
        var fichemot = selection.value
        fichemot!!.couplage = type
        selection.value = fichemot!!
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun addPhoto(photo: String) {
        var list = selection.value?.photos?.toMutableList()
        if (list != null) {
            list.add(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        }
        selection.value?.photos = list?.toTypedArray()
        photos.value = list!!
        galleryAddPic(photo)
        localSave()
    }


    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    fun setSchema(sch: String) {
        schema.value = sch
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheDemontageDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }
    fun retour(view: View) {
        Navigation.findNavController(view).popBackStack()
        /*var action = FicheDemontageDirections.deDemontageversAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)*/
    }
    fun getTime() {
        var now = Date()
        if (selection.value!!.dureeTotale !== null) {
            selection.value!!.dureeTotale =
                (Date().time - start.value!!.time) + selection.value!!.dureeTotale!!
        } else {
            selection.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }
    fun localSave() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.demontageRepository!!.updateDemontageLocalDatabse(selection.value!!.toEntity())
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun sendExternalPicture(path: String?): String? {
        if (isOnline(context)) {
            if (!sharedPref.getBoolean("connected",false) && (sharedPref?.getString("login", "") !== "" && sharedPref?.getString("password", "") !== "" )){
                connection(sharedPref?.getString("login", "")!!,sharedPref?.getString("password", "")!!)
            }
            delay(200)
            getNameURI()
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                val file = File(dir, imageName.value!!.name!!)
                File(path).copyTo(file)
                delay(100)
                sendPhoto(file)
                return imageName.value!!.name!!
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return null
            }
        } else {
            val dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
            val file = File(dir, selection.value?.numFiche + "_" + SystemClock.uptimeMillis()+".jpg")
            File(path).copyTo(file)
            return file.name
        }

    }
    fun connection(username: String, password: String) {
        val resp = repository.logUser(username, password, object : Callback<LoginResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    if (resp != null) {
                        token = resp.token
                        Log.i("info","new token ${resp.token}")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
     fun sendFiche(view: View) = runBlocking{
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (isOnline(context) == true) {
                if (!sharedPref.getBoolean("connected",false) && (sharedPref?.getString("login", "") !== "" && sharedPref?.getString("password", "") !== "" )){
                    connection(sharedPref?.getString("login", "")!!,sharedPref?.getString("password", "")!!)
                }
                delay(200)
                getNameURI()
                repository.demontageRepository!!.patchFicheDemontage(token!!, selection.value!!._id, selection.value!!, object : Callback<FicheDemontageResponse> {
                    override fun onResponse(
                        call: Call<FicheDemontageResponse>,
                        response: Response<FicheDemontageResponse>
                    ) {
                        if (response.code() == 200) {
                            val resp = response.body()
                            if (resp != null) {
                                val mySnackbar =
                                    Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré")

                            }
                        } else {
                            val mySnackbar =
                                Snackbar.make(view, "erreur d'enregistrement", 3600)
                            mySnackbar.show()
                            Log.i(
                                "INFO",
                                "code : ${response.code()} - erreur : ${response.message()} - body request ${
                                    response.errorBody()!!.charStream().readText()
                                }"
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<FicheDemontageResponse>,
                        t: Throwable
                    ) {
                        Log.e("Error", "${t.stackTraceToString()}")
                        Log.e("Error", "erreur ${t.message}")
                    }})
            } else {
                repository.demontageRepository!!.updateDemontageLocalDatabse(selection.value!!.toEntity())
            }

        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
    suspend fun getNameURI()  {
            val resp1 = repository.getURLToUploadPhoto(token!!)
            withContext(Dispatchers.Main) {
                if (resp1.isSuccessful) {
                    imageName.postValue(resp1.body())
                    Log.i("INFO", resp1.body()?.name!!)
                } else {
                    exceptionHandler
                }
            }
    }
    fun sendPhoto(photo:File)= runBlocking{
        var s = imageName.value!!.url!!.removePrefix("https://minio.stb.dev.alf-environnement.net/images/${imageName.value!!.name!!}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab[1] = tab[1].replace("%2F","/")
        viewModelScope.launch(Dispatchers.IO) {
            lateinit var  compressedPicture :File
            var job = launch { compressedPicture = Compressor.compress(context, photo) }
            job.join()
            compressedPicture.renameTo(photo)
            repositoryPhoto.uploadPhoto(
                token!!,
                imageName.value!!.name!!,
                tab.toList(),
                compressedPicture,
                object : Callback<URLPhotoResponse> {
                    override fun onResponse(
                        call: Call<URLPhotoResponse>,
                        response: Response<URLPhotoResponse>
                    ) {
                        Log.i("INFO", response.code().toString() + " - " + response.message() )
                        Log.i("INFO", "envoyé ${call.request().url()}")
                    }

                    override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                        Log.i("INFO", t.message!!)
                    }
                })
        }
    }
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage}")
    }

}