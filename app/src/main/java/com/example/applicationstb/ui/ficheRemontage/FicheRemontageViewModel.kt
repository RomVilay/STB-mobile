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
import kotlinx.coroutines.launch
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
    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
        }
    }

    fun select (i:Int){
        selection.value =listeRemontages[i]
        //selection.value?.let { afficherFiche(it) }
    }
    fun addPhoto(index:Int,photo: Uri) {
        photos.value!!.add(photo.toString())
    }
    fun retour(view: View){
        var action = FicheRemontageDirections.deRemontageverAccueil(token!!,username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun fullScreen(view: View,uri: String) {
        val action = FicheRemontageDirections.deRemoVersFScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }

    fun getTime(){
        Log.i("INFO","duree avant : ${selection.value?.dureeTotale}")
        var now = Date()
        if (selection.value!!.dureeTotale !== null) {
            selection.value!!.dureeTotale =
                (now.time - start.value!!.time ) + selection.value!!.dureeTotale!!
        } else {
            selection.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }
    fun quickSave(){
        Log.i("INFO","quick save")
        getTime()
        viewModelScope.launch(Dispatchers.IO){
            if (selection.value!!.typeFicheRemontage == 1) {
                var tri = repository.getByIdRemoTriLocalDatabse(selection.value!!._id)
                //Log.i("INFO","${ch}")
                if (tri !== null) {
                    repository.updateRemoTriLocalDatabse(tri.toEntity())
                    Log.i("INFO","patch ${selection.value!!._id}")
                } else {
                    var t = selection.value as RemontageTriphase
                    repository.insertRemoTriLocalDatabase(t)
                    Log.i("INFO","insert ${selection.value!!._id}")
                }
            }
            if (selection.value!!.typeFicheRemontage == 2) {
                var remo = repository.getByIdRemoCCLocalDatabse(selection.value!!._id)
                //Log.i("INFO","${ch}")
                if (remo !== null) {
                    repository.updateRemoCCLocalDatabse(remo.toEntity())
                    Log.i("INFO","patch ${selection.value!!._id}")
                } else {
                    var t = selection.value as RemontageCourantC
                    repository.insertRemoCCLocalDatabase(t)
                    Log.i("INFO","insert ${selection.value!!._id} ")
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun enregistrer(view:View){
        if (selection.value!!.typeFicheRemontage == 1)  {
            var t = selection.value!! as RemontageTriphase
            Log.i("Info","v2H : ${t.vitesse2H}")
            if (isOnline(context))   {
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
                                    val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                                    mySnackbar.show()
                                    Log.i("INFO", "enregistré")
                                }
                            } else {
                                val mySnackbar = Snackbar.make(view,"erreur d'enregistrement", 3600)
                                mySnackbar.show()
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${response.message()} - body request ${response.errorBody()!!.charStream().readText()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<RemontageTriphaseResponse>, t: Throwable) {
                            val mySnackbar = Snackbar.make(view.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"erreur d'enregistrement", 3600)
                            mySnackbar.show()
                            Log.e("Error", "erreur ${t.message} - body request ${
                                call.request().body().toString()
                            }\"")
                        }
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO){
                    var tri = repository.getByIdRemoTriLocalDatabse(selection.value!!._id)
                    if (tri !== null ) {
                        repository.updateRemoTriLocalDatabse(t.toEntity())
                        val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                        mySnackbar.show()
                        Log.i("INFO", "patch local")
                    } else  {
                        repository.insertRemoTriLocalDatabase(t)
                        val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                        mySnackbar.show()
                        Log.i("INFO", "enregistré local")
                    }
                }
            }
        }
        if (selection.value!!.typeFicheRemontage == 2)  {
            var c = selection.value!! as RemontageCourantC
            if (isOnline(context))   {
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
                                    val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                                    mySnackbar.show()
                                    Log.i("INFO", "Remontage enregistré")
                                }
                            } else {
                                val mySnackbar = Snackbar.make(view,"erreur d'enregistrement", 3600)
                                mySnackbar.show()
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${response.errorBody()!!.charStream().readText()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<RemontageCCResponse>, t: Throwable) {
                            Log.e("Error", "erreur ${t.message}")
                            val mySnackbar = Snackbar.make(view,"erreur d'enregistrement", 3600)
                            mySnackbar.show()
                        }
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO){
                    var tri = repository.getByIdRemoCCLocalDatabse(selection.value!!._id)
                    if (tri !== null ) {
                        repository.updateRemoCCLocalDatabse(c.toEntity())
                    } else  {
                        repository.insertRemoCCLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService( Context.CONNECTIVITY_SERVICE ) as ConnectivityManager
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