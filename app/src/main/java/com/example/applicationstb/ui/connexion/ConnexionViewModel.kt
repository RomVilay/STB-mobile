package com.example.applicationstb.ui.connexion

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.User
import com.example.applicationstb.repository.*
import com.google.android.material.snackbar.Snackbar
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(username: String, psw: String, view: View, loading: CardView){
        if (loading.visibility == View.GONE) loading.visibility = View.VISIBLE
        if (isOnline(context) == true) {
            val resp = repository.logUser(username, psw, object : Callback<LoginResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
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
                                var listRT: List<RemontageTriphaseEntity> =
                                    repository.getAllRemontageTriLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches RemontageTriphase: ${listRT.size}")
                                if (listRT.size > 0) {
                                    for (fiche in listRT) {
                                        var dt = fiche.toRTriphase()
                                        val resp = repository.patchRemontageTriphase(
                                            user!!.token!!,
                                            dt._id,
                                            dt,
                                            object : Callback<RemontageTriphaseResponse> {
                                                override fun onResponse(
                                                    call: Call<RemontageTriphaseResponse>,
                                                    response: Response<RemontageTriphaseResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteRemontageTriphaseLocalDatabse(
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
                                                    call: Call<RemontageTriphaseResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listRCC: List<RemontageCCEntity> =
                                    repository.getAllRemontageCCLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches remontageCC: ${listRCC.size}")
                                if (listRCC.size > 0) {
                                    for (fiche in listRCC) {
                                        var rc = fiche.toRCourantC()
                                        val resp = repository.patchRemontageCC(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<RemontageCCResponse> {
                                                override fun onResponse(
                                                    call: Call<RemontageCCResponse>,
                                                    response: Response<RemontageCCResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteRemontageCCLocalDatabse(
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
                                                    call: Call<RemontageCCResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listRm: List<RemontageEntity> =
                                    repository.getAllRemontageLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches remontage: ${listRm.size}")
                                if (listRm.size > 0) {
                                    for (fiche in listRm) {
                                        var rc = fiche.toRemo()
                                        val resp = repository.patchRemontage(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<RemontageResponse> {
                                                override fun onResponse(
                                                    call: Call<RemontageResponse>,
                                                    response: Response<RemontageResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteRemontageLocalDatabse(
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
                                                    call: Call<RemontageResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listDP: List<DemoPompeEntity> =
                                    repository.getAllDemontagePompeLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches Demontage pompe: ${listDP.size}")
                                if (listDP.size > 0) {
                                    for (fiche in listDP) {
                                        var rc = fiche.toDemoPompe()
                                        val resp = repository.patchDemontagePompe(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<DemontagePompeResponse> {
                                                override fun onResponse(
                                                    call: Call<DemontagePompeResponse>,
                                                    response: Response<DemontagePompeResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontagePompeLocalDatabse(
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
                                                    call: Call<DemontagePompeResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Error", "${t.stackTraceToString()}")
                                                    Log.e("Error", "erreur ${t.message}")
                                                }
                                            })
                                    }
                                }
                                var listDM: List<DemontageMonophaseEntity> =
                                    repository.getAllDemontageMonoLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches Demontage monophase: ${listDM.size}")
                                if (listDM.size > 0) {
                                    for (fiche in listDM) {
                                        var rc = fiche.toMonophase()
                                        val resp = repository.patchDemontageMono(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<DemontageMonophaseResponse> {
                                                override fun onResponse(
                                                    call: Call<DemontageMonophaseResponse>,
                                                    response: Response<DemontageMonophaseResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontageMonoLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "erreur mono code : ${response.code()} - erreur : ${response.message()}"
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
                                    }
                                }
                                var listDA: List<DemontageAlternateurEntity> =
                                    repository.getAllDemontageAlterLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches Demontage Alternateur: ${listDA.size}")
                                if (listDA.size > 0) {
                                    for (fiche in listDA) {
                                        var rc = fiche.toDemontageAlternateur()
                                        val resp = repository.patchDemontageAlter(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<DemontageAlternateurResponse> {
                                                override fun onResponse(
                                                    call: Call<DemontageAlternateurResponse>,
                                                    response: Response<DemontageAlternateurResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontageAlterLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "erreur alter code : ${response.code()} - erreur : ${response.message()}"
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
                                    }
                                }
                                var listDRB: List<DemontageRotorBEntity> =
                                    repository.getAllDemontageRBLocalDatabase()
                                //Log.i("INFO", "token : ${user!!.token}")
                                Log.i("INFO", "nb de fiches Demontage Rotor Bobine: ${listDRB.size}")
                                if (listDRB.size > 0) {
                                    for (fiche in listDRB) {
                                        var rc = fiche.toDemoRotorB()
                                        val resp = repository.patchDemontageRotor(
                                            user!!.token!!,
                                            rc._id,
                                            rc,
                                            object : Callback<DemontageRotorBobineResponse> {
                                                override fun onResponse(
                                                    call: Call<DemontageRotorBobineResponse>,
                                                    response: Response<DemontageRotorBobineResponse>
                                                ) {
                                                    if (response.code() == 200) {
                                                        val resp = response.body()
                                                        if (resp != null) {
                                                            Log.i("INFO", "fiche enregistrée")
                                                        }
                                                        viewModelScope.launch(Dispatchers.IO) {
                                                            repository.deleteDemontageRBLocalDatabse(
                                                                fiche
                                                            )
                                                        }
                                                    } else {
                                                        Log.i(
                                                            "INFO",
                                                            "erreur rotor bobine, code : ${response.code()} - erreur : ${response.message()}"
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
                                    }
                                }
                            }
                            if (action != null) {
                                if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
                                Navigation.findNavController(view).navigate(action)
                            }
                            //toAccueil(view)
                        }
                    }
                    if (response.code() == 401 || response.code() == 404) {
                        if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
                        val mySnackbar = Snackbar.make(view.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),"Veuillez Vérifier votre identifiant et votre mot de passe", 3600)
                        mySnackbar.show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
                    val mySnackbar = Snackbar.make(view.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),"Une erreur réseau est survenue.", 3600)
                    mySnackbar.show()
                    Log.e("Error", "erreur ${t.message}")
                }
            })
        } else {
            Log.i("INFO","connexion offline")
            if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
            var action = ConnexionDirections.versAccueil("", username)
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