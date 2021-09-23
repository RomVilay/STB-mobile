package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import com.example.applicationstb.repository.BobinageResponse
import com.example.applicationstb.repository.ChantierResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.ficheChantier.FicheChantierDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FicheBobinageViewModel : ViewModel() {

    var listeBobinage = arrayListOf<Fiche>()
    var sections = MutableLiveData<MutableList<Section>>(mutableListOf())
    var schemas = MutableLiveData<MutableList<String>>(mutableListOf())
    var bobinage = MutableLiveData<Bobinage>()
    var schema = MutableLiveData<String>()
    var token :String? = null;
    var username: String? = null;
    var repository = Repository();

    init {

        /*var i =0
        while (i<10)
        {
            var bobinage = Bobinage (i.toString(),
                    i.toString() ,
                    client,
                    "Dupond M.",
                    3369077543,
                    arrayOf<User>(tech),
                    tech,
                    "Bocsh",
                    "asynchrone triphasé",
                    200 ,
                    300,
                    10,
                    "1",
                    "20Hz",
                    "10A",
                    true
            )
            listeBobinage.add(bobinage);
            i++;
        }*/
        //bobinage.value = listeBobinage[0]
    }
    fun selectBobinage(id: String){
        val resp = repository.getBobinage(token!!, id, object: Callback<BobinageResponse> {
            override fun onResponse(call: Call<BobinageResponse>, response: Response<BobinageResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        Log.i("INFO","${resp.fiche!!._id}")
                        bobinage.value = resp.fiche
                        sections.value = bobinage.value!!.sectionsFils
                        schemas.value = bobinage.value!!.schemas
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
    fun addSection(diametre:Double, longueur:Double, nbBrins: Long){
        var list = sections.value
        var section = Section(nbBrins,longueur,diametre)
        list!!.add(section)
        sections.value = list
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }
    fun addSchema(schema: Uri) {
        var list = schemas.value
        list!!.add(schema.toString())
        schemas.value=list
    }
    fun somme(list: MutableList<Section>): Double {
        var tab = list.map { it.longueur * it.diametre }
        //Log.i("info", tab.toString())
        return tab.sum()
    }
    fun back(view: View){
        val action = FicheBobinageDirections.deBobinageverAccueil(token!!,username!!)
        Navigation.findNavController(view).navigate(action)
    }
    fun backFs(view: View){
        Navigation.findNavController(view).navigate(R.id.de_fscreen_vers_fb)
    }
    fun setSchema(sch: String){
        schema.value = sch
        Log.i("INFO", sch.toString())
    }
    fun fullScreen(view: View,uri:String) {
        val action = FicheChantierDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
        //Navigation.findNavController(view).navigate(R.id.versFullScreen)
    }
    fun save(){
        Log.i("INFO", bobinage.value!!._id)
        val resp = repository.patchBobinage(token!!,bobinage.value!!._id, bobinage.value!! , object: Callback<BobinageResponse> {
            override fun onResponse(call: Call<BobinageResponse>, response: Response<BobinageResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        Log.i("INFO","enregistré")
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