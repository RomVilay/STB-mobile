package com.example.applicationstb.ui.FicheDemontage

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import org.json.JSONArray

class FicheDemontageViewModel : ViewModel() {
    var listeDemontages = arrayListOf<Fiche>()
    var client = Client(0,"Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var tech = User("0","Dumont","Toto",1,"toto","toto","0")
    val selection : MutableLiveData<Fiche> by lazy {
        MutableLiveData<Fiche>()
    }
    init{
        var i = 0;
        var fiche: Fiche ? = null
        while (i <= 5){
            when (i) {
                0 -> listeDemontages.add(Monophase(
                    i.toString(),
                    i.toString(),
                    client,
                    "Dupond M.",
                    3369077543,
                    arrayOf<User>(tech),
                    tech))
                1 -> listeDemontages.add(Triphase(
                        i.toString(),
                        i.toString(),
                        client,
                        "Dupond M.",
                        3369077543,
                        arrayOf<User>(tech),
                        tech
                ))
                2 -> listeDemontages.add(RotorBobine( i.toString(),
                        i.toString(),
                        client,
                        "Dupond M.",
                        3369077543,
                        arrayOf<User>(tech),
                        tech))
                3 -> listeDemontages.add(CourantContinu( i.toString(),
                        i.toString(),
                        client,
                        "Dupond M.",
                        3369077543,
                        arrayOf<User>(tech),
                        tech))
                4 -> listeDemontages.add(Alternateur( i.toString(),
                        i.toString(),
                        client,
                        "Dupond M.",
                        3369077543,
                        arrayOf<User>(tech),
                        tech))
                5 -> listeDemontages.add(DemontagePompe( i.toString(),
                        i.toString(),
                        client,
                        "Dupond M.",
                        3369077543,
                        arrayOf<User>(tech),
                        tech))
            }
            Log.i("INFO", "fiche n°${listeDemontages[i].numFiche}")
            i=i+1;
        }
    }
    fun select (i:Int){
        selection.value =listeDemontages[i]
        selection.value?.let { afficherFiche(it) }
    }
    fun afficherFiche(fiche:Fiche){
        when (fiche){
            is Monophase -> Log.i("Info","Type monophasé")
            is Triphase -> Log.i("Info", "Type triphasé")
            is RotorBobine -> Log.i("INFO","Type Rotor Bobine")
            is CourantContinu -> Log.i("INFO", "Type Courant Continu")
            is Alternateur -> Log.i("INFO","type alternateur")
            is DemontagePompe -> Log.i("Info", "type pompe")
        }
    }
    fun retour(view:View){
        var action = FicheDemontageDirections.deDemontageversAccueil("Token")
        Navigation.findNavController(view).navigate(action)
    }

}