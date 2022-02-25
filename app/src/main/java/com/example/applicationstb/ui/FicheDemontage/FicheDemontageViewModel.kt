package com.example.applicationstb.ui.FicheDemontage

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import org.json.JSONArray
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.ficheBobinage.FicheBobinageDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class FicheDemontageViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context)
    var listeDemontages = arrayListOf<DemontageMoteur>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    var selection = MutableLiveData<DemontageMoteur>()
    var start = MutableLiveData<Date>()
    var image = MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
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
        var fichemot = selection.value as DemontageMoteur
        fichemot.couplage = type
        selection.value = fichemot
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
            if (selection.value!!.typeFicheDemontage == 1) {
                var fiche = selection.value!! as DemontagePompe
                if (fiche.sensRotation == null) fiche.sensRotation = false
                var f = repository.getByIdDemoPompeLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoPompeLocalDatabse(fiche.toEntity())
                } else {
                    repository.insertDemoPompeLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 2) {
                var fiche = selection.value!! as DemontageMonophase
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.getByIdDemoMonoLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoMonoLocalDatabse(fiche.toEntity())

                } else {
                    repository.insertDemoMonoLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 3) {
                var fiche = selection.value!! as DemontageAlternateur
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.getByIdDemoAlterLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoAlterLocalDatabse(fiche.toEntity())

                } else {
                    repository.insertDemoAlterLocalDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 4) {
                var fiche = selection.value!! as DemontageRotorBobine
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.getByIdDemoRBLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoRBLocalDatabse(fiche.toEntity())

                } else {
                    repository.insertDemoRBLocalDatabase(fiche)

                }
            }
            if (selection.value!!.typeFicheDemontage == 5) {
                var fiche = selection.value!! as CourantContinu
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.getByIdDemoCCLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoCCLocalDatabse(fiche.toEntity())

                } else {
                    try {
                        repository.deleteDemontageCCLocalDatabse(fiche.toEntity())
                    } catch (e: Exception) {

                    }
                    repository.insertDemoCCLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheDemontage == 6) {

                var fiche = selection.value!! as Triphase
                if (fiche.arbreSortantEntrant == null) fiche.arbreSortantEntrant = false
                if (fiche.accouplement == null) fiche.accouplement = false
                if (fiche.clavette == null) fiche.clavette = false
                var f = repository.getByIdDemoTriLocalDatabse(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoTriLocalDatabse(fiche.toEntity())

                } else {
                    repository.insertDemoTriLocalDatabase(fiche)

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
                var f = repository.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoMotoPompeLocalDatabase(fiche.toEntity())

                } else {
                    repository.insertDemoMotopompeDatabase(fiche)

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
                var f = repository.getByIdDemoReducteurLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoReducteurLocalDatabase(fiche.toEntity())

                } else {
                    repository.insertDemoReducteurDatabase(fiche)

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
                var f = repository.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)
                if (f !== null) {
                    repository.updateDemoMotoreducteurLocalDatabase(fiche.toEntity())

                } else {
                    repository.insertDemoMotoreducteurDatabase(fiche)

                }
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFiche(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            getNameURI()
            when (selection.value!!.typeFicheDemontage) {
                1 -> {
                    var fiche =
                        repository.getByIdDemoPompeLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoPompeLocalDatabse(fiche.toEntity())

                        val resp = repository.patchDemontagePompe(
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
                            var pmp = repository.getByIdDemoPompeLocalDatabse(selection.value!!._id)
                            if (pmp !== null) {
                                repository.updateDemoPompeLocalDatabse(p.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()

                            } else {
                                repository.insertDemoPompeLocalDatabase(p)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()

                            }
                        }
                    }
                }
                2 -> {
                    var fiche = repository.getByIdDemoMonoLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoMonoLocalDatabse(fiche.toEntity())
                        val resp = repository.patchDemontageMono(
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
                            var mono = repository.getByIdDemoMonoLocalDatabse(selection.value!!._id)
                            if (mono !== null) {
                                repository.updateDemoMonoLocalDatabse(m.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoMonoLocalDatabase(m)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }

                }
                3 -> {
                    var fiche = repository.getByIdDemoAlterLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoAlterLocalDatabse(fiche.toEntity())
                        val resp = repository.patchDemontageAlter(
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
                                repository.getByIdDemoAlterLocalDatabse(selection.value!!._id)
                            if (alter !== null) {
                                repository.updateDemoAlterLocalDatabse(a.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoAlterLocalDatabase(a)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }

                }
                4 -> {
                    var fiche = repository.getByIdDemoRBLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoRBLocalDatabse(fiche.toEntity())
                        val resp = repository.patchDemontageRotor(
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
                            var rotor = repository.getByIdDemoRBLocalDatabse(selection.value!!._id)
                            if (rotor !== null) {
                                repository.updateDemoRBLocalDatabse(rb.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoRBLocalDatabase(rb)
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
                        var dcc = repository.getByIdDemoCCLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoCCLocalDatabse(dcc.toEntity())
                        val resp = repository.patchDemontageCC(
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
                            var tri = repository.getByIdDemoCCLocalDatabse(selection.value!!._id)
                            if (tri !== null) {
                                repository.updateDemoCCLocalDatabse(c.toEntity())
                            } else {
                                repository.insertDemoCCLocalDatabase(c)
                            }
                            val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                            mySnackbar.show()
                        }
                    }
                }
                6 -> {
                    if (isOnline(context) == true) {
                        var dt = repository.getByIdDemoTriLocalDatabse(selection.value!!._id)!!
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
                        repository.updateDemoTriLocalDatabse(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.patchDemontageTriphase(
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
                            var tri = repository.getByIdDemoTriLocalDatabse(selection.value!!._id)
                            if (tri !== null) {
                                repository.updateDemoTriLocalDatabse(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoTriLocalDatabase(t)
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
                            repository.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)!!
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
                        repository.updateDemoMotoPompeLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.patchDemontageMotopompe(
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
                                repository.getByIdDemoMotopompeLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.updateDemoMotoPompeLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoMotopompeDatabase(t)
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
                            repository.getByIdDemoReducteurLocalDatabase(selection.value!!._id)!!
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
                        repository.updateDemoReducteurLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.patchDemontageReducteur(
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
                                repository.getByIdDemoReducteurLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.updateDemoReducteurLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoReducteurDatabase(t)
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
                            repository.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)!!
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
                        repository.updateDemoMotoreducteurLocalDatabase(dt.toEntity())
                        selection.postValue(dt)
                        photos.postValue(listPhotos!!)
                        val resp = repository.patchDemontageMotoreducteur(
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
                                repository.getByIdDemoMotoreducteurLocalDatabase(selection.value!!._id)
                            if (tri !== null) {
                                repository.updateDemoMotoreducteurLocalDatabase(t.toEntity())
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "patch local")
                            } else {
                                repository.insertDemoMotoreducteurDatabase(t)
                                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré local")
                            }
                        }
                    }
                }
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

    suspend fun getNameURI() = runBlocking {
        var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
        job.join()
    }

    fun sendPhoto(photo: File) {
        var s =
            imageName.value!!.url!!.removePrefix("http://195.154.107.195:9000/images/${imageName.value!!.name!!}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab.forEach {
            Log.i("INFO", it)
        }
        tab[1] = tab[1].replace("%2F", "/")
        repository.uploadPhoto(
            token!!,
            imageName.value!!.name!!,
            tab.toList(),
            photo,
            object : Callback<URLPhotoResponse> {
                override fun onResponse(
                    call: Call<URLPhotoResponse>,
                    response: Response<URLPhotoResponse>
                ) {
                    Log.i("INFO", response.code().toString() + " - " + response.message())
                    Log.i("INFO", "envoyé ${call.request().url()}")
                }

                override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                    Log.i("INFO", t.message!!)
                }
            })
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage}")
    }

}