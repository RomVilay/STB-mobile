package com.example.applicationstb.ui.accueil

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.Fiche
import com.example.applicationstb.model.User
import com.example.applicationstb.repository.FichesResponse
import com.example.applicationstb.repository.LoginResponse
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.connexion.ConnexionDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccueilViewModel : ViewModel() {
    var repository = Repository();
    var token: String? = null
    var username: String? = null
    var fiches: Array<Fiche>? = null
    fun listeFiches(token: String, userid: String){
        val resp = repository.getFichesUser(token, userid, object: Callback<FichesResponse> {
            override fun onResponse(call: Call<FichesResponse>, response: Response<FichesResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    Log.i("INFO","${resp!!.fiches!!.size}")
                    if (resp != null) {
                       fiches = resp.fiches
                        Log.i("INFO","fiches : ${resp.fiches}")
                    }
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<FichesResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })
    }
    fun toChantier(view: View){
        /*if (fiches !== null){
            var tab = fiches.filter { it.type == 1 }
            for (fiche in tab) {
                Log.i("fiche n°: ${fiche.numFiche} - client: ${fiche.client} ")
            }
        }*/
        //Navigation.findNavController(view).navigate(R.id.versFicheChantier)
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