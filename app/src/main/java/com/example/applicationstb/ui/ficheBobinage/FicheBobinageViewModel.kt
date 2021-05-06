package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*

class FicheBobinageViewModel : ViewModel() {

    var listeBobinage = arrayListOf<Bobinage>()
    var client = Client(0,"Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var tech = User(0,"Dumont","Toto",1,"toto","toto")
    var sections = MutableLiveData<MutableList<Section>>()
    var schemas = MutableLiveData<MutableList<Uri>>()
    var bobinage = MutableLiveData<Bobinage>()
    var schema = MutableLiveData<Uri>()

    init {
        var i =0
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
        }
        bobinage.value = listeBobinage[0]
    }
    fun selectBobinage(index: Int){
        bobinage.value = listeBobinage[index];
        sections.value = bobinage.value!!.sectionsFils
        schemas.value = bobinage.value!!.schemas
    }
    fun addSection(brins:Int, longueur:Double){
        listeBobinage[0].addSection(brins, longueur)
        sections.value = listeBobinage[0].sectionsFils
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }
    fun addSchema(schema: Uri) {
        listeBobinage[0].addSchema(schema)
        schemas.value=listeBobinage[0].schemas
    }
    fun somme(list: MutableList<Section>): Double {
        var tab = list.map { it.longueur }
        //Log.i("info", tab.toString())
        return tab.sum()
    }
    fun back(view: View){
        Navigation.findNavController(view).navigate(R.id.deBobinageverAccueil)
    }
    fun backFs(view: View){
        Navigation.findNavController(view).navigate(R.id.de_fscreen_vers_fb)
    }
    fun setSchema(sch: Uri){
        schema.value = sch
        Log.i("INFO", sch.toString())
    }
    fun fullScreen(view: View) {
        Navigation.findNavController(view).navigate(R.id.versFullScreen)
    }
    fun save(){}
    // TODO: Implement the ViewModel
}