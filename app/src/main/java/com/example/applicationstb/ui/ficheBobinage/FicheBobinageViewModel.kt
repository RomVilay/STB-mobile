package com.example.applicationstb.ui.ficheBobinage

import android.app.Application
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.example.applicationstb.ui.ficheChantier.FicheChantierDirections
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class FicheBobinageViewModel(application: Application) : AndroidViewModel(application) {

    var repository = Repository(getApplication<Application>().applicationContext);
    var repositoryPhoto = PhotoRepository(getApplication<Application>().applicationContext);
    var listeBobinage = arrayListOf<Bobinage>()
    var sections = MutableLiveData<MutableList<Section>>(mutableListOf())
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var bobinage = MutableLiveData<Bobinage>()
    var schema = MutableLiveData<String>()
    var token = MutableLiveData<String>()
    var username: String? = null;
    var context = getApplication<Application>().applicationContext
    var start = MutableLiveData<Date>()
    var image = MutableLiveData<File>()
    var imageName = MutableLiveData<URLPhotoResponse2>()
    val sharedPref =
        getApplication<Application>().getSharedPreferences("identifiants", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun selectBobinage(id: String) {
        if (isOnline(context)) {
            val resp =
                repository.getBobinage(token.value!!, id, object : Callback<BobinageResponse> {
                    override fun onResponse(
                        call: Call<BobinageResponse>,
                        response: Response<BobinageResponse>
                    ) {
                        if (response.code() == 200) {
                            val resp = response.body()
                            if (resp != null) {
                                Log.i("INFO", "${resp.data!!._id}")
                                bobinage.value = resp.data!!
                                sections.value = bobinage.value!!.sectionsFils!!
                                photos.value = mutableListOf()
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

        }
    }

    fun addSection(nbBrins: Long, diametre: Double) {
        var list = sections.value
        var section = Section(nbBrins, diametre)
        list!!.add(section)
        sections.value = list!!
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }

    fun addPhoto(photo: String) {
        Log.i("INFO", "add photo")
        var list = bobinage?.value?.photos?.toMutableList()
        list!!.removeAll { it == "" }
        if (list != null) {
            list.add(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        } else {
            //Log.i("INFO", photo.path.toString())
            bobinage.value?.photos =
                arrayOf(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        }
        bobinage.value?.photos = list?.toTypedArray()
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
                if (bobinage.value!!.photos?.size!! > 0) {
                    bobinage.value!!.photos?.forEach {
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

                val resp = repository.patchBobinage(
                    token.value!!,
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
                                val mySnackbar =
                                Log.i(
                                    "INFO",
                                    "code : ${response.code()} - erreur : ${response.message()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                            val mySnackbar =
                            Log.e("Error", "erreur ${t.message}")
                        }
                    })
            } else {
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
            }

        }

    }

    fun somme(list: MutableList<Section>): Double {
        var tab = list.map { Math.sqrt(it.diametre) * (Math.PI / 4) * it.nbBrins }
        //Log.i("info", tab.toString())
        return tab.sum()
    }

    fun back(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    fun setSchema(sch: String) {
        schema.value = sch
        Log.i("INFO", sch.toString())
    }

    fun addSchema(photo: String) {
        var list = bobinage?.value?.photos?.toMutableList()
        if (list != null) {
            list.add(photo.removePrefix("/storage/emulated/0/Pictures/test_pictures/"))
        }
        bobinage?.value?.photos = list!!.toTypedArray()
        photos.value = list
        quickSave()
    }

    fun fullScreen(view: View, uri: String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
        //Navigation.findNavController(view).navigate(R.id.versFullScreen)
    }

    fun saveWconnection(context: Context, view: View) = runBlocking {
        if (isOnline(context) && (sharedPref.getString(
                "login",
                ""
            ) !== "") && (sharedPref.getString("password", "") !== "")
        ) {
            val resp = launch {
                repository.logUser(
                    sharedPref.getString("login", "")!!,
                    sharedPref.getString("password", "")!!,
                    object : Callback<LoginResponse> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    token.postValue(resp.token!!)
                                    save(context, view, resp.token!!)
                                    Log.i("info", "bobinage - connecté")
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Log.e("Error", "erreur ${t.message}")
                        }
                    })
            }
            resp.join()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(context: Context, view: View, t: String) {
        if (isOnline(context) && token.value !== "") {
            CoroutineScope(Dispatchers.IO).launch {
                getNameURI()
            }
            viewModelScope.launch(Dispatchers.IO) {
                var bob = repository.getByIdBobinageLocalDatabse(bobinage.value!!._id)
                var photos = bobinage.value?.photos?.toMutableList()
                /* var iter = photos?.listIterator()
                 while (iter?.hasNext() == true) {
                     var name = iter.next()
                     if (name !== "") {
                         runBlocking {
                             if (name.contains(bob?.numFiche!!)) {
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
                             }
                         }
                     }
                     if (name == "") {
                         iter.remove()
                     }
                 }
                 bob?.photos = photos?.toTypedArray()*/
                bob?.status = bobinage.value?.status
                bob?.toEntity()?.let { repository.updateBobinageLocalDatabse(it) }
                bobinage.postValue(bob!!)
                val resp = repository.patchBobinage(
                    t,
                    bobinage.value!!._id,
                    bob!!,
                    object : Callback<BobinageResponse> {
                        override fun onResponse(
                            call: Call<BobinageResponse>,
                            response: Response<BobinageResponse>
                        ) {
                            if (response.code() == 200) {
                                val resp = response.body()
                                if (resp != null) {
                                    val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                                    mySnackbar.show()
                                    Log.i("INFO", "enregistré")
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

                        override fun onFailure(call: Call<BobinageResponse>, t: Throwable) {
                            val mySnackbar =
                                Snackbar.make(view, "erreur lors de l'enregistrement", 3600)
                            mySnackbar.show()
                            Log.e("Error", "erreur ${t.message}")
                        }
                    })
            }
        } else {
            localSave(view)
        }

    }

    fun localSave(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            var bob = repository.getByIdBobinageLocalDatabse(bobinage.value!!._id)
            Log.i("INFO", "status ${bobinage.value!!.status}")
            if (bob !== null) {
                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                mySnackbar.show()
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
                Log.i("INFO", "patch ${bobinage.value!!.sectionsFils}")
            } else {
                val mySnackbar = Snackbar.make(view, "fiche enregistrée", 3600)
                mySnackbar.show()
                repository.insertBobinageLocalDatabase(bobinage.value!!)
                Log.i("INFO", "insert ${bobinage.value!!._id}")
            }
        }
    }

    fun getTime() {
        Log.i("INFO", "duree avant : ${bobinage.value?.dureeTotale}")
        var now = Date()
        if (bobinage.value!!.dureeTotale !== null) {
            bobinage.value!!.dureeTotale =
                (now.time - start.value!!.time) + bobinage.value!!.dureeTotale!!
        } else {
            bobinage.value!!.dureeTotale = now.time - start.value!!.time
        }
        start.postValue(now)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun quickSave() {
        getTime()
        viewModelScope.launch(Dispatchers.IO) {
                repository.updateBobinageLocalDatabse(bobinage.value!!.toEntity())
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
                var file = if (bobinage.value?.photos?.size!! == 1 && bobinage.value?.photos!![0] == "") File(
                    dir,
                    "${bobinage.value?.numFiche}_${bobinage.value?.photos?.size!!}.jpg"
                ) else File(
                    dir,
                    "${bobinage.value?.numFiche}_${bobinage.value?.photos?.size!!+1}.jpg"
                )
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
                var file = if (bobinage.value?.photos?.size!! == 1 && bobinage.value?.photos!![0] == "") File(
                    dir,
                    "${bobinage.value?.numFiche}_${bobinage.value?.photos?.size!!}.jpg"
                ) else File(
                    dir,
                    "${bobinage.value?.numFiche}_${bobinage.value?.photos?.size!!+1}.jpg"
                )
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
                    Log.i("INFO", resp1.body()?.name!!)
                } else {
                    exceptionHandler
                }
            }
        }
        job.join()
    }

    /*fun sendPhoto(photo: File) = runBlocking {
        var s =
            imageName.value!!.url!!.removePrefix("https://minio.stb.dev.alf-environnement.net/images/${imageName.value!!.name!!}?X-Amz-Algorithm=")
        var tab = s.split("&").toMutableList()
        tab.forEach {
            Log.i("INFO", it)
        }
        tab[1] = tab[1].replace("%2F", "/")
        viewModelScope.launch(Dispatchers.IO) {
            lateinit var compressedPicture: File
            var job = launch { compressedPicture = Compressor.compress(context, photo) }
            job.join()
            //compressedPicture.renameTo(photo)
            Log.i("info", "taille ${compressedPicture.totalSpace}")
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
    }*/

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("INFO", "Exception handled: ${throwable.localizedMessage}")
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
}