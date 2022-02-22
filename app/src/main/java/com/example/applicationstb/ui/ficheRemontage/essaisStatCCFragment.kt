package com.example.applicationstb.ui.ficheRemontage

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.applicationstb.model.RemontageCourantC


class essaisStatCCFragment : Fragment() {
    private val viewModel: FicheRemontageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_essais_stat_c_c, container, false)
        val spiInductMasse = layout.findViewById<Spinner>(R.id.spinnerIsoInduMasse)
        spiInductMasse.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiInduiMasse = layout.findViewById<Spinner>(R.id.spinnerIsoIndui)
        spiInduiMasse.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiInduiInduc = layout.findViewById<Spinner>(R.id.spinnerIntuiInduc)
        spiInduiInduc.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        var resistanceInducteurs = layout.findViewById<EditText>(R.id.valResInducts)
        var resistanceInduit = layout.findViewById<EditText>(R.id.valResInduit)
        var releveIsoInducteursMasse = layout.findViewById<EditText>(R.id.valIsoInductMasse)
        var releveIsoInduitMasse = layout.findViewById<EditText>(R.id.valIsoInduitMasse)
        var releveIsoInduitInducteurs = layout.findViewById<EditText>(R.id.valIsoInduitInduct)
        var fiche = viewModel.selection.value as RemontageCourantC
        if (fiche.isolementInducteursMasse !== null ) spiInductMasse.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementInducteursMasse!!.toInt().toString()))
        if (fiche.isolementInduitMasse !== null ) spiInduiMasse.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementInduitMasse!!.toInt().toString()))
        if (fiche.isolementInduitInducteurs !== null ) spiInduiInduc.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementInduitInducteurs!!.toInt().toString()))
        if (fiche.resistanceInduit !== null ) resistanceInduit.setText(fiche.resistanceInduit.toString())
        if (fiche.resistanceInducteurs !== null ) resistanceInducteurs.setText(fiche.resistanceInducteurs.toString())
        if (fiche.releveIsoInducteursMasse !== null ) releveIsoInducteursMasse.setText(fiche.releveIsoInducteursMasse.toString())
        if (fiche.releveIsoInduitInducteurs !== null ) releveIsoInduitInducteurs.setText(fiche.releveIsoInduitInducteurs.toString())
        if (fiche.releveIsoInduitMasse !== null ) releveIsoInduitMasse.setText(fiche.releveIsoInduitMasse.toString())

        spiInductMasse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spiInductMasse.selectedItem.toString() !== " ") fiche.isolementInducteursMasse = spiInductMasse.selectedItem.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.quickSave()
            }
        }
        spiInduiInduc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spiInduiInduc.selectedItem.toString() !== " ") fiche.isolementInduitInducteurs = spiInduiInduc.selectedItem.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.quickSave()
            }
        }
        spiInduiMasse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spiInduiMasse.selectedItem.toString() !== " ") fiche.isolementInduitMasse = spiInduiMasse.selectedItem.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.quickSave()
            }
        }
        resistanceInduit.doAfterTextChanged {
            if (resistanceInduit.text.isNotEmpty()) fiche.resistanceInduit = resistanceInduit.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        resistanceInducteurs.doAfterTextChanged {
            Log.i("INFO","resInduct change")
            if (resistanceInducteurs.text.isNotEmpty()) fiche.resistanceInducteurs = resistanceInducteurs.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        releveIsoInducteursMasse.doAfterTextChanged {
            if (releveIsoInducteursMasse.text.isNotEmpty()) fiche.releveIsoInducteursMasse = releveIsoInducteursMasse.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        releveIsoInduitInducteurs.doAfterTextChanged {
            if (releveIsoInduitInducteurs.text.isNotEmpty()) fiche.releveIsoInduitInducteurs = releveIsoInduitInducteurs.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        releveIsoInduitMasse.doAfterTextChanged {
            if (releveIsoInduitMasse.text.isNotEmpty()) fiche.releveIsoInduitMasse = releveIsoInduitMasse.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        return layout
    }

    companion object {
        @JvmStatic
        fun newInstance() = essaisStatCCFragment()
    }
}