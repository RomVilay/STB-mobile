package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R


class MonophaseFragment : Fragment() {

    companion object {
        fun newInstance() = MonophaseFragment()
    }
    private val viewModel: FicheDemontageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_monophase, container, false)
        var titre1 = layout.findViewById<TextView>(R.id.titreMono)
        var titre2 = layout.findViewById<TextView>(R.id.titre2)
        var titre3 = layout.findViewById<TextView>(R.id.titre3)
        //infos générales
        var marque = layout.findViewById<EditText>(R.id.marc)
        var numSerie = layout.findViewById<EditText>(R.id.numSerie)
        var puissance = layout.findViewById<EditText>(R.id.pmoteur)
        var bride = layout.findViewById<EditText>(R.id.bride)
        var vitesse = layout.findViewById<EditText>(R.id.vitesseM)
        var arbre = layout.findViewById<Switch>(R.id.etatArbre)
        var clavette = layout.findViewById<Switch>(R.id.pclav)
        var accouplement = layout.findViewById<Switch>(R.id.pacc)
        var cote = layout.findViewById<EditText>(R.id.cacc)
        var aspect = layout.findViewById<RecyclerView>(R.id.enumaspect)
        var boite = layout.findViewById<RecyclerView>(R.id.enumboite)
        //
        var infos = layout.findViewById<CardView>(R.id.infoMoteur)
        var essais = layout.findViewById<CardView>(R.id.essais)
        var meca = layout.findViewById<CardView>(R.id.meca)
        var retour = layout.findViewById<Button>(R.id.retourMono)
        var aspectExt = layout.findViewById<Spinner>(R.id.extSpinner)
        var aspectBte = layout.findViewById<Spinner>(R.id.spinnerBo)
        var optionsAsp = arrayOf<String>("propre","sale","très sale")
        var adaptExt = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,optionsAsp)
        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)

        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.PartMeca)
            setReorderingAllowed(true)
        }
        titre1.setOnClickListener {
            var layout = infos.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            infos.layoutParams = layout
        }
        titre2.setOnClickListener {
            var layout = essais.layoutParams
            if (layout.height == 130){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 130
                Log.i("INFO","in")
            }
            essais.layoutParams = layout
        }
        titre3.setOnClickListener {
            var layout = meca.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            meca.layoutParams = layout
        }
        retour.setOnClickListener {
            viewModel.retour(layout)
        }

        return layout
    }


}