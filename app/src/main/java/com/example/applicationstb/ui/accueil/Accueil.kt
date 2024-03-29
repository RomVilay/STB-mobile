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
import androidx.lifecycle.lifecycleScope
import com.example.applicationstb.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
        viewModel.token.value = arguments?.get("Token") as? String
        viewModel.userId = arguments?.get("Username") as? String
        val reload = layout.findViewById<Button>(R.id.reload)
        val send = layout.findViewById<Button>(R.id.send)
        if (viewModel.token !== null && viewModel.userId !== null && viewModel.isOnline(viewModel.context) && viewModel.token.value !== "" && viewModel.fiches == null) {
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
                var job2 = launch {
                    viewModel.listeFiches(viewModel.token.value!!, viewModel.userId.toString())
                }
                delay(200)
                job2.join()
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                    "Liste des fiches mise à jour.",
                    3600
                )
                mySnackbar.show()
            }

        } else {
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
        val loading = layout.findViewById<CardView>(R.id.loadingHome)
        val sharedPref = activity?.getSharedPreferences(
            "identifiants", Context.MODE_PRIVATE
        )
        var login = sharedPref?.getString("login", "")
        var pwd = sharedPref?.getString("password", "")
        val exit = layout.findViewById<TextView>(R.id.exit)
        val btnPtn = layout.findViewById<ToggleButton>(R.id.btnPointage)
        val listePointage = layout.findViewById<TextView>(R.id.listePtn)
        viewModel.tracking.observe(viewLifecycleOwner, {
            btnPtn.setChecked(viewModel.tracking.value!!)
        })
        btnPtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                viewModel.sendPointage()
            }
        }
        listePointage.setOnClickListener {
            if (viewModel.isOnline(requireContext())){
                loading.visibility = View.VISIBLE
                lifecycleScope.launch(Dispatchers.IO) {
                    async {  viewModel.updatePointages()}.await()
                    withContext(Dispatchers.Main){
                        loading.visibility = View.GONE
                        viewModel.toPointages(layout)
                    }
                }
            } else {
                viewModel.toPointages(layout)
            }
        }
        deco.setOnClickListener {
            val alertDialogBuilder: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("se Déconnecter ",
                        DialogInterface.OnClickListener { dialog, id ->
                            sharedPref?.edit {
                                putString("login", "")
                                putString("password", "")
                                putBoolean("connected", false)
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
            if (loading.visibility == View.GONE) loading.visibility = View.VISIBLE
            if (viewModel.isOnline(viewModel.context)) {
                if (viewModel.token.value == "" ) {
                    if (login == "" && pwd == "") {
                        val dialogBuilder = AlertDialog.Builder(context)
                        val inflater = requireActivity().layoutInflater
                        val view = inflater.inflate(R.layout.connexion_dialog, null)
                        val log = view.findViewById<EditText>(R.id.login)
                        val pass = view.findViewById<EditText>(R.id.pass)
                        dialogBuilder
                            .setCancelable(true)
                            .setView(view)
                            .setPositiveButton(
                                "Connexion",
                                DialogInterface.OnClickListener { dialog, id ->
                                    if (log.text.isEmpty()) {
                                        val mySnackbar = Snackbar.make(
                                            layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                            "Veuillez Saisir votre pseudo utilisateur",
                                            3600
                                        )
                                        mySnackbar.show()
                                    }
                                    if (pass.text.isEmpty()) {
                                        val mySnackbar = Snackbar.make(
                                            layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                            "Veuillez Saisir votre mot de passe",
                                            3600
                                        )
                                        mySnackbar.show()
                                    }
                                    if (pass.text.length < 5) {
                                        val mySnackbar = Snackbar.make(
                                            layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                            "Veuillez vérifier votre mot de passe ( 5 caractères minimum)",
                                            3600
                                        )
                                        mySnackbar.show()
                                    } else {
                                        if (sharedPref != null) {
                                            sharedPref.edit {
                                                putString("login", log.text.toString())
                                                putString("password", pass.text.toString())
                                            }
                                        }
                                        viewModel.connection(
                                            log.text.toString(),
                                            pass.text.toString()
                                        )
                                    }
                                    dialog.dismiss()
                                })
                        val alert = dialogBuilder.create()
                        alert.show()
                    } else {
                        if (login != null && pwd != null) {
                            viewModel.reloadWconnection()
                            val mySnackbar = Snackbar.make(
                                layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                "Liste des fiches mise à jour.",
                                3600
                            )
                            mySnackbar.show()
                        }
                    }
                }
                if (viewModel.token.value !== "") {
                    viewModel.listeFiches(viewModel.token.value!!, login!!)
                    val mySnackbar = Snackbar.make(
                        layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                        "Liste des fiches mise à jour.",
                        3600
                    )
                    mySnackbar.show()
                } else {
                    val mySnackbar = Snackbar.make(
                        layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                        "Vous ne pouvez pas recharger les fiches , veuillez vous reconnecter à portée du wifi",
                        3600
                    )
                    mySnackbar.show()
                }
            }
            loading.visibility = View.GONE
        }
        send.setOnClickListener {
            if (viewModel.isOnline(viewModel.context)) {
                runBlocking {
                    if (viewModel.token.value == "") {
                        if (login == "" && pwd == "") {
                            val dialogBuilder = AlertDialog.Builder(context)
                            val inflater = requireActivity().layoutInflater
                            val view = inflater.inflate(R.layout.connexion_dialog, null)
                            val log = view.findViewById<EditText>(R.id.login)
                            val pass = view.findViewById<EditText>(R.id.pass)
                            dialogBuilder
                                .setCancelable(true)
                                .setView(view)
                                .setPositiveButton(
                                    "Connexion",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        if (log.text.isEmpty()) {
                                            val mySnackbar = Snackbar.make(
                                                layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                                "Veuillez Saisir votre pseudo utilisateur",
                                                3600
                                            )
                                            mySnackbar.show()
                                        }
                                        if (pass.text.isEmpty()) {
                                            val mySnackbar = Snackbar.make(
                                                layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                                "Veuillez Saisir votre mot de passe",
                                                3600
                                            )
                                            mySnackbar.show()
                                        }
                                        if (pass.text.length < 5) {
                                            val mySnackbar = Snackbar.make(
                                                layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                                                "Veuillez vérifier votre mot de passe ( 5 caractères minimum)",
                                                3600
                                            )
                                            mySnackbar.show()
                                        } else {
                                            if (sharedPref != null) {
                                                sharedPref.edit {
                                                    putString("login", log.text.toString())
                                                    putString("password", pass.text.toString())
                                                }
                                            }
                                            viewModel.saveWconnection(requireContext(),loading)
                                        }
                                        dialog.dismiss()
                                    })
                            val alert = dialogBuilder.create()
                            alert.show()
                        } else {
                            if (login !== null && pwd !== null) {
                                viewModel.saveWconnection(requireContext(),loading)
                                /*lifecycleScope.launch(Dispatchers.IO) {
                                       var job = launch { viewModel.connection(login, pwd) }
                                        job.join()
                                    }
                                    viewModel.sendFiche(loading)*/
                            }
                        }
                    }
                    else viewModel.sendFiche(loading, viewModel.token.value!!)
                }
            } else {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                    "Vous ne pouvez pas envoyer les fiches, veuillez vous reconnecter à portée du wifi",
                    3600
                )
                mySnackbar.show()
            }
        }
        dm.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var index = viewModel.nbFichesDemontage()
                    if (index > 0) {
                        withContext(Dispatchers.Main) {
                            viewModel.toFicheD(layout)
                        }
                    } else {
                        val mySnackbar = Snackbar.make(
                            layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                            "Vous n'avez pas de Demontages attribués",
                            3600
                        )
                        mySnackbar.show()
                    }
            }
        }
        cht.setOnClickListener {
            Log.i("info", viewModel.chantiers.size.toString())
            if (viewModel.chantiers.size > 0) {
                viewModel.toChantier(layout)
            } else {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                    "Vous n'avez pas de chantiers attribués",
                    3600
                )
                mySnackbar.show()
            }
        }
        rm.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var index = viewModel.nbFichesRemontage()
                if (index > 0) {
                    withContext(Dispatchers.Main) {
                        viewModel.toFicheR(layout)
                    }
                } else {
                    val mySnackbar = Snackbar.make(
                        layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                        "Vous n'avez pas de Demontages attribués",
                        3600
                    )
                    mySnackbar.show()
                }
            }
        }
        rb.setOnClickListener {
            if (viewModel.bobinages.size > 0) {
                viewModel.toBobinage(layout)
            } else {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),
                    "Vous n'avez pas de bobinages attribués",
                    3600
                )
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
            if (sharedPref != null) {
                sharedPref.edit {
                    putBoolean("connected", false)
                }
            }
            activity?.finish()
        }
        var suppr = layout.findViewById<Button>(R.id.buttonSuppr2)
        suppr.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
               viewModel.repository.deleteAllPointages("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYxNDM1ZTFhMjI3MzI1MGZlZDI1OTg1NyIsImlhdCI6MTY1MjE3Njc5NX0.8zszEqomoauOKPB8QSlGQaMPH-DAbn-YOsqbZYIApCU",viewModel.sharedPref.getString("userId","")!!)
            }
            Log.i("info", " token ${viewModel.token.value!!}")
        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}