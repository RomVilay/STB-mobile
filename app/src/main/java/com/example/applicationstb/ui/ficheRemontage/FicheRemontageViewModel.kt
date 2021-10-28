package com.example.applicationstb.ui.ficheRemontage

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FicheRemontageViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context)
    var listeRemontages = arrayListOf<Remontage>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    val selection = MutableLiveData<Remontage>()
    var start = MutableLiveData<Date>()
    var listeDemo = MutableLiveData<Array<DemontageMoteur>?>(arrayOf())
    var ficheDemo = MutableLiveData<DemontageMoteur>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
        }
    }

    fun select(i: Int) {
        selection.value = listeRemontages[i]
        //selection.value?.let { afficherFiche(it) }
    }

    fun addPhoto(index: Int, photo: Uri) {
        photos.value!!.add(photo.toString())
    }

    fun retour(view: View) {
        var action = FicheRemontageDirections.deRemontageverAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheRemontageDirections.deRemoVersFScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }

    fun toFicheDemo(view: View, fiche: DemontageMoteur) {
        val action = FicheRemontageDirections.actionFicheRemontageToFicheDemontage(
            token!!, username!!,
            arrayOf(fiche)
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
            if (selection.value!!.typeFicheRemontage == 6) {
                var fiche = selection.value!! as RemontageTriphase
                var tri = repository.getByIdRemoTriLocalDatabse(selection.value!!._id)
                if (tri !== null) {
                    repository.updateRemoTriLocalDatabse(fiche.toEntity())
                } else {
                    repository.insertRemoTriLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheRemontage == 5) {
                var fiche = selection.value!! as RemontageCourantC
                var remo = repository.getByIdRemoCCLocalDatabse(selection.value!!._id)
                //Log.i("INFO","${ch}")
                if (remo !== null) {
                    repository.updateRemoCCLocalDatabse(fiche.toEntity())
                } else {
                    repository.insertRemoCCLocalDatabase(fiche)
                }
            }
            if (selection.value!!.typeFicheRemontage == 3 || selection.value!!.typeFicheRemontage == 4 || selection.value!!.typeFicheRemontage == 1 || selection.value!!.typeFicheRemontage == 2) {
                var fiche = selection.value!!
                var remo = repository.getByIdRemoLocalDatabse(fiche._id)
                Log.i("INFO","${remo !== null}")
                Log.i("INFO", remo.toString())
                if (remo !== null) {
                    repository.updateRemoLocalDatabse(fiche.toRemoEntity())
                } else {
                    repository.insertRemoLocalDatabase(fiche)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun enregistrer(view: View) {
        if (selection.value!!.typeFicheRemontage == 6) {
            var t = selection.value!! as RemontageTriphase
            if (isOnline(context)) {
                val resp = repository.patchRemontageTriphase(
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
                    var tri = repository.getByIdRemoTriLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                        repository.updateRemoTriLocalDatabse(t.toEntity())
                        val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                        mySnackbar.show()
                    } else {
                        repository.insertRemoTriLocalDatabase(t)
                        val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                        mySnackbar.show()
                    }
                }
            }
        }
        if (selection.value!!.typeFicheRemontage == 5) {
            var c = selection.value!! as RemontageCourantC
            if (isOnline(context)) {
                val resp = repository.patchRemontageCC(
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
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    var tri = repository.getByIdRemoCCLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                        repository.updateRemoCCLocalDatabse(c.toEntity())
                    } else {
                        repository.insertRemoCCLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }
        if (selection.value!!.typeFicheRemontage == 3 || selection.value!!.typeFicheRemontage == 4 || selection.value!!.typeFicheRemontage == 1 || selection.value!!.typeFicheRemontage == 2) {
            var c = selection.value!!
            if (isOnline(context)) {
                val resp = repository.patchRemontage(
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
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    var tri = repository.getByIdRemoLocalDatabse(selection.value!!._id)
                    if (tri !== null) {
                        repository.updateRemoLocalDatabse(c.toRemoEntity())
                    } else {
                        repository.insertRemoLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getListeDemontage() {
        if (isOnline(context)) {
            repository.getFichesForRemontage(
                token!!,
                selection.value!!.numDevis!!,
                object : Callback<FichesResponse> {
                    override fun onResponse(
                        call: Call<FichesResponse>,
                        response: Response<FichesResponse>
                    ) {
                        var l = response.body()!!.fiches!!
                        for (f in l) {
                            getFichesDemontage(f._id)
                        }
                    }

                    override fun onFailure(call: Call<FichesResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        } else {
            if (selection.value!!.typeFicheRemontage == 1) {
                viewModelScope.launch {
                    var list = repository.getAllDemontagePompeLocalDatabase()
                    var t = mutableListOf<DemontageMoteur>()
                    for (i in list) {
                        if (i.numDevis == selection.value!!.numDevis) {
                            t.add(i.toDemoPompe())
                        }
                    }
                    listeDemo.value = t.toTypedArray()
                }
            }
            if (selection.value!!.typeFicheRemontage == 2) {
                    viewModelScope.launch {
                        var list = repository.getAllDemontageMonoLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toMonophase())
                            }
                        }
                        listeDemo.value = t.toTypedArray()
                    }

            }
            if (selection.value!!.typeFicheRemontage == 3) {
                    viewModelScope.launch {
                        var list = repository.getAllDemontageAlterLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toDemontageAlternateur())
                            }
                        }
                        listeDemo.value = t.toTypedArray()
                    }

            }
            if (selection.value!!.typeFicheRemontage == 4) {
                    viewModelScope.launch {
                        var list = repository.getAllDemontageRBLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toDemoRotorB())
                            }
                        }
                        listeDemo.value = t.toTypedArray()
                    }
            }
            if (selection.value!!.typeFicheRemontage == 5) {
                    viewModelScope.launch {
                        var list = repository.getAllDemontageCCLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toCContinu())
                            }
                        }
                        listeDemo.value = t.toTypedArray()
                    }
            }
            if (selection.value!!.typeFicheRemontage == 6) {
                    viewModelScope.launch {
                        var list = repository.getAllDemontageTriLocalDatabase()
                        var t = mutableListOf<DemontageMoteur>()
                        for (i in list) {
                            if (i.numDevis == selection.value!!.numDevis) {
                                t.add(i.toTriphase())
                            }
                        }
                        listeDemo.value = t.toTypedArray()
                    }
            }
        }
    }

    fun getFichesDemontage(idFiche: String) {
        if (selection.value!!.typeFicheRemontage == 1) {
            var fiche = repository.getDemontagePompe(
                token!!,
                idFiche,
                object : Callback<DemontagePompeResponse> {
                    override fun onResponse(
                        call: Call<DemontagePompeResponse>,
                        response: Response<DemontagePompeResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            if (copy != null) {
                                copy.add(response.body()!!.fiche!!)
                            }
                            if (copy != null) {
                                listeDemo.value = copy.toTypedArray()
                            }
                            // Log.i("INFO", "fiche ${ficheDemo.value?.numFiche.toString()}")
                        }
                    }

                    override fun onFailure(call: Call<DemontagePompeResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        }
        if (selection.value!!.typeFicheRemontage == 2) {
            var fiche = repository.getDemontageMono(
                token!!,
                idFiche,
                object : Callback<DemontageMonophaseResponse> {
                    override fun onResponse(
                        call: Call<DemontageMonophaseResponse>,
                        response: Response<DemontageMonophaseResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            copy?.add(response.body()!!.fiche!!)
                            listeDemo.value = copy?.toTypedArray()
                            Log.i("INFO", listeDemo.value!!.size.toString())
                        }
                    }

                    override fun onFailure(call: Call<DemontageMonophaseResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })

        }
        if (selection.value!!.typeFicheRemontage == 3) {
            var fiche = repository.getDemontageAlternateur(
                token!!,
                idFiche,
                object : Callback<DemontageAlternateurResponse> {
                    override fun onResponse(
                        call: Call<DemontageAlternateurResponse>,
                        response: Response<DemontageAlternateurResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            copy?.add(response.body()!!.fiche!!)
                            listeDemo.value = copy?.toTypedArray()
                        }
                    }

                    override fun onFailure(call: Call<DemontageAlternateurResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        }
        if (selection.value!!.typeFicheRemontage == 4) {
            var fiche = repository.getDemontageRB(
                token!!,
                idFiche,
                object : Callback<DemontageRotorBobineResponse> {
                    override fun onResponse(
                        call: Call<DemontageRotorBobineResponse>,
                        response: Response<DemontageRotorBobineResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            copy?.add(response.body()!!.fiche!!)
                            listeDemo.value = copy?.toTypedArray()
                        }
                    }

                    override fun onFailure(call: Call<DemontageRotorBobineResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        }
        if (selection.value!!.typeFicheRemontage == 5) {
            var fiche =
                repository.getDemontageCC(token!!, idFiche, object : Callback<DemontageCCResponse> {
                    override fun onResponse(
                        call: Call<DemontageCCResponse>,
                        response: Response<DemontageCCResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            copy?.add(response.body()!!.fiche!!)
                            listeDemo.value = copy?.toTypedArray()
                        }
                    }

                    override fun onFailure(call: Call<DemontageCCResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        }
        if (selection.value!!.typeFicheRemontage == 6) {
            var fiche = repository.getDemontageTriphase(
                token!!,
                idFiche,
                object : Callback<DemontageTriphaseResponse> {
                    override fun onResponse(
                        call: Call<DemontageTriphaseResponse>,
                        response: Response<DemontageTriphaseResponse>
                    ) {
                        if (response.code() == 200) {
                            var copy = listeDemo.value?.toMutableList()
                            copy?.add(response.body()!!.fiche!!)
                            listeDemo.value = copy?.toTypedArray()
                        }
                    }

                    override fun onFailure(call: Call<DemontageTriphaseResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
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
}