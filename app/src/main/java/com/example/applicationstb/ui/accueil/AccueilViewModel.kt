package com.example.applicationstb.ui.accueil

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R

class AccueilViewModel : ViewModel() {
    fun toChantier(view: View){
        Log.i("INFO","click vers Chantier")
    }
    fun toFicheD(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheD)
    }
    fun toFicheM(view: View){
        Log.i("INFO","click vers Remontage")
    }
    fun toBobinage(view: View){
        Log.i("INFO","click vers Bobinage")
    }
    fun toDeconnexion(view: View){
        Navigation.findNavController(view).navigate(R.id.versConnexion)
    }
    // TODO: Implement the ViewModel
}