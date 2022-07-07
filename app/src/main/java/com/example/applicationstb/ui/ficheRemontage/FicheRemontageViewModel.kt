package com.example.applicationstb.ui.ficheRemontage

import android.app.Application
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import com.example.applicationstb.model.FicheRemontage
import kotlin.reflect.typeOf

class FicheRemontageViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context)
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    var listeRemontages = MutableLiveData<MutableList<FicheRemontage>>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var photo = MutableLiveData<String>()
    val selection = MutableLiveData<FicheRemontage>()
    var start = MutableLiveData<Date>()
    val sharedPref = getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)
    var GALLERY_CAPTURE = 2
    var CAMERA_CAPTURE = 1

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
            getLocalFiches()
        }
    }
    suspend fun getLocalFiches(){
        listeRemontages.postValue(repository.remontageRepository!!.getAllRemontageLocalDatabase().map {it.toFicheRemo()}.toMutableList())
    }
    fun select(i: Int) {
        selection.value = listeRemontages.value!![i]
        //selection.value?.let { afficherFiche(it) }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        quickSave()
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
                var s2 = async { enregistrerSansMessage() }
                s2.await()
            }
        }
    }

    fun retour(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheRemontageDirections.deRemoVersFScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }

    fun getTime() {
        //Log.i("INFO", "duree avant : ${selection.value?.dureeTotale}")
        var now = Date()
        if (selection.value!!.dureeTotale !== null) {
            selection.value!!.dureeTotale =
                (now.time - start.value!!.time) + selection.value!!.dureeTotale!!
        } else {
            selection.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }

    fun quickSave() {
        Log.i("INFO", "quick save")
        viewModelScope.launch(Dispatchers.IO) {
            repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun enregistrer(view: View) = runBlocking {
        if (isOnline(getApplication<Application>().baseContext)) {
            if (!sharedPref.getBoolean("connected",false) && (sharedPref?.getString("login", "") !== "" && sharedPref?.getString("password", "") !== "" )){
                connection(sharedPref?.getString("login", "")!!,sharedPref?.getString("password", "")!!)
            }
            delay(200)
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                repository.remontageRepository!!.patchRemontage(token!!, selection.value!!._id, selection.value!!, object : Callback<RemontageResponse>{
                    override fun onResponse(
                        call: Call<RemontageResponse>,
                        response: Response<RemontageResponse>
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
                            viewModelScope.launch(Dispatchers.IO){
                                repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
                            }
                            Log.i(
                                "INFO",
                                "code : ${response.code()} - erreur : ${response.message()} - body request ${
                                    response.errorBody()!!.charStream().readText()
                                }"
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<RemontageResponse>,
                        t: Throwable
                    ) {
                        Log.e("Error", "${t.stackTraceToString()}")
                        Log.e("Error", "erreur ${t.message}")
                    }})
            }
        } else {
            viewModelScope.launch(Dispatchers.IO){
                repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
            }
            val mySnackbar =
                Snackbar.make(view, "fiche enregistrée", 3600)
            mySnackbar.show()

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun enregistrerSansMessage() = runBlocking {
        if (isOnline(getApplication<Application>().baseContext)) {
            if (!sharedPref.getBoolean("connected",false) && (sharedPref?.getString("login", "") !== "" && sharedPref?.getString("password", "") !== "" )){
                connection(sharedPref?.getString("login", "")!!,sharedPref?.getString("password", "")!!)
            }
            delay(200)
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                repository.remontageRepository!!.patchRemontage(token!!, selection.value!!._id, selection.value!!, object : Callback<RemontageResponse>{
                    override fun onResponse(
                        call: Call<RemontageResponse>,
                        response: Response<RemontageResponse>
                    ) {
                        if (response.code() == 200) {
                            val resp = response.body()
                        } else {
                            viewModelScope.launch(Dispatchers.IO){
                                repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
                            }
                            Log.i(
                                "INFO",
                                "code : ${response.code()} - erreur : ${response.message()} - body request ${
                                    response.errorBody()!!.charStream().readText()
                                }"
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<RemontageResponse>,
                        t: Throwable
                    ) {
                        Log.e("Error", "${t.stackTraceToString()}")
                        Log.e("Error", "erreur ${t.message}")
                    }})
            }
        } else {
            viewModelScope.launch(Dispatchers.IO){
                repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
            }
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
                var s2 = async { enregistrerSansMessage() }
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


    @RequiresApi(Build.VERSION_CODES.M)

    suspend fun getPhotoFile(photoName: String): String? = runBlocking {
        var file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_pictures/" + photoName
        )
        Log.i("INFO", "fichier ${file.absolutePath} exist: ${file.exists().toString()}")
        var path: String? = null
        if (!file.exists()) {
            var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val resp1 = repository.getURLPhoto(token!!, photoName)
                withContext(Dispatchers.Main) {
                    if (resp1.isSuccessful) {
                        if (resp1.code() == 200) {
                            path = resp1.body()?.url!!
                            Log.i(
                                "INFO",
                                "url de la photo ${photoName} :" + resp1.body()?.url!!.replace(
                                    "%2F",
                                    "/"
                                )
                            )
                            CoroutineScope((Dispatchers.IO + exceptionHandler2)).launch {
                                saveImage(
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(resp1.body()!!.url!!.replace("%2F", "/"))
                                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                                        .error(android.R.drawable.stat_notify_error)
                                        .submit()
                                        .get(), photoName
                                )
                            }
                        }
                    } else {
                        exceptionHandler
                        path = null
                    }
                }
            }
            job.join()
        } else {
            if (file.exists()) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/test_pictures/" + photoName
            } else {
                path = null
            }
        }
        return@runBlocking path
    }
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage} - ${throwable.cause}")
    }
    val exceptionHandler2 = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "erreur enregistrement: ${throwable.localizedMessage}")
    }

    fun toDemontage(view: View, fiche: String) {
        Navigation.findNavController(view).navigate(FicheRemontageDirections.actionFicheRemontageToFicheDemontage(token,sharedPref.getString("userId","")!!,fiche))
    }
    fun getListeDemontage(view: View){
        view.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            var list = async { repository.demontageRepository!!.getFicheForRemontage(token!!,selection.value!!.numDevis!!) }.await()
            for (fiche in list.body()!!.data){
                var check  = repository.demontageRepository!!.getAllDemontageLocalDatabase().map { it._id }.indexOf(fiche._id)
                if (check < 0) {
                    repository.demontageRepository!!.insertDemontageLocalDatabase(fiche)
                } else {
                    repository.demontageRepository!!.updateDemontageLocalDatabse(fiche.toEntity())
                }
            }
            if (list.isSuccessful){
                val mySnackbar =
                    Snackbar.make(view, "liste des fiches mise à jour", 3600)
                mySnackbar.show()
            } else {
                val mySnackbar =
                    Snackbar.make(view, "erreur lors de la mise à jour des fiches", 3600)
                mySnackbar.show()
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

    private fun saveImage(image: Bitmap, name: String): String? {
        Log.i("INFO", "start")
        var savedImagePath: String? = null
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_pictures/"
        )
        Log.i("INFO", storageDir.exists().toString())
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, name)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            //updateStorage(context,name)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
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
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
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
}