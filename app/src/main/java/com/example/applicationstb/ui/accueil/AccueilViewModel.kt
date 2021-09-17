package com.example.applicationstb.ui.accueil

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.User

class AccueilViewModel : ViewModel() {
    var token: String? = null
    fun toChantier(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheChantier)
    }
    fun toFicheD(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheD)
    }
    fun toFicheR(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheRemontage)
    }
    fun toBobinage(view: View){
        Navigation.findNavController(view).navigate(R.id.versFicheBobinage)
    }
    fun toDeconnexion(view: View){
        Navigation.findNavController(view).navigate(R.id.versConnexion)
    }
    // TODO: Implement the ViewModel
}