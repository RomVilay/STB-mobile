package com.example.applicationstb.ui.ficheBobinage

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.BobinageResponse
import com.example.applicationstb.repository.ChantierResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.ficheChantier.FicheChantierDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FicheBobinageViewModel(application: Application) : AndroidViewModel(application) {

    var repository = Repository(getApplication<Application>().applicationContext);
    var listeBobinage = arrayListOf<Bobinage>()
    var sections = MutableLiveData<MutableList<Section>>(mutableListOf())
    var schemas = MutableLiveData<MutableList<String>>(mutableListOf())
    var bobinage = MutableLiveData<Bobinage>()
    var schema = MutableLiveData<String>()
    var token: String? = null;
    var username: String? = null;
    var context = getApplication<Application>().applicationContext
    var start = MutableLiveData<Date>()

    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
        }
        /*var i =0
        while (i<10)
        {
            var bobinage = Bobinage (i.toString(),
                    i.toString() ,
                    client,
                    "Dupond M.",
                    3369077543,
                    arrayOf<User>(tech),
                    tech,
                    "Bocsh",
                    "asynchrone triphasé",
                    200 ,
                    300,
                    10,
                    "1",
                    "20Hz",
                    "10A",
                    true
            )
            listeBobinage.add(bobinage);
            i++;
        }*/
        //bobinage.value = listeBobinage[0]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun selectBobinage(id: String) {
        if (isOnline(context)) {
            val resp = repository.getBobinage(token!!, id, object : Callback<BobinageResponse> {
                override fun onResponse(
                    call: Call<BobinageResponse>,
                    response: Response<BobinageResponse>
                ) {
                    if (response.code() == 200) {
                        val resp = response.body()
                        if (resp != null) {
                            Log.i("INFO", "${resp.fiche!!._id}")
                            bobinage.value = resp.fiche!!
                            sections.value = bobinage.value!!.sectionsFils!!
                            schemas.value = mutableListOf()
                        }
                    } else {
                        Log.i("INFO", "code : ${response.code()} - erreur : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            })
        } else {

        }
    }

    fun addSection(nbBrins: Long, diametre: Double) {
        var list = sections.value
        var section = Section(nbBrins, diametre)
        list!!.add(section)
        sections.value = list!!
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }

    /*fun addSchema(schema: Uri) {
        var list = schemas.value!!
        list.add(schema.toString())
        schemas.value = list
    }*/

    fun somme(list: MutableList<Section>): Double {
        var tab = list.map { Math.sqrt(it.diametre) * (Math.PI / 4) * it.nbBrins }
        //Log.i("info", tab.toString())
        return tab.sum()
    }

    fun back(view: View) {
        val action = FicheBobinageDirections.deBobinageverAccueil(token!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }

    fun backFs(view: View) {
        Navigation.findNavController(view).navigate(R.id.de_fscreen_vers_fb)
    }

    fun setSchema(sch: String) {
        schema.value = sch
        Log.i("INFO", sch.toString())
    }

    fun addSchema(index:Int,photo: Uri) {
        var list = bobinage?.value?.photos?.toMutableList()
        if (list != null) {
            list.add(photo.toString())
        }
        bobinage?.value?.photos = list?.toTypedArray()
        schemas.value = list!!
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
        //Navigation.findNavController(view).navigate(R.id.versFullScreen)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun save(context: Context, view:View) {
        Log.i("INFO", "iso: ${bobinage.value!!.isolementUT}")
        if (isOnline(context)) {
            val resp = repository.patchBobinage(
                token!!,
                bobinage.value!!._id,
                bobinage.value!!,
                object : Callback<BobinageResponse> {
                    override fun onResponse(
                        call: Call<BobinageResponse>,
                        response: Response<BobinageResponse>
                    ) {
                        if (response.code() == 200) {
                            val resp = response.body()
                            if (resp != null) {
                                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                                mySnackbar.show()
                                Log.i("INFO", "enregistré")
                            }
                        } else {
                            val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                            mySnackbar.show()
                            Log.i(
                                "INFO",
                                "code : ${response.code()} - erreur : ${response.message()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                        val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                        mySnackbar.show()
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
            //localSave(view)
        } else {
            localSave(view)
        }

    }
    fun localSave(view:View){
        Log.i("INFO","local save fiche ${bobinage.value!!._id}")
        viewModelScope.launch(Dispatchers.IO){
            var bob = repository.getByIdBobinageLocalDatabse(bobinage.value!!._id)
            if (bob !== null) {
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
                Log.i("INFO","patch ${bobinage.value!!.sectionsFils}")
            } else {
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                repository.insertBobinageLocalDatabase(bobinage.value!!)
                Log.i("INFO","insert ${bobinage.value!!._id}")
            }
        }
    }

    fun getTime(){
        Log.i("INFO","duree avant : ${bobinage.value?.dureeTotale}")
        var now = Date()
        if (bobinage.value!!.dureeTotale !== null) {
            bobinage.value!!.dureeTotale =
                (now.time - start.value!!.time ) + bobinage.value!!.dureeTotale!!
        } else {
            bobinage.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun quickSave(){
        Log.i("INFO","quick save")
        getTime()
        Log.i("INFO","duree après : ${bobinage.value?.dureeTotale}")
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdBobinageLocalDatabse(bobinage.value!!._id)
            //Log.i("INFO","${ch}")
            if (ch !== null) {
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
                //Log.i("INFO","patch ${bobinage.value!!._id}")
            } else {
                repository.insertBobinageLocalDatabase(bobinage.value!!)
                //Log.i("INFO","insert ${chantier.value!!._id}")
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
}