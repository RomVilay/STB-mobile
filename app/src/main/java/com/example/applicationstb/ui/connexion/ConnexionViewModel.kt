package com.example.applicationstb.ui.connexion

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.User
import com.example.applicationstb.repository.LoginResponse
import com.example.applicationstb.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnexionViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var user:User? = null
    var repository = Repository();
    fun toAccueil(view: View) {
        //Log.i("INFO","click vers Accueil - ${user?.username}")
        Navigation.findNavController(view).navigate(R.id.versAccueil)

    }
    fun login(username: String,psw: String, view: View){
        val resp = repository.logUser(username,psw,object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if ( response.code() == 200 ) {
                    val resp = response.body()
                    if (resp != null) {
                        user = resp.user
                        user?.token = resp.token
                        Log.i("INFO","connecté - token ${user?.token}")
                        val action = user?.token?.let { it1 -> ConnexionDirections.versAccueil(it1) }
                        if (action != null) {
                            Navigation.findNavController(view).navigate(action)
                        }
                        //toAccueil(view)
                    }
                } else {
                    Log.i("INFO","code : ${response.code()} - erreur : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Error","erreur ${t.message}")
            }
        })

       /* if (resp != null) {
            Log.i("INFO", user?.token)
        }*/
    }
}