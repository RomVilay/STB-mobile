package com.example.applicationstb.ui.ficheRemontage

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.model.*
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageDirections

class FicheRemontageViewModel : ViewModel() {
    var listeRemontages = arrayListOf<Fiche>()
    var client = Client("0","Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var tech = User("0","Dumont","Toto",1,"toto","toto","0")
    val selection : MutableLiveData<Fiche> by lazy {
        MutableLiveData<Fiche>()
    }
    init{
       /* var i = 0;
        var fiche: Fiche ? = null
        while (i <= 1){
            when (i) {
                0 -> listeRemontages.add(
                    RemontageTriphase(
                    i.toString(),
                    i.toString(),
                    client,
                    "Dupond M.",
                    3369077543,
                    arrayOf<User>(tech),
                    tech)
                )
                1 -> listeRemontages.add(
                    RemontageCourantC(
                    i.toString(),
                    i.toString(),
                    client,
                    "Dupond M.",
                    3369077543,
                    arrayOf<User>(tech),
                    tech
                )
                )
            }
            Log.i("INFO", "fiche n°${listeRemontages[i].numFiche}")
            i=i+1;
        }*/
    }

    fun select (i:Int){
        selection.value =listeRemontages[i]
        //selection.value?.let { afficherFiche(it) }
    }
    fun retour(view: View){
        var action = FicheRemontageDirections.deRemontageverAccueil("Token")
        Navigation.findNavController(view).navigate(action)
    }
    fun enregistrer(view: View){
        var action = FicheRemontageDirections.deRemontageverAccueil("Token")
        Navigation.findNavController(view).navigate(action)
    }
}