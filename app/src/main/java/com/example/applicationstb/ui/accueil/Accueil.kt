package com.example.applicationstb.ui.accueil

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.core.app.ApplicationProvider
import com.example.applicationstb.R
import com.google.android.material.snackbar.Snackbar

class Accueil : Fragment() {

    companion object {
        fun newInstance() = Accueil()
    }

    private lateinit var viewModel: AccueilViewModel

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
        deco.setOnClickListener{
            viewModel.toDeconnexion(layout)
        }
        dm.setOnClickListener {
            viewModel.toFicheD(layout)
        }
        cht.setOnClickListener {
            Log.i("INFO",token!!)
            if (viewModel.chantiers.size > 0) {
                viewModel.toChantier(layout)
            } else {
                val mySnackbar = Snackbar.make(layout.findViewById<CoordinatorLayout>(R.id.AccueilLayout),"Vous n'avez pas de chantiers attribués", 3600)
                mySnackbar.show()
            }
        }
        rm.setOnClickListener {
            viewModel.toFicheR(layout)
        }
        rb.setOnClickListener {
            if (viewModel.chantiers.size > 0) {
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