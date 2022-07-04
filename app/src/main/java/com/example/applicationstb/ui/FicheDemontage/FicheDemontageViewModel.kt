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
    var GALLERY_CAPTURE = 2
    var CAMERA_CAPTURE = 1
    var typeRoulements = MutableLiveData<MutableList<String>>();
    var refRoulements = MutableLiveData<MutableList<String>>();
    var posRoulement = MutableLiveData<MutableList<String>>();

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
            //getLocalFiches()
        }
    }
    fun setRoulements (refsAr :Array<String>,typeAr :Array<String>, refsAv :Array<String>, typeAv :Array<String>){
        if ( refsAr.size > 0){
            for (r in 0..refsAr.size-1){
                if (refsAr[r].length > 0) {
                    if (typeRoulements.value == null) typeRoulements.value = mutableListOf(typeAr[r])  else typeRoulements.value!!.add(typeAr[r])
                    if (refRoulements.value == null) refRoulements.value = mutableListOf(refsAr[r]) else refRoulements.value!!.add(refsAr[r])
                    if (posRoulement.value == null)  posRoulement.value = mutableListOf("arrière") else  posRoulement.value!!.add("arrière")
                }
            }
        }
        if ( refsAv.size > 0 ){
            for (r in 0..refsAv.size-1){
                if (refsAv[r].length > 0) {
                    if (typeRoulements.value == null) typeRoulements.value = mutableListOf(typeAv[r])  else typeRoulements.value!!.add(typeAv[r])
                    if (refRoulements.value == null) refRoulements.value = mutableListOf(refsAv[r]) else refRoulements.value!!.add(refsAv[r])
                    if (posRoulement.value == null)  posRoulement.value = mutableListOf("avant") else  posRoulement.value!!.add("avant")
                }
            }
        }
    }
    fun insertRoulements (ref:String,type:String,position:String)= runBlocking{
       if (typeRoulements.value == null) typeRoulements.value = mutableListOf(type)  else typeRoulements.value!!.add(type)
        if (refRoulements.value == null) refRoulements.value = mutableListOf(ref) else refRoulements.value!!.add(ref)
        if (posRoulement.value == null)  posRoulement.value = mutableListOf(position) else  posRoulement.value!!.add(position)
        if (position == "avant"){
            var copy = selection.value!!.refRoulementAvant!!.toMutableList()
            copy.add(ref)
            selection.value!!.refRoulementAvant = copy.filter { it.length > 0 }.toTypedArray()
            var copy2 = selection.value!!.typeRoulementAvant!!.toMutableList()
            copy2.add(type)
            selection.value!!.typeRoulementAvant = copy2.filter { it.length > 0 }.toTypedArray()
        }
        if (position == "arrière"){
            var copy = selection.value!!.refRoulementArriere!!.toMutableList()
            copy.filter { it.isNotBlank() }
            copy.add(ref)
            selection.value!!.refRoulementArriere = copy.filter { it.length > 0 }.toTypedArray()
            var copy2 = selection.value!!.typeRoulementArriere!!.toMutableList()
            copy2.filter { it.isNotBlank() }
            copy2.add(type)
            selection.value!!.typeRoulementArriere = copy2.filter { it.length > 0 }.toTypedArray()
        }
        getTime()
        localSave()
    }
    fun updateRoulements (ref:String,type:String,index: Int,){
        var roul = arrayOf(refRoulements.value!![index],typeRoulements.value!![index],posRoulement.value!![index])
        if (roul[2] == "avant" ){
            var copy = selection.value!!.refRoulementAvant!!.toMutableList()
            var copy2 = selection.value!!.typeRoulementAvant!!.toMutableList()
            copy[copy.indexOf(roul[0])] = ref
            selection.value!!.refRoulementAvant = copy.toTypedArray()
            copy2[copy2.indexOf(roul[1])] = type
            selection.value!!.typeRoulementAvant = copy2.toTypedArray()
        }
        if (roul[2] == "arrière"){
            var copy = selection.value!!.refRoulementArriere!!.toMutableList()
            copy[copy.indexOf(roul[0])] = ref
            selection.value!!.refRoulementArriere = copy.toTypedArray()
            var copy2 = selection.value!!.typeRoulementArriere!!.toMutableList()
            copy2[copy2.indexOf(roul[1])] = type
            selection.value!!.typeRoulementArriere = copy2.toTypedArray()
        }
        refRoulements.value!![index] = ref
        typeRoulements.value!![index] = type
        getTime()
        localSave()
    }
    fun removeRoulements (position: Int){
        var roul = arrayOf(refRoulements.value!![position],typeRoulements.value!![position],posRoulement.value!![position])

        if (roul[2] == "avant" ){
            var copy = selection.value!!.refRoulementAvant!!.toMutableList()
            var copy2 = selection.value!!.typeRoulementAvant!!.toMutableList()
            copy.removeAt(copy.indexOf(roul[0]))
            selection.value!!.refRoulementAvant = copy.toTypedArray()
            copy2.removeAt(copy2.indexOf(roul[1]))
            selection.value!!.typeRoulementAvant = copy2.toTypedArray()

        }
        if (roul[2] == "arrière"){
            var copy = selection.value!!.refRoulementArriere!!.toMutableList()
            copy.removeAt(copy.indexOf(roul[0]))
            selection.value!!.refRoulementArriere = copy.toTypedArray()
            var copy2 = selection.value!!.typeRoulementArriere!!.toMutableList()
            copy2.removeAt(copy2.indexOf(roul[1]))
            selection.value!!.typeRoulementArriere = copy2.toTypedArray()
        }
        refRoulements.value!!.removeAt(position)
        typeRoulements.value!!.removeAt(position)
        posRoulement.value!!.removeAt(position)
        getTime()
        localSave()
    }
    suspend fun getLocalFiches() {
        listeDemontages.postValue(
            repository.demontageRepository!!.getAllDemontageLocalDatabase().map { it.toFicheDemontage() }.filter { it.status!! < 3 }.toMutableList()
        )
    }
    fun back(view: View) {
        val action = FicheDemontageDirections.deDemontageversAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }

    fun setCouplage(type: String) {
        var fichemot = selection.value
        fichemot!!.couplage = type
        selection.value = fichemot!!
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addPhoto(photo: String) {
        var list = selection.value?.photos?.toMutableList()
        list!!.removeAll { it == "" }
        if (list != null) {
            list.add(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        } else {
            selection.value?.photos =
                arrayOf(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        }
        selection.value?.photos = list?.toTypedArray()
        photos.value = list!!
        galleryAddPic(photo)
        localSave()
        if (isOnline(context)) {
            viewModelScope.launch {
                var s = async {
                    repositoryPhoto.sendPhoto(
                        token!!.filterNot { it.isWhitespace() },
                        File(photo).name,
                        context
                    )
                }
                s.await()
                var s2 = async { sendFicheNoText() }
                s2.await()
            }
        }
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
            Log.i("info", " time ${(Date().time - start.value!!.time) + selection.value!!.dureeTotale!!}")
        } else {
            selection.value!!.dureeTotale = now.time - start.value!!.time
            Log.i("info", " time ${now.time - start.value!!.time}")
        }
        start.value = now
    }
    fun localSave() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.demontageRepository!!.updateDemontageLocalDatabse(selection.value!!.toEntity())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendExternalPicture(path: String): String? = runBlocking {
        if (isOnline(context)) {
            if (!sharedPref.getBoolean("connected", false) && (sharedPref?.getString(
                    "login",
                    ""
                ) !== "" && sharedPref?.getString("password", "") !== "")
            ) {
                connection(
                    sharedPref?.getString("login", "")!!,
                    sharedPref?.getString("password", "")!!
                )
            }
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                Log.i("info", "size ${selection.value?.photos?.size} is empty ${selection.value?.photos?.isEmpty()}")
                var file = if (selection.value?.photos?.size!! == 1 && selection.value?.photos!![0] == "") File(
                    dir,
                    "${selection.value?.numFiche}_${selection.value?.photos?.size!!}.jpg"
                ) else File(
                    dir,
                    "${selection.value?.numFiche}_${selection.value?.photos?.size!!+1}.jpg"
                )
                galleryAddPic(file.absolutePath)
                var old = File(path)
                old.copyTo(file, true)
                var s = async {
                    repositoryPhoto.sendPhoto(
                        token!!.filterNot { it.isWhitespace() },
                        file.name,
                        context
                    )
                }
                s.join()
                /* while(File(dir,"${selection.value?.numFiche}_${selection.value?.photos?.size}.jpg").exists()){
                     file = File(dir, "${selection.value?.numFiche}_${file.name.substringAfter("_").substringBefore(".").toInt()+1}.jpg")
                     Log.i("info","photo nom send ext ${file}")
                 }*/
                var s2 = async { sendFicheNoText() }
                s2.await()
                return@runBlocking file.name
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return@runBlocking null
            }
        } else {
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                Log.i("info", "size ${selection.value?.photos?.size}  first ${selection.value?.photos!![0]} ")
                var file = if (selection.value?.photos?.size!! == 1 && selection.value?.photos!![0] == "") File(
                    dir,
                    "${selection.value?.numFiche}_${selection.value?.photos?.size!!}.jpg"
                ) else File(
                    dir,
                    "${selection.value?.numFiche}_${selection.value?.photos?.size!!+1}.jpg"
                )
                galleryAddPic(file.absolutePath)
                var old = File(path)
                old.copyTo(file, true)
                return@runBlocking file.name
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return@runBlocking null
            }
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
                        Log.i("info", "new token ${resp.token}")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFiche(view: View) = runBlocking {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (isOnline(context) == true) {
                if (!sharedPref.getBoolean("connected", false) && (sharedPref?.getString(
                        "login",
                        ""
                    ) !== "" && sharedPref?.getString("password", "") !== "")
                ) {
                    connection(
                        sharedPref?.getString("login", "")!!,
                        sharedPref?.getString("password", "")!!
                    )
                }
                if (selection.value?.photos?.size!! > 0) {
                    selection.value?.photos?.forEach {
                        var test = async { repositoryPhoto.getURL(token!!, it) }
                        test.await()
                        if (test.isCompleted) {
                            if (test.await().code().equals(200)) {
                                var check = async {
                                    repositoryPhoto.getURLPhoto(
                                        token!!,
                                        test.await().body()?.name!!
                                    )
                                }
                                check.await()
                                if (check.isCompleted) {
                                    var code =
                                        async { repository.getPhoto(check.await().body()!!.url!!) }
                                    code.await()
                                    //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                    if (code.await().code() >= 400) {
                                        Log.i("info", "photo à envoyer${it}")
                                        var s = async {
                                            repositoryPhoto.sendPhoto(
                                                token!!,
                                                it,
                                                context
                                            )
                                        }
                                        s.await()
                                    }
                                }
                            }
                        }
                    }
                }
                repository.demontageRepository!!.patchFicheDemontage(
                    token!!,
                    selection.value!!._id,
                    selection.value!!,
                    object : Callback<FicheDemontageResponse> {
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
                        }
                    })
            } else {
                repository.demontageRepository!!.updateDemontageLocalDatabse(selection.value!!.toEntity())
            }

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFicheNoText() = runBlocking {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (isOnline(context) == true) {
                if (!sharedPref.getBoolean("connected", false) && (sharedPref?.getString(
                        "login",
                        ""
                    ) !== "" && sharedPref?.getString("password", "") !== "")
                ) {
                    connection(
                        sharedPref?.getString("login", "")!!,
                        sharedPref?.getString("password", "")!!
                    )
                }
                if (selection.value?.photos?.size!! > 0) {
                    selection.value?.photos?.forEach {
                        var test = async { repositoryPhoto.getURL(token!!, it) }
                        test.await()
                        if (test.isCompleted) {
                            if (test.await().code().equals(200)) {
                                var check = async {
                                    repositoryPhoto.getURLPhoto(
                                        token!!,
                                        test.await().body()?.name!!
                                    )
                                }
                                check.await()
                                if (check.isCompleted) {
                                    var code =
                                        async { repository.getPhoto(check.await().body()!!.url!!) }
                                    code.await()
                                    //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                    if (code.await().code() >= 400) {
                                        var s = async {
                                            repositoryPhoto.sendPhoto(
                                                token!!,
                                                it,
                                                context
                                            )
                                        }
                                        s.await()
                                    }
                                }
                            }
                        }
                    }
                }
                repository.demontageRepository!!.patchFicheDemontage(
                    token!!,
                    selection.value!!._id,
                    selection.value!!,
                    object : Callback<FicheDemontageResponse> {
                        override fun onResponse(
                            call: Call<FicheDemontageResponse>,
                            response: Response<FicheDemontageResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    Log.i("INFO", "enregistré")

                                }
                            } else {
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
                        }
                    })
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

    suspend fun getNameURI() {
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

    /*fun sendPhoto(photo:File)= runBlocking{
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
    }*/
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage}")
    }

}