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
import androidx.core.widget.doAfterTextChanged


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
        if (fiche.couplage !== null && arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) == -1) txtclp.setText(fiche.couplage)
        txtclp.doAfterTextChanged {
           if(txtclp.text.isNotEmpty()) viewModel.selection.value!!.couplage = txtclp.text.toString()
        }
        couplage.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Y","Δ","Autre"))
        if (fiche.couplage !== null) couplage.setSelection(if (arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) >=0 ) arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) else 2)
        couplage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = couplage.selectedItem.toString()
                if (couplage.selectedItem.toString() == "Autre") {
                    txtclp.visibility = View.VISIBLE
                } else {
                    txtclp.visibility = View.GONE
                    viewModel.selection.value!!.couplage = selection
                    viewModel.localSave()
                }
            }
        }
        //etats flasque
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        if (fiche.flasqueAvant !== null) etatFlasqueAvant.setSelection(fiche.flasqueAvant!!-1)
        etatFlasqueAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.flasqueAvant = position + 1
                viewModel.getTime()
                viewModel.localSave()
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
        if (fiche.flasqueArriere !== null) etatFlasqueArrière.setSelection(fiche.flasqueArriere!!-1)
        etatFlasqueArrière.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.flasqueArriere = position + 1
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        // portée roulements
        var roulementAvant = layout.findViewById<Spinner>(R.id.spiRAv)
        roulementAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        if (fiche.porteeRAvant !== null) roulementAvant.setSelection(fiche.porteeRAvant!!-1)
        roulementAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementAvant.selectedItem.toString()
                viewModel.selection.value!!.porteeRAvant = position + 1
                viewModel.getTime()
                viewModel.localSave()
                    //Log.i("INFO", "roulement arrière:"+)
            }
        }
        var roulementArriere = layout.findViewById<Spinner>(R.id.spiRAr)
        roulementArriere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        if (fiche.porteeRArriere!== null) roulementArriere.setSelection(fiche.porteeRArriere!!-1)
        roulementArriere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementArriere.selectedItem.toString()
                viewModel.selection.value!!.porteeRArriere = position + 1
                viewModel.getTime()
                viewModel.localSave()
                //Log.i("INFO", "roulement arrière:"+)
            }
        }
        //etat bout arbre
        var etatBA = layout.findViewById<Switch>(R.id.switchBA)
        if(fiche.boutArbre !== null) etatBA.setChecked(fiche.boutArbre!!)
        etatBA.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.boutArbre = isChecked
                viewModel.getTime()
                viewModel.localSave()
        }
        //rondelle élastique
        var PRE = layout.findViewById<Switch>(R.id.switchPRE)
        if(fiche.rondelleElastique !== null) PRE.setChecked(fiche.rondelleElastique!!)
        PRE.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.rondelleElastique = isChecked
            viewModel.getTime()
            viewModel.localSave()
        }
        //roulements
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        var roulement = layout.findViewById<TextView>(R.id.valRoulement)
        if (switchRoullements.isChecked && fiche.refRoulementArriere !== null){
            Log.i("INFO","roulAr = ${fiche.typeRoulementArriere!![0]}")
            var s = "roulement arrière :"
            for ( i in 0..fiche.typeRoulementArriere!!.size-1){
                s = "$s ${fiche.typeRoulementArriere!![i]},${fiche.refRoulementArriere!![i]} - "
            }
            refRoul.setText(fiche.refRoulementArriere!![0])
        } else if( fiche.refRoulementAvant !== null) {
            var s = "roulement avant :"
            for ( i in 0..fiche.typeRoulementAvant!!.size-1){
                s = "$s ${fiche.typeRoulementAvant!![i]},${fiche.refRoulementAvant!![i]} - "
            }
            roulement.setText(s)
                refRoul.setText(fiche.refRoulementAvant!![0])}
        switchRoullements.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var s = "roulement arrière :"
                var type = if (viewModel.selection.value!!.typeRoulementArriere == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(viewModel.selection.value!!.typeRoulementArriere!![0])
                typeRoulement.setSelection(type)
                refRoul.setText(viewModel.selection.value!!.refRoulementArriere!![0])
                for ( i in 0..fiche.typeRoulementArriere!!.size-1){
                    s = "$s ${fiche.typeRoulementArriere!![i]},${fiche.refRoulementArriere!![i]} - "
                }
                roulement.setText(s)
            } else {
                var s = "roulement avant :"
                var type = if (viewModel.selection.value!!.typeRoulementAvant == null) 0 else arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M").indexOf(viewModel.selection.value!!.typeRoulementAvant!![0])
                typeRoulement.setSelection(type)
                refRoul.setText(viewModel.selection.value!!.refRoulementAvant!![0])
                for ( i in 0..fiche.typeRoulementAvant!!.size-1){
                    s = "$s ${fiche.typeRoulementAvant!![i]},${fiche.refRoulementAvant!![i]} - "
                }
                Log.i("INFO","av")
                roulement.setText(s)
            }
        }
        /*typeRoulement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeRoulement.selectedItem.toString()
                if (switchRoullements.isChecked) {
                    viewModel.selection.value!!.typeRoulementArriere =  selection
                    viewModel.getTime()
                    viewModel.localSave()
                } else {
                    viewModel.selection.value!!.typeRoulementAvant = selection
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }

        }
        refRoul.doAfterTextChanged {
                if (switchRoullements.isChecked) {
                   if (refRoul.text.isNotEmpty()) {
                       viewModel.selection.value!!.refRoulementArriere = refRoul.text.toString()
                       viewModel.getTime()
                       viewModel.localSave()
                   }
                } else  {
                    if (refRoul.text.isNotEmpty()) {
                        viewModel.selection.value!!.refRoulementAvant = refRoul.text.toString()
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
        }*/
        //joints
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("simple lèvre","double lèvre"))
        var switchJoints = layout.findViewById<Switch>(R.id.switchJoints)
        var refJoints = layout.findViewById<EditText>(R.id.refJoints)
        if (switchJoints.isChecked && fiche.typeJointArriere !== null) refJoints.setText(fiche.refJointArriere) else if (fiche.refJointAvant !== null) refJoints.setText(fiche.refJointAvant)
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
                    if (viewModel.selection.value!!.typeJointAvant!! == false) {
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
                    if (position == 1 ) {
                        viewModel.selection.value!!.typeJointArriere = true
                        viewModel.getTime()
                        viewModel.localSave()
                    } else {
                        viewModel.selection.value!!.typeJointArriere = false
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                    Log.i("INFO", "position:${position} - valeur: ${selection} - type joint arrière : ${viewModel.selection.value!!.typeJointArriere!!.toString()}")
                } else {
                    if (position == 1 ) viewModel.selection.value!!.typeJointAvant = true else viewModel.selection.value!!.typeJointAvant = false
                    Log.i("INFO", "position:${position} - valeur: ${selection} - type joint av : ${viewModel.selection.value!!.typeJointAvant!!.toString()}")
                }
            }
        }
        /*refJoints.setOnFocusChangeListener { _, hasFocus ->
            if (switchJoints.isChecked) {
                Log.i("INFO","update ar")
                viewModel.selection.value!!.refJointArriere = refJoints.text.toString()
            } else {
                Log.i("INFO","update av")
                viewModel.selection.value!!.refJointAvant = refJoints.text.toString()
            }
        }*/
        refJoints.doAfterTextChanged {
                Log.i("INFO", "position ${if (switchJoints.isChecked) "ar" else "av"}")
                if (switchJoints.isChecked) {
                    Log.i("INFO", "set ar")
                    viewModel.selection.value!!.refJointArriere = refJoints.text.toString()
                    viewModel.getTime()
                    viewModel.localSave()
                } else {
                    Log.i("INFO", "set av")
                    viewModel.selection.value!!.refJointAvant = refJoints.text.toString()
                    viewModel.getTime()
                    viewModel.localSave()
                }
        }
        //capot ventilateur
        var cvent = layout.findViewById<Spinner>(R.id.spiCapot)
        cvent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
           //if(viewModel.selection.value!!.capotV !== null)  cvent.setSelection(viewModel.selection.value!!.capotV!!)
        if (fiche.capotV !== null) cvent.setSelection(arrayOf<String>("Bon état","Cassé","Absent").indexOf(fiche.capotV))
        cvent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               viewModel.selection.value!!.capotV = position + 1
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        var vent = layout.findViewById<Spinner>(R.id.spiVentilateur)
        vent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Absent"))
        if (fiche.ventilateur !== null) vent.setSelection(arrayOf<String>("Bon état","A changer","Absent").indexOf(fiche.ventilateur))
        vent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
              viewModel.selection.value!!.ventilateur = position +1
                viewModel.getTime()
                viewModel.localSave()
            }

        }
        if (viewModel.selection.value!!.ventilateur !== null) vent.setSelection(viewModel.selection.value!!.ventilateur!! - 1)
            var socle = layout.findViewById<Spinner>(R.id.spiSocle)
        socle.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.socleBoiteABorne !== null) socle.setSelection(viewModel.selection.value!!.socleBoiteABorne!! - 1)
           socle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onNothingSelected(parent: AdapterView<*>?) {

               }
               override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.selection.value!!.socleBoiteABorne = position + 1
                   viewModel.getTime()
                   viewModel.localSave()
               }
           }
        var capot = layout.findViewById<Spinner>(R.id.spiCap)
        capot.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.capotBoiteABorne !== null) capot.setSelection(viewModel.selection.value!!.capotBoiteABorne!! - 1)
         capot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onNothingSelected(parent: AdapterView<*>?) {
             }
             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.capotBoiteABorne = position+1
                 viewModel.getTime()
                 viewModel.localSave()
             }

         }
            var plaque = layout.findViewById<Spinner>(R.id.spiPla!!)
        plaque.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Sortie par câbles"))
        if (viewModel.selection.value!!.plaqueABorne !== null ) plaque.setSelection(viewModel.selection.value!!.plaqueABorne!! - 1)
        plaque.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.plaqueABorne = position + 1
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        var sondes = layout.findViewById<Switch>(R.id.switchSondes)
        if (viewModel.selection.value!!.presenceSondes !== null && viewModel.selection.value!!.presenceSondes!!)
        {
           sondes.setChecked(true)
        }  else {
            sondes.setChecked(false)
        }
        sondes.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.presenceSondes = isChecked
            viewModel.getTime()
            viewModel.localSave()
        }
        var typeSondes = layout.findViewById<EditText>(R.id.typeSonde)
        typeSondes.setText(viewModel.selection.value!!.typeSondes)
        typeSondes.doAfterTextChanged {
            viewModel.selection.value!!.typeSondes = typeSondes.text.toString()
            viewModel.getTime()
            viewModel.localSave()
        }
        var equi = layout.findViewById<Switch>(R.id.swEqui)
        if (viewModel.selection.value!!.equilibrage !== null) equi.setChecked(viewModel.selection.value!!.equilibrage!!)
        equi.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.equilibrage = isChecked
            viewModel.getTime()
            viewModel.localSave()
        }
        var peint = layout.findViewById<EditText>(R.id.coul)
        if (viewModel.selection.value!!.peinture !== null) peint.setText(viewModel.selection.value!!.peinture)
        peint.doAfterTextChanged {
            viewModel.selection.value!!.peinture = peint.text.toString()
            viewModel.getTime()
            viewModel.localSave()
        }
        return layout
    }

}