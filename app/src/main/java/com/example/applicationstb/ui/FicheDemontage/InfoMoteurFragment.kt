package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.applicationstb.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class InfoMoteurFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_info_moteur, container, false)
        var marque = layout.findViewById<EditText>(R.id.marc)
        var num = layout.findViewById<EditText>(R.id.numSerie)
        var puissance = layout.findViewById<EditText>(R.id.pmoteur)
        var bride = layout.findViewById<EditText>(R.id.bride)
        var vitesse = layout.findViewById<EditText>(R.id.vitesseM)
        var clavette = layout.findViewById<Switch>(R.id.pclav)
        var arbre = layout.findViewById<Switch>(R.id.etatArbre)
        var accouplement = layout.findViewById<Switch>(R.id.pacc)
        var cote = layout.findViewById<EditText>(R.id.cacc)
        var aspectExt = layout.findViewById<Spinner>(R.id.enumaspect)
        var aspectBte = layout.findViewById<Spinner>(R.id.enumboite)
        var optionsAsp = arrayOf<String>("propre","sale","très sale")
        var adaptExt = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,optionsAsp)
        var typeMoteur = layout.findViewById<EditText>(R.id.typeMoteur)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        aspectExt.adapter = adaptExt
        aspectBte.adapter = adaptExt

        viewModel.selection.observe(viewLifecycleOwner, {
            if (it.marque !== null) marque.setText(it.marque)
            if (it.numSerie !== null) num.setText(it.numSerie!!.toString())
            if (it.puissance !== null) puissance.setText(it.puissance.toString())
            if (it.bride !== null) bride.setText(it.bride.toString())
            if (it.vitesse !== null) vitesse.setText(it.vitesse.toString())
            if (it.clavette !== null) clavette.setChecked(it.clavette!!) else it.clavette = false
            if (it.arbreSortantEntrant !== null) arbre.setChecked(it.arbreSortantEntrant!!) else it.arbreSortantEntrant = false
            if (it.accouplement !== null) accouplement.setChecked(it.accouplement!!) else it.accouplement = false
            if (it.coteAccouplement !== null) cote.setText(it.coteAccouplement.toString())
            if (it.aspect !== null) aspectExt.setSelection(it.aspect!!-1)
            if (it.aspectInterieur !== null) aspectBte.setSelection(it.aspectInterieur!!-1)
            if (it.typeMoteur !== null) typeMoteur.setText(it.typeMoteur)
        })
        if (viewModel.selection.value?.status!! == 3L) {
            marque.isEnabled = false
            num.isEnabled = false
            puissance.isEnabled = false
            bride.isEnabled = false
            vitesse.isEnabled = false
            clavette.isEnabled = false
            arbre.isEnabled = false
            accouplement.isEnabled = false
            cote.isEnabled = false
            aspectExt.isEnabled = false
            aspectBte.isEnabled = false
            typeMoteur.isEnabled = false
        }
        if (viewModel.selection.value?.status!! < 3L) {
            marque.doAfterTextChanged {
                if (marque.text.isNotEmpty()) viewModel.selection.value!!.marque =
                    marque.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            num.doAfterTextChanged {
                if (num.text.isNotEmpty()) viewModel.selection.value!!.numSerie =
                    num.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            puissance.doAfterTextChanged {
                if (puissance.text.isNotEmpty() && puissance.text.matches(regexNombres) && puissance.hasFocus()) viewModel.selection.value!!.puissance =
                    puissance.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            bride.doAfterTextChanged {
                if (bride.text.isNotEmpty() && bride.text.matches(regexNombres) && bride.hasFocus()) viewModel.selection.value!!.bride =
                    bride.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            vitesse.doAfterTextChanged {
                if (vitesse.text.isNotEmpty() && vitesse.text.matches(regexNombres) && vitesse.hasFocus()) viewModel.selection.value!!.vitesse =
                    vitesse.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            clavette.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.clavette = isChecked
                viewModel.getTime()
                viewModel.localSave()
            }
            arbre.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.arbreSortantEntrant = isChecked
                viewModel.getTime()
                viewModel.localSave()
            }
            cote.doAfterTextChanged {
                Log.i("INFO", "${cote.text} - ${viewModel.selection.value!!.coteAccouplement}")
                if (cote.text.isNotEmpty()) viewModel.selection.value!!.coteAccouplement =
                    cote.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
            accouplement.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.accouplement = isChecked
                viewModel.getTime()
                viewModel.localSave()
            }
            aspectBte.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selection.value!!.aspectInterieur = position + 1
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            aspectExt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.selection.value!!.aspect = position + 1
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            typeMoteur.doAfterTextChanged {
                if (typeMoteur.text.isNotEmpty())
                viewModel.selection.value!!.typeMoteur = typeMoteur.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        return layout
    }



}