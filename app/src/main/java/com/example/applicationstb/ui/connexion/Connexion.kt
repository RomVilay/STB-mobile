package com.example.applicationstb.ui.connexion

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.edit
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.applicationstb.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class Connexion : Fragment() {
    companion object {
        fun newInstance() = Connexion()
    }

    private lateinit var viewModel: ConnexionViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref = activity?.getSharedPreferences(
            "identifiants", Context.MODE_PRIVATE)
        var login = sharedPref?.getString("login","")
        var pwd = sharedPref?.getString("password","")
        viewModel = ViewModelProvider(this).get(ConnexionViewModel::class.java)
        val user = viewModel.user
        val layout = inflater.inflate(R.layout.connexion_fragment, container, false)
        val username = layout.findViewById<EditText>(R.id.username)
        val loading = layout.findViewById<CardView>(R.id.loadingLogIn)
        val password = layout.findViewById<EditText>(R.id.psw)
        val button = layout.findViewById<Button>(R.id.button)
        if (login !== "") username.setText(login)
        if (pwd !== "") password.setText(pwd)
        button.setOnClickListener{
            runBlocking {
                if (username.text.isEmpty() ) {
                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),"Veuillez Saisir votre pseudo utilisateur", 3600)
                    mySnackbar.show()
                }
                if (password.text.isEmpty() ) {
                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),"Veuillez Saisir votre mot de passe", 3600)
                    mySnackbar.show()
                }
                if (password.text.length < 5 ) {
                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.ConnexionFrag),"Veuillez vérifier votre mot de passe ( 5 caractères minimum)", 3600)
                    mySnackbar.show()
                }
                else {
                    if (login == "") {
                        if (sharedPref !== null) {
                            sharedPref.edit {
                                putString("login",username.text.toString())
                                putString("password",password.text.toString())
                            }
                        }
                    }
                    viewModel.login(username.text.toString(), password.text.toString(), layout, loading)
                    viewModel.localGet()
                }
            }

        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}