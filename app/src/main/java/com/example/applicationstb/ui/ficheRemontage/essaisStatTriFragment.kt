package com.example.applicationstb.ui.ficheRemontage

import android.os.Build
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
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        var layout = inflater.inflate(R.layout.fragment_essais_stat_tri, container, false)
        var fiche = viewModel.selection.value!!
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
            if (fiche.isolementPhase !== null) spiIsoP.setSelection(arrayOf<String>(" ","500","1000","2500","5000").indexOf(fiche.isolementPhase!!.toInt().toString()))
          /*  if (fiche.isolementPMStatorU !== null) isoPMSU.setText(fiche.isolementPMStatorU.toString())
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
        spiIsoPM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if(spiIsoPM.selectedItem.toString() !== " ") fiche.isolementPhaseMasse = spiIsoPM.selectedItem.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.quickSave()
            }
        }*/
        spiIsoP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             /*   if(spiIsoP.selectedItem.toString() !== " ") fiche.isolementPhase = spiIsoP.selectedItem.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.quickSave()*/
            }
        }
        var regex = Regex.fromLiteral("""\d{0,2}(\.\d{1,2})?""")
        /*isoPMSU.doAfterTextChanged {
            if (isoPMSU.text.isNotEmpty()) fiche.isolementPMStatorU = isoPMSU.text.toString().toFloat()
            Log.i("INFO","Isopmsu stat = ${fiche.isolementPMStatorU}")
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPMSV.doAfterTextChanged {
            if (isoPMSV.text.isNotEmpty()) fiche.isolementPMStatorV = isoPMSV.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPMSW.doAfterTextChanged {
            if (isoPMSW.text.isNotEmpty()) fiche.isolementPMStatorW = isoPMSW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPMRU.doAfterTextChanged {
            if (isoPMRU.text.isNotEmpty()) fiche.isolementPMRotorU = isoPMRU.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPMRV.doAfterTextChanged {
            if (isoPMRV.text.isNotEmpty()) fiche.isolementPMRotorV = isoPMRV.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPMRW.doAfterTextChanged {
            if (isoPMRW.text.isNotEmpty()) fiche.isolementPMRotorW = isoPMRW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPSUV.doAfterTextChanged {
            if (isoPSUV.text.isNotEmpty()) fiche.isolementPhaseStatorUV = isoPSUV.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPSUW.doAfterTextChanged {
            if (isoPSUW.text.isNotEmpty()) fiche.isolementPhaseStatorUW = isoPSUW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPSVW.doAfterTextChanged {
            if (isoPSVW.text.isNotEmpty() ) fiche.isolementPhaseStatorVW = isoPSVW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPRUV.doAfterTextChanged {
            if (isoPRUV.text.isNotEmpty() ) fiche.isolementPhaseRotorUV = isoPRUV.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPRUW.doAfterTextChanged {
            if (isoPRUW.text.isNotEmpty() ) fiche.isolementPhaseRotorUW = isoPRUW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        isoPRVW.doAfterTextChanged {
            if (isoPRVW.text.isNotEmpty() ) fiche.isolementPhaseRotorVW = isoPRVW.text.toString().toFloat()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }*/
        resV.doAfterTextChanged {
            if (resV.text.isNotEmpty() ) fiche.resistanceStatorV = resV.text.toString()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        resU.doAfterTextChanged {
            if (resU.text.isNotEmpty() ) fiche.resistanceStatorU = resU.text.toString()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        resW.doAfterTextChanged {
            if (resW.text.isNotEmpty() ) fiche.resistanceStatorW = resW.text.toString()
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.quickSave()
        }
        return layout
    }

    companion object {

        @JvmStatic
        fun newInstance() = essaisStatTriFragment()
    }
}