package com.example.applicationstb.ui.ficheChantier

import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import android.net.Uri
import android.util.Log
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.ChantierResponse
import com.example.applicationstb.repository.FichesResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FicheChantierViewModel : ViewModel() {
    var token: String? = null;
    var repository = Repository();
    var listeChantiers = arrayListOf<Fiche>()
    var chantier = MutableLiveData<Chantier>()
    var signatures = arrayListOf<Uri?>()
    var photos = MutableLiveData<MutableList<Uri>>(mutableListOf())
    var schema = MutableLiveData<Uri>()
    init {
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
        //console.log(signatures)
        val action = FicheChantierDirections.deChantierversAccueil("Token","username")
        Navigation.findNavController(view).navigate(action)
    }
    fun addPhoto(index:Int,photo: Uri) {
        photos.value!!.add(photo)
    }
    fun setSchema(sch: Uri){
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
                        chantier.value = resp.fiche
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
    /*fun setSignature(sign:Bitmap){
        signature.value = sign
    }*/

    fun save(){

    }

    // TODO: Implement the ViewModel
}