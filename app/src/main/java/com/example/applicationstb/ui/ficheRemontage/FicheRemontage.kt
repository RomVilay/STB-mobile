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
        var essaisStats = layout.findViewById<FrameLayout>(R.id.essaisStatiqueslayout)
        val fragmentManager = childFragmentManager
        btnDemontage.setOnClickListener {
            //Log.i("INFO","moteur ${viewModel.listeDemontages[spinner.selectedItemPosition].telContact}")
            viewModel.select(spinner.selectedItemPosition)
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
        //var risoPBV = layout.findViewById<EditText>(R.id.risoPBV)
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