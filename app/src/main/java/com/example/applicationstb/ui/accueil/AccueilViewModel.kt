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
                            Log.i("INFO","fiche remontage ${fiche.numFiche}")
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
                Log.i("INFO",ch.toChantier().toString())
            }
            var listBobinage = repository.getAllBobinageLocalDatabase()
            Log.i("INFO",listBobinage.size.toString()+" bobinages")
            for( bobinage in listBobinage){
                bobinages.add(bobinage.toBobinage())
            }
        }
    }
    fun toChantier(view: View){
        var action = AccueilDirections.versFicheChantier(chantiers!!.toTypedArray(),token,username)
        Navigation.findNavController(view).navigate(action)
    }
    fun toFicheD(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheD)
    }
    fun toFicheR(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheRemontage)
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