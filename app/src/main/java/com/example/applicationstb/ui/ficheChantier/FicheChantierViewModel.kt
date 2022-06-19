package com.example.applicationstb.ui.ficheChantier

import android.app.Application
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.navigation.Navigation
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.applicationstb.MainActivity
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import okhttp3.HttpUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class FicheChantierViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token= MutableLiveData<String>();
    var username: String? = null;
    var repository = Repository(context );
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    var listeChantiers = MutableLiveData<MutableList<Chantier>>()
    var chantier = MutableLiveData<Chantier>()
    var signatures = arrayListOf<String?>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    var start = MutableLiveData<Date>()
    var image =MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()
    val sharedPref =
        getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)
    init {
         viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
             getLocalFiches()
       }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getLocalFiches() {
        listeChantiers.postValue(repository.getAllChantierLocalDatabase()
                .map { it.toChantier() }.toMutableList())

    }
    fun back(view:View){
        Navigation.findNavController(view).popBackStack()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addPhoto(photo: String) {
        var list = chantier.value?.photos?.toMutableList()
        list!!.removeAll { it == "" }
        if (list != null) {
            list.add(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        } else {
            chantier.value?.photos =
                arrayOf(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        }
        chantier.value?.photos = list?.toTypedArray()
        photos.value = list!!
        galleryAddPic(photo)
        quickSave()
        if (isOnline(context)) {
            viewModelScope.launch {
                var s = async { repositoryPhoto.sendPhoto(
                    token.value!!.filterNot { it.isWhitespace() },
                    File(photo).name,
                    context
                )}
                s.await()
                var s2 = async { sendFicheNoText()}
                s2.await()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendFicheNoText() = runBlocking {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            if (isOnline(context) == true) {
                if (!sharedPref.getBoolean("connected", false) && (sharedPref?.getString(
                        "login",
                        ""
                    ) !== "" && sharedPref?.getString("password", "") !== "")
                ) {
                    connection(
                        sharedPref?.getString("login", "")!!,
                        sharedPref?.getString("password", "")!!
                    )
                }
                if (chantier.value!!.photos?.size!! > 0) {
                    chantier.value!!.photos?.forEach {
                        var test = async { repositoryPhoto.getURL(token.value!!, it) }
                        test.await()
                        if (test.isCompleted) {
                            if (test.await().code().equals(200)) {
                                var check = async {
                                    repositoryPhoto.getURLPhoto(
                                        token.value!!,
                                        test.await().body()?.name!!
                                    )
                                }
                                check.await()
                                if (check.isCompleted) {
                                    var code =
                                        async { repository.getPhoto(check.await().body()!!.url!!) }
                                    code.await()
                                    //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                    if (code.await().code() >= 400) {
                                        Log.i("info", "photo à envoyer${it}")
                                        var s = async{ repositoryPhoto.sendPhoto(token.value!!, it, context) }
                                        s.await()
                                    }
                                }
                            }
                        }
                    }
                }
                if (chantier.value!!.signatureClient !== null) {
                    var test = async { repositoryPhoto.getURL(token.value!!, chantier.value!!.signatureClient!!) }
                    test.await()
                    if (test.isCompleted) {
                        if (test.await().code().equals(200)) {
                            var check = async {
                                repositoryPhoto.getURLPhoto(
                                    token.value!!,
                                    test.await().body()?.name!!
                                )
                            }
                            check.await()
                            if (check.isCompleted) {
                                var code =
                                    async { repository.getPhoto(check.await().body()!!.url!!) }
                                code.await()
                                //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                if (code.await().code() >= 400) {
                                    Log.i("info", "signature à envoyer${chantier.value!!.signatureClient}")
                                    var s = async{ repositoryPhoto.sendPhoto(token.value!!, chantier.value!!.signatureClient!!, context) }
                                    s.await()
                                }
                            }
                        }
                    }

                }
                if (chantier.value!!.signatureTech !== null) {
                    var test = async { repositoryPhoto.getURL(token.value!!, chantier.value!!.signatureTech!!) }
                    test.await()
                    if (test.isCompleted) {
                        if (test.await().code().equals(200)) {
                            var check = async {
                                repositoryPhoto.getURLPhoto(
                                    token.value!!,
                                    test.await().body()?.name!!
                                )
                            }
                            check.await()
                            if (check.isCompleted) {
                                var code =
                                    async { repository.getPhoto(check.await().body()!!.url!!) }
                                code.await()
                                //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                if (code.await().code() >= 400) {
                                    var s = async{ repositoryPhoto.sendPhoto(token.value!!, chantier.value!!.signatureTech!!, context) }
                                    s.await()
                                }
                            }
                        }
                    }

                }

                repository.patchChantier(
                    token.value!!,
                    chantier.value!!._id,
                    chantier.value!!,
                    object : Callback<ChantierResponse> {
                        override fun onResponse(
                            call: Call<ChantierResponse>,
                            response: Response<ChantierResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    //Log.i("INFO","fiche chantier enregistrée")
                                }
                            } else {
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${response.message()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<ChantierResponse>, t: Throwable) {
                            Log.e("Error", "${t.stackTraceToString()}")
                            Log.e("Error", "erreur ${t.message}")
                        }
                    })
            } else {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
            }

        }

    }

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
                        token = token
                        Log.i("info", "new token ${resp.token}")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error", "erreur ${t.message}")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendExternalPicture(path: String): String? = runBlocking {
        if (isOnline(context)) {
            if (!sharedPref.getBoolean("connected", false) && (sharedPref?.getString(
                    "login",
                    ""
                ) !== "" && sharedPref?.getString("password", "") !== "")
            ) {
                connection(
                    sharedPref?.getString("login", "")!!,
                    sharedPref?.getString("password", "")!!
                )
            }
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                var file =
                    File(dir, "${chantier.value?.numFiche}_${chantier.value?.photos?.size}.jpg")
                galleryAddPic(file.absolutePath)
                var old = File(path)
                old.copyTo(file, true)
                var s = async {
                    repositoryPhoto.sendPhoto(
                        token.value!!.filterNot { it.isWhitespace() },
                        file.name,
                        context
                    )
                }
                s.join()
                /* while(File(dir,"${selection.value?.numFiche}_${selection.value?.photos?.size}.jpg").exists()){
                     file = File(dir, "${selection.value?.numFiche}_${file.name.substringAfter("_").substringBefore(".").toInt()+1}.jpg")
                     Log.i("info","photo nom send ext ${file}")
                 }*/
                return@runBlocking file.name
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return@runBlocking null
            }
        } else {
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                var file =
                    File(dir, "${chantier.value?.numFiche}_${chantier.value?.photos?.size}.jpg")
                galleryAddPic(file.absolutePath)
                var old = File(path)
                old.copyTo(file, true)
                return@runBlocking file.name
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return@runBlocking null
            }
        }

    }
    fun setSchema(sch: String){
        schema.value = sch
    }
    fun fullScreen(view: View,uri: String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }
    fun selectChantier(token: String,id: String){
        val resp = repository.getChantier(token, id, object: Callback<ChantierResponse> {
            override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        chantier.value = resp.data!!
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
    fun getVehicule(id:String, textView: TextView) : String? {
        var nom : String? = null;
        viewModelScope.launch(Dispatchers.IO){
            var v = repository.getByIdVehiculesLocalDatabse(id)
            textView.setText(v!!.nom)
        }
        //Log.i("INFO","vehicule ${nom}")
        /*val resp = repository.getVehiculeById(token!!, id, object: Callback<VehiculesResponse> {
            override fun onResponse(call: Call<VehiculesResponse>, response: Response<VehiculesResponse>){
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        nom = resp!!.vehicule!!.nom.toString()
                        Log.i("INFO","vehicule ${nom}")
                    }
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<VehiculesResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
        Log.i("INFO","vehicule ${nom}")*/
        return nom
    }
    /*fun setSignature(sign:Bitmap){
        signature.value = sign
    }*/
    fun localGet(){
        viewModelScope.launch(Dispatchers.IO){
            var list = repository.getAllChantierLocalDatabase()
            for (fiche in list){
                Log.i("INFO", "id:${fiche._id}")
            }
        }
    }

    fun getTime(){
        Log.i("INFO","duree avant : ${chantier.value?.dureeTotale}")
        var now = Date()
        if (chantier.value!!.dureeTotale !== null) {
            chantier.value!!.dureeTotale =
                (now.time - start.value!!.time ) + chantier.value!!.dureeTotale!!
        } else {
            chantier.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.value = now
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun quickSave(){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun localSave(view:View){
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
            if (ch !== null) {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
            } else {
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                repository.insertChantierLocalDatabase(chantier.value!!)
            }
        }
    }
   fun saveWconnection(context: Context, view: View)= runBlocking{
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
                        save(context,view,resp.token!!)
                        Log.i("info","chantier - connecté")
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
    fun save(context: Context, view: View, t:String)= runBlocking{
        if (isOnline(context) && token.value !== "") {
            viewModelScope.launch(Dispatchers.IO) {
                var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
                if (chantier.value!!.photos?.size!! > 0) {
                    chantier.value!!.photos?.forEach {
                        var test = async { repositoryPhoto.getURL(token.value!!, it) }
                        test.await()
                        if (test.isCompleted) {
                            if (test.await().code().equals(200)) {
                                var check = async {
                                    repositoryPhoto.getURLPhoto(
                                        token.value!!,
                                        test.await().body()?.name!!
                                    )
                                }
                                check.await()
                                if (check.isCompleted) {
                                    var code =
                                        async { repository.getPhoto(check.await().body()!!.url!!) }
                                    code.await()
                                    //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                    if (code.await().code() >= 400) {
                                        Log.i("info", "photo à envoyer${it}")
                                        var s = async{ repositoryPhoto.sendPhoto(token.value!!, it, context) }
                                        s.await()
                                    }
                                }
                            }
                        }
                    }
                }
                if (chantier.value!!.signatureClient !== null) {
                    var test = async { repositoryPhoto.getURL(token.value!!, chantier.value!!.signatureClient!!) }
                    test.await()
                    if (test.isCompleted) {
                        if (test.await().code().equals(200)) {
                            var check = async {
                                repositoryPhoto.getURLPhoto(
                                    token.value!!,
                                    test.await().body()?.name!!
                                )
                            }
                            check.await()
                            if (check.isCompleted) {
                                var code =
                                    async { repository.getPhoto(check.await().body()!!.url!!) }
                                code.await()
                                //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                if (code.await().code() >= 400) {
                                    Log.i("info", "signature à envoyer${chantier.value!!.signatureClient}")
                                    var s = async{ repositoryPhoto.sendSignature(token.value!!, chantier.value!!.signatureClient!!, context) }
                                    s.await()
                                }
                            }
                        }
                    }

                }
                if (chantier.value!!.signatureTech !== null) {
                    var test = async { repositoryPhoto.getURL(token.value!!, chantier.value!!.signatureTech!!) }
                    test.await()
                    if (test.isCompleted) {
                        if (test.await().code().equals(200)) {
                            var check = async {
                                repositoryPhoto.getURLPhoto(
                                    token.value!!,
                                    test.await().body()?.name!!
                                )
                            }
                            check.await()
                            if (check.isCompleted) {
                                var code =
                                    async { repository.getPhoto(check.await().body()!!.url!!) }
                                code.await()
                                //var isUploaded = async { repositoryPhoto.photoCheck(token!!,it)}
                                if (code.await().code() >= 400) {
                                    var s = async{ repositoryPhoto.sendSignature(token.value!!, chantier.value!!.signatureTech!!, context) }
                                    s.await()
                                }
                            }
                        }
                    }

                }
                ch?.toEntity()?.let { repository.updateChantierLocalDatabse(it) }
                chantier.postValue(ch!!)
                val resp = repository.patchChantier(
                    t,
                    chantier.value!!._id,
                    ch!!,
                    object : Callback<ChantierResponse> {
                        override fun onResponse(
                            call: Call<ChantierResponse>,
                            response: Response<ChantierResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                    mySnackbar.show()
                                    //Log.i("INFO","fiche chantier enregistrée")
                                }
                            } else {
                                val mySnackbar =
                                    Snackbar.make(view, "erreur lors de l'enregistrement", 3600)
                                mySnackbar.show()
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${response.message()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<ChantierResponse>, t: Throwable) {
                            val mySnackbar =
                                Snackbar.make(view, "erreur lors de l'enregistrement", 3600)
                            mySnackbar.show()
                            Log.e("Error", "${t.stackTraceToString()}")
                            Log.e("Error", "erreur ${t.message}")
                        }
                    })
            }
            } else {
                localSave(view)
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
  /*  fun getImage(name: String?){
        var request =   repository.getURLPhoto(token!!,name!!,object: Callback<URLPhotoResponse> {
            var i : File? = null
            override fun onResponse(
                call: Call<URLPhotoResponse>,
                response: Response<URLPhotoResponse>
            ) {
                if (response.code() == 200) {
                    var resp = response.body()
                    repository.getPhoto(token!!,resp?.url!!, object: Callback<PhotoResponse> {
                        override fun onResponse(
                            call: Call<PhotoResponse>,
                            response: Response<PhotoResponse>
                        ) {
                            if (response.code() == 200) {
                                image.value = response.body()!!.photo
                            }
                        }

                        override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                            Log.e("Error","erreur ${t.message}")
                        }

                    })
                }
            }

            override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }*/
  /* suspend fun getImageName() = withContext(Dispatchers.IO){
       repository.getURLToUploadPhoto(token!!, object: Callback<URLPhotoResponse2>{
            override fun onResponse(
                call: Call<URLPhotoResponse2>,
                response: Response<URLPhotoResponse2>
            ) {
                if (response.code() == 200) {
                    imageName.value = response.body()!!.name
                    Log.i("INFO", "nom renvoyé "+response.body()!!.name!!)
                }
            }

            override fun onFailure(call: Call<URLPhotoResponse2>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }

        })
    }*/
    suspend fun getNameURI() = runBlocking {
        var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resp1 = repository.getURLToUploadPhoto(token.value!!)
            withContext(Dispatchers.Main){
                if (resp1.isSuccessful) {
                    imageName.postValue(resp1.body())
                    Log.i("INFO",resp1.body()?.name!!)
                } else {
                    exceptionHandler
                }
            }
        }
      job.join()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    /*suspend fun sendExternalPicture(path: String?): String? {
        if (isOnline(context)) {
            getNameURI()
            try {
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                val file = File(dir, imageName.value!!.name!!)
                File(path).copyTo(file)
                delay(100)
                sendPhoto(file)
                return imageName.value!!.name!!
            } catch (e: java.lang.Exception) {
                Log.e("EXCEPTION", e.message!!, e.cause)
                return null
            }
        } else {
            val dir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
            val file = File(dir, chantier.value?.numFiche + "_" + SystemClock.uptimeMillis()+".jpg")
            File(path).copyTo(file)
            return file.name
        }

    }*/

    /*fun sendPhoto(photo:File)= runBlocking{
        var s = imageName.value!!.url!!.removePrefix("https://minio.stb.dev.alf-environnement.net/images/${imageName.value!!.name!!}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab[1] = tab[1].replace("%2F","/")
        viewModelScope.launch(Dispatchers.IO) {
            lateinit var  compressedPicture :File
            var job = launch { compressedPicture = Compressor.compress(context, photo) }
            job.join()
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
                         Log.i("INFO", response.code().toString() + " - " + response.message() )
                         Log.i("INFO", "envoyé ${call.request().url()}")
                    }

                    override fun onFailure(call: Call<URLPhotoResponse>, t: Throwable) {
                        Log.i("INFO", t.message!!)
                    }
                })
        }
    }*/
    suspend fun getPhotoFile(photoName: String, index: Int) : String? = runBlocking {
        var file: String? = null
        var job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resp1 = repository.getURLPhoto(token.value!!,photoName)
            withContext(Dispatchers.Main){
                if (resp1.isSuccessful) {
                    if (resp1.code() == 200) {
                       file = resp1.body()?.url!!
                        Log.i("INFO","url de la photo ${photoName} :"+resp1.body()?.url!!)
                        CoroutineScope((Dispatchers.IO + exceptionHandler2)).launch {
                            saveImage(Glide.with(context)
                                .asBitmap()
                                .load(resp1.body()!!.url!!)
                                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                                .error(android.R.drawable.stat_notify_error)
                                .submit()
                                .get(), photoName)
                        }
                    }
                } else {
                    exceptionHandler
                }
            }
        }
        job.join()
        return@runBlocking file
    }
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO","Exception handled: ${throwable.localizedMessage}")
    }
    val exceptionHandler2 = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO","erreur enregistrement: ${throwable.localizedMessage}")
    }
    private fun saveImage(image: Bitmap, name: String): String? {
        Log.i("INFO","start")
        var savedImagePath: String? = null
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/test_pictures/"
        )
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
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        return savedImagePath
    }
    fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }
    fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String{
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

}