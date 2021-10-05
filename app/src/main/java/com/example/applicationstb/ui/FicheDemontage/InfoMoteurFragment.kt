package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
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

    // TODO: Rename and change types of parameters


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
        aspectExt.adapter = adaptExt
        aspectBte.adapter = adaptExt

        viewModel.selection.observe(viewLifecycleOwner, {
            if (it.marque !== null) marque.setText(it.marque)
            if (it.numSerie !== null) num.setText(it.numSerie!!.toString())
            if (it.puissance !== null) puissance.setText(it.puissance.toString())
            if (it.bride !== null) bride.setText(it.bride.toString())
            if (it.vitesse !== null) vitesse.setText(it.vitesse.toString())
            if (it.clavette !== null) clavette.setChecked(it.clavette!!)
            if (it.arbreSortantEntrant !== null) arbre.setChecked(it.arbreSortantEntrant!!)
            if (it.accouplement !== null) accouplement.setChecked(it.accouplement!!)
            if (it.coteAccouplement !== null) cote.setText(it.coteAccouplement.toString())
            if (it.aspect !== null) aspectExt.setSelection(it.aspect!!-1)
            if (it.aspectInterieur !== null) aspectBte.setSelection(it.aspectInterieur!!-1)
        })

        marque.doAfterTextChanged {
            viewModel.selection.value!!.marque = marque.text.toString()
        }
        num.doAfterTextChanged {
            viewModel.selection.value!!.numSerie = num.text.toString().toInt()
        }
        puissance.doAfterTextChanged {
            viewModel.selection.value!!.puissance = puissance.text.toString().toFloat()
        }
        bride.doAfterTextChanged {
            viewModel.selection.value!!.bride = bride.text.toString().toFloat()
        }
        cote.doAfterTextChanged {
            viewModel.selection.value!!.coteAccouplement = cote.text.toString()
        }
        vitesse.doAfterTextChanged {
            viewModel.selection.value!!.vitesse = vitesse.text.toString().toFloat()
        }
        cote.doAfterTextChanged {
            viewModel.selection.value!!.coteAccouplement = cote.text.toString()
        }
        clavette.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.clavette = isChecked
        }
        arbre.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.arbreSortantEntrant = isChecked
        }
        accouplement.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.accouplement = isChecked
        }
        aspectBte.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.aspectInterieur = position+1
            }
        }
        aspectExt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.aspect = position+1
            }
        }



        return layout
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TriphaseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TriphaseFragment()
    }



}