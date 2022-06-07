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
                /* var listRT: List<RemontageTriphaseEntity> =
                    repository.remontageRepository!!.getAllRemontageTriLocalDatabase()
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
                        val resp = repository.remontageRepository!!.patchRemontageTriphase(
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
                                            repository.remontageRepository!!.deleteRemontageTriphaseLocalDatabse(
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
                    repository.remontageRepository!!.getAllRemontageCCLocalDatabase()
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
                        val resp = repository.remontageRepository!!.patchRemontageCC(
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
                                            repository.remontageRepository!!.deleteRemontageCCLocalDatabse(
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
                    repository.remontageRepository!!.getAllRemontageLocalDatabase()
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
                        val resp = repository.remontageRepository!!.patchRemontage(
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
                                            repository.remontageRepository!!.deleteRemontageLocalDatabse(
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
                }*/
                var listD = repository.demontageRepository!!.getAllDemontageLocalDatabase()
                if (listD.size > 0) {
                    for (fiche in listD) {
                        var photos = fiche.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(fiche.numFiche!!)) {
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
                        fiche.photos = photos?.toTypedArray()
                        repository.demontageRepository!!.patchFicheDemontage(
                            user!!.token!!,
                            fiche._id,
                            fiche.toFicheDemontage(),
                            object : Callback<FicheDemontageResponse> {
                                override fun onResponse(
                                    call: Call<FicheDemontageResponse>,
                                    response: Response<FicheDemontageResponse>
                                ) {
                                    if (response.code() == 200) {
                                    } else {
                                        Log.i(
                                            "INFO",
                                            "code : ${response.code()} - erreur : ${response.message()} - body request ${
                                                response.errorBody()!!.charStream().readText()
                                            }"
                                        )
                                    }
                                }

                                override fun onFailure(
                                    call: Call<FicheDemontageResponse>,
                                    t: Throwable
                                ) {
                                    Log.e("Error", "${t.stackTraceToString()}")
                                    Log.e("Error", "erreur ${t.message}")
                                }
                            })
                    }
                }
                var listR = repository.remontageRepository!!.getAllRemontageLocalDatabase()
                if (listR.size > 0) {
                    for (fiche in listR) {
                        var photos = fiche.photos?.toMutableList()
                        var iter = photos?.listIterator()
                        while (iter?.hasNext() == true) {
                            var name = iter.next()
                            if (name.contains(fiche.numFiche!!)) {
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
                        fiche.photos = photos?.toTypedArray()
                        repository.remontageRepository!!.patchRemontage(user?.token!!, fiche._id, fiche.toFicheRemo(), object : Callback<RemontageResponse>{
                            override fun onResponse(
                                call: Call<RemontageResponse>,
                                response: Response<RemontageResponse>
                            ) {
                                if (response.code() == 200) {
                                    val resp = response.body()
                                    if (resp != null) {
                                    }
                                } else {
                                    Log.i(
                                        "INFO",
                                        "code : ${response.code()} - erreur : ${response.message()} - body request ${
                                            response.errorBody()!!.charStream().readText()
                                        }"
                                    )
                                }
                            }

                            override fun onFailure(
                                call: Call<RemontageResponse>,
                                t: Throwable
                            ) {
                                Log.e("Error", "${t.stackTraceToString()}")
                                Log.e("Error", "erreur ${t.message}")
                            }})
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
                        list2.forEach { p2 ->
                            var index =
                                list.indexOfFirst { p1 -> p1.timestamp.isEqual(p2.timestamp) && p1.user == p2.user }
                            if (index < 0) {
                                if (p2.timestamp.isBefore(date)) {
                                    repository.postPointages(token, p2.user, p2.timestamp)
                                    repository.deletePointageLocalDatabse(p2)
                                } else {
                                    tours += 1
                                    var ptn = repository.postPointages(token, p2.user, p2.timestamp)
                                    repository.deletePointageLocalDatabse(p2)
                                    repository.insertPointageDatabase(ptn.body()!!.data)
                                }

                            } else {
                                repository.deletePointageLocalDatabse(p2)
                                if (p2.timestamp.month == LocalDateTime.now().month)
                                    repository.insertPointageDatabase(list[index])

                                list.removeAt(index)
                            }
                        }
                        if (list2.size <= 0 || list.size > 0) list.forEach {
                            if (it.timestamp.month == LocalDateTime.now().month) {
                                repository.insertPointageDatabase(it)
                                tours += 1
                            }
                        }
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