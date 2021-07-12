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
        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)
        //var autreType = layout.findViewById<EditText>(R.id.autreCpl)
        couplage.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Y","Δ","Autre"))
        couplage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = couplage.selectedItem.toString()
                //if (selection !== "Autre") {
                    fiche.couplage = selection
                    Log.i("INFO", fiche.couplage)
                /*} else {
                    autreType.visibility = View.VISIBLE
                }*/
            }

        }
        //couplage.setSelection(arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage))
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var etatFlasqueArrière = layout.findViewById<Spinner>(R.id.spiFAr)
        etatFlasqueArrière.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var roulementAvant = layout.findViewById<Spinner>(R.id.spiRAv)
        roulementAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var roulementArriere = layout.findViewById<Spinner>(R.id.spiRAr)
        roulementArriere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        switchRoullements.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (fiche.typeRoulementAr == null) 0 else arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAr) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeRoulement.setSelection(type)
                refRoul.setText(fiche.refRoulementAr)
            } else {
                var type = if (fiche.typeRoulementAv == null) 0 else arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeRoulement.setSelection(type)
                refRoul.setText(fiche.refRoulementAv)
            }
        }
        typeRoulement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeRoulement.selectedItem.toString()
                if (switchRoullements.isChecked) {
                    viewModel.setRoulAr(selection)
                    Log.i("INFO", "roulement arrière:"+fiche.refRoulementAr)
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
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("simple lèvre","double lèvre"))
        var switchJoints = layout.findViewById<Switch>(R.id.switchJoints)
        var refJoints = layout.findViewById<EditText>(R.id.refJoints)
        switchJoints.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (fiche.typeJointAr == null) 0 else arrayOf<String>("simple lèvre","double lèvre").indexOf(fiche.typeJointAr) //arrayOf<String>("","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAv).toString()
                typeJoints.setSelection(type)
                refJoints.setText(fiche.refJointAr)
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
                    viewModel.setJointAr(selection)
                    //Log.i("INFO", "roulement arrière:"+fiche.refRoulementAr)
                } else {
                    viewModel.setJointAv(selection)
                    //Log.i("INFO", "roulement avant:"+selection)
                }
            }
        }
        refJoints.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (switchJoints.isChecked) viewModel.setRefJoint("ar",refJoints.text.toString()) else viewModel.setRefJoint("av",refJoints.text.toString())
            }
        }
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