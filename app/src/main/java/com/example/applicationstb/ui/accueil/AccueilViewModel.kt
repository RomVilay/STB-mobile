package com.example.applicationstb.ui.accueil

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.Chantier
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
                       /* for(fiche in resp!!.fiches!!) {
                            Log.i("info",fiche.type.toString())
                        }*/
                        //Log.i("INFO","fiches : ${resp.fiches}")
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
            var tab = mutableListOf<Fiche>()
                for (fiche in fiches!!) {
                    if (fiche.type == 1L) {
                        //Log.i("INFO", "fiche n°: ${fiche.numFiche} - client: ${fiche.client.enterprise} - vehicule :${fiche.vehicule}")
                        tab.add(fiche)
                    }
                }
            var tab2 = tab.toTypedArray()
        val action = AccueilDirections.versFicheChantier(tab2,token)
        Navigation.findNavController(view).navigate(action)
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