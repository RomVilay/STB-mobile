package com.example.applicationstb.ui.ficheBobinage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.applicationstb.model.*

class FicheBobinageViewModel : ViewModel() {

    var listeBobinage = arrayListOf<Bobinage>()
    var client = Client(0,"Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var tech = User(0,"Dumont","Toto",1,"toto","toto")
    var sections = arrayListOf<Section>()
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
        addSection(2,"2".toDouble())
    }
    fun addSection(brins:Int, longueur:Double){
        listeBobinage[0].addSection(brins, longueur)
        Log.i("INFO", "section $brins - $longueur")
    }
    fun setSections(index:Int){
        sections = listeBobinage[index].sectionsFils
    }
    fun somme(): Double {
        var tab = sections.map { it.longueur }
        return tab.sum()
    }

    // TODO: Implement the ViewModel
}