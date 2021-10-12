package com.example.applicationstb.ui.accueil

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.connexion.ConnexionDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccueilViewModel(application: Application) : AndroidViewModel(application) {
    var repository = Repository(getApplication<Application>().applicationContext);
    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
        }
    }
    var token: String? = null
    var username: String? = null
    var fiches: Array<Fiche>? = null
    var context = getApplication<Application>().applicationContext
    var chantiers: MutableList<Chantier> = mutableListOf();
    var bobinages: MutableList<Bobinage> = mutableListOf();
    var demontages: MutableList<DemontageMoteur> = mutableListOf();
    var remontages: MutableList<Remontage> = mutableListOf();
    fun listeFiches(token: String, userid: String){
        val resp = repository.getFichesUser(token, userid, object: Callback<FichesResponse> {
            override fun onResponse(call: Call<FichesResponse>, response: Response<FichesResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    Log.i("INFO","${resp!!.fiches!!.size}")
                    if (resp != null) {
                       fiches = resp.fiches
                    }
                    var nbCh = 0;
                    var nbBo = 0;
                    for (fiche in resp!!.fiches!! ){
                        if (fiche.type == 1L ){
                            val resp = repository.getChantier(token, fiche._id, object: Callback<ChantierResponse> {
                                override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                                    if ( response.code() == 200 ) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            viewModelScope.launch(Dispatchers.IO){
                                                var ch = repository.getByIdChantierLocalDatabse(resp.fiche!!._id)
                                                if (ch == null) {
                                                    repository.insertChantierLocalDatabase(resp!!.fiche!!)
                                                    chantiers!!.add(resp!!.fiche!!)
                                                    Log.i("INFO","ajout en bdd locale")
                                                } else {
                                                    chantiers!!.add(ch)
                                                }
                                                //Log.i("INFO","fiche chantier :${ch!!._id} - matériel : ${ch!!.materiel}")
                                            }
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
                        if (fiche.type == 2L){
                            Log.i("INFO","fiche demontage ${fiche.numFiche}")
                            val resp = repository.getDemontage(token, fiche._id, object: Callback<DemontageResponse> {
                                override fun onResponse(call: Call<DemontageResponse>, response: Response<DemontageResponse>) {
                                    if ( response.code() == 200 ) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            // fiche demontage pompe
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 1) {
                                                val demoTri = repository.getDemontagePompe(token, resp.fiche!!._id, object: Callback<DemontagePompeResponse>{
                                                    override fun onResponse(call: Call<DemontagePompeResponse>, response: Response<DemontagePompeResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                Log.i("INFO","fiche DemontagePompe :${resp.fiche!!.numFiche}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoP =
                                                                        repository.getByIdDemoPompeLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoP == null) {
                                                                        repository.insertDemoPompeLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo pompe en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                        demontages!!.add(demoP)
                                                                    }
                                                                }
                                                            } else {
                                                                Log.i(
                                                                    "INFO",
                                                                    "code : ${response.code()} - erreur : ${response.message()}"
                                                                )
                                                            }
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<DemontagePompeResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                            //fiche demontage triphase
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 6) {
                                                val demoTri = repository.getDemontageTriphase(token, resp.fiche!!._id, object: Callback<DemontageTriphaseResponse>{
                                                    override fun onResponse(call: Call<DemontageTriphaseResponse>, response: Response<DemontageTriphaseResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                //Log.i("INFO","fiche DemontageTriphase :${resp.fiche!!._id} - isoPPSUV : ${resp.fiche!!.isolementPhasePhaseStatorUV}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoT =
                                                                        repository.getByIdDemoTriLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoT == null) {
                                                                        repository.insertDemoTriLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo tri en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                        demontages!!.add(demoT)
                                                                    }
                                                                }
                                                            } else {
                                                                Log.i(
                                                                    "INFO",
                                                                    "code : ${response.code()} - erreur : ${response.message()}"
                                                                )
                                                            }
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<DemontageTriphaseResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                            // fiche demontage Courant Continu
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 5) {
                                                val demoTri = repository.getDemontageCC(token, resp.fiche!!._id, object: Callback<DemontageCCResponse>{
                                                    override fun onResponse(call: Call<DemontageCCResponse>, response: Response<DemontageCCResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoCC =
                                                                        repository.getByIdDemoCCLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoCC == null) {
                                                                        repository.insertDemoCCLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo CC en bdd locale"
                                                                        )
                                                                    } else {
                                                                        demontages!!.add(demoCC)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<DemontageCCResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                           /* Demontages!!.add(resp.fiche!!)
                                            viewModelScope.launch(Dispatchers.IO){
                                                var b = repository.getByIdDemontageLocalDatabse(resp.fiche!!._id)
                                                if (b == null) {
                                                    repository.insertDemontageLocalDatabase(resp!!.fiche!!)
                                                    Log.i("INFO","ajout en bdd locale")
                                                }*/

                                         //   }
                                        }
                                    } else {
                                        Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                                    }
                                }
                                override fun onFailure(call: Call<DemontageResponse>, t: Throwable) {
                                    Log.e("Error","erreur ${t.message}")
                                }
                            })
                        }
                        if (fiche.type == 3L){
                            Log.i("INFO","fiche remontage ${fiche.numFiche} ")
                            var remo = repository.getRemontage(token, fiche._id, object: Callback<RemontageResponse> {
                                override fun onResponse(call: Call<RemontageResponse>, response: Response<RemontageResponse>){
                                    if ( response.code() == 200 ) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            if (resp.fiche!!.typeFicheRemontage !== null && resp.fiche!!.typeFicheRemontage!! == 1) {
                                                val demoTri = repository.getRemontageTriphase(token, resp.fiche!!._id, object: Callback<RemontageTriphaseResponse>{
                                                    override fun onResponse(call: Call<RemontageTriphaseResponse>, response: Response<RemontageTriphaseResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                //Log.i("INFO","fiche RemontageTriphase :${resp.fiche!!._id} - isoPPSUV : ${resp.fiche!!.isolementPhasePhaseStatorUV}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoT =
                                                                        repository.getByIdRemoTriLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoT == null) {
                                                                        repository.insertRemoTriLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        remontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout remo tri en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                        remontages!!.add(demoT as Remontage)
                                                                    }
                                                                }
                                                            } else {
                                                                Log.i(
                                                                    "INFO",
                                                                    "code : ${response.code()} - erreur : ${response.message()}"
                                                                )
                                                            }
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<RemontageTriphaseResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                            if (resp.fiche!!.typeFicheRemontage !== null && resp.fiche!!.typeFicheRemontage!! == 2) {
                                                val demoTri = repository.getRemontageCC(token, resp.fiche!!._id, object: Callback<RemontageCCResponse >{
                                                    override fun onResponse(call: Call<RemontageCCResponse>, response: Response<RemontageCCResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                //Log.i("INFO","fiche RemontageCC :${resp.fiche!!._id} - isoPPSUV : ${resp.fiche!!.isolementPhasePhaseStatorUV}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoT =
                                                                        repository.getByIdRemoCCLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoT == null) {
                                                                        repository.insertRemoCCLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        remontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout remo cc en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                        remontages!!.add(demoT as Remontage)
                                                                    }
                                                                }
                                                            } else {
                                                                Log.i(
                                                                    "INFO",
                                                                    "code : ${response.code()} - erreur : ${response.message()}"
                                                                )
                                                            }
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<RemontageCCResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<RemontageResponse>, t: Throwable) {
                                    Log.e("Error","erreur ${t.message}")
                                }
                            })
                        }
                        if ( fiche.type == 4L ){
                            val resp = repository.getBobinage(token, fiche._id, object: Callback<BobinageResponse> {
                                override fun onResponse(call: Call<BobinageResponse>, response: Response<BobinageResponse>) {
                                    if ( response.code() == 200 ) {
                                        val resp = response.body()
                                        if (resp != null) {
                                           // Log.i("INFO","fiche bobinage :${resp.fiche!!._id} - spires : ${resp.fiche!!.nbSpires}")
                                            bobinages!!.add(resp.fiche!!)
                                            viewModelScope.launch(Dispatchers.IO){
                                                var b = repository.getByIdBobinageLocalDatabse(resp.fiche!!._id)
                                                if (b == null) {
                                                    repository.insertBobinageLocalDatabase(resp!!.fiche!!)
                                                    Log.i("INFO","ajout en bdd locale")
                                                }
                                                //Log.i("INFO","fiche bobinage :${b!!._id} - spires : ${b!!.nbSpires}")
                                            }
                                        }
                                    } else {
                                        Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                                    }
                                }
                                override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                                    Log.e("Error","erreur ${t.message}")
                                }
                            })
                        }
                    }
                    Log.i("INFO"," nb de chantier :${chantiers.size} - nb bobinages : ${bobinages.size}")
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<FichesResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }
    fun listeFicheLocal(){
        viewModelScope.launch(Dispatchers.IO){
            var listChantier = repository.getAllChantierLocalDatabase()
            for (ch in listChantier){
                chantiers.add(ch.toChantier())
            }
            var listBobinage = repository.getAllBobinageLocalDatabase()
            for( bobinage in listBobinage){
                bobinages.add(bobinage.toBobinage())
            }
            var listDT = repository.getAllDemontageTriLocalDatabase()
            for (dt in listDT){
                demontages.add(dt.toTriphase())
            }
            var listDCC = repository.getAllDemontageCCLocalDatabase()
            for (dcc in listDCC){
                demontages.add(dcc.toCContinu())
            }
            var listRT = repository.getAllRemontageTriLocalDatabase()
            for (rt in listRT){
                remontages.add(rt.toRTriphase())
            }
            var listRCC = repository.getAllRemontageCCLocalDatabase()
            for (rcc in listRCC){
                remontages.add(rcc.toRCourantC())
            }
        }
    }
    fun toChantier(view: View){
        var action = AccueilDirections.versFicheChantier(chantiers!!.toTypedArray(),token,username)
        Navigation.findNavController(view).navigate(action)
    }
    fun toFicheD(view: View){
        var action = AccueilDirections.versFicheD(token!!,username!!,demontages!!.toTypedArray())
        Navigation.findNavController(view).navigate(action)
    }
    fun toFicheR(view: View){
        var action = AccueilDirections.versFicheRemontage(token!!,username!!,remontages!!.toTypedArray())
        Navigation.findNavController(view).navigate(action)
    }
    fun toBobinage(view: View){
            var action = AccueilDirections.versFicheBobinage(bobinages!!.toTypedArray(),token,username)
            Navigation.findNavController(view).navigate(action)
    }
    fun toDeconnexion(view: View){
        Navigation.findNavController(view).navigate(R.id.versConnexion)
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

    // TODO: Implement the ViewModel
}