package com.example.applicationstb.ui.ficheChantier

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.ChantierResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.repository.VehiculesResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FicheChantierViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context);
    var listeChantiers = arrayListOf<Chantier>()
    var chantier = MutableLiveData<Chantier>()
    var signatures = arrayListOf<Uri?>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    var start = MutableLiveData<Date>()
    init {
         viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
       }
       /* var i =0
        while (i<10)
        {
            var chantier = Chantier (i.toString(),
                i.toString() ,
                client,
                "Dupond M.",
                3369077543,
                listeTechs,
                listeTechs[0],
                Vehicule(1,"trafic",230000),
                "2 rue des paquerettes",
                "Lorem ipsum",
                "Lorem ipsum",
                "Lorem ipsum",

            )
            listeChantiers.add(chantier);
            i++;
        }*/

    }


    fun back(view:View){
        //console.log(signatures)
        val action = FicheChantierDirections.deChantierversAccueil(token!!,username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun addPhoto(index:Int,photo: Uri) {
        photos.value!!.add(photo.toString())
    }
    fun setSchema(sch: String){
        schema.value = sch
        Log.i("INFO", sch.toString())
    }
    fun fullScreen(view: View,uri: String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }
    fun selectChantier(token: String,id: String){
        val resp = repository.getChantier(token, id, object: Callback<ChantierResponse> {
            override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        //Log.i("INFO","${resp.fiche!!.client.enterprise}")
                        chantier.value = resp.fiche!!
                        getVehicule(resp!!.fiche!!.vehicule!!)
                    }
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ChantierResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }
    fun getVehicule(id:String){
        val resp = repository.getVehiculeById(token!!, id, object: Callback<VehiculesResponse> {
            override fun onResponse(call: Call<VehiculesResponse>, response: Response<VehiculesResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                       // Log.i("INFO","${resp.vehicule!!.nom}")
                        var c = chantier.value!!
                        c.vehicule = resp!!.vehicule!!.nom.toString()
                        chantier.value = c
                    }
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<VehiculesResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }
    /*fun setSignature(sign:Bitmap){
        signature.value = sign
    }*/
    fun localGet(){
        viewModelScope.launch(Dispatchers.IO){
            var list = repository.getAllChantierLocalDatabase()
            for (fiche in list){
                Log.i("INFO", "id:${fiche._id}")
            }
        }
    }

    fun getTime(){
        Log.i("INFO","duree avant : ${chantier.value?.dureeTotale}")
        var now = Date()
        if (chantier.value!!.dureeTotale !== null) {
            chantier.value!!.dureeTotale =
                (now.time - start.value!!.time ) + chantier.value!!.dureeTotale!!
        } else {
            chantier.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun quickSave(){
        Log.i("INFO","quick save")
        getTime()
        Log.i("INFO","duree après : ${chantier.value?.dureeTotale}")
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
            //Log.i("INFO","${ch}")
            if (ch !== null) {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
                //Log.i("INFO","patch ${chantier.value!!._id}")
            } else {
                repository.insertChantierLocalDatabase(chantier.value!!)
                //Log.i("INFO","insert ${chantier.value!!._id}")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun localSave(view:View){
        Log.i("INFO","local save")
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
            //Log.i("INFO","${ch}")
            if (ch !== null) {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                //Log.i("INFO","patch ${chantier.value!!._id}")
            } else {
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                repository.insertChantierLocalDatabase(chantier.value!!)
                //Log.i("INFO","insert ${chantier.value!!._id}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(context: Context, view: View){
        if (isOnline(context)){
            val resp = repository.patchChantier(token!!, chantier.value!!._id, chantier!!.value!!, object: Callback<ChantierResponse> {
                override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                    if ( response.code() == 200 ) {
                        val resp = response.body()
                        if (resp != null) {
                            val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                            mySnackbar.show()
                            // Log.i("INFO","${resp.fiche!!.observations}")
                        }
                    } else {
                        val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                        mySnackbar.show()
                        Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ChantierResponse>, t: Throwable) {
                    val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                    mySnackbar.show()
                    Log.e("Error","${t.stackTraceToString()}")
                    Log.e("Error","erreur ${t.message}")
                }
            })
        } else {
            localSave(view)
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
}