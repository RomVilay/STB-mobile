package com.example.applicationstb.ui.ficheChantier

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class FicheChantierViewModel(application: Application) : AndroidViewModel(application) {
    var context = getApplication<Application>().applicationContext
    var token: String? = null;
    var username: String? = null;
    var repository = Repository(context);
    var listeChantiers = arrayListOf<Chantier>()
    var chantier = MutableLiveData<Chantier>()
    var signatures = arrayListOf<Uri?>()
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    var start = MutableLiveData<Date>()
    var image =MutableLiveData<File>()
    var imageName = MutableLiveData<String>()
    init {
         viewModelScope.launch(Dispatchers.IO){
            repository.createDb()
       }
       /* var i =0
        while (i<10)
        {
            var chantier = Chantier (i.toString(),
                i.toString() ,
                client,
                "Dupond M.",
                3369077543,
                listeTechs,
                listeTechs[0],
                Vehicule(1,"trafic",230000),
                "2 rue des paquerettes",
                "Lorem ipsum",
                "Lorem ipsum",
                "Lorem ipsum",

            )
            listeChantiers.add(chantier);
            i++;
        }*/

    }


    fun back(view:View){
        Log.i("INFO",token!!+" "+username!!)
        val action = FicheChantierDirections.deChantierversAccueil(token!!,username!!)
        Navigation.findNavController(view).navigate(action)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addPhoto(index:Int, photo: Uri) {
        Log.i("INFO", "add photo")
       var list = chantier?.value?.photos?.toMutableList()
        if (list != null) {
            list.add(photo.toString())
        }
        chantier?.value?.photos = list?.toTypedArray()
        photos.value = list!!
        quickSave()
    }
    fun setSchema(sch: String){
        schema.value = sch
        Log.i("INFO", sch.toString())
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
                        //Log.i("INFO","${resp.fiche!!.client.enterprise}")
                        chantier.value = resp.fiche!!
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
        Log.i("INFO","quick save")
        getTime()
        var size = chantier.value?.photos?.size?.minus(1)
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
            //Log.i("INFO","${ch}")
            if (ch !== null) {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
                //Log.i("INFO","patch ${chantier.value!!._id}")
            } else {
                repository.insertChantierLocalDatabase(chantier.value!!)
                //Log.i("INFO","insert ${chantier.value!!._id}")
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun localSave(view:View){
        Log.i("INFO","local save")
        Log.i("INFO","pictures ${chantier.value!!.photos!!.size}")
        viewModelScope.launch(Dispatchers.IO){
            var ch = repository.getByIdChantierLocalDatabse(chantier.value!!._id)
            //Log.i("INFO","${ch}")
            if (ch !== null) {
                repository.updateChantierLocalDatabse(chantier.value!!.toEntity())
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                //Log.i("INFO","patch ${chantier.value!!._id}")
            } else {
                val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                mySnackbar.show()
                repository.insertChantierLocalDatabase(chantier.value!!)
                //Log.i("INFO","insert ${chantier.value!!._id}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(context: Context, view: View){
        if (isOnline(context)){
            val resp = repository.patchChantier(token!!, chantier.value!!._id, chantier!!.value!!, object: Callback<ChantierResponse> {
                override fun onResponse(call: Call<ChantierResponse>, response: Response<ChantierResponse>) {
                    if ( response.code() == 200 ) {
                        val resp = response.body()
                        if (resp != null) {
                            val mySnackbar = Snackbar.make(view,"fiche enregistrée", 3600)
                            mySnackbar.show()
                           // Log.i("INFO","${resp.fiche!!.photos?.get(0)!!}")
                        }
                    } else {
                        val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                        mySnackbar.show()
                        Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ChantierResponse>, t: Throwable) {
                    val mySnackbar = Snackbar.make(view,"erreur lors de l'enregistrement", 3600)
                    mySnackbar.show()
                    Log.e("Error","${t.stackTraceToString()}")
                    Log.e("Error","erreur ${t.message}")
                }
            })
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
    fun getImage(name: String?){
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
    }
   suspend fun getImageName() = withContext(Dispatchers.IO){
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
    }
    fun upload(file: File){
        repository.uploadPhoto(token!!,)
    }

}