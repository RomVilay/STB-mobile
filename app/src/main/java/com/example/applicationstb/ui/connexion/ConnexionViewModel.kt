package com.example.applicationstb.ui.connexion

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R

class ConnexionViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var user = arrayOf("username","password")
    fun toAccueil(view: View) {
        Log.i("INFO","click vers Accueil")
        Navigation.findNavController(view).navigate(R.id.versAccueil)
    }
}