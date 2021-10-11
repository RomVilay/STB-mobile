package com.example.applicationstb.ui.ficheRemontage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.applicationstb.R
import com.example.applicationstb.model.RemontageTriphase
import com.example.applicationstb.model.Triphase



class essaisStatTriFragment : Fragment() {
    private val viewModel: FicheRemontageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        var layout = inflater.inflate(R.layout.fragment_essais_stat_tri, container, false)
        var fiche = viewModel.selection.value!! as RemontageTriphase
        val spiIsoPM = layout.findViewById<Spinner>(R.id.spinnerIsoPMT)
        spiIsoPM.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiIsoP = layout.findViewById<Spinner>(R.id.spinnerIsoP)
        spiIsoP.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        var isoPMSU = layout.findViewById<EditText>(R.id.isoPMSU)
        var isoPMSV = layout.findViewById<EditText>(R.id.isoPMSV)
        var isoPMSW = layout.findViewById<EditText>(R.id.isoPMSW)
        var isoPMRU = layout.findViewById<EditText>(R.id.isoPMRU)
        var isoPMRV = layout.findViewById<EditText>(R.id.isoPMRV)
        var isoPMRW = layout.findViewById<EditText>(R.id.isoPMRW)
        var isoPSUV = layout.findViewById<EditText>(R.id.isoPSUV)
        var isoPSVW = layout.findViewById<EditText>(R.id.isoPSVW)
        var isoPSUW = layout.findViewById<EditText>(R.id.isoPSUW)
        var isoPRUV = layout.findViewById<EditText>(R.id.isoPRUV)
        var isoPRVW = layout.findViewById<EditText>(R.id.isoPRVW)
        var isoPRUW = layout.findViewById<EditText>(R.id.isoPRUW)
        var resU = layout.findViewById<EditText>(R.id.ResU)
        var resV = layout.findViewById<EditText>(R.id.ResV)
        var resW = layout.findViewById<EditText>(R.id.ResW)
        viewModel.selection.observe(viewLifecycleOwner, {
            var fiche = viewModel.selection.value!! as RemontageTriphase
            if (fiche.isolementPhase !== null) spiIsoP.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementPhase!!.toInt().toString()))
            if (fiche.isolementPhaseMasse !== null) spiIsoPM.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementPhaseMasse!!.toInt().toString()))
            if (fiche.isolementPMStatorU !== null) isoPMSU.setText(fiche.isolementPMStatorU.toString())
            if (fiche.isolementPMStatorV !== null) isoPMSV.setText(fiche.isolementPMStatorV.toString())
            if (fiche.isolementPMStatorW !== null) isoPMSW.setText(fiche.isolementPMStatorW.toString())
            if (fiche.isolementPMRotorU !== null) isoPMRU.setText(fiche.isolementPMRotorU.toString())
            if (fiche.isolementPMRotorV !== null) isoPMRV.setText(fiche.isolementPMRotorV.toString())
            if (fiche.isolementPMRotorW !== null) isoPMRW.setText(fiche.isolementPMRotorW.toString())
            if (fiche.isolementPhaseStatorUV !== null) isoPSUV.setText(fiche.isolementPhaseStatorUV.toString())
            if (fiche.isolementPhaseStatorUW !== null) isoPSUW.setText(fiche.isolementPhaseStatorUW.toString())
            if (fiche.isolementPhaseStatorVW !== null) isoPSVW.setText(fiche.isolementPhaseStatorVW.toString())
            if (fiche.isolementPhaseRotorUV !== null) isoPRUV.setText(fiche.isolementPhaseRotorUV.toString())
            if (fiche.isolementPhaseRotorUW !== null) isoPRUW.setText(fiche.isolementPhaseRotorUW.toString())
            if (fiche.isolementPhaseRotorVW !== null) isoPRVW.setText(fiche.isolementPhaseRotorVW.toString())
            if (fiche.resistanceStatorU !== null) resU.setText(fiche.resistanceStatorU.toString())
            if (fiche.resistanceStatorV !== null) resV.setText(fiche.resistanceStatorV.toString())
            if (fiche.resistanceStatorW !== null) resW.setText(fiche.resistanceStatorW.toString())
        })
        spiIsoPM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if(spiIsoPM.selectedItem.toString() !== " ") fiche.isolementPhaseMasse = spiIsoPM.selectedItem.toString().toFloat()
            }
        }
        spiIsoP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spiIsoP.selectedItem.toString() !== " ") fiche.isolementPhase = spiIsoP.selectedItem.toString().toFloat()
            }
        }
        var regex = Regex.fromLiteral("\\d{0,2}(\\.\\d{1,2})?)")
        /*isoPMSU.doAfterTextChanged {
            if (isoPMSU.text.isNotEmpty() && isoPMSU.text.matches(regex)) fiche.isolementPMStatorU = isoPMSU.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPMSV.doAfterTextChanged {
            if (isoPMSV.text.isNotEmpty() && isoPMSV.text.matches(regex)) fiche.isolementPMStatorV = isoPMSV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPMSW.doAfterTextChanged {
            if (isoPMSW.text.isNotEmpty() && isoPMSW.text.matches(regex)) fiche.isolementPMStatorV = isoPMSV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPMRU.doAfterTextChanged {
            if (isoPMRU.text.isNotEmpty() && isoPMRU.text.matches(regex)) fiche.isolementPMRotorU = isoPMRU.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPMRV.doAfterTextChanged {
            if (isoPMRV.text.isNotEmpty() && isoPMRV.text.matches(regex)) fiche.isolementPMRotorU = isoPMRV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPMRW.doAfterTextChanged {
            if (isoPMRW.text.isNotEmpty() && isoPMRW.text.matches(regex)) fiche.isolementPMRotorU = isoPMRW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPSUV.doAfterTextChanged {
            if (isoPSUV.text.isNotEmpty() && isoPSUV.text.matches(regex)) fiche.isolementPhaseStatorUV = isoPSUV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPSUW.doAfterTextChanged {
            if (isoPSUW.text.isNotEmpty() && isoPSUW.text.matches(regex)) fiche.isolementPhaseStatorUW = isoPSUW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPSVW.doAfterTextChanged {
            if (isoPSVW.text.isNotEmpty() && isoPSVW.text.matches(regex)) fiche.isolementPhaseStatorVW = isoPSVW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPRUV.doAfterTextChanged {
            if (isoPRUV.text.isNotEmpty() && isoPRUV.text.matches(regex)) fiche.isolementPhaseRotorUV = isoPRUV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPRUW.doAfterTextChanged {
            if (isoPRUW.text.isNotEmpty() && isoPRUW.text.matches(regex)) fiche.isolementPhaseRotorUW = isoPRUW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        isoPRVW.doAfterTextChanged {
            if (isoPSVW.text.isNotEmpty() && isoPSVW.text.matches(regex)) fiche.isolementPhaseStatorVW = isoPSVW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        resV.doAfterTextChanged {
            if (resV.text.isNotEmpty() && resV.text.matches(regex)) fiche.resistanceStatorV = resV.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        resU.doAfterTextChanged {
            if (resU.text.isNotEmpty() && resU.text.matches(regex)) fiche.resistanceStatorU = resU.text.toString().toFloat()
            viewModel.selection.value = fiche
        }
        resW.doAfterTextChanged {
            if (resW.text.isNotEmpty() && resW.text.matches(regex)) fiche.resistanceStatorW = resW.text.toString().toFloat()
            viewModel.selection.value = fiche
        }*/
        return layout
    }

    companion object {

        @JvmStatic
        fun newInstance() = essaisStatTriFragment()
    }
}