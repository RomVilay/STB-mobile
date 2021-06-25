package com.example.applicationstb.ui.ficheRemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.RadioButton
import android.widget.EditText
import android.widget.TextView
import com.example.applicationstb.R
import android.util.Log
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageViewModel

class FicheRemontage : Fragment() {

    companion object {
        fun newInstance() = FicheRemontage()
    }

    private lateinit var viewModel: FicheRemontageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterRemontages = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeRemontages.map { it.javaClass.name.substring(33)  })
        spinner.adapter = adapterRemontages
        //var infoMoteur = layout.findViewById<CardView>(R.id.infoMoteur)
        var btnDemontage = layout.findViewById<Button>(R.id.btnDemarrer)
        //var essaisDynamiques = layout.findViewById<CardView>(R.id.essaisDynamiques)
        btnDemontage.setOnClickListener {
            //Log.i("INFO","moteur ${viewModel.listeDemontages[spinner.selectedItemPosition].telContact}")
            viewModel.select(spinner.selectedItemPosition)
            Log.i("INFO",viewModel.selection.value?.javaClass.toString())
            //infoMoteur.visibility = View.VISIBLE
            //essaisDynamiques.visibility = View.VISIBLE

        }
        //infos moteur
        var titre = layout.findViewById<TextView>(R.id.titreRemontage)
        var spinnerMnt = layout.findViewById<Spinner>(R.id.spinnerMntRll)
        var spinnerCPA = layout.findViewById<Spinner>(R.id.spinnerCPA)
        var spinnerCIF = layout.findViewById<Spinner>(R.id.spinnerCIF)
        var fixCouronne = layout.findViewById<RadioButton>(R.id.fixCouronne)
        var isoPBRB = layout.findViewById<RadioButton>(R.id.isoPBRB)
        var isoPBV = layout.findViewById<EditText>(R.id.isoPBV)
        //var risoPBV = layout.findViewById<EditText>(R.id.risoPBV)
        titre.setOnClickListener{

        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}