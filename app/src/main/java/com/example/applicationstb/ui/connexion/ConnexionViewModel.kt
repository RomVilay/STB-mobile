package com.example.applicationstb.ui.connexion

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.localdatabase.BobinageEntity
import com.example.applicationstb.localdatabase.ChantierEntity
import com.example.applicationstb.localdatabase.DemontageCCEntity
import com.example.applicationstb.localdatabase.DemontageTriphaseEntity
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.User
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.ficheBobinage.FicheBobinageDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnexionViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    var user:User? = null
    var context = getApplication<Application>().applicationContext
    var repository = Repository(getApplication<Application>().applicationContext);
    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
        }
    }

    fun toAccueil(view: View) {
        //Log.i("INFO","click vers Accueil - ${user?.username}")
        var action = ConnexionDirections.versAccueil(user!!.token!!, user!!.username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun login(username: String,psw: String, view: View){
        if (isOnline(context) == true) {
            val resp = repository.logUser(username, psw, object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.code() == 200) {
                        val resp = response.body()
                        if (resp != null) {
                            user = resp.user
                            user?.token = resp.token
                            // Log.i("INFO","connecté - token ${user?.token} - user  ${user?.username} - resp: ${resp}")
                            //val action = ConnexionDirections.versAccueil(user!!.token!!,user!!.username)
                            val action = user?.let { it1 ->
                                ConnexionDirections.versAccueil(
                                    it1.token!!,
                                    it1.username
                                )
                            }
                            viewModelScope.launch(Dispatchers.IO) {
                                var listCh: List<ChantierEntity> =
                                    repository.getAllChantierLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches chantier: ${listCh.size}")
                                if (listCh.size > 0) {
                                    for (fiche in listCh) {
                                        var ch = fiche.toChantier()
                                        val resp = repository.patchChantier(
                                            user!!.token!!,
                                            ch._id,
                                            ch,
                                            object : Callback<ChantierResponse> {
                                                override fun onResponse(
                                                    call: Call<ChantierResponse>,
                                                    response: Response<ChantierResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteChantierLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "code : ${response.code()} - erreur : ${response.message()}"
                                                        )
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<ChantierResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listb: List< BobinageEntity > =
                                    repository.getAllBobinageLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches bobinage: ${listb.size}")
                                if (listb.size > 0) {
                                    for (fiche in listb) {
                                        var ch = fiche.toBobinage()
                                        val resp = repository.patchBobinage(
                                            user!!.token!!,
                                            ch._id,
                                            ch,
                                            object : Callback<BobinageResponse> {
                                                override fun onResponse(
                                                    call: Call<BobinageResponse>,
                                                    response: Response<BobinageResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteBobinageLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "code : ${response.code()} - erreur : ${response.message()}"
                                                        )
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<BobinageResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listDT: List<DemontageTriphaseEntity> =
                                    repository.getAllDemontageTriLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches DemontageTriphase: ${listDT.size}")
                                if (listDT.size > 0) {
                                    for (fiche in listDT) {
                                        var dt = fiche.toTriphase()
                                        val resp = repository.patchDemontageTriphase(
                                            user!!.token!!,
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
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontageTriphaseLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "code : ${response.code()} - erreur : ${response.message()}"
                                                        )
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
                                    }
                                }
                                var listCC: List<DemontageCCEntity> =
                                    repository.getAllDemontageCCLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches DemontageTriphase: ${listCC.size}")
                                if (listCC.size > 0) {
                                    for (fiche in listCC) {
                                        var dcc = fiche.toCContinu()
                                        val resp = repository.patchDemontageCC(
                                            user!!.token!!,
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
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontageCCLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
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
                                    }
                                }
                            }
                            if (action != null) {
                                Navigation.findNavController(view).navigate(action)
                            }
                            //toAccueil(view)
                        }
                    } else {
                        Log.i("INFO", "code : ${response.code()} - erreur : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            })
        } else {
            Log.i("INFO","connexion offline")
            var action = ConnexionDirections.versAccueil("tech", "pwd")
            Navigation.findNavController(view).navigate(action)
        }
    }
    fun localGet(){
        viewModelScope.launch(Dispatchers.IO){
            var list = repository.getAllChantierLocalDatabase()
            for (fiche in list){
                Log.i("INFO", "id:${fiche._id}")
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