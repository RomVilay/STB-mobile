package com.example.applicationstb.ui.ficheBobinage

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.view.View
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                            bobinage.value = resp.fiche
                            sections.value = bobinage.value!!.sectionsFils
                            schemas.value = bobinage.value!!.schemas
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
        sections.value = list
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }

    fun addSchema(schema: Uri) {
        var list = schemas.value
        list!!.add(schema.toString())
        schemas.value = list
    }

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

    fun fullScreen(view: View, uri: String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
        //Navigation.findNavController(view).navigate(R.id.versFullScreen)
    }

    fun save(context: Context) {
        Log.i("INFO", bobinage.value!!._id)
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
                                Log.i("INFO", "enregistré")
                            }
                        } else {
                            Log.i(
                                "INFO",
                                "code : ${response.code()} - erreur : ${response.message()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                        Log.e("Error", "erreur ${t.message}")
                    }
                })
        } else {
            localSave()
        }

    }
    fun localSave(){
        Log.i("INFO","local save fiche ${bobinage.value!!._id}")
        viewModelScope.launch(Dispatchers.IO){
            var bob = repository.getByIdBobinageLocalDatabse(bobinage.value!!._id)
            if (bob !== null) {
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
                Log.i("INFO","patch ${bobinage.value!!.sectionsFils}")
            } else {
                repository.insertBobinageLocalDatabase(bobinage.value!!)
                Log.i("INFO","insert ${bobinage.value!!._id}")
            }
        }
    }

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