package com.example.applicationstb.ui.ficheRemontage

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
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

class FicheRemontageViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context)
    var listeRemontages = mutableListOf<FicheRemontage>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    val selection = MutableLiveData<FicheRemontage>()
    var start = MutableLiveData<Date>()
    var listeDemo = MutableLiveData<Array<DemontageMoteur>?>(arrayOf())
    val sharedPref = getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)
    var ficheDemo = MutableLiveData<DemontageMoteur>()
    fun getListeDemo() : Array<DemontageMoteur> { return listeDemo.value!! }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
            listeRemontages = repository.remontageRepository!!.getAllRemontageLocalDatabase().map {it.toFicheRemo()}.toMutableList()
        }
    }

    fun select(i: Int) {
        selection.value = listeRemontages[i]
        //selection.value?.let { afficherFiche(it) }
    }

    fun addPhoto(index: Int, photo: Uri) {
        var list = selection?.value?.photos?.toMutableList()
        if (list != null) {
            list.add(photo.toString())
        }
        selection?.value?.photos = list?.toTypedArray()
        photos.value = list!!
    }

    fun retour(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheRemontageDirections.deRemoVersFScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }

    fun toFicheDemo(view: View, fiche: DemontageMoteur) {
        val action = FicheRemontageDirections.actionFicheRemontageToFicheDemontage(
            token!!, username!!
        )
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
        getTime()
        viewModelScope.launch(Dispatchers.IO) {
            repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
            /*if (selection.value!!.typeFicheRemontage == 6 || selection.value!!.typeFicheRemontage == 7 || selection.value!!.typeFicheRemontage == 9) {
                var fiche = selection.value!! as RemontageTriphase
                var tri = repository.remontageRepository!!.getByIdRemoTriLocalDatabse(selection.value!!._id)
                if (tri !== null) {
                    repository.remontageRepository!!.updateRemoTriLocalDatabse(fiche.toEntity())
                } else {
                    repository.remontageRepository!!.insertRemoTriLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheRemontage == 5) {
                var fiche = selection.value!! as RemontageCourantC
                var remo = repository.remontageRepository!!.getByIdRemoCCLocalDatabse(selection.value!!._id)
                //Log.i("INFO","${ch}")
                if (remo !== null) {
                    repository.remontageRepository!!.updateRemoCCLocalDatabse(fiche.toEntity())
                } else {
                    repository.remontageRepository!!.insertRemoCCLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheRemontage == 3 || selection.value!!.typeFicheRemontage == 4 || selection.value!!.typeFicheRemontage == 1 || selection.value!!.typeFicheRemontage == 2 || selection.value!!.typeFicheRemontage == 8) {
            /*    var fiche = selection.value!!
                var remo = repository.remontageRepository!!.getByIdRemoLocalDatabse(fiche._id)
                if (remo !== null) {
                    repository.remontageRepository!!.updateRemoLocalDatabse(fiche.to)
                } else {
                    repository.remontageRepository!!.insertRemoLocalDatabase(fiche)
                }*/
            }*/
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
            repository.remontageRepository!!.updateRemoLocalDatabse(selection.value!!.toEntity())
        }

        /*if (selection.value!!.typeFicheRemontage == 6 || selection.value!!.typeFicheRemontage == 7 || selection.value!!.typeFicheRemontage == 9 ) {
            var t = selection.value!! as RemontageTriphase
            if (isOnline(context) && token !== "") {
                val resp = repository.remontageRepository!!.patchRemontageTriphase(
                    token!!,
                    selection.value!!._id,
                    t,
                    object : Callback<RemontageTriphaseResponse> {
                        override fun onResponse(
                            call: Call<RemontageTriphaseResponse>,
                            response: Response<RemontageTriphaseResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                    mySnackbar.show()
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
                            call: Call<RemontageTriphaseResponse>,
                            t: Throwable
                        ) {
                            val mySnackbar = Snackbar.make(
                                view.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                "erreur d'enregistrement",
                                3600
                            )
                            mySnackbar.show()
                            Log.e(
                                "Error", "erreur ${t.message} - body request ${
                                    call.request().body().toString()
                                }\""
                            )
                        }
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    var tri = repository.remontageRepository!!.getByIdRemoTriLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                        repository.remontageRepository!!.updateRemoTriLocalDatabse(t.toEntity())
                        val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                        mySnackbar.show()
                    } else {
                        repository.remontageRepository!!.insertRemoTriLocalDatabase(t)
                        val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                        mySnackbar.show()
                    }
                }
            }
        }
        if (selection.value!!.typeFicheRemontage == 5) {
            var c = selection.value!! as RemontageCourantC
            if (isOnline(context) && token !== "") {
               /* val resp = repository.remontageRepository!!.patchRemontageCC(
                    token!!,
                    selection.value!!._id,
                    c,
                    object : Callback<RemontageCCResponse> {
                        override fun onResponse(
                            call: Call<RemontageCCResponse>,
                            response: Response<RemontageCCResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                    mySnackbar.show()
                                }
                            } else {
                                val mySnackbar =
                                    Snackbar.make(view, "erreur d'enregistrement", 3600)
                                mySnackbar.show()
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${
                                        response.errorBody()!!.charStream().readText()
                                    }"
                                )
                            }
                        }

                        override fun onFailure(call: Call<RemontageCCResponse>, t: Throwable) {
                            Log.e("Error", "erreur ${t.message}")
                            val mySnackbar = Snackbar.make(view, "erreur d'enregistrement", 3600)
                            mySnackbar.show()
                        }
                    })*/
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    var tri = repository.remontageRepository!!.getByIdRemoCCLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                        repository.remontageRepository!!.updateRemoCCLocalDatabse(c.toEntity())
                    } else {
                        repository.remontageRepository!!.insertRemoCCLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }
        if (selection.value!!.typeFicheRemontage == 3 || selection.value!!.typeFicheRemontage == 4 || selection.value!!.typeFicheRemontage == 1 || selection.value!!.typeFicheRemontage == 2) {
            var c = selection.value!!
            if (isOnline(context) && token !== "") {
                /*val resp = repository.remontageRepository!!.patchRemontage(
                    token!!,
                    selection.value!!._id,
                    c,
                    object : Callback<RemontageResponse> {
                        override fun onResponse(
                            call: Call<RemontageResponse>,
                            response: Response<RemontageResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                    mySnackbar.show()
                                }
                            } else {
                                val mySnackbar =
                                    Snackbar.make(view, "erreur d'enregistrement", 3600)
                                mySnackbar.show()
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${
                                        response.errorBody()!!.charStream().readText()
                                    }"
                                )
                            }
                        }

                        override fun onFailure(call: Call<RemontageResponse>, t: Throwable) {
                            Log.e("Error", "erreur ${t.message}")
                            val mySnackbar = Snackbar.make(view, "erreur d'enregistrement", 3600)
                            mySnackbar.show()
                        }
                    })*/
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    var tri = repository.remontageRepository!!.getByIdRemoLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                     //   repository.remontageRepository!!.updateRemoLocalDatabse(c.toRemoEntity())
                    } else {
                       // repository.remontageRepository!!.insertRemoLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.M)
    /*fun getListeDemontage(): Array<DemontageMoteur> = runBlocking{
        var liste = mutableListOf<DemontageMoteur>()
            if (isOnline(context) && sharedPref.getBoolean("connected",false)) {
                var job = viewModelScope.async {
                    repository.remontageRepository!!.getFichesForRemontage(
                        token!!,
                        selection.value!!.numDevis!!,
                        object : Callback<DemontagesResponse> {
                            override fun onResponse(
                                call: Call<DemontagesResponse>,
                                response: Response<DemontagesResponse>
                            ) {
                                var l = response.body()!!.data!!
                                for (f in l) {
                                    Log.i("INFO","add fiche")
                                    getFichesDemontage(f._id)
                                    liste.add(f)
                                }
                            }

                            override fun onFailure(call: Call<DemontagesResponse>, t: Throwable) {
                                Log.e("Error", "erreur ${t.message}")
                            }
                        })
                }
                Log.i("INFO", "fiches ${liste.size}")
               return@runBlocking liste.toTypedArray()
            } else {
                /*if (selection.value!!.typeFicheRemontage == 1) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontagePompeLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toDemoPompe())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                    }
                }
                if (selection.value!!.typeFicheRemontage == 2) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontageMonoLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toMonophase())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                    }

                }
                if (selection.value!!.typeFicheRemontage == 3) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontageAlterLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toDemontageAlternateur())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                    }

                }
                if (selection.value!!.typeFicheRemontage == 4) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontageRBLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toDemoRotorB())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                    }
                }
                if (selection.value!!.typeFicheRemontage == 5) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontageCCLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toCContinu())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                    }
                }
                if (selection.value!!.typeFicheRemontage == 6) {
                    viewModelScope.launch(Dispatchers.IO) {
                        var list = repository.getAllDemontageTriLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toTriphase())
                            }
                        }
                        listeDemo.postValue(t.toTypedArray())
                        listeDemo?.value?.get(0)?.numFiche?.let { Log.i("INFO", it) }
                    }
                }
                else{}*/
            }
        return@runBlocking liste.toTypedArray()
    }*/

    /*fun getFichesDemontage(id: String) : DemontageMoteur? {
        var demo = MutableLiveData<DemontageMoteur>()
        runBlocking {
            repository.demontageRepository!!.getDemontage(token!!, id, object: Callback<DemontageResponse> {
                override fun onResponse(
                    call: Call<DemontageResponse>,
                    response: Response<DemontageResponse>
                ) {
                    runBlocking {
                        var fiche = response.body()?.data
                        if (fiche !== null) {
                            Log.i("INFO", (fiche.typeFicheDemontage == selection.value?.typeFicheRemontage).toString())
                            if (fiche.typeFicheDemontage == 1 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                var pompe = viewModelScope.async {repository.demontageRepository!!.getDemontagePompe(
                                    token!!,
                                    fiche._id,
                                    object : Callback<DemontagePompeResponse> {
                                        override fun onResponse(
                                            call: Call<DemontagePompeResponse>,
                                            response: Response<DemontagePompeResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                fiche
                                                var copy = listeDemo.value?.toMutableList()
                                                if (copy != null) {
                                                    demo.value = response.body()!!.data!!
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var photos =
                                                            demo.value!!.photos?.toMutableList()
                                                        var iter = photos?.listIterator()
                                                        while (iter?.hasNext() == true) {
                                                            getPhotoFile(iter.next().toString())
                                                        }
                                                        demo.value!!.photos = photos?.toTypedArray()
                                                    }
                                                    if (!copy.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                    listeDemo.value = copy.toTypedArray()

                                                }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<DemontagePompeResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })}
                                pompe.await()
                                return@runBlocking demo.value
                            }
                            if (fiche.typeFicheDemontage == 2 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                Log.i("INFO", "mono")
                                var mono =  viewModelScope.async {
                                    repository.demontageRepository!!.getDemontageMono(
                                        token!!,
                                        fiche._id,
                                        object : Callback<DemontageMonophaseResponse> {
                                            override fun onResponse(
                                                call: Call<DemontageMonophaseResponse>,
                                                response: Response<DemontageMonophaseResponse>
                                            ) {
                                                if (response.code() == 200) {
                                                    demo.value = response.body()!!.data!!
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var photos =
                                                            demo.value!!.photos?.toMutableList()
                                                        var iter = photos?.listIterator()
                                                        while (iter?.hasNext() == true) {
                                                            getPhotoFile(iter.next().toString())
                                                        }
                                                        demo.value!!.photos = photos?.toTypedArray()
                                                    }
                                                    var copy = listeDemo.value?.toMutableList()
                                                    if (!copy!!.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                    listeDemo.value = copy?.toTypedArray()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<DemontageMonophaseResponse>,
                                                t: Throwable
                                            ) {
                                                Log.e("Error", "erreur ${t.message}")
                                            }
                                        })
                                }
                                mono.await()
                                return@runBlocking demo.value
                            }
                            if (fiche.typeFicheDemontage == 3 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                var alter = viewModelScope.async { repository.demontageRepository!!.getDemontageAlternateur(
                                    token!!,
                                    fiche._id,
                                    object : Callback<DemontageAlternateurResponse> {
                                        override fun onResponse(
                                            call: Call<DemontageAlternateurResponse>,
                                            response: Response<DemontageAlternateurResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                demo.value = response.body()!!.data!!
                                                var copy = listeDemo.value?.toMutableList()
                                                if (!copy!!.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                viewModelScope.launch(Dispatchers.IO) {
                                                    var photos =
                                                        demo.value!!.photos?.toMutableList()
                                                    var iter = photos?.listIterator()
                                                    while (iter?.hasNext() == true) {
                                                        getPhotoFile(iter.next().toString())
                                                    }
                                                    demo.value!!.photos = photos?.toTypedArray()
                                                }
                                                listeDemo.value = copy?.toTypedArray()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<DemontageAlternateurResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    }) }
                                return@runBlocking demo
                            }
                            if (fiche.typeFicheDemontage == 4 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                Log.i("INFO", "rotor")
                                var RB = repository.demontageRepository!!.getDemontageRB(
                                    token!!,
                                    fiche._id,
                                    object : Callback<DemontageRotorBobineResponse> {
                                        override fun onResponse(
                                            call: Call<DemontageRotorBobineResponse>,
                                            response: Response<DemontageRotorBobineResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                Log.i(
                                                    "INFO",
                                                    "type fiche ${response.body()?.data?.javaClass}"
                                                )
                                                demo.value = response.body()!!.data!!
                                                viewModelScope.launch(Dispatchers.IO) {
                                                    var photos =
                                                        demo.value!!.photos?.toMutableList()
                                                    var iter = photos?.listIterator()
                                                    while (iter?.hasNext() == true) {
                                                        getPhotoFile(iter.next().toString())
                                                    }
                                                    demo.value!!.photos = photos?.toTypedArray()
                                                }
                                                var copy = listeDemo.value?.toMutableList()
                                                if (!copy!!.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                listeDemo.value = copy?.toTypedArray()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<DemontageRotorBobineResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })
                            }
                            if (fiche.typeFicheDemontage == 5 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                var CC =
                                    repository.demontageRepository!!.getDemontageCC(
                                        token!!,
                                        fiche._id,
                                        object : Callback<DemontageCCResponse> {
                                            override fun onResponse(
                                                call: Call<DemontageCCResponse>,
                                                response: Response<DemontageCCResponse>
                                            ) {
                                                if (response.code() == 200) {
                                                    demo.value = response.body()!!.data!!
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var photos =
                                                            demo.value!!.photos?.toMutableList()
                                                        var iter = photos?.listIterator()
                                                        while (iter?.hasNext() == true) {
                                                            getPhotoFile(iter.next().toString())
                                                        }
                                                        demo.value!!.photos = photos?.toTypedArray()
                                                    }
                                                    var copy = listeDemo.value?.toMutableList()
                                                    if (!copy!!.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                    listeDemo.value = copy?.toTypedArray()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<DemontageCCResponse>,
                                                t: Throwable
                                            ) {
                                                Log.e("Error", "erreur ${t.message}")
                                            }
                                        })
                            }
                            if (fiche.typeFicheDemontage == 6 && fiche.typeFicheDemontage == selection.value?.typeFicheRemontage) {
                                var tri = repository.demontageRepository!!.getDemontageTriphase(
                                    token!!,
                                    fiche._id,
                                    object : Callback<DemontageTriphaseResponse> {
                                        override fun onResponse(
                                            call: Call<DemontageTriphaseResponse>,
                                            response: Response<DemontageTriphaseResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                demo.value = response.body()!!.data!!
                                                viewModelScope.launch(Dispatchers.IO) {
                                                    var photos =
                                                        demo.value!!.photos?.toMutableList()
                                                    var iter = photos?.listIterator()
                                                    while (iter?.hasNext() == true) {
                                                        getPhotoFile(iter.next().toString())
                                                    }
                                                   demo.value!!.photos = photos?.toTypedArray()
                                                }
                                                var copy = listeDemo.value?.toMutableList()
                                                if (!copy!!.contains(response.body()!!.data!!)) copy.add(response.body()!!.data!!)
                                                listeDemo.value = copy?.toTypedArray()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<DemontageTriphaseResponse>,
                                            t: Throwable
                                        ) {
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })
                            }
                            else{}
                    } else {}
                    }
                }

                override fun onFailure(call: Call<DemontageResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            })
        }
        return demo.value
    }*/
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
            /*var job2 = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var resp2 = repository.getPhoto(file!!)
            withContext(Dispatchers.Main){
                if( resp2.isSuccessful) {
                    saveImage(Glide.with(this@withContext)
                        .asBitmap()
                        .load(resp2.))
                   // var p = saveFile(resp2.body(), Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES).absolutePath+"/test_pictures/"+photoName)
                   // photos?.value!!.add(p)
                   // Log.i("INFO", "chemin:"+p)
                } else{
                    exceptionHandler
                }
            }
        }
        job2.join()*/
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