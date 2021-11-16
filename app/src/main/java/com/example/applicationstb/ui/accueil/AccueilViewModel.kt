package com.example.applicationstb.ui.accueil

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.applicationstb.R
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.connexion.ConnexionDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    fun connection (username: String, password: String) {
        val resp = repository.logUser(username, password, object : Callback<LoginResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    if (resp != null) {
                        token = resp.token

                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }
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
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                                    if ( response.code() == 200 ) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            viewModelScope.launch(Dispatchers.IO){
                                                var ch = repository.getByIdChantierLocalDatabse(resp.fiche!!._id)
                                                if (ch == null) {
                                                    repository.insertChantierLocalDatabase(resp!!.fiche!!)
                                                    Log.i("INFO","${resp!!.fiche!!.vehicule}")
                                                    if (resp!!.fiche!!.vehicule !== null) getVehicule(resp!!.fiche!!.vehicule!!)
                                                    if (!chantiers.contains(resp!!.fiche!!)) chantiers!!.add(resp!!.fiche!!)
                                                    Log.i("INFO","ajout en bdd locale")
                                                } else {
                                                  //  if (!chantiers!!.contains(ch)) chantiers!!.add(ch)
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
                                                                        if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo pompe en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!demontages.contains(demoP)) demontages!!.add(demoP)
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
                                            // fiche demontage Monophase
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 2) {
                                                runBlocking {
                                                    val demoTri = repository.getDemontageMono(token, resp.fiche!!._id, object: Callback<DemontageMonophaseResponse>{
                                                        override fun onResponse(call: Call<DemontageMonophaseResponse>, response: Response<DemontageMonophaseResponse>) {
                                                            if ( response.code() == 200 ) {
                                                                val resp2 = response.body()
                                                                if (resp2 != null) {
                                                                    Log.i("INFO","fiche DemontageMonophase :${resp.fiche!!.numFiche}")
                                                                    //demontages!!.add(resp.fiche!!)
                                                                    viewModelScope.launch(Dispatchers.IO) {
                                                                        var demoM =
                                                                            repository.getByIdDemoMonoLocalDatabse(
                                                                                resp2.fiche!!._id
                                                                            )
                                                                        if (demoM == null) {
                                                                            repository.insertDemoMonoLocalDatabase(
                                                                                resp2!!.fiche!!
                                                                            )
                                                                            if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                            Log.i(
                                                                                "INFO",
                                                                                "ajout demo Monophase en bdd locale"
                                                                            )
                                                                        } else {
                                                                            Log.i(
                                                                                "INFO",
                                                                                "fiche déjà en bdd"
                                                                            )
                                                                           // if (!demontages.contains(demoM)) demontages!!.add(demoM)
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
                                                        override fun onFailure(call: Call<DemontageMonophaseResponse>, t: Throwable) {
                                                            Log.e("Error","erreur ${t.message}")
                                                        }
                                                    })
                                                }
                                            }
                                            // fiche demontage Alternateur
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 3) {
                                                val demoTri = repository.getDemontageAlternateur(token, resp.fiche!!._id, object: Callback<DemontageAlternateurResponse>{
                                                    override fun onResponse(call: Call<DemontageAlternateurResponse>, response: Response<DemontageAlternateurResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                Log.i("INFO","fiche DemontageAlternateur :${resp.fiche!!.numFiche}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoA =
                                                                        repository.getByIdDemoAlterLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoA == null) {
                                                                        repository.insertDemoAlterLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo Alternateur en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!demontages.contains(demoA)) demontages!!.add(demoA)
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
                                                    override fun onFailure(call: Call<DemontageAlternateurResponse>, t: Throwable) {
                                                        Log.e("Error","erreur ${t.message}")
                                                    }
                                                })
                                            }
                                            // fiche demontage Rotor
                                            if (resp.fiche!!.typeFicheDemontage !== null && resp.fiche!!.typeFicheDemontage!! == 4) {
                                                val demoTri = repository.getDemontageRB(token, resp.fiche!!._id, object: Callback<DemontageRotorBobineResponse>{
                                                    override fun onResponse(call: Call<DemontageRotorBobineResponse>, response: Response<DemontageRotorBobineResponse>) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                Log.i("INFO","fiche DemontageRotorBobine :${resp.fiche!!.numFiche}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoRB =
                                                                        repository.getByIdDemoRBLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoRB == null) {
                                                                        repository.insertDemoRBLocalDatabase(
                                                                            resp2!!.fiche!!
                                                                        )
                                                                        if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo RotorBobine en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                      //  if (!demontages.contains(demoRB)) demontages!!.add(demoRB)
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
                                                    override fun onFailure(call: Call<DemontageRotorBobineResponse>, t: Throwable) {
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
                                                                        if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo CC en bdd locale"
                                                                        )
                                                                    } else {
                                                                       // if (!demontages.contains(demoCC)) demontages!!.add(demoCC)
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
                                                                        if (!demontages.contains(resp2!!.fiche!!)) demontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout demo tri en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!demontages.contains(demoT)) demontages!!.add(demoT)
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
                                            if (resp.fiche!!.typeFicheRemontage !== null && resp.fiche!!.typeFicheRemontage!! == 6) {
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
                                                                        if (!remontages.contains(resp2!!.fiche!!)) remontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout remo tri en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!remontages.contains(demoT)) remontages!!.add(demoT as Remontage)
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
                                            if (resp.fiche!!.typeFicheRemontage !== null && resp.fiche!!.typeFicheRemontage!! == 5) {
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
                                                                        if (!remontages.contains(resp2!!.fiche!!)) remontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout remo cc en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!remontages.contains(demoT)) remontages!!.add(demoT as Remontage)
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
                                            if (resp.fiche!!.typeFicheRemontage !== null && (resp.fiche!!.typeFicheRemontage == 3 || resp.fiche!!.typeFicheRemontage == 4 || resp.fiche!!.typeFicheRemontage == 1 || resp.fiche!!.typeFicheRemontage == 2))
                                            {
                                                val remo = repository.getRemontage(token, resp.fiche!!._id, object: Callback<RemontageResponse>{
                                                    override fun onResponse(
                                                        call: Call<RemontageResponse>,
                                                        response: Response<RemontageResponse>
                                                    ) {
                                                        if ( response.code() == 200 ) {
                                                            val resp2 = response.body()
                                                            if (resp2 != null) {
                                                                //Log.i("INFO","fiche RemontageCC :${resp.fiche!!._id} - isoPPSUV : ${resp.fiche!!.isolementPhasePhaseStatorUV}")
                                                                //demontages!!.add(resp.fiche!!)
                                                                viewModelScope.launch(Dispatchers.IO) {
                                                                    var demoT =
                                                                        repository.getByIdRemoLocalDatabse(
                                                                            resp2.fiche!!._id
                                                                        )
                                                                    if (demoT == null) {
                                                                        repository.insertRemoLocalDatabase(
                                                                            resp2.fiche!!
                                                                        )
                                                                        if (!remontages.contains(resp2!!.fiche!!)) remontages!!.add(resp2!!.fiche!!)
                                                                        Log.i(
                                                                            "INFO",
                                                                            "ajout remo en bdd locale"
                                                                        )
                                                                    } else {
                                                                        Log.i(
                                                                            "INFO",
                                                                            "fiche déjà en bdd"
                                                                        )
                                                                       // if (!remontages.contains(resp2!!.fiche!!)) remontages!!.add(resp2!!.fiche!!)
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

                                                    override fun onFailure(
                                                        call: Call<RemontageResponse>,
                                                        t: Throwable
                                                    ) {
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
                                            if (!bobinages.contains(resp!!.fiche!!)) bobinages!!.add(resp.fiche!!)
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
    @RequiresApi(Build.VERSION_CODES.O)
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
    fun getVehicule(id:String){
        var vehicule = repository.getVehiculeById(token!!,id, object: Callback<VehiculesResponse>{
            override fun onResponse(
                call: Call<VehiculesResponse>,
                response: Response<VehiculesResponse>
            ) {
                if ( response.code() == 200){
                    val resp = response.body()
                    Log.i("INFO","vehicule ${resp!!.vehicule!!.nom}")
                    viewModelScope.launch(Dispatchers.IO){
                       var local =  repository.getByIdVehiculesLocalDatabse(resp!!.vehicule!!._id)
                        if (local == null){
                            Log.i("INFO","ajout ${resp!!.vehicule!!.nom} en bdd locale")
                            repository.insertVehiculesLocalDatabase(resp!!.vehicule!!)
                        }
                    }

                }

            }

            override fun onFailure(call: Call<VehiculesResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFiche(loading: CardView){
        if (loading.visibility == View.GONE) loading.visibility = View.VISIBLE
        if (isOnline(context) == true) {
            viewModelScope.launch(Dispatchers.IO) {
                var listCh: List<ChantierEntity> =
                    repository.getAllChantierLocalDatabase()
                //Log.i("INFO", "token : ${user!!.token}")
                Log.i("INFO", "nb de fiches chantier: ${listCh.size}")
                if (listCh.size > 0) {
                    for (fiche in listCh) {
                        var ch = fiche.toChantier()
                        val resp = repository.patchChantier(
                            token!!,
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
                var listb: List<BobinageEntity> =
                    repository.getAllBobinageLocalDatabase()
                //Log.i("INFO", "token : ${user!!.token}")
                Log.i("INFO", "nb de fiches bobinage: ${listb.size}")
                if (listb.size > 0) {
                    for (fiche in listb) {
                        var ch = fiche.toBobinage()
                        val resp = repository.patchBobinage(
                            token!!,
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
                            token!!,
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
                            token!!,
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
                //Log.i("INFO", "token : ${.token}")
                Log.i("INFO", "nb de fiches RemontageTriphase: ${listRT.size}")
                if (listRT.size > 0) {
                    for (fiche in listRT) {
                        var dt = fiche.toRTriphase()
                        val resp = repository.patchRemontageTriphase(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches remontageCC: ${listRCC.size}")
                if (listRCC.size > 0) {
                    for (fiche in listRCC) {
                        var rc = fiche.toRCourantC()
                        val resp = repository.patchRemontageCC(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches remontage: ${listRm.size}")
                if (listRm.size > 0) {
                    for (fiche in listRm) {
                        var rc = fiche.toRemo()
                        val resp = repository.patchRemontage(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches Demontage pompe: ${listDP.size}")
                if (listDP.size > 0) {
                    for (fiche in listDP) {
                        var rc = fiche.toDemoPompe()
                        val resp = repository.patchDemontagePompe(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches Demontage monophase: ${listDM.size}")
                if (listDM.size > 0) {
                    for (fiche in listDM) {
                        var rc = fiche.toMonophase()
                        val resp = repository.patchDemontageMono(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches Demontage Alternateur: ${listDA.size}")
                if (listDA.size > 0) {
                    for (fiche in listDA) {
                        var rc = fiche.toDemontageAlternateur()
                        val resp = repository.patchDemontageAlter(
                            token!!,
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
                //Log.i("INFO", "token : ${token}")
                Log.i("INFO", "nb de fiches Demontage Rotor Bobine: ${listDRB.size}")
                if (listDRB.size > 0) {
                    for (fiche in listDRB) {
                        var rc = fiche.toDemoRotorB()
                        val resp = repository.patchDemontageRotor(
                            token!!,
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
        } else {
            Log.i("INFO","connexion offline")
        }
        if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
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

    // TODO: Implement the ViewModel
}