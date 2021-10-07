package com.example.applicationstb.ui.ficheRemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R
import android.util.Log
import android.widget.*
import com.example.applicationstb.model.*
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageViewModel
import androidx.cardview.widget.CardView
import java.util.*

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
        var list = arguments?.get("listRemontages") as Array<Remontage>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeRemontages = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterRemontages = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeRemontages.map { it.numFiche  })
        spinner.adapter = adapterRemontages
        //var infoMoteur = layout.findViewById<CardView>(R.id.infoMoteur)
        var btnDemontage = layout.findViewById<Button>(R.id.btnDemarrer)
        //var essaisDynamiques = layout.findViewById<CardView>(R.id.essaisDynamiques)
        var essaisStats = layout.findViewById<FrameLayout>(R.id.essaisStatiqueslayout)
        val fragmentManager = childFragmentManager
        btnDemontage.setOnClickListener {
            //Log.i("INFO","moteur ${viewModel.listeDemontages[spinner.selectedItemPosition].telContact}")
            viewModel.start.value = Date()
            viewModel.selection.value = viewModel.listeRemontages.find { it.numFiche == spinner.selectedItem }
            var demo = viewModel.listeRemontages.find { it.numFiche == spinner.selectedItem }
            when (viewModel.selection.value){
                is RemontageTriphase -> fragmentManager.commit {
                    replace<essaisStatTriFragment>(R.id.essaisStatiqueslayout)
                }
                is RemontageCourantC ->
                {   fragmentManager.commit {
                    replace<essaisStatCCFragment>(R.id.essaisStatiqueslayout)
                    }
                }
            }
            layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisSats).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisDynamiques).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE
            layout.findViewById<EditText>(R.id.observations).visibility = View.VISIBLE
            layout.findViewById<LinearLayout>(R.id.btns).visibility = View.VISIBLE
            //infoMoteur.visibility = View.VISIBLE
            //essaisDynamiques.visibility = View.VISIBLE
        }
        //infos moteur
        var titre = layout.findViewById<TextView>(R.id.titreRemontage)
        var spinnerMnt = layout.findViewById<Spinner>(R.id.spinnerMntRll)
        spinnerMnt.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","à la presse","douille de frappe","chauffe roulement"))
        var spinnerCPA = layout.findViewById<Spinner>(R.id.spinnerCPA)
        spinnerCPA.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","avant","arrière","aucun"))
        var spinnerCIF = layout.findViewById<Spinner>(R.id.spinnerCIF)
        spinnerCIF.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","avant","arrière","aucun"))
        var fixCouronne = layout.findViewById<RadioButton>(R.id.fixCouronne)
        var isoPBRB = layout.findViewById<RadioButton>(R.id.isoPBRB)
        var isoPBV = layout.findViewById<EditText>(R.id.isoPBV)
        var risoPBV = layout.findViewById<EditText>(R.id.RisoPBV)
        viewModel.selection.observe(viewLifecycleOwner, {
            Log.i("INFO","fiche ${viewModel.selection.value!!.collageRoulementFlasque}")
            var fiche = viewModel.selection.value!!
            if(fiche.remontageRoulement !== null) spinnerMnt.setSelection(fiche.remontageRoulement!!)
            if(fiche.collageRoulementPorteeArbre !== null) spinnerCPA.setSelection(fiche.collageRoulementPorteeArbre!!)
            if(fiche.collageRoulementFlasque !== null) spinnerCIF.setSelection(fiche.collageRoulementFlasque!!)
            if(fiche.verificationFixationCouronne !== null) fixCouronne.setChecked(fiche.verificationFixationCouronne!!)
            if(fiche.verificationIsolementPorteBalais !== null) isoPBRB.setChecked(fiche.verificationIsolementPorteBalais!!)
            if(fiche.isolementPorteBalaisV !== null) isoPBV.setText(fiche.isolementPorteBalaisV!!.toString())
            if(fiche.isolementPorteBalaisOhm !== null) risoPBV.setText(fiche.isolementPorteBalaisOhm!!.toString())
        })

        titre.setOnClickListener{
        }
        var btnquitter = layout.findViewById<Button>(R.id.quit)
        var btnenregistrer = layout.findViewById<Button>(R.id.enregistrer)
        btnquitter.setOnClickListener {
            viewModel.retour(layout)
        }
        btnenregistrer.setOnClickListener {
            viewModel.enregistrer(layout)
        }


        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}