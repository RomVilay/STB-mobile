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
    var listeDemontages = mutableListOf<FicheDemontage>()
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
            listeDemontages = repository.demontageRepository!!.getAllDemontageLocalDatabase().map { it.toFicheDemontage() }.toMutableList()
        }
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
           /* if (selection.value!!.typeFicheDemontage == 1) {
                var fiche = selection.value!! as DemontagePompe
                if (fiche.sensRotation == null) fiche.sensRotation = false
                var f = repository.demontageRepository!!.getByIdDemoPompeLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoPompeLocalDatabse(fiche.toEntity())
                } else {
                    repository.demontageRepository!!.insertDemoPompeLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 2) {
                var fiche = selection.value!! as DemontageMonophase
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoMonoLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoMonoLocalDatabse(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoMonoLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 3) {
                var fiche = selection.value!! as DemontageAlternateur
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoAlterLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoAlterLocalDatabse(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoAlterLocalDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 4) {
                var fiche = selection.value!! as DemontageRotorBobine
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoRBLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoRBLocalDatabse(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoRBLocalDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 5) {
                var fiche = selection.value!! as CourantContinu
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoCCLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoCCLocalDatabse(fiche.toEntity())

                } else {
                    try {
                        repository.demontageRepository!!.deleteDemontageCCLocalDatabse(fiche.toEntity())
                    } catch (e: Exception) {

                    }
                    repository.demontageRepository!!.insertDemoCCLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 6) {

                var fiche = selection.value!! as Triphase
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoTriLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoTriLocalDatabse(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoTriLocalDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 7) {

                var fiche = selection.value!! as DemontageMotopompe
                if (fiche.sensRotation == null) fiche.sensRotation = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.demontageRepository!!.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoMotoPompeLocalDatabase(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoMotopompeDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 8) {

                var fiche = selection.value!! as DemontageReducteur
                /*if (fiche.sensRotation == null) fiche.sensRotation = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false*/
                var f = repository.demontageRepository!!.getByIdDemoReducteurLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoReducteurLocalDatabase(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoReducteurDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 9) {

                var fiche = selection.value!! as DemontageMotoreducteur
                /*if (fiche.sensRotation == null) fiche.sensRotation = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false*/
                var f = repository.demontageRepository!!.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.demontageRepository!!.updateDemoMotoreducteurLocalDatabase(fiche.toEntity())

                } else {
                    repository.demontageRepository!!.insertDemoMotoreducteurDatabase(fiche)

                }
            }*/

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
           /* when (selection.value!!.subtype) {
                1 -> {
                    var fiche =
                        repository.demontageRepository!!.getByIdDemoPompeLocalDatabse(selection.value!!._id)!!
                    if (isOnline(context)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            getNameURI()
                        }
                        var listPhotos = fiche.photos?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(fiche.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        Log.i("INFO", "new name: ${imageName.value!!.name}")
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        fiche.photos = listPhotos?.toTypedArray()
                        selection.postValue(fiche)
                        photos.postValue(listPhotos!!)
                        repository.demontageRepository!!.updateDemoPompeLocalDatabse(fiche.toEntity())

                        val resp = repository.demontageRepository!!.patchDemontagePompe(
                            token!!,
                            fiche._id,
                            fiche,
                            object : Callback<DemontagePompeResponse> {
                                override fun onResponse(
                                    call: Call<DemontagePompeResponse>,
                                    response: Response<DemontagePompeResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            /*if (fiche.status!! == 3L){
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    repository.deleteDemontagePompeLocalDatabse(fiche.toEntity())
                                                    delay(100)
                                                    listeDemontages.remove(selection.value!!)
                                                    selection.postValue(null)
                                                }
                                            }*/
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
                                    call: Call<DemontagePompeResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            var p = selection.value as DemontagePompe
                            var pmp = repository.demontageRepository!!.getByIdDemoPompeLocalDatabse(selection.value!!._id)
                            if (pmp !== null) {
                                repository.demontageRepository!!.updateDemoPompeLocalDatabse(p.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()

                            } else {
                                repository.demontageRepository!!.insertDemoPompeLocalDatabase(p)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()

                            }
                        }
                    }
                }
                2 -> {
                    var fiche = repository.demontageRepository!!.getByIdDemoMonoLocalDatabse(selection.value!!._id)!!
                    if (isOnline(context)) {
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(fiche.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        fiche.photos = listPhotos?.toTypedArray()
                        selection.postValue(fiche)
                        photos.postValue(listPhotos)
                        repository.demontageRepository!!.updateDemoMonoLocalDatabse(fiche.toEntity())
                        val resp = repository.demontageRepository!!.patchDemontageMono(
                            token!!,
                            fiche._id,
                            fiche,
                            object : Callback<DemontageMonophaseResponse> {
                                override fun onResponse(
                                    call: Call<DemontageMonophaseResponse>,
                                    response: Response<DemontageMonophaseResponse>
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
                                    call: Call<DemontageMonophaseResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            getNameURI()
                        }
                        var m = selection.value!! as DemontageMonophase
                        viewModelScope.launch(Dispatchers.IO) {
                            var mono = repository.demontageRepository!!.getByIdDemoMonoLocalDatabse(selection.value!!._id)
                            if (mono !== null) {
                                repository.demontageRepository!!.updateDemoMonoLocalDatabse(m.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoMonoLocalDatabase(m)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }

                }
                3 -> {
                    var fiche = repository.demontageRepository!!.getByIdDemoAlterLocalDatabse(selection.value!!._id)!!
                    if (isOnline(context)) {
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        CoroutineScope(Dispatchers.IO).launch {
                            getNameURI()
                        }
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(fiche.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        fiche.photos = listPhotos?.toTypedArray()
                        selection.postValue(fiche)
                        photos.postValue(listPhotos!!)
                        repository.demontageRepository!!.updateDemoAlterLocalDatabse(fiche.toEntity())
                        val resp = repository.demontageRepository!!.patchDemontageAlter(
                            token!!,
                            fiche._id,
                            fiche,
                            object : Callback<DemontageAlternateurResponse> {
                                override fun onResponse(
                                    call: Call<DemontageAlternateurResponse>,
                                    response: Response<DemontageAlternateurResponse>
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
                                    call: Call<DemontageAlternateurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var a = selection.value!! as DemontageAlternateur
                        viewModelScope.launch(Dispatchers.IO) {
                            var alter =
                                repository.demontageRepository!!.getByIdDemoAlterLocalDatabse(selection.value!!._id)
                            if (alter !== null) {
                                repository.demontageRepository!!.updateDemoAlterLocalDatabse(a.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoAlterLocalDatabase(a)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }

                }
                4 -> {
                    var fiche = repository.demontageRepository!!.getByIdDemoRBLocalDatabse(selection.value!!._id)!!
                    var rb = selection.value!! as DemontageRotorBobine
                    if (isOnline(context)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            getNameURI()
                        }
                        var listPhotos = fiche.photos?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(fiche.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        fiche.photos = listPhotos?.toTypedArray()
                        selection.postValue(fiche)
                        photos.postValue(listPhotos!!)
                        repository.demontageRepository!!.updateDemoRBLocalDatabse(fiche.toEntity())
                        val resp = repository.demontageRepository!!.patchDemontageRotor(
                            token!!,
                            fiche._id,
                            fiche,
                            object : Callback<DemontageRotorBobineResponse> {
                                override fun onResponse(
                                    call: Call<DemontageRotorBobineResponse>,
                                    response: Response<DemontageRotorBobineResponse>
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
                                    call: Call<DemontageRotorBobineResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            var rotor = repository.demontageRepository!!.getByIdDemoRBLocalDatabse(selection.value!!._id)
                            if (rotor !== null) {
                                repository.demontageRepository!!.updateDemoRBLocalDatabse(rb.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoRBLocalDatabase(rb)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }

                }
                5 -> {
                    if (isOnline(context)) {
                        Log.i("info", "fiche id: ${selection.value!!._id}")
                        var dcc = repository.demontageRepository!!.getByIdDemoCCLocalDatabse(selection.value!!._id)!!
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dcc.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        dcc.photos = listPhotos?.toTypedArray()
                        selection.postValue(dcc)
                        photos.postValue(listPhotos)
                        repository.demontageRepository!!.updateDemoCCLocalDatabse(dcc.toEntity())
                        val resp = repository.demontageRepository!!.patchDemontageCC(
                            token!!,
                            dcc._id,
                            dcc,
                            object : Callback<DemontageCCResponse> {
                                override fun onResponse(
                                    call: Call<DemontageCCResponse>,
                                    response: Response<DemontageCCResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                            val mySnackbar =
                                                Snackbar.make(view, "fiche enregistrée", 3600)
                                            mySnackbar.show()
                                        }
                                    } else {
                                        val mySnackbar =
                                            Snackbar.make(view, "erreur enregistrement", 3600)
                                        mySnackbar.show()
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()}"
                                        )
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DemontageCCResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var c = selection.value!! as CourantContinu
                        viewModelScope.launch(Dispatchers.IO) {
                            var tri = repository.demontageRepository!!.getByIdDemoCCLocalDatabse(selection.value!!._id)
                            if (tri !== null) {
                                repository.demontageRepository!!.updateDemoCCLocalDatabse(c.toEntity())
                            } else {
                                repository.demontageRepository!!.insertDemoCCLocalDatabase(c)
                            }
                            val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                            mySnackbar.show()
                        }
                    }
                }
                6 -> {
                    if (isOnline(context) == true) {
                        var dt = repository.demontageRepository!!.getByIdDemoTriLocalDatabse(selection.value!!._id)!!
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            Log.i("INFO", "fichier original : ${name}")
                            if (name !== "") {
                                // Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dt.numFiche!!)) {
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dt.photos = listPhotos?.toTypedArray()
                        Log.i(
                            "info",
                            "list photo selection size 2 ${selection.value?.photos?.size} - liste photos ${photos.value?.size}"
                        )
                        repository.demontageRepository!!.updateDemoTriLocalDatabse(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.demontageRepository!!.patchDemontageTriphase(
                            token!!,
                            dt._id,
                            dt,
                            object : Callback<DemontageTriphaseResponse> {
                                override fun onResponse(
                                    call: Call<DemontageTriphaseResponse>,
                                    response: Response<DemontageTriphaseResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            val mySnackbar =
                                                Snackbar.make(view, "fiche enregistrée", 3600)
                                            mySnackbar.show()
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                    } else {
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()}"
                                        )
                                        val mySnackbar =
                                            Snackbar.make(view, "erreur enregistrement", 3600)
                                        mySnackbar.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DemontageTriphaseResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var t = selection.value!! as Triphase
                        viewModelScope.launch(Dispatchers.IO) {
                            var tri = repository.demontageRepository!!.getByIdDemoTriLocalDatabse(selection.value!!._id)
                            if (tri !== null) {
                                repository.demontageRepository!!.updateDemoTriLocalDatabse(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoTriLocalDatabase(t)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }
                }
                7 -> {
                    if (isOnline(context) == true) {
                        Log.i("info", "marque viewmodel ${selection.value?.marque}")
                        var dt =
                            repository.demontageRepository!!.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)!!
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(dt.numFiche!!)) {
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dt.photos = listPhotos?.toTypedArray()
                        repository.demontageRepository!!.updateDemoMotoPompeLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.demontageRepository!!.patchDemontageMotopompe(
                            token!!,
                            dt._id,
                            dt,
                            object : Callback<DemontageMotopompeResponse> {
                                override fun onResponse(
                                    call: Call<DemontageMotopompeResponse>,
                                    response: Response<DemontageMotopompeResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            val mySnackbar =
                                                Snackbar.make(view, "fiche enregistrée", 3600)
                                            mySnackbar.show()
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                    } else {
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()}"
                                        )
                                        val mySnackbar =
                                            Snackbar.make(view, "erreur enregistrement", 3600)
                                        mySnackbar.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DemontageMotopompeResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var t = selection.value!! as DemontageMotopompe
                        viewModelScope.launch(Dispatchers.IO) {
                            var tri =
                                repository.demontageRepository!!.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.demontageRepository!!.updateDemoMotoPompeLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoMotopompeDatabase(t)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }
                }
                8 -> {
                    if (isOnline(context) == true) {
                        var dt =
                            repository.demontageRepository!!.getByIdDemoReducteurLocalDatabase(selection.value!!._id)!!
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            Log.i("INFO", "fichier original : ${name}")
                            if (name !== "") {
                                // Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dt.numFiche!!)) {
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dt.photos = listPhotos?.toTypedArray()
                        repository.demontageRepository!!.updateDemoReducteurLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.demontageRepository!!.patchDemontageReducteur(
                            token!!,
                            dt._id,
                            dt,
                            object : Callback<DemontageReducteurResponse> {
                                override fun onResponse(
                                    call: Call<DemontageReducteurResponse>,
                                    response: Response<DemontageReducteurResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            val mySnackbar =
                                                Snackbar.make(view, "fiche enregistrée", 3600)
                                            mySnackbar.show()
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                    } else {
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()}"
                                        )
                                        val mySnackbar =
                                            Snackbar.make(view, "erreur enregistrement", 3600)
                                        mySnackbar.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DemontageReducteurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var t = selection.value!! as DemontageReducteur
                        viewModelScope.launch(Dispatchers.IO) {
                            var tri =
                                repository.demontageRepository!!.getByIdDemoReducteurLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.demontageRepository!!.updateDemoReducteurLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoReducteurDatabase(t)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }
                }
                9 -> {
                    if (isOnline(context) == true) {
                        var dt =
                            repository.demontageRepository!!.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)!!
                        var listPhotos = photos.value?.toMutableList()
                        var iter = listPhotos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            Log.i("INFO", "fichier original : ${name}")
                            if (name !== "") {
                                // Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dt.numFiche!!)) {
                                        //var test = getPhotoFile(name)
                                        var job =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                getNameURI()
                                            }
                                        job.join()
                                        var job2 =
                                            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                try {
                                                    val dir =
                                                        Environment.getExternalStoragePublicDirectory(
                                                            Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                        )
                                                    val from = File(
                                                        dir,
                                                        name
                                                    )
                                                    val to = File(dir, imageName.value!!.name!!)
                                                    Log.i(
                                                        "INFO",
                                                        from.exists()
                                                            .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                    )
                                                    if (from.exists()) from.renameTo(to)
                                                    sendPhoto(to)
                                                    iter.set(imageName.value!!.name!!)
                                                } catch (e: java.lang.Exception) {
                                                    Log.e("EXCEPTION", e.message!!)
                                                }
                                            }
                                        job2.join()
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dt.photos = listPhotos?.toTypedArray()
                        repository.demontageRepository!!.updateDemoMotoreducteurLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.demontageRepository!!.patchDemontageMotoreducteur(
                            token!!,
                            dt._id,
                            dt,
                            object : Callback<DemontageMotoreducteurResponse> {
                                override fun onResponse(
                                    call: Call<DemontageMotoreducteurResponse>,
                                    response: Response<DemontageMotoreducteurResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            val mySnackbar =
                                                Snackbar.make(view, "fiche enregistrée", 3600)
                                            mySnackbar.show()
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                    } else {
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()}"
                                        )
                                        val mySnackbar =
                                            Snackbar.make(view, "erreur enregistrement", 3600)
                                        mySnackbar.show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<DemontageMotoreducteurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    } else {
                        var t = selection.value!! as DemontageMotoreducteur
                        viewModelScope.launch(Dispatchers.IO) {
                            var tri =
                                repository.demontageRepository!!.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.demontageRepository!!.updateDemoMotoreducteurLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.demontageRepository!!.insertDemoMotoreducteurDatabase(t)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }
                }
            }*/

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