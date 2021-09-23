package com.example.applicationstb.ui.FicheDemontage

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import org.json.JSONArray
import android.util.Log

class FicheDemontageViewModel : ViewModel() {
    var listeDemontages = arrayListOf<Fiche>()
    var client = Client("0","Dupond ets.","3369077543","8 rue truc, 31000 Toulouse")
    var tech = User("0","Dumont","Toto",1,"toto","toto","0")
    var photos = MutableLiveData<MutableList<String>>(mutableListOf())
    var schema = MutableLiveData<String>()
    val selection : MutableLiveData<Fiche> by lazy {
        MutableLiveData<Fiche>()
    }
    init{
        /*var i = 0;
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
        }*/
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
    fun setCouplage(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.couplage = type
        selection.value = fichemot
    }
    fun setFlasques(etat:String, position: String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.flasqueAvant = etat
        } else {
            fichemot.flasqueArriere = etat
        }
        selection.value = fichemot
    }
    fun setPRoulements(position:String,etat:String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.porteeravt = etat
        } else {
            fichemot.porteerar = etat
        }
        selection.value = fichemot
    }
    fun setEtatBA(etat:Boolean){
        var fichemot = selection.value as DemontageMoteur
        fichemot.boutarbre = etat
        selection.value = fichemot
    }
    fun setRoulAr(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeRoulementAr = type
        selection.value = fichemot
    }
    fun setRoulAv(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeRoulementAv = type
        selection.value = fichemot
    }
    fun setRefRoul(position:String,ref:String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.refRoulementAv = ref
        } else {
            fichemot.refRoulementAr = ref
        }
        selection.value = fichemot
    }
    fun setJointAr(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeJointAr = type
        selection.value = fichemot
    }
    fun setJointAv(type:String){
        var fichemot = selection.value as DemontageMoteur
        fichemot.typeJointAvant = type
        selection.value = fichemot
    }
    fun setRefJoint(position:String,ref:String){
        var fichemot = selection.value as DemontageMoteur
        if (position == "av") {
            fichemot.refJointAvant = ref
        } else {
            fichemot.refJointAr = ref
        }
        selection.value = fichemot
    }

    fun addPhoto(index:Int,photo: Uri) {
        photos.value!!.add(photo.toString())
    }
    fun setSchema(sch: String){
        schema.value = sch
        Log.i("INFO", sch.toString())
    }
    fun fullScreen(view: View,uri: String) {
        val action = FicheDemontageDirections.versFullScreen(uri.toString())
        Navigation.findNavController(view).navigate(action)
    }
    fun retour(view:View){
        var action = FicheDemontageDirections.deDemontageversAccueil("Token","username")
        Navigation.findNavController(view).navigate(action)
    }
    fun enregistrer(view:View){
        Log.i("Info",selection.value.toString())
        var action = FicheDemontageDirections.deDemontageversAccueil("Token","username")
        Navigation.findNavController(view).navigate(action)
    }

}