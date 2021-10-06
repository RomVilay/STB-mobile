package com.example.applicationstb.ui.FicheDemontage

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import org.json.JSONArray
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstb.repository.BobinageResponse
import com.example.applicationstb.repository.DemontageCCResponse
import com.example.applicationstb.repository.DemontageTriphaseResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.ficheBobinage.FicheBobinageDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
        }
    }
    fun select (id: String){

    }
    fun back(view: View) {
        val action = FicheDemontageDirections.deDemontageversAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun afficherFiche(fiche:Fiche){
        when (fiche){
            is CourantContinu -> Log.i("INFO", "Type Courant Continu")
            is Triphase -> Log.i("Info", "Type triphasé")
            /*is RotorBobine -> Log.i("INFO","Type Rotor Bobine")
            is Monophase -> Log.i("Info","Type monophasé")
            is Alternateur -> Log.i("INFO","type alternateur")
            is DemontagePompe -> Log.i("Info", "type pompe")*/
        }
    }
    fun setCouplage(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.couplage = type
        selection.value = fichemot
    }
    fun setFlasques(etat:Int, position: String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.flasqueAvant = etat
        } else {
            fichemot.flasqueArriere = etat
        }
        selection.value = fichemot
    }
    fun setPRoulements(position:String,etat:Int){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.porteeRAvant = etat
        } else {
            fichemot.porteeRArriere = etat
        }
        selection.value = fichemot
    }
    fun setEtatBA(etat:Boolean){
        var fichemot = selection.value as DemontageMoteur
        fichemot.boutArbre = etat
        selection.value = fichemot
    }
    fun setRoulAr(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeRoulementArriere = type
        selection.value = fichemot
    }
    fun setRoulAv(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeRoulementAvant = type
        selection.value = fichemot
    }
    fun setRefRoul(position:String,ref:String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.refRoulementAvant = ref
        } else {
            fichemot.refRoulementArriere = ref
        }
        selection.value = fichemot
    }
    fun setJointAr(type:Boolean){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeJointArriere = type
        selection.value = fichemot
    }
    fun setJointAv(type:Boolean){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeJointAvant = type
        selection.value = fichemot
    }
    fun setRefJoint(position:String,ref:String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.refJointAvant = ref
        } else {
            fichemot.refJointArriere = ref
        }
        selection.value = fichemot
    }

    fun addPhoto(index:Int,photo: Uri) {
        photos.value!!.add(photo.toString())
    }
    fun setSchema(sch: String){
        schema.value = sch
        Log.i("INFO", sch.toString())
    }
    fun fullScreen(view: View,uri: String) {
        val action = FicheDemontageDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }
    fun retour(view:View){
        var action = FicheDemontageDirections.deDemontageversAccueil("Token","username")
        Navigation.findNavController(view).navigate(action)
    }
    fun enregistrer(view:View){
        if (selection.value!!.typeFicheDemontage == 6)  {
            var t = selection.value!! as Triphase
            if (isOnline(context))   {
                    val resp = repository.patchDemontageTriphase(
                        token!!,
                        selection.value!!._id,
                        t,
                        object : Callback<DemontageTriphaseResponse> {
                            override fun onResponse(
                                call: Call<DemontageTriphaseResponse>,
                                response: Response<DemontageTriphaseResponse>
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

                            override fun onFailure(call: Call<DemontageTriphaseResponse>, t: Throwable) {
                                val mySnackbar = Snackbar.make(view.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"erreur d'enregistrement", 3600)
                                mySnackbar.show()
                                Log.e("Error", "erreur ${t.message} - body request ${
                                    call.request().body().toString()
                                }\"")
                            }
                        })
                } else {
                viewModelScope.launch(Dispatchers.IO){
                    var tri = repository.getByIdDemoTriLocalDatabse(selection.value!!._id)
                    if (tri !== null ) {
                        repository.updateDemoTriLocalDatabse(t.toEntity())
                        val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                        mySnackbar.show()
                        Log.i("INFO", "patch local")
                    } else  {
                        repository.insertDemoTriLocalDatabase(t)
                        val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                        mySnackbar.show()
                        Log.i("INFO", "enregistré local")
                        }
                    }
                }
            }
        if (selection.value!!.typeFicheDemontage == 5)  {
            var c = selection.value!! as CourantContinu
            if (isOnline(context))   {
                val resp = repository.patchDemontageCC(
                    token!!,
                    selection.value!!._id,
                    c,
                    object : Callback<DemontageCCResponse> {
                        override fun onResponse(
                            call: Call<DemontageCCResponse>,
                            response: Response<DemontageCCResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                                    mySnackbar.show()
                                    Log.i("INFO", "demontage enregistré")
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

                        override fun onFailure(call: Call<DemontageCCResponse>, t: Throwable) {
                            Log.e("Error", "erreur ${t.message}")
                            val mySnackbar = Snackbar.make(view,"erreur d'enregistrement", 3600)
                            mySnackbar.show()
                        }
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO){
                    var tri = repository.getByIdDemoCCLocalDatabse(selection.value!!._id)
                    if (tri !== null ) {
                        repository.updateDemoCCLocalDatabse(c.toEntity())
                    } else  {
                        repository.insertDemoCCLocalDatabase(c)
                    }
                    val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                    mySnackbar.show()
                }
            }
        }
    }

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

}