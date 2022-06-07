package com.example.applicationstb.ui.accueil

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.Glide
import com.example.applicationstb.R
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.connexion.ConnexionDirections
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class AccueilViewModel(application: Application) : AndroidViewModel(application) {
    var repository = Repository(getApplication<Application>().applicationContext);
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
        }
    }

    val sharedPref =
        getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)
    var token =  MutableLiveData<String>()
    var username: String? = null
    var fiches: Array<Fiche>? = null
    var context = getApplication<Application>().applicationContext
    var chantiers: MutableList<Chantier> = mutableListOf();
    var bobinages: MutableList<Bobinage> = mutableListOf();
    var image = MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()
    var tracking = MutableLiveData<Boolean>(false)

    fun connection(username: String, password: String) {
        val resp = repository.logUser(username, password, object : Callback<LoginResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    if (resp != null) {
                        token.postValue(resp.token)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
    }

    fun listeFiches(token: String, userid: String) {
            viewModelScope.launch(Dispatchers.IO) {
                for (i in chantiers) {
                    repository.deleteChantierLocalDatabse(i.toEntity())
                }
                chantiers.clear()
                for (i in bobinages) {
                    repository.deleteBobinageLocalDatabse(i.toEntity())
                }
                bobinages.clear()
                var demontages2 = repository.demontageRepository!!.getAllDemontageLocalDatabase()
                for (d in demontages2) {
                    repository.demontageRepository!!.deleteDemontageLocalDatabse(d)
                }
                var remontages2 = repository.remontageRepository!!.getAllRemontageLocalDatabase()
                for (r in remontages2) {
                    repository.remontageRepository!!.deleteRemontageLocalDatabse(r)
                }
                isTracking()
            }
            val resp = repository.getFichesUser(token, userid, object : Callback<FichesResponse> {
                override fun onResponse(
                    call: Call<FichesResponse>,
                    response: Response<FichesResponse>
                ) {
                    Log.i("INFO", "liste fiche")
                    if (response.code() == 200) {
                        val resp = response.body()
                        if (resp != null) {
                            fiches = resp.data
                            Log.i("info", "nb fiches ${resp.data!!.size}")
                        }
                        for (fiche in resp!!.data!!) {
                            if (fiche.type == 1L) {
                                val resp = repository.getChantier(
                                    token,
                                    fiche._id,
                                    object : Callback<ChantierResponse> {
                                        @RequiresApi(Build.VERSION_CODES.O)
                                        override fun onResponse(
                                            call: Call<ChantierResponse>,
                                            response: Response<ChantierResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                val resp = response.body()
                                                if (resp != null) {
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var photos =
                                                            resp.data?.photos?.toMutableList()
                                                        var iter = photos?.listIterator()
                                                        while (iter?.hasNext() == true) {
                                                            getPhotoFile(iter.next().toString())
                                                        }
                                                        resp.data?.photos = photos?.toTypedArray()
                                                        var ch =
                                                            repository.getByIdChantierLocalDatabse(
                                                                resp.data!!._id
                                                            )
                                                        if (ch == null) {
                                                            repository.insertChantierLocalDatabase(
                                                                resp!!.data!!
                                                            )
                                                            if (resp!!.data!!.vehicule !== null) getVehicule(
                                                                resp!!.data!!.vehicule!!
                                                            )
                                                            if (chantiers.indexOf(resp.data!!) == -1) chantiers!!.add(
                                                                resp!!.data!!
                                                            )
                                                            Log.i("INFO", "ajout en bdd locale")
                                                        } else {
                                                            //  if (!chantiers!!.contains(ch)) chantiers!!.add(ch)
                                                        }
                                                        //Log.i("INFO","data chantier :${ch!!._id} - matériel : ${ch!!.materiel}")
                                                    }
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
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })
                            }
                            if (fiche.type == 2L) {
                                repository.demontageRepository!!.getFicheDemontage(token, fiche._id,  object : Callback<FicheDemontageResponse> {
                                    override fun onResponse(
                                        call: Call<FicheDemontageResponse>,
                                        response: Response<FicheDemontageResponse>
                                    ) {
                                        if (response.code() == 200) {
                                            val resp = response.body()!!
                                            viewModelScope.launch(Dispatchers.IO) {
                                                var index = repository.demontageRepository!!.getByIdDemontageLocalDatabse(resp.data!!._id)
                                                if (index !== null){
                                                    repository.demontageRepository!!.updateDemontageLocalDatabse(resp.data!!.toEntity())
                                                } else {
                                                    repository.demontageRepository!!.insertDemontageLocalDatabase(resp.data!!)
                                                    Log.i("info","fiche demontage: ${resp.data!!._id} ajout BDD")
                                                }
                                            }
                                        } else {
                                            Log.i(
                                                "INFO",
                                                "code : ${response.code()} - erreur : ${response.message()}"
                                            )
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<FicheDemontageResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e("Error", "erreur ${t.message}")
                                    }
                                } )
                            }
                            if (fiche.type == 3L) {
                                Log.i("INFO", "fiche remontage ${fiche.numFiche} ")
                                var remo = repository.remontageRepository!!.getRemontage(
                                    token,
                                    fiche._id,
                                    object : Callback<RemontageResponse> {
                                        override fun onResponse(
                                            call: Call<RemontageResponse>,
                                            response: Response<RemontageResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                val resp = response.body()
                                                if (resp != null) {
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var index = repository.remontageRepository!!.getByIdRemoLocalDatabse(resp.data!!._id)
                                                        if (index !== null){
                                                            repository.remontageRepository!!.updateRemoLocalDatabse (resp.data!!.toEntity())
                                                        } else {
                                                            repository.remontageRepository!!.insertRemoLocalDatabase(resp.data!!)
                                                            Log.i("info","fiche demontage: ${resp.data!!._id} ajout BDD")
                                                        }
                                                    }
                                                }
                                            }   else {
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
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })
                            }
                            if (fiche.type == 4L) {
                                val resp = repository.getBobinage(
                                    token,
                                    fiche._id,
                                    object : Callback<BobinageResponse> {
                                        override fun onResponse(
                                            call: Call<BobinageResponse>,
                                            response: Response<BobinageResponse>
                                        ) {
                                            if (response.code() == 200) {
                                                val resp = response.body()
                                                if (resp != null) {
                                                    Log.i(
                                                        "INFO",
                                                        "pas dans la liste" + (bobinages.indexOf(
                                                            resp.data!!
                                                        ) == -1).toString()
                                                    )
                                                    viewModelScope.launch(Dispatchers.IO) {
                                                        var photos =
                                                            resp.data?.photos?.toMutableList()
                                                        var iter = photos?.listIterator()
                                                        while (iter?.hasNext() == true) {
                                                            getPhotoFile(iter.next().toString())
                                                        }
                                                        resp.data?.photos = photos?.toTypedArray()
                                                        var b =
                                                            repository.getByIdBobinageLocalDatabse(
                                                                resp.data!!._id
                                                            )
                                                        Log.i(
                                                            "INFO",
                                                            "présence bdd : " + (b == null).toString()
                                                        )
                                                        if (b == null) {
                                                            repository.insertBobinageLocalDatabase(
                                                                resp!!.data!!
                                                            )
                                                            if (bobinages.indexOf(resp.data!!) == -1) bobinages!!.add(
                                                                resp.data!!
                                                            )
                                                            Log.i("INFO", "ajout en bdd locale")
                                                        }
                                                        //Log.i("INFO","fiche bobinage :${b!!._id} - spires : ${b!!.nbSpires}")
                                                    }
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
                                            Log.e("Error", "erreur ${t.message}")
                                        }
                                    })
                            }
                        }
                        Log.i(
                            "INFO",
                            " nb de chantier :${resp.data?.filter { it.type == 1L }!!.size} - nb bobinages : ${resp.data?.filter { it.type == 4L }!!.size}"
                        )
                    } else {
                        Log.i("INFO", "code : ${response.code()} - erreur : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<FichesResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            })

    }

    suspend fun nbFichesDemontage() : Int {
        return repository.demontageRepository!!.demontageDao.getAll().size
    }
    suspend fun nbFichesRemontage() : Int {
        return repository.remontageRepository!!.remontageDao.getAll().size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun listeFicheLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            var listChantier = repository.getAllChantierLocalDatabase()
            chantiers.clear()
            for (ch in listChantier) {
                chantiers.add(ch.toChantier())
            }
            var listBobinage = repository.getAllBobinageLocalDatabase()
            for (bobinage in listBobinage) {
                bobinages.add(bobinage.toBobinage())
            }
            isTracking()
        }
    }

    fun updatePointages(){
        repository.getPointages(token.value!!,sharedPref.getString("userId","")!!, object : Callback<PointagesResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<PointagesResponse>,
                response: Response<PointagesResponse>
            ) {
                if (response.code() == 200) {
                    val resp = response.body()
                    if (resp != null) {
                        var list = mutableListOf<Pointage>()
                        resp.data!!.forEach {
                            list.add(it.toPointage())
                            viewModelScope.launch(Dispatchers.IO){
                                var test = repository.getByIdPointageDatabase(it._id)
                                if (test == null) repository.insertPointageDatabase(it.toPointage())
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<PointagesResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
        viewModelScope.launch(Dispatchers.IO) {
            isTracking()
        }
    }
    fun Pointage() =  runBlocking{
        viewModelScope.launch(Dispatchers.IO) {
            var date = ZonedDateTime.of(
                LocalDateTime.now().minusSeconds(5),
                ZoneOffset.of(SimpleDateFormat("Z").format(Date()))
            ) //definition du fuseau horaire
            if (isOnline(context) && token.value !== "") {
               var ptn = repository.postPointages(
                    token.value!!,
                    sharedPref.getString("userId", "")!!,
                    date
                )
                repository.insertPointageDatabase(
                    Pointage(
                        ptn.body()!!.data!!._id, sharedPref.getString("userId", "")!!, date
                    )
                )
            } else {
                repository.insertPointageDatabase(
                    Pointage(
                        SystemClock.uptimeMillis().toString(), sharedPref.getString("userId", "")!!,
                        date
                    )
                )
            }
            isTracking()
        }
    }
    suspend fun isTracking(){
        var list = repository.getAllPointageLocalDatabase()
        list.filter { it.timestamp.dayOfMonth == LocalDate.now().dayOfMonth }
        tracking.postValue(list.size % 2 != 0)
    }
    fun toPointages(view: View) {
        Navigation.findNavController(view)
            .navigate(AccueilDirections.actionAccueilToPointageFragment())
    }
    fun toChantier(view: View) {
        var action =
            AccueilDirections.versFicheChantier(chantiers!!.toTypedArray(), token.value!!, username)
        Navigation.findNavController(view).navigate(action)
    }
    fun toFicheD(view: View) {
        var action = AccueilDirections.versFicheD(token.value!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun toFicheR(view: View) {
        var action =
            AccueilDirections.versFicheRemontage(token.value!!, username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun toBobinage(view: View) {
        var action =
            AccueilDirections.versFicheBobinage(bobinages!!.toTypedArray(), token.value!!, username)
        Navigation.findNavController(view).navigate(action)
    }
    fun toDeconnexion(view: View) {
        Navigation.findNavController(view).navigate(R.id.versConnexion)
    }
    fun getVehicule(id: String) {
        var vehicule =
            repository.getVehiculeById(token.value!!, id, object : Callback<VehiculesResponse> {
                override fun onResponse(
                    call: Call<VehiculesResponse>,
                    response: Response<VehiculesResponse>
                ) {
                    if (response.code() == 200) {
                        val resp = response.body()
                        Log.i("INFO", "vehicule ${resp!!.data!!.nom}")
                        viewModelScope.launch(Dispatchers.IO) {
                            var local =
                                repository.getByIdVehiculesLocalDatabse(resp!!.data!!._id)
                            if (local == null) {
                                Log.i("INFO", "ajout ${resp!!.data!!.nom} en bdd locale")
                                repository.insertVehiculesLocalDatabase(resp!!.data!!)
                            }
                        }

                    }

                }

                override fun onFailure(call: Call<VehiculesResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            })
    }
    fun saveWconnection(context: Context, loading: CardView)= runBlocking{
        if (isOnline(context) && (sharedPref.getString("login","") !== "") && (sharedPref.getString("password","") !== "") ) {
            val resp = launch { repository.logUser(sharedPref.getString("login","")!!, sharedPref.getString("password","")!!, object : Callback<LoginResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.code() == 200) {
                        val resp = response.body()
                        if (resp != null) {
                            token.postValue(resp.token!!)
                            sendFiche(loading,resp.token!!)
                            Log.i("info","envoi des fiches - connecté")
                        }
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            }) }
            resp.join()
        }
    }
    fun reloadWconnection() = runBlocking {
        if (isOnline(context) && (sharedPref.getString("login","") !== "") && (sharedPref.getString("password","") !== "") ) {
            val resp = launch { repository.logUser(sharedPref.getString("login","")!!, sharedPref.getString("password","")!!, object : Callback<LoginResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.code() == 200) {
                        val resp = response.body()
                        if (resp != null) {
                            token.postValue(resp.token!!)
                           listeFiches(resp.token!!, resp.user!!._id!!)
                            Log.i("info","réccupération des fiches - connecté")
                        }
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Error", "erreur ${t.message}")
                }
            }) }
            resp.join()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFiche(loading: CardView, token: String) = runBlocking{
        if (loading.visibility == View.GONE) loading.visibility = View.VISIBLE
        if (isOnline(context) == true) {
            if ( token !== "") {
                viewModelScope.launch(Dispatchers.IO) {
                    var job = CoroutineScope(Dispatchers.IO).launch {
                        getNameURI()
                    }
                    job.join()
                    var listCh: List<ChantierEntity> =
                        repository.getAllChantierLocalDatabase()
                    Log.i("INFO", "nb de fiches chantier: ${listCh.size}")
                    if (listCh.size > 0) {
                        for (fiche in listCh) {
                            var ch = fiche.toChantier()
                            runBlocking {
                                var photos = ch.photos?.toMutableList()
                                var iter = photos?.listIterator()
                                while (iter?.hasNext() == true) {
                                    var name = iter.next()
                                    if (name !== "") {
                                        //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                        runBlocking {
                                            if (name.contains(ch.numFiche!!)) {
                                                //var test = getPhotoFile(name)
                                                var job =
                                                    CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                        getNameURI()
                                                    }
                                                job.join()
                                                var job2 =
                                                    CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
                                                                File(dir, imageName.value!!.name!!)
                                                            if (from.exists()) from.renameTo(to)
                                                            sendPhoto(to)
                                                            iter.set(imageName.value!!.name!!)
                                                        } catch (e: java.lang.Exception) {
                                                            Log.e("EXCEPTION", e.message!!)
                                                        }
                                                    }
                                                job2.join()
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
                                    var job3 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job3.join()
                                    var job4 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_signatures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    ch.signatureTech!!
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                if (from.exists()) from.renameTo(to)
                                                ch.signatureTech = imageName.value!!.name
                                                sendPhoto(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job4.join()
                                    delay(200)
                                }
                                if (ch.signatureClient !== null && ch.signatureClient!!.contains("sign_")) {
                                    var job3 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job3.join()
                                    var job4 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_signatures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    ch.signatureClient!!
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                if (from.exists()) from.renameTo(to)
                                                ch.signatureClient = imageName.value!!.name
                                                sendPhoto(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job4.join()
                                }
                            }
                            val resp = repository.patchChantier(
                                token,
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
                                    runBlocking {
                                        if (name.contains(ch.numFiche!!)) {
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                                token,
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
                        repository.demontageRepository!!.getAllDemontageTriLocalDatabase()
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
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                            val resp = repository.demontageRepository!!.patchDemontageTriphase(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageTriphaseLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageCCLocalDatabase()
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
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
                                            delay(200)
                                        }
                                    }
                                }
                                if (name == "") {
                                    iter.remove()
                                }
                            }
                            dcc.photos = photos?.toTypedArray()
                            val resp = repository.demontageRepository!!.patchDemontageCC(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageCCLocalDatabse(
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
                    var listDP: List<DemoPompeEntity> =
                        repository.demontageRepository!!.getAllDemontagePompeLocalDatabase()
                    Log.i("INFO", "nb de fiches Demontage pompe: ${listDP.size}")
                    if (listDP.size > 0) {
                        for (fiche in listDP) {
                            var rc = fiche.toDemoPompe()
                            var photos = rc.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name !== "") {
                                    //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                    runBlocking {
                                        if (name.contains(rc.numFiche!!)) {
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) {
                                                            from.renameTo(to)
                                                            sendPhoto(to)
                                                            iter.set(imageName.value!!.name!!)
                                                        }
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
                                            delay(200)
                                        }
                                    }
                                }
                                if (name == "") {
                                    iter.remove()
                                }
                            }
                            rc.photos = photos?.toTypedArray()
                            val resp = repository.demontageRepository!!.patchDemontagePompe(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontagePompeLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageMonoLocalDatabase()
                    Log.i("INFO", "nb de fiches Demontage monophase: ${listDM.size}")
                    if (listDM.size > 0) {
                        for (fiche in listDM) {
                            var rc = fiche.toMonophase()
                            var photos = rc.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name !== "") {
                                    //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                    runBlocking {
                                        if (name.contains(rc.numFiche!!)) {
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
                                            delay(200)
                                        }
                                    }
                                }
                                if (name == "") {
                                    iter.remove()
                                }
                            }
                            rc.photos = photos?.toTypedArray()
                            val resp = repository.demontageRepository!!.patchDemontageMono(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageMonoLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageAlterLocalDatabase()
                    Log.i("INFO", "nb de fiches Demontage Alternateur: ${listDA.size}")
                    if (listDA.size > 0) {
                        for (fiche in listDA) {
                            var rc = fiche.toDemontageAlternateur()
                            var photos = rc.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name !== "") {
                                    //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                    runBlocking {
                                        if (name.contains(rc.numFiche!!)) {
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
                                            delay(200)
                                        }
                                    }
                                }
                                if (name == "") {
                                    iter.remove()
                                }
                            }
                            rc.photos = photos?.toTypedArray()
                            val resp = repository.demontageRepository!!.patchDemontageAlter(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageAlterLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageRBLocalDatabase()
                    Log.i("INFO", "nb de fiches Demontage Rotor Bobine: ${listDRB.size}")
                    if (listDRB.size > 0) {
                        for (fiche in listDRB) {
                            var rc = fiche.toDemoRotorB()
                            var photos = rc.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name !== "") {
                                    //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                    runBlocking {
                                        if (name.contains(rc.numFiche!!)) {
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                            val resp = repository.demontageRepository!!.patchDemontageRotor(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageRBLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageMotopompeLocalDatabase()
                    if (listDMP.size > 0) {
                        for (fiche in listDMP) {
                            var dmp = fiche.toMotoPompe()
                            var photos = dmp.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name !== "") {
                                    //Log.i("INFO", name.contains(dt.numFiche!!).toString()+"nom fichier ${name} - nom fiche ${dt.numFiche}")
                                    runBlocking {
                                        if (name.contains(dmp.numFiche!!)) {
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                            val resp = repository.demontageRepository!!.patchDemontageMotopompe(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageMotoPompeLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageReducteurLocalDatabase()
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
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                            val resp = repository.demontageRepository!!.patchDemontageReducteur(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageReducteurLocalDatabse(
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
                        repository.demontageRepository!!.getAllDemontageMotoreducteurLocalDatabase()
                    if (listDMP.size > 0) {
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
                                            Log.i("INFO", "fichier à upload : ${name}")
                                            //var test = getPhotoFile(name)
                                            var job =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    getNameURI()
                                                }
                                            job.join()
                                            var job2 =
                                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                                    try {
                                                        val dir =
                                                            Environment.getExternalStoragePublicDirectory(
                                                                Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                            )
                                                        val from = File(
                                                            dir,
                                                            name
                                                        )
                                                        val to = File(dir, imageName.value!!.name!!)
                                                        Log.i(
                                                            "INFO",
                                                            from.exists()
                                                                .toString() + " - path ${from.absolutePath} - new name ${imageName.value!!.name!!}"
                                                        )
                                                        if (from.exists()) from.renameTo(to)
                                                        sendPhoto(to)
                                                        iter.set(imageName.value!!.name!!)
                                                    } catch (e: java.lang.Exception) {
                                                        Log.e("EXCEPTION", e.message!!)
                                                    }
                                                }
                                            job2.join()
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
                            val resp = repository.demontageRepository!!.patchDemontageMotoreducteur(
                                token,
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
                                                repository.demontageRepository!!.deleteDemontageMotoreducteurLocalDatabse(
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
                    /*var listRT: List<RemontageTriphaseEntity> =
                        repository.remontageRepository!!.getAllRemontageTriLocalDatabase()
                    Log.i("INFO", "nb de fiches RemontageTriphase: ${listRT.size}")
                    if (listRT.size > 0) {
                        for (fiche in listRT) {
                            var dt = fiche.toRTriphase()
                            var photos = dt.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                Log.i(
                                    "INFO",
                                    name.contains(dt.numFiche!!)
                                        .toString() + " fichier ${name} - numfiche ${dt.numFiche!!}"
                                )
                                if (name.contains(dt.numFiche!!)) {
                                    Log.i("INFO", "fichier à upload : ${name}")
                                    //var test = getPhotoFile(name)
                                    var job =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job.join()
                                    var job2 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                iter.set(imageName.value!!.name!!)
                                                sendPhoto(from)
                                                Log.i(
                                                    "INFO",
                                                    from.exists()
                                                        .toString() + " - path ${from.absolutePath}"
                                                )
                                                if (from.exists()) from.renameTo(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job2.join()
                                }
                            }
                            dt.photos = photos?.toTypedArray()
                            val resp = repository.remontageRepository!!.patchRemontageTriphase(
                                token,
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
                                Log.i("INFO", name.contains(rc.numFiche!!).toString())
                                if (name.contains(rc.numFiche!!)) {
                                    Log.i("INFO", "fichier à upload : ${name}")
                                    //var test = getPhotoFile(name)
                                    var job =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job.join()
                                    var job2 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                iter.set(imageName.value!!.name!!)
                                                sendPhoto(from)
                                                Log.i(
                                                    "INFO",
                                                    from.exists()
                                                        .toString() + " - path ${from.absolutePath}"
                                                )
                                                if (from.exists()) from.renameTo(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job2.join()
                                }
                            }
                            rc.photos = photos?.toTypedArray()
                            val resp = repository.remontageRepository!!.patchRemontageCC(
                                token,
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
                                Log.i("INFO", name.contains(rc.numFiche!!).toString())
                                if (name.contains(rc.numFiche!!)) {
                                    Log.i("INFO", "fichier à upload : ${name}")
                                    //var test = getPhotoFile(name)
                                    var job =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job.join()
                                    var job2 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                iter.set(imageName.value!!.name!!)
                                                sendPhoto(from)
                                                Log.i(
                                                    "INFO",
                                                    from.exists()
                                                        .toString() + " - path ${from.absolutePath}"
                                                )
                                                if (from.exists()) from.renameTo(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job2.join()
                                    delay(200)
                                }
                            }
                            rc.photos = photos?.toTypedArray()
                            val resp = repository.remontageRepository!!.patchRemontage(
                                token,
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
                    }
                    var listRMP: List<RemontageMotopompeEntity> =
                        repository.remontageRepository!!.getAllRemontageMotopompeLocalDatabase()
                    Log.i("INFO", "nb de fiches remontage Motopompe: ${listRMP.size}")
                    if (listRMP.size > 0) {
                        for (fiche in listRMP) {
                            var rmp = fiche.toRemontageMotopompe()
                            var photos = rmp.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name.contains(rmp.numFiche!!)) {
                                    var job =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job.join()
                                    var job2 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                iter.set(imageName.value!!.name!!)
                                                sendPhoto(from)
                                                Log.i(
                                                    "INFO",
                                                    from.exists()
                                                        .toString() + " - path ${from.absolutePath}"
                                                )
                                                if (from.exists()) from.renameTo(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job2.join()
                                    delay(200)
                                }
                            }
                            rmp.photos = photos?.toTypedArray()
                            val resp = repository.remontageRepository!!.patchRemontageMotopompe(
                                token,
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
                                                repository.remontageRepository!!.deleteRemontageMotoPompeLocalDatabse(
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
                        repository.remontageRepository!!.getAllRemontageMotoreducteurLocalDatabase()
                    Log.i("INFO", "nb de fiches remontage: ${listRMR.size}")
                    if (listRMR.size > 0) {
                        for (fiche in listRMR) {
                            var rmp = fiche.toRemontageMotoreducteur()
                            var photos = rmp.photos?.toMutableList()
                            var iter = photos?.listIterator()
                            while (iter?.hasNext() == true) {
                                var name = iter.next()
                                if (name.contains(rmp.numFiche!!)) {
                                    var job =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            getNameURI()
                                        }
                                    job.join()
                                    var job2 =
                                        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                            try {
                                                val dir =
                                                    Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_PICTURES + "/test_pictures"
                                                    )
                                                val from = File(
                                                    dir,
                                                    name
                                                )
                                                val to = File(dir, imageName.value!!.name!!)
                                                iter.set(imageName.value!!.name!!)
                                                sendPhoto(from)
                                                Log.i(
                                                    "INFO",
                                                    from.exists()
                                                        .toString() + " - path ${from.absolutePath}"
                                                )
                                                if (from.exists()) from.renameTo(to)
                                            } catch (e: java.lang.Exception) {
                                                Log.e("EXCEPTION", e.message!!)
                                            }
                                        }
                                    job2.join()
                                    delay(200)
                                }
                            }
                            rmp.photos = photos?.toTypedArray()
                            val resp = repository.remontageRepository!!.patchRemontageMotoreducteur(
                                token,
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
                                                repository.remontageRepository!!.deleteRemontageMotoreducteurLocalDatabse(
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
                    }*/
                }
            } else {
                var job =
                    CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                        connection(
                            sharedPref?.getString("login", "")!!,
                            sharedPref?.getString("password", "")!!
                        )
                    }
                job.join()
                Log.i("INFO", "token ${token}")
                Log.i("INFO", "connexion offline")
            }
        }
        if (loading.visibility == View.VISIBLE) loading.visibility = View.GONE
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
        var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resp1 = repository.getURLToUploadPhoto(token.value!!)
            withContext(Dispatchers.Main) {
                if (resp1.isSuccessful) {
                    imageName.postValue(resp1.body())
                    //Log.i("INFO", resp1.body()?.name!!)
                } else {
                    exceptionHandler
                }
            }
        }
        job.join()
    }
    fun sendPhoto(photo: File) = runBlocking{
        var s =
            imageName.value!!.url!!.removePrefix("https://minio.stb.dev.alf-environnement.net/images/"+imageName.value!!.name!!+"?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab[1] = tab[1].replace("%2F", "/")
        viewModelScope.launch(Dispatchers.IO) {
            lateinit var  compressedPicture :File
            try {
                var job = launch { compressedPicture = Compressor.compress(context, photo) }
                job.join()
            } catch (e:Throwable) {
                Log.e("error",e.message!!)
            }
            delay(200)
            compressedPicture.renameTo(photo)
            repositoryPhoto.uploadPhoto(
                token.value!!,
                imageName.value!!.name!!,
                tab.toList(),
                compressedPicture,
                object : Callback<URLPhotoResponse> {
                    override fun onResponse(
                        call: Call<URLPhotoResponse>,
                        response: Response<URLPhotoResponse>
                    ) {
                        // Log.i("INFO", response.code().toString() + " - " + response.message())
                        // Log.i("INFO", "envoyé ${call.request().url()}")
                    }

                    override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                        Log.i("INFO", t.message!!)
                    }
                })
        }
    }
    suspend fun getPhotoFile(photoName: String): String? = runBlocking {
        var file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_pictures/" + photoName
        )
        Log.i("INFO", "fichier ${file.absolutePath} exist: ${file.exists().toString()}")
        var path: String? = null
        if (!file.exists()) {
            var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val resp1 = repository.getURLPhoto(token.value!!, photoName)
                withContext(Dispatchers.Main) {
                    if (resp1.isSuccessful) {
                        if (resp1.code() == 200) {
                            path = resp1.body()?.url!!
                            Log.i(
                                "INFO",
                                "url de la photo ${photoName} :" + resp1.body()?.url!!.replace(
                                    "%2F",
                                    "/"
                                )
                            )
                            CoroutineScope((Dispatchers.IO + exceptionHandler2)).launch {
                                saveImage(
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(resp1.body()!!.url!!.replace("%2F", "/"))
                                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                                        .error(android.R.drawable.stat_notify_error)
                                        .submit()
                                        .get(), photoName
                                )
                            }
                        }
                    } else {
                        exceptionHandler
                        path = null
                    }
                }
            }
            job.join()
            /*var job2 = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var resp2 = repository.getPhoto(file!!)
            withContext(Dispatchers.Main){
                if( resp2.isSuccessful) {
                    saveImage(Glide.with(this@withContext)
                        .asBitmap()
                        .load(resp2.))
                   // var p = saveFile(resp2.body(), Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES).absolutePath+"/test_pictures/"+photoName)
                   // photos?.value!!.add(p)
                   // Log.i("INFO", "chemin:"+p)
                } else{
                    exceptionHandler
                }
            }
        }
        job2.join()*/
        } else {
            if (file.exists()) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/test_pictures/" + photoName
            } else {
                path = null
            }
        }
        return@runBlocking path
    }
    suspend fun getSignature(photoName: String): String? = runBlocking {
        var file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_signature/" + photoName
        )
        Log.i("INFO", "fichier ${file.absolutePath} exist: ${file.exists().toString()}")
        var path: String? = null
        if (!file.exists()) {
            var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val resp1 = repository.getURLPhoto(token.value!!, photoName)
                withContext(Dispatchers.Main) {
                    if (resp1.isSuccessful) {
                        if (resp1.code() == 200) {
                            path = resp1.body()?.url!!
                            Log.i("INFO", "url de la photo ${photoName} :" + resp1.body()?.url!!)
                            CoroutineScope((Dispatchers.IO + exceptionHandler2)).launch {
                                saveImage(
                                    Glide.with(context)
                                        .asBitmap()
                                        .load(resp1.body()!!.url!!)
                                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                                        .error(android.R.drawable.stat_notify_error)
                                        .submit()
                                        .get(), photoName
                                )
                            }
                        }
                    } else {
                        exceptionHandler
                    }
                }
            }
            job.join()
            /*var job2 = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var resp2 = repository.getPhoto(file!!)
            withContext(Dispatchers.Main){
                if( resp2.isSuccessful) {
                    saveImage(Glide.with(this@withContext)
                        .asBitmap()
                        .load(resp2.))
                   // var p = saveFile(resp2.body(), Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES).absolutePath+"/test_pictures/"+photoName)
                   // photos?.value!!.add(p)
                   // Log.i("INFO", "chemin:"+p)
                } else{
                    exceptionHandler
                }
            }
        }
        job2.join()*/
        } else {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_signature/" + photoName
        }
        return@runBlocking path
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage} - ${throwable.cause}")
    }
    val exceptionHandler2 = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "erreur enregistrement: ${throwable.localizedMessage}")
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
            //updateStorage(context,name)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
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