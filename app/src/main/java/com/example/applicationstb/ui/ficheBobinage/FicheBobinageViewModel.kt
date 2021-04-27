package com.example.applicationstb.ui.ficheBobinage

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
    }
    fun addSection(brins:Int, longueur:Double){
        listeBobinage[0].addSection(brins, longueur)
        sections.value = listeBobinage[0].sectionsFils
        //Log.i("INFO", "add section $brins - $longueur")
        //Log.i("INFO","current sections : ${listeBobinage[0].sectionsFils.toString()}")
    }
    fun somme(list: MutableList<Section>): Double {
        var tab = list.map { it.longueur }
        Log.i("info", tab.toString())
        return tab.sum()
    }
    fun back(view: View){
        Navigation.findNavController(view).navigate(R.id.deBobinageverAccueil)
    }
    fun save(){}
    // TODO: Implement the ViewModel
}