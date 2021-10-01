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
           viewModel.setCouplage(txtclp.text.toString())
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
                    viewModel.setCouplage(selection)
                }
            }
        }
        //etats flasque
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("N/A","OK","A contrôler","A rebaguer"))
        etatFlasqueAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setFlasques(position+1,"av")
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
                viewModel.setFlasques(position+1,"ar")
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
                    viewModel.setPRoulements("av",position)
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
                viewModel.setPRoulements("ar",position)
                //Log.i("INFO", "roulement arrière:"+)
            }
        }
        //etat bout arbre
        var etatBA = layout.findViewById<Switch>(R.id.switchBA)
        etatBA.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setEtatBA(isChecked)
        }
        //roulements
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        switchRoullements.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (fiche.typeRoulementArriere == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementArriere) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeRoulement.setSelection(type)
                refRoul.setText(fiche.refRoulementArriere)
            } else {
                var type = if (fiche.typeRoulementAvant == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAvant) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeRoulement.setSelection(type)
                refRoul.setText(fiche.refRoulementAvant)
            }
        }
        typeRoulement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeRoulement.selectedItem.toString()
                if (switchRoullements.isChecked) {
                    viewModel.setRoulAr(selection)
                    Log.i("INFO", "roulement arrière:"+fiche.refRoulementArriere)
                } else {
                    viewModel.setRoulAv(selection)
                    Log.i("INFO", "roulement avant:"+selection)
                }
            }

        }
        refRoul.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (switchRoullements.isChecked) viewModel.setRefRoul("ar",refRoul.text.toString()) else viewModel.setRefRoul("av",refRoul.text.toString())
            }
        }
        //joints
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("simple lèvre","double lèvre"))
        var switchJoints = layout.findViewById<Switch>(R.id.switchJoints)
        var refJoints = layout.findViewById<EditText>(R.id.refJoints)
        switchJoints.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (fiche.typeJointArriere == null) 0 else arrayOf<String>("simple lèvre","double lèvre").indexOf(fiche.typeJointArriere) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeJoints.setSelection(type)
                refJoints.setText(fiche.refJointArriere)
            } else {
                var type = if (fiche.typeJointAvant == null) 0 else arrayOf<String>("simple lèvre","double lèvre").indexOf(fiche.typeJointAvant) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeJoints.setSelection(type)
                refJoints.setText(fiche.refJointAvant)
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
        var vent = layout.findViewById<Spinner>(R.id.spiVentilateur)
        vent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Absent"))
        var socle = layout.findViewById<Spinner>(R.id.spiSocle)
        socle.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        var capot = layout.findViewById<Spinner>(R.id.spiCap)
        capot.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        var plaque = layout.findViewById<Spinner>(R.id.spiPla)
        plaque.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Sortie par câbles"))
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