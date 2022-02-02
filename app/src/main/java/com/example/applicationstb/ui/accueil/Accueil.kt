package com.example.applicationstb.ui.accueil

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.navigation.findNavController
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheChantier.DawingView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class Accueil : Fragment() {

    companion object {
        fun newInstance() = Accueil()
    }

    private lateinit var viewModel: AccueilViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AccueilViewModel::class.java)
        val layout = inflater.inflate(R.layout.accueil_fragment, container, false)
        viewModel.token = arguments?.get("Token") as? String
        viewModel.username = arguments?.get("Username") as? String
        if (viewModel.token !== null && viewModel.username !== null && viewModel.isOnline(viewModel.context)) {
            runBlocking {
                var job = launch {
                    var test = ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!test) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 1
                        )
                    }
                }
                 viewModel.listeFiches(viewModel.token.toString(), viewModel.username.toString())
            }
        } else {
            val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'êtes pas connecté au réseau Internet.", 3600)
            mySnackbar.show()
            viewModel.listeFicheLocal()
        }
        val deco = layout.findViewById<TextView>(R.id.btnDeco)
        val cht = layout.findViewById<Button>(R.id.btnChantier)
        val dm = layout.findViewById<TextView>(R.id.btnDemo)
        val list = layout.findViewById<LinearLayout>(R.id.list)
        val rep = layout.findViewById<TextView>(R.id.Reparations)
        val rm = layout.findViewById<TextView>(R.id.btnRemo)
        val rb = layout.findViewById<Button>(R.id.btnRebobinage)
        val token = arguments?.let { AccueilArgs.fromBundle(it).token }
        val reload = layout.findViewById<Button>(R.id.reload)
        val send = layout.findViewById<Button>(R.id.send)
        val loading = layout.findViewById<CardView>(R.id.loadingHome)
        val sharedPref = activity?.getSharedPreferences(
            "identifiants", Context.MODE_PRIVATE)
        var login = sharedPref?.getString("login","")
        var pwd = sharedPref?.getString("password","")
        val exit = layout.findViewById<TextView>(R.id.exit)

        deco.setOnClickListener{
            val alertDialogBuilder: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("se Déconnecter ",
                        DialogInterface.OnClickListener { dialog, id ->
                            sharedPref?.edit {
                                putString("login","")
                                putString("password","")
                            }
                            viewModel.toDeconnexion(layout)
                        })
                    setNegativeButton("Rester connecté",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                }
                builder.setMessage("Êtes vous sûr de vouloir vous déconnecter ?")
                builder.create()
            }
            alertDialogBuilder!!.show()
        }
        reload.setOnClickListener {
            if (viewModel.isOnline(viewModel.context)) {
                if ( viewModel.token == "" && viewModel.isOnline(viewModel.context)) {
                    if ( login == "" && pwd == "") {
                            val dialogBuilder = AlertDialog.Builder(context)
                        val inflater = requireActivity().layoutInflater
                        val view =  inflater.inflate(R.layout.connexion_dialog, null)
                        val log = view.findViewById<EditText>(R.id.login)
                        val pass = view.findViewById<EditText>(R.id.pass)
                        dialogBuilder
                            .setCancelable(true)
                            .setView(view)
                            .setPositiveButton("Connexion", DialogInterface.OnClickListener{
                                    dialog, id ->
                                if (log.text.isEmpty() ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez Saisir votre pseudo utilisateur", 3600)
                                    mySnackbar.show()
                                }
                                if (pass.text.isEmpty() ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez Saisir votre mot de passe", 3600)
                                    mySnackbar.show()
                                }
                                if (pass.text.length < 5 ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez vérifier votre mot de passe ( 5 caractères minimum)", 3600)
                                    mySnackbar.show()
                                }
                                else {
                                    if (sharedPref != null) {
                                        sharedPref.edit {
                                            putString("login", log.text.toString())
                                            putString("password", pass.text.toString())
                                        }
                                    }
                                    viewModel.connection(log.text.toString(), pass.text.toString())
                                }
                                dialog.dismiss()
                            })
                        val alert = dialogBuilder.create()
                        alert.show()
                    } else {
                        if (login != null && pwd != null) {
                            viewModel.connection(login,pwd)
                            Log.i("INFO", viewModel.token.toString())
                        }
                    }
                }
                if (viewModel.token !== "") {
                    viewModel.listeFiches(viewModel.token.toString(), login!!)
                } else {
                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous ne pouvez pas recharger les fiches , veuillez vous reconnecter à portée du wifi", 3600)
                    mySnackbar.show()
                }
            }
        }
        send.setOnClickListener {
            if (viewModel.isOnline(viewModel.context)) {
                if ( viewModel.token == null && viewModel.isOnline(viewModel.context)) {
                    if ( login == "" && pwd == "") {
                        val dialogBuilder = AlertDialog.Builder(context)
                        val inflater = requireActivity().layoutInflater
                        val view =  inflater.inflate(R.layout.connexion_dialog, null)
                        val log = view.findViewById<EditText>(R.id.login)
                        val pass = view.findViewById<EditText>(R.id.pass)
                        dialogBuilder
                            .setCancelable(true)
                            .setView(view)
                            .setPositiveButton("Connexion", DialogInterface.OnClickListener{
                                    dialog, id ->
                                if (log.text.isEmpty() ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez Saisir votre pseudo utilisateur", 3600)
                                    mySnackbar.show()
                                }
                                if (pass.text.isEmpty() ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez Saisir votre mot de passe", 3600)
                                    mySnackbar.show()
                                }
                                if (pass.text.length < 5 ) {
                                    val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Veuillez vérifier votre mot de passe ( 5 caractères minimum)", 3600)
                                    mySnackbar.show()
                                }
                                else {
                                    if (sharedPref != null) {
                                        sharedPref.edit {
                                            putString("login", log.text.toString())
                                            putString("password", pass.text.toString())
                                        }
                                    }
                                    viewModel.connection(log.text.toString(), pass.text.toString())
                                }
                                dialog.dismiss()
                            })
                        val alert = dialogBuilder.create()
                        alert.show()
                    } else {
                        if (login != null && pwd != null) {
                            viewModel.connection(login,pwd)
                        }
                    }
                }
                viewModel.sendFiche(loading)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous ne pouvez pas envoyer les fiches, veuillez vous reconnecter à portée du wifi", 3600)
                mySnackbar.show()
            }
        }
        dm.setOnClickListener {
            if (viewModel.demontages.size > 0) {
                viewModel.toFicheD(layout)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'avez pas de Demontages attribués", 3600)
                mySnackbar.show()
            }
        }
        cht.setOnClickListener {
            if (viewModel.chantiers.size > 0) {
                viewModel.toChantier(layout)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'avez pas de chantiers attribués", 3600)
                mySnackbar.show()
            }
        }
        rm.setOnClickListener {
            if (viewModel.remontages.size > 0) {
                viewModel.toFicheR(layout)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'avez pas de Remontages attribués", 3600)
                mySnackbar.show()
            }
        }
        rb.setOnClickListener {
            if (viewModel.bobinages.size > 0) {
                viewModel.toBobinage(layout)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'avez pas de bobinages attribués", 3600)
                mySnackbar.show()
            }
        }
        rep.setOnClickListener {
            if (list.visibility == View.GONE) {
                list.visibility = View.VISIBLE
            } else {
                list.visibility = View.GONE
            }
        }
        exit.setOnClickListener {
            activity?.finish()
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}