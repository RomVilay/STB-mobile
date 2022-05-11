package com.example.applicationstb.ui.connexion

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.applicationstb.R
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

class ConnexionViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    var user: User? = null
    var context = getApplication<Application>().applicationContext
    val sharedPref =
        getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)
    var repository = Repository(getApplication<Application>().applicationContext);
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    var image = MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
        }
    }

    fun toAccueil(view: View) {
        //Log.i("INFO","click vers Accueil - ${user?.username}")
        var action = ConnexionDirections.versAccueil(user!!.token!!, user!!.username!!)
        Navigation.findNavController(view).navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(username: String, psw: String, view: View, loading: CardView) = runBlocking {
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
                            var editor = sharedPref.edit()
                            editor.putString("userId", resp.user!!._id)
                            editor.putBoolean("connected", true)
                            editor.apply()
                            Log.i(
                                "INFO",
                                "connecté - token ${user?.token} - user  ${user?._id} - resp: ${resp}"
                            )
                            //val action = ConnexionDirections.versAccueil(user!!.token!!,user!!.username)
                            val action = user?.let { it1 ->
                                ConnexionDirections.versAccueil(
                                    it1.token!!,
                                    it1.username
                                )
                            }
                            sendPointage(resp.token!!, resp.user!!._id!!)

                            sendFiche()
                            if (action != null) {
                                if (loading.visibility == View.VISIBLE) loading.visibility =
                                    View.GONE
                                Navigation.findNavController(view).navigate(action)
                            }
                            //toAccueil(view)
                        }
                    }
                    if (response.code() == 401 || response.code() == 404) {
                        if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
                        val mySnackbar = Snackbar.make(
                            view.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),
                            "Veuillez Vérifier votre identifiant et votre mot de passe",
                            3600
                        )
                        mySnackbar.show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
                    val mySnackbar = Snackbar.make(
                        view.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),
                        "Une erreur réseau est survenue.",
                        3600
                    )
                    mySnackbar.show()
                    Log.e("Error", "erreur ${t.message}")
                }
            })
        } else {
            Log.i("INFO", "connexion offline")
            var editor2 = sharedPref.edit()
            editor2.putBoolean("connected", false)
            editor2.apply()
            if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
            var action = ConnexionDirections.versAccueil("", username)
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun localGet() {
        viewModelScope.launch(Dispatchers.IO) {
            var list = repository.getAllChantierLocalDatabase()
            for (fiche in list) {
                Log.i("INFO", "id:${fiche._id}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFiche() = runBlocking {
        if (isOnline(context) == true) {
            viewModelScope.launch(Dispatchers.IO) {
                var job = launch {
                    getNameURI()
                }
                job.join()
                var listCh: List<ChantierEntity> =
                    repository.getAllChantierLocalDatabase()
                Log.i("INFO", "nb de fiches chantier: ${listCh.size}")
                if (listCh.size > 0) {
                    for (fiche in listCh) {
                        var ch = fiche.toChantier()
                        var photos = ch.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(ch.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        ch.photos = photos?.toTypedArray()
                        if (ch.signatureTech !== null && ch.signatureTech!!.contains("sign_")) {
                            getNameURI2 {
                                try {
                                    val dir =
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES + "/test_signatures"
                                        )
                                    val from = File(
                                        dir,
                                        ch.signatureTech!!
                                    )
                                    val to = File(dir, it!!.name!!)
                                    if (from.exists()) from.renameTo(to)
                                    ch.signatureTech = it!!.name!!
                                    sendPhoto2(to, it.url!!)
                                } catch (e: java.lang.Exception) {
                                    Log.e("EXCEPTION", e.message!! + e.cause)
                                }
                            }
                        }
                        if (ch.signatureClient !== null && ch.signatureClient!!.contains("sign_")) {
                            getNameURI2 {
                                try {
                                    val dir =
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES + "/test_signatures"
                                        )
                                    val from = File(
                                        dir,
                                        ch.signatureClient!!
                                    )
                                    val to = File(dir, it!!.name!!)
                                    if (from.exists()) from.renameTo(to)
                                    ch.signatureClient = it!!.name!!
                                    sendPhoto2(to, it.url!!)
                                } catch (e: java.lang.Exception) {
                                    Log.e("EXCEPTION", e.message!! + e.cause)
                                }
                            }
                        }
                        Log.i("INFO", "signature ${ch.signatureTech}")
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
                var listb: List<BobinageEntity> =
                    repository.getAllBobinageLocalDatabase()
                Log.i("INFO", "nb de fiches bobinage: ${listb.size}")
                if (listb.size > 0) {
                    for (fiche in listb) {
                        var ch = fiche.toBobinage()
                        var photos = ch.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(ch.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        ch.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches DemontageTriphase: ${listDT.size}")
                if (listDT.size > 0) {
                    for (fiche in listDT) {
                        var dt = fiche.toTriphase()
                        var photos = dt.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(dt.numFiche!!)) {
                                        Log.i("INFO", "fichier à upload : ${name}")
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dt.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches DemontageCourantContinu: ${listCC.size}")
                if (listCC.size > 0) {
                    for (fiche in listCC) {
                        var dcc = fiche.toCContinu()
                        var photos = dcc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dcc.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        dcc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches RemontageTriphase: ${listRT.size}")
                if (listRT.size > 0) {
                    for (fiche in listRT) {
                        var dt = fiche.toRTriphase()
                        var photos = dt.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(dt.numFiche!!)) {
                                getNameURI2 {
                                    try {
                                        val dir =
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                            )
                                        val from = File(
                                            dir,
                                            name
                                        )
                                        val to =
                                            File(dir, it!!.name!!)
                                        if (from.exists()) {
                                            from.renameTo(to)
                                            sendPhoto2(to, it.url!!)
                                            iter.set(it!!.name!!)
                                        }
                                    } catch (e: java.lang.Exception) {
                                        Log.e("EXCEPTION", e.message!!)
                                    }
                                }
                                delay(200)
                            }
                        }
                        dt.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches remontageCC: ${listRCC.size}")
                if (listRCC.size > 0) {
                    for (fiche in listRCC) {
                        var rc = fiche.toRCourantC()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(rc.numFiche!!)) {
                                getNameURI2 {
                                    try {
                                        val dir =
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                            )
                                        val from = File(
                                            dir,
                                            name
                                        )
                                        val to =
                                            File(dir, it!!.name!!)
                                        if (from.exists()) {
                                            from.renameTo(to)
                                            sendPhoto2(to, it.url!!)
                                            iter.set(it!!.name!!)
                                        }
                                    } catch (e: java.lang.Exception) {
                                        Log.e("EXCEPTION", e.message!!)
                                    }
                                }
                                delay(200)
                            }
                        }
                        rc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches remontage: ${listRm.size}")
                if (listRm.size > 0) {
                    for (fiche in listRm) {
                        var rc = fiche.toRemo()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(rc.numFiche!!)) {
                                getNameURI2 {
                                    try {
                                        val dir =
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                            )
                                        val from = File(
                                            dir,
                                            name
                                        )
                                        val to =
                                            File(dir, it!!.name!!)
                                        if (from.exists()) {
                                            from.renameTo(to)
                                            sendPhoto2(to, it.url!!)
                                            iter.set(it!!.name!!)
                                        }
                                    } catch (e: java.lang.Exception) {
                                        Log.e("EXCEPTION", e.message!!)
                                    }
                                }
                                delay(200)
                            }
                        }
                        rc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches Demontage pompe: ${listDP.size}")
                if (listDP.size > 0) {
                    for (fiche in listDP) {
                        var rc = fiche.toDemoPompe()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(rc.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        rc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches Demontage monophase: ${listDM.size}")
                if (listDM.size > 0) {
                    for (fiche in listDM) {
                        var rc = fiche.toMonophase()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(rc.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        rc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches Demontage Alternateur: ${listDA.size}")
                if (listDA.size > 0) {
                    for (fiche in listDA) {
                        var rc = fiche.toDemontageAlternateur()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(rc.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }
                        rc.photos = photos?.toTypedArray()
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
                Log.i("INFO", "nb de fiches Demontage Rotor Bobine: ${listDRB.size}")
                if (listDRB.size > 0) {
                    for (fiche in listDRB) {
                        var rc = fiche.toDemoRotorB()
                        var photos = rc.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(rc.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        rc.photos = photos?.toTypedArray()
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
                var listDMP: List<DemontageMotopompeEntity> =
                    repository.getAllDemontageMotopompeLocalDatabase()
                if (listDMP.size > 0) {
                    for (fiche in listDMP) {
                        var dmp = fiche.toMotoPompe()
                        var photos = dmp.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                runBlocking {
                                    if (name.contains(dmp.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dmp.photos = photos?.toTypedArray()
                        val resp = repository.patchDemontageMotopompe(
                            user!!.token!!,
                            dmp._id,
                            dmp,
                            object : Callback<DemontageMotopompeResponse> {
                                override fun onResponse(
                                    call: Call<DemontageMotopompeResponse>,
                                    response: Response<DemontageMotopompeResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                        viewModelScope.launch(Dispatchers.IO) {
                                            repository.deleteDemontageMotoPompeLocalDatabse(
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
                                    call: Call<DemontageMotopompeResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
                Log.i("INFO", "nb de fiches Demontage Motopompe: ${listDMP.size}")
                var listDR: List<DemontageReducteurEntity> =
                    repository.getAllDemontageReducteurLocalDatabase()
                if (listDR.size > 0) {
                    for (fiche in listDR) {
                        var dr = fiche.toReducteur()
                        var photos = dr.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dr.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dr.photos = photos?.toTypedArray()
                        val resp = repository.patchDemontageReducteur(
                            user!!.token!!,
                            dr._id,
                            dr,
                            object : Callback<DemontageReducteurResponse> {
                                override fun onResponse(
                                    call: Call<DemontageReducteurResponse>,
                                    response: Response<DemontageReducteurResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                        viewModelScope.launch(Dispatchers.IO) {
                                            repository.deleteDemontageReducteurLocalDatabse(
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
                                    call: Call<DemontageReducteurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
                Log.i("INFO", "nb de fiches Demontage Reducteur: ${listDR.size}")
                var listDMR: List<DemontageMotoreducteurEntity> =
                    repository.getAllDemontageMotoreducteurLocalDatabase()
                if (listDMR.size > 0) {
                    for (fiche in listDMR) {
                        var dmr = fiche.toDemontageMotoreducteur()
                        var photos = dmr.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name !== "") {
                                //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                runBlocking {
                                    if (name.contains(dmr.numFiche!!)) {
                                        getNameURI2 {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to =
                                                    File(dir, it!!.name!!)
                                                if (from.exists()) {
                                                    from.renameTo(to)
                                                    sendPhoto2(to, it.url!!)
                                                    iter.set(it!!.name!!)
                                                }

                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                        delay(200)
                                    }
                                }
                            }
                            if (name == "") {
                                iter.remove()
                            }
                        }

                        //Log.i("INFO",photos?.filter { it !== "" }?.size.toString())
                        dmr.photos = photos?.toTypedArray()
                        val resp = repository.patchDemontageMotoreducteur(
                            user!!.token!!,
                            dmr._id,
                            dmr,
                            object : Callback<DemontageMotoreducteurResponse> {
                                override fun onResponse(
                                    call: Call<DemontageMotoreducteurResponse>,
                                    response: Response<DemontageMotoreducteurResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                        viewModelScope.launch(Dispatchers.IO) {
                                            repository.deleteDemontageMotoreducteurLocalDatabse(
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
                                    call: Call<DemontageMotoreducteurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
                Log.i("INFO", "nb de fiches Demontage Motoreducteur: ${listDMR.size}")
                var listRMP: List<RemontageMotopompeEntity> =
                    repository.getAllRemontageMotopompeLocalDatabase()
                Log.i("INFO", "nb de fiches remontage Motopompe: ${listRMP.size}")
                if (listRMP.size > 0) {
                    for (fiche in listRMP) {
                        var rmp = fiche.toRemontageMotopompe()
                        var photos = rmp.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(rmp.numFiche!!)) {
                                getNameURI2 {
                                    try {
                                        val dir =
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                            )
                                        val from = File(
                                            dir,
                                            name
                                        )
                                        val to =
                                            File(dir, it!!.name!!)
                                        if (from.exists()) {
                                            from.renameTo(to)
                                            sendPhoto2(to, it.url!!)
                                            iter.set(it!!.name!!)
                                        }
                                    } catch (e: java.lang.Exception) {
                                        Log.e("EXCEPTION", e.message!!)
                                    }
                                }
                                delay(200)
                            }
                        }
                        rmp.photos = photos?.toTypedArray()
                        val resp = repository.patchRemontageMotopompe(
                            user!!.token!!,
                            rmp._id,
                            rmp,
                            object : Callback<RemontageMotopompeResponse> {
                                override fun onResponse(
                                    call: Call<RemontageMotopompeResponse>,
                                    response: Response<RemontageMotopompeResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                        viewModelScope.launch(Dispatchers.IO) {
                                            repository.deleteRemontageMotoPompeLocalDatabse(
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
                                    call: Call<RemontageMotopompeResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
                var listRMR: List<RemontageMotoreducteurEntity> =
                    repository.getAllRemontageMotoreducteurLocalDatabase()
                Log.i("INFO", "nb de fiches remontage: ${listRMR.size}")
                if (listRMR.size > 0) {
                    for (fiche in listRMR) {
                        var rmp = fiche.toRemontageMotoreducteur()
                        var photos = rmp.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(rmp.numFiche!!)) {
                                getNameURI2 {
                                    try {
                                        val dir =
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                            )
                                        val from = File(
                                            dir,
                                            name
                                        )
                                        val to =
                                            File(dir, it!!.name!!)
                                        if (from.exists()) {
                                            from.renameTo(to)
                                            sendPhoto2(to, it.url!!)
                                            iter.set(it!!.name!!)
                                        }

                                    } catch (e: java.lang.Exception) {
                                        Log.e("EXCEPTION", e.message!!)
                                    }
                                }
                                delay(200)
                            }
                        }
                        rmp.photos = photos?.toTypedArray()
                        val resp = repository.patchRemontageMotoreducteur(
                            user!!.token!!,
                            rmp._id,
                            rmp,
                            object : Callback<RemontageMotoreducteurResponse> {
                                override fun onResponse(
                                    call: Call<RemontageMotoreducteurResponse>,
                                    response: Response<RemontageMotoreducteurResponse>
                                ) {
                                    if (response.code() == 200) {
                                        val resp = response.body()
                                        if (resp != null) {
                                            Log.i("INFO", "fiche enregistrée")
                                        }
                                        viewModelScope.launch(Dispatchers.IO) {
                                            repository.deleteRemontageMotoreducteurLocalDatabse(
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
                                    call: Call<RemontageMotoreducteurResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
            }
        }
    }

    fun sendPointage(token: String, userId: String) {
        var date = ZonedDateTime.of(
            LocalDateTime.now().withDayOfMonth(1),
            ZoneOffset.of(SimpleDateFormat("Z").format(Date()))
        )
        repository.getPointages(token, userId, object : Callback<PointagesResponse> {
            override fun onResponse(
                call: Call<PointagesResponse>,
                response: Response<PointagesResponse>
            ) {
                if (response.code() == 200) {
                    var list = response.body()!!.data!!.map { it.toPointage() }.toMutableList()
                    viewModelScope.launch(Dispatchers.IO) {
                        var list2 = repository.getAllPointageLocalDatabase().toMutableList()
                        var tours = 0;
                        list2.forEach { p2 -> //pointage issu de la bdd
                         //   Log.i("info", "pointage ${p2.timestamp}  - ${list[0].timestamp} position ${list.indexOfFirst { p1 -> p1.timestamp.isEqual(p2.timestamp) }}")
                            var index = list.indexOfFirst { p1 -> p1.timestamp.isEqual(p2.timestamp) && p1.user == p2.user }
                            if (index < 0) {
                                    if (p2.timestamp.isBefore(date)) {
                                        Log.i("info", "pointage  ${p2._id} est du mois précédent, pas d'ajout local")
                                         repository.postPointages(token, p2.user, p2.timestamp)
                                         repository.deletePointageLocalDatabse(p2)
                                    } else {
                                        Log.i("info", "pointage  ${p2._id} est envoyé au serveur, remplacé en bdd")
                                        tours+=1
                                         var ptn = repository.postPointages(token, p2.user, p2.timestamp)
                                         repository.deletePointageLocalDatabse(p2)
                                        repository.insertPointageDatabase(ptn.body()!!.data)
                                    }

                            } else {
                                    Log.i("info", "pointage ${list[index]._id} et ${p2._id} existent, p1 remplace")
                                     repository.deletePointageLocalDatabse(p2)
                                     repository.insertPointageDatabase(list[index])
                                     list.removeAt(index)
                            }
                        }
                        if (list2.size <= 0 || list.size > 0) list.forEach {
                            repository.insertPointageDatabase(it)
                            tours+=1
                        }
                        Log.i("infos","pointages serveurs restants ${list.size} - pointages bdd ${list2.size} - pointages crées ${tours}")
                    }
                }
            }

            override fun onFailure(call: Call<PointagesResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
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

    suspend fun getNameURI() = runBlocking {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resp1 = repository.getURLToUploadPhoto(user?.token!!)
            withContext(Dispatchers.Main) {
                if (resp1.isSuccessful) {
                    imageName.postValue(resp1.body())
                    Log.i("info", "image name ${imageName.value?.name}")
                } else {
                    exceptionHandler
                }
            }
        }
    }

    suspend fun getNameURI2(callback: (URLPhotoResponse2?) -> Unit) {
        val resp1 = repository.getURLToUploadPhoto(user?.token!!)
        withContext(Dispatchers.Main) {
            if (resp1.isSuccessful) {
                callback(resp1.body()!!)
            } else {
                exceptionHandler
            }
        }

    }

    fun sendPhoto(photo: File) {
        var s =
            imageName.value!!.url!!.removePrefix("https://minio.stb.dev.alf-environnement.net/images/${imageName.value!!.name!!}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab[1] = tab[1].replace("%2F", "/")
        repositoryPhoto.uploadPhoto(
            user?.token!!,
            imageName.value!!.name!!,
            tab.toList(),
            photo,
            object : Callback<URLPhotoResponse> {
                override fun onResponse(
                    call: Call<URLPhotoResponse>,
                    response: Response<URLPhotoResponse>
                ) {
                    //Log.i("INFO", "envoyé ${call.request().url()}")
                }

                override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                    Log.i("INFO", t.message!!)
                }
            })
    }

    fun sendPhoto2(photo: File, url: String) {
        var s =
            url.removePrefix("https://minio.stb.dev.alf-environnement.net/images/${photo.name}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab[1] = tab[1].replace("%2F", "/")
        lateinit var compressedPicture: File
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var job = launch { compressedPicture = Compressor.compress(context, photo) }
                job.join()
            } catch (e: Throwable) {
                Log.e("error", e.message!!)
            }
            repositoryPhoto.uploadPhoto(
                user?.token!!,
                photo.name,
                tab.toList(),
                compressedPicture,
                object : Callback<URLPhotoResponse> {
                    override fun onResponse(
                        call: Call<URLPhotoResponse>,
                        response: Response<URLPhotoResponse>
                    ) {
                        //Log.i("INFO", "envoyé ${call.request().url()}")
                    }

                    override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                        Log.i("INFO", t.message!!)
                    }
                })
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage}")
    }

    private fun saveImage(image: Bitmap, name: String): String? {
        Log.i("INFO", "start")
        var savedImagePath: String? = null
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_pictures/"
        )
        Log.i("INFO", storageDir.exists().toString())
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, name)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}