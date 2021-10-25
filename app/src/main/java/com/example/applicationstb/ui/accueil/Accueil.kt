package com.example.applicationstb.ui.accueil

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.applicationstb.R
import com.google.android.material.snackbar.Snackbar

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
            viewModel.listeFiches(viewModel.token.toString(), viewModel.username.toString())
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
        deco.setOnClickListener{
            viewModel.toDeconnexion(layout)
        }
        reload.setOnClickListener {
            if (viewModel.token !== null && viewModel.username !== null && viewModel.isOnline(viewModel.context)) {
                viewModel.listeFiches(viewModel.token.toString(), viewModel.username.toString())
            }
        }
        send.setOnClickListener {
            viewModel.sendFiche(loading)
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
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}