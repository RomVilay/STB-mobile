package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.applicationstb.R
import com.example.applicationstb.model.DemontageMoteur
import android.util.Log
import android.widget.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MecaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MecaFragment : Fragment() {

    private val viewModel: FicheDemontageViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var layout = inflater.inflate(R.layout.fragment_meca, container, false)
        var fiche = viewModel.selection.value as DemontageMoteur
        //couplage
        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)
        var txtclp = layout.findViewById<EditText>(R.id.autreCpl)
        txtclp.setOnFocusChangeListener { _, hasFocus ->
           viewModel.selection.value!!.couplage = txtclp.text.toString()
        }
        couplage.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Y","Δ","Autre"))
        couplage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = couplage.selectedItem.toString()
                if (couplage.selectedItem.toString() == "Autre") {
                    txtclp.visibility = View.VISIBLE
                } else {
                    txtclp.visibility = View.GONE
                    viewModel.selection.value!!.couplage = selection
                }
            }
        }
        //etats flasque
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        etatFlasqueAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.flasqueAvant = position
            }
        }
        etatFlasqueAvant.setOnFocusChangeListener{ view, hasFocus ->
            if (hasFocus) {
                etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
            } else {

            }
        }
        var etatFlasqueArrière = layout.findViewById<Spinner>(R.id.spiFAr)
        etatFlasqueArrière.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        etatFlasqueArrière.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.flasqueArriere = position
            }
        }
        // portée roulements
        var roulementAvant = layout.findViewById<Spinner>(R.id.spiRAv)
        roulementAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        roulementAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementAvant.selectedItem.toString()
                viewModel.selection.value!!.porteeRAvant = position
                    //Log.i("INFO", "roulement arrière:"+)
            }
        }
        var roulementArriere = layout.findViewById<Spinner>(R.id.spiRAr)
        roulementArriere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        roulementArriere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementArriere.selectedItem.toString()
                viewModel.selection.value!!.porteeRArriere = position
                //Log.i("INFO", "roulement arrière:"+)
            }
        }
        //etat bout arbre
        var etatBA = layout.findViewById<Switch>(R.id.switchBA)
        etatBA.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.boutArbre = isChecked
        }
        //rondelle élastique
        var PRE = layout.findViewById<Switch>(R.id.switchPRE)
        PRE.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.rondelleElastique = isChecked
        }
        //roulements
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        switchRoullements.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (viewModel.selection.value!!.typeRoulementArriere == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(viewModel.selection.value!!.typeRoulementArriere!!)
                typeRoulement.setSelection(type)
                refRoul.setText(viewModel.selection.value!!.refRoulementArriere)
            } else {
                var type = if (viewModel.selection.value!!.typeRoulementAvant == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(viewModel.selection.value!!.typeRoulementAvant!!)
                typeRoulement.setSelection(type)
                refRoul.setText(viewModel.selection.value!!.refRoulementAvant)
            }
        }
        typeRoulement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeRoulement.selectedItem.toString()
                if (switchRoullements.isChecked) {
                    viewModel.selection.value!!.typeRoulementArriere =  selection
                } else {
                    viewModel.selection.value!!.typeRoulementAvant = selection
                }
            }

        }
        refRoul.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (switchRoullements.isChecked) viewModel.selection.value!!.refRoulementArriere = refRoul.text.toString() else  viewModel.selection.value!!.refRoulementAvant = refRoul.text.toString()
            }
        }
        //joints
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("simple lèvre","double lèvre"))
        var switchJoints = layout.findViewById<Switch>(R.id.switchJoints)
        var refJoints = layout.findViewById<EditText>(R.id.refJoints)
        switchJoints.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (viewModel.selection.value!!.typeJointArriere == null) {
                    0
                } else { if (viewModel.selection.value!!.typeJointArriere == false) {
                        0
                    } else {
                    1
                 }
                }
                typeJoints.setSelection(type)
                refJoints.setText(viewModel.selection.value!!.refJointArriere)
            } else {
                var type = if (viewModel.selection.value!!.typeJointAvant == null) {
                    0
                } else {
                    if (viewModel.selection.value!!.typeJointAvant == false) {
                        0
                    } else {
                        1
                    }
                }
                    typeJoints.setSelection(type)
                refJoints.setText(viewModel.selection.value!!.refJointAvant)
            }
        }
        typeJoints.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeJoints.selectedItem.toString()
                if (switchJoints.isChecked) {
                    viewModel.setJointAr(true)
                    //Log.i("INFO", "roulement arrière:"+fiche.refRoulementArriere)
                } else {
                    viewModel.setJointAv(false)
                    //Log.i("INFO", "roulement avant:"+selection)
                }
            }
        }
        refJoints.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (switchJoints.isChecked) viewModel.setRefJoint("ar",refJoints.text.toString()) else viewModel.setRefJoint("av",refJoints.text.toString())
            }
        }
        //capot ventilateur
        var cvent = layout.findViewById<Spinner>(R.id.spiCapot)
        cvent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
           if(viewModel.selection.value!!.capotV !== null)  cvent.setSelection(viewModel.selection.value!!.capotV!!)
        var vent = layout.findViewById<Spinner>(R.id.spiVentilateur)
        vent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Absent"))
        if (viewModel.selection.value!!.ventilateur !== null) vent.setSelection(viewModel.selection.value!!.ventilateur!!)
            var socle = layout.findViewById<Spinner>(R.id.spiSocle)
        socle.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.socleBoiteABorne !== null) socle.setSelection(viewModel.selection.value!!.socleBoiteABorne!!)
            var capot = layout.findViewById<Spinner>(R.id.spiCap)
        capot.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.capotBoiteABorne !== null) capot.setSelection(viewModel.selection.value!!.capotBoiteABorne!!)
            var plaque = layout.findViewById<Spinner>(R.id.spiPla!!)
        plaque.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Sortie par câbles"))
        if (viewModel.selection.value!!.plaqueABorne !== null ) plaque.setSelection(viewModel.selection.value!!.plaqueABorne!!)
        var sondes = layout.findViewById<Switch>(R.id.switchSondes)
        if (viewModel.selection.value!!.presenceSondes !== null) { sondes.isChecked(viewModel.selection.value!!.presenceSondes!!) }  else {sondes.isChecked(false)}
        sondes.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.presenceSondes = isChecked
        }
        var typeSondes = layout.findViewById<EditText>(R.id.typeSonde)
        typeSondes.setText(viewModel.selection.value!!.typeSondes)
        typeSondes.onFocusChangeListener {
            viewModel.selection.value!!.typeSondes = typeSondes.text
        }
        var equi = layout.findViewById<Switch>(R.id.swEqui)
        if (viewModel.selection.value!!.equilibrage !== null) equi.isChecked(viewModel.selection.value!!.equilibrage)
        equi.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.equilibrage = isChecked
        }
        var peint = layout.findViewById<Switch>(R.id.swPeinture)
        if (viewModel.selection.value!!.peinture !== null) peint.isChecked(viewModel.selection.value!!.peinture)
        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MecaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MecaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}