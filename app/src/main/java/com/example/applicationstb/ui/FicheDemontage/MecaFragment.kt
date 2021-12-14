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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
        var regexNombres = Regex("/[+-]?([0-9]*[.])?[0-9]+/")
        var regexInt = Regex("^\\d+")
        if (fiche.status == 3L) {
            couplage.isEnabled = false
            txtclp.isEnabled = false
        }
        if (fiche.couplage !== null && arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) == -1) txtclp.setText(fiche.couplage)
        txtclp.doAfterTextChanged {
           if(txtclp.text.isNotEmpty()) viewModel.selection.value!!.couplage = txtclp.text.toString()
        }
        couplage.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Y","Δ","Autre"))
        if (fiche.couplage !== null) couplage.setSelection(if (arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) >=0 ) arrayOf<String>("Y","Δ","Autre").indexOf(fiche.couplage) else 0)
        couplage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = couplage.selectedItem.toString()
                if (couplage.selectedItem.toString() == "Autre") {
                    txtclp.visibility = View.VISIBLE
                } else {
                    txtclp.visibility = View.GONE
                    if (selection !== "") {
                        viewModel.selection.value!!.couplage = selection
                        viewModel.localSave()
                    }
                }
            }
        }
        //etats flasque
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","OK","A contrôler","A rebaguer"))
        if (fiche.flasqueAvant !== null) etatFlasqueAvant.setSelection(fiche.flasqueAvant!!)
        if (fiche.status == 3L) {
            etatFlasqueAvant.isEnabled = false
        }
        etatFlasqueAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0 ) {
                    viewModel.selection.value!!.flasqueAvant = position
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
        }
        etatFlasqueAvant.setOnFocusChangeListener{ view, hasFocus ->
            if (hasFocus) {
                etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","OK","A contrôler","A rebaguer"))
            } else {

            }
        }
        var etatFlasqueArrière = layout.findViewById<Spinner>(R.id.spiFAr)
        if (fiche.status == 3L) {
            etatFlasqueArrière.isEnabled = false
        }
        etatFlasqueArrière.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","OK","A contrôler","A rebaguer"))
        if (fiche.flasqueArriere !== null) etatFlasqueArrière.setSelection(fiche.flasqueArriere!!)
        etatFlasqueArrière.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    viewModel.selection.value!!.flasqueArriere = position
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
        }
        // portée roulements
        var roulementAvant = layout.findViewById<Spinner>(R.id.spiRAv)
        if (fiche.status == 3L) {
           roulementAvant.isEnabled = false
        }
        roulementAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","OK","A contrôler","A rebaguer"))
        if (fiche.porteeRAvant !== null) roulementAvant.setSelection(fiche.porteeRAvant!!)
        roulementAvant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementAvant.selectedItem.toString()
                if (position > 0) {
                    viewModel.selection.value!!.porteeRAvant = position
                    viewModel.getTime()
                    viewModel.localSave()
                }
                    //Log.i("INFO", "roulement arrière:"+)
            }
        }
        var roulementArriere = layout.findViewById<Spinner>(R.id.spiRAr)
        if (fiche.status == 3L) {
            roulementArriere.isEnabled = false
        }
        roulementArriere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","OK","A contrôler","A rebaguer"))
        if (fiche.porteeRArriere!== null) roulementArriere.setSelection(fiche.porteeRArriere!!)
        roulementArriere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = roulementArriere.selectedItem.toString()
                if (position > 0) {
                    viewModel.selection.value!!.porteeRArriere = position
                    viewModel.getTime()
                    viewModel.localSave()
                }
                //Log.i("INFO", "roulement arrière:"+)
            }
        }

        //etat bout arbre
        var etatBA = layout.findViewById<Switch>(R.id.switchBA)
        if (fiche.status == 3L) {
            etatBA.isEnabled = false
        }
        if(fiche.boutArbre !== null) etatBA.setChecked(fiche.boutArbre!!)
        etatBA.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.boutArbre = isChecked
                viewModel.getTime()
                viewModel.localSave()
        }
        //rondelle élastique
        var PRE = layout.findViewById<Switch>(R.id.switchPRE)
        if (fiche.status == 3L) {
            PRE.isEnabled = false
        }
        if(fiche.rondelleElastique !== null) PRE.setChecked(fiche.rondelleElastique!!)
        PRE.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.rondelleElastique = isChecked
            viewModel.getTime()
            viewModel.localSave()
        }
        //roulements
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        var specsRoul = layout.findViewById<RecyclerView>(R.id.specsRoul)
        if (fiche.status == 3L) {
            refRoul.isEnabled = false
            specsRoul.isEnabled = false
            typeRoulement.isEnabled = false
        }
        specsRoul.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false )
        var adapter = roulementAdapter( viewModel.selection.value!!.typeRoulementArriere!!,viewModel.selection.value!!.refRoulementArriere!!) { item ->
            viewModel.selection.value!!.typeRoulementArriere =
                removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
            viewModel.selection.value!!.refRoulementArriere = removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
            viewModel.getTime()
            viewModel.localSave()
        }
        specsRoul.adapter = adapter
        viewModel.selection.observe(viewLifecycleOwner,{
            if (switchRoullements.isChecked) {
                adapter.update(
                    viewModel.selection.value!!.typeRoulementArriere!!,
                    viewModel.selection.value!!.refRoulementArriere!!,
                )
            } else {
                adapter.update(
                    viewModel.selection.value!!.typeRoulementAvant!!,
                    viewModel.selection.value!!.refRoulementAvant!!,
                )
            }
        })
        if (switchRoullements.isChecked && fiche.refRoulementArriere !== null && fiche.refRoulementArriere!!.size > 0){
            refRoul.setText(fiche.refRoulementArriere!![0])
        } else if( fiche.refRoulementAvant !== null && fiche.refRoulementAvant!!.size > 0) {
                refRoul.setText(fiche.refRoulementAvant!![0])}
        switchRoullements.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (viewModel.selection.value!!.typeRoulementArriere!!.filter{it !== ""}.size > 0) {
                    var type =
                        if (viewModel.selection.value!!.typeRoulementArriere == null) 0 else arrayOf<String>(
                            "2Z/ECJ",
                            "2RS/ECP",
                            "C3",
                            "M"
                        ).indexOf(viewModel.selection.value!!.typeRoulementArriere!![0])
                    typeRoulement.setSelection(type)
                    refRoul.setText(viewModel.selection.value!!.refRoulementArriere!![0])
                    specsRoul.adapter = roulementAdapter(
                        viewModel.selection.value!!.typeRoulementArriere!!.filter{it !== ""}.toTypedArray(),
                        viewModel.selection.value!!.refRoulementArriere!!
                    ) { item ->
                        viewModel.selection.value!!.typeRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
                        viewModel.selection.value!!.refRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                } else {
                    Log.i("INFO", " length ${viewModel.selection.value!!.typeRoulementArriere!!.filter{it !== ""}.size}")
                    typeRoulement.setSelection(0)
                    refRoul.setText("")
                    specsRoul.adapter = roulementAdapter(
                        arrayOf(),
                        arrayOf()
                    ) { item ->
                        viewModel.selection.value!!.typeRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
                        viewModel.selection.value!!.refRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            } else {
                if (viewModel.selection.value!!.typeRoulementAvant!!.filter{it !== ""}.size > 0) {
                    var type =
                        if (viewModel.selection.value!!.typeRoulementAvant == null) 0 else arrayOf<String>(
                            "2Z/ECJ",
                            "2RS/ECP",
                            "C3",
                            "M"
                        ).indexOf(viewModel.selection.value!!.typeRoulementAvant!![0])
                    typeRoulement.setSelection(type)
                    refRoul.setText(viewModel.selection.value!!.refRoulementAvant!![0])
                    specsRoul.adapter = roulementAdapter(
                        viewModel.selection.value!!.typeRoulementAvant!!.filter{it !== ""}.toTypedArray(),
                        viewModel.selection.value!!.refRoulementAvant!!
                    ) { item ->
                        viewModel.selection.value!!.typeRoulementAvant =
                            removeRef(item, viewModel.selection.value!!.typeRoulementAvant!!)
                        viewModel.selection.value!!.refRoulementAvant =
                            removeRef(item, viewModel.selection.value!!.refRoulementAvant!!)
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                } else {
                    Log.i("INFO", " length ${viewModel.selection.value!!.typeRoulementArriere!!.filter{it !== ""}.size}")
                    typeRoulement.setSelection(0)
                    refRoul.setText("")
                    specsRoul.adapter = roulementAdapter(
                        arrayOf(),
                        arrayOf()
                    ) { item ->
                        viewModel.selection.value!!.typeRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
                        viewModel.selection.value!!.refRoulementArriere =
                            removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }
        }
        typeRoulement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeRoulement.selectedItem.toString()
                if (position > 0) {
                    if (switchRoullements.isChecked) {
                        if (viewModel.selection.value!!.typeRoulementArriere!!.indexOf(selection) == -1) {
                            var tab =
                                viewModel.selection.value!!.typeRoulementArriere!!.toMutableList()
                            tab.add(selection)
                            viewModel.selection.value!!.typeRoulementArriere = tab.toTypedArray()
                            var tab2 =
                                viewModel.selection.value!!.refRoulementArriere!!.toMutableList()
                            tab2.add("")
                            viewModel.selection.value!!.refRoulementArriere = tab2.toTypedArray()
                            refRoul.setText("")
                        } else {
                            refRoul.setText(
                                viewModel.selection.value!!.refRoulementArriere!![viewModel.selection.value!!.typeRoulementArriere!!.indexOf(
                                    selection
                                )]
                            )
                        }
                        specsRoul.adapter = roulementAdapter(
                            fiche.typeRoulementArriere!!.filter{it !== ""}.toTypedArray(),
                            fiche.refRoulementArriere!!
                        ) { item ->
                            viewModel.selection.value!!.typeRoulementArriere =
                                removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
                            viewModel.selection.value!!.refRoulementArriere =
                                removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
                            viewModel.getTime()
                            viewModel.localSave()
                        }
                        viewModel.getTime()
                        viewModel.localSave()
                    } else {
                        if (viewModel.selection.value!!.typeRoulementAvant!!.indexOf(selection) == -1) {
                            var tab =
                                viewModel.selection.value!!.typeRoulementAvant!!.toMutableList()
                            tab.add(selection)
                            viewModel.selection.value!!.typeRoulementAvant = tab.toTypedArray()
                            var tab2 =
                                viewModel.selection.value!!.refRoulementAvant!!.toMutableList()
                            tab2.add("")
                            viewModel.selection.value!!.refRoulementAvant = tab2.toTypedArray()
                            refRoul.setText("")
                        } else {
                            refRoul.setText(
                                viewModel.selection.value!!.refRoulementAvant!![viewModel.selection.value!!.typeRoulementAvant!!.indexOf(
                                    selection
                                )]
                            )
                        }
                        /* else {
                        var tab = viewModel.selection.value!!.typeRoulementAvant!!.toMutableList()
                        var tab2 = viewModel.selection.value!!.refRoulementAvant!!.toMutableList()
                        tab.removeAt(viewModel.selection.value!!.typeRoulementAvant!!.indexOf(selection))
                        tab2.removeAt(viewModel.selection.value!!.typeRoulementAvant!!.indexOf(selection))
                        viewModel.selection.value!!.typeRoulementAvant = tab.toTypedArray()
                        viewModel.selection.value!!.refRoulementAvant = tab2.toTypedArray()
                    }*/
                        specsRoul.adapter = roulementAdapter(
                            fiche.typeRoulementAvant!!.filter{it !== ""}.toTypedArray(),
                            fiche.refRoulementAvant!!
                        ) { item ->
                            viewModel.selection.value!!.typeRoulementAvant =
                                removeRef(item, viewModel.selection.value!!.typeRoulementAvant!!)
                            viewModel.selection.value!!.refRoulementAvant =
                                removeRef(item, viewModel.selection.value!!.refRoulementAvant!!)
                            viewModel.getTime()
                            viewModel.localSave()
                        }
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }

        }
        refRoul.doAfterTextChanged {
            var index = 0
            if (switchRoullements.isChecked) {
                 index =   viewModel.selection.value!!.typeRoulementArriere!!.indexOf(typeRoulement.selectedItem)
            } else {
                index = viewModel.selection.value!!.typeRoulementAvant!!.indexOf(typeRoulement.selectedItem)
            }
            if (index !== -1) {
                if (switchRoullements.isChecked) {
                    if (refRoul.text.isNotEmpty()) {
                        viewModel.selection.value!!.refRoulementArriere!![index] =
                            refRoul.text.toString()
                        specsRoul.adapter = roulementAdapter( fiche.typeRoulementArriere!!,fiche.refRoulementArriere!!) { item ->
                            viewModel.selection.value!!.typeRoulementArriere =
                                removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
                            viewModel.selection.value!!.refRoulementArriere = removeRef(item, viewModel.selection.value!!.refRoulementArriere!!)
                            viewModel.getTime()
                            viewModel.localSave()
                        }
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                } else {
                    if (refRoul.text.isNotEmpty()) {
                        viewModel.selection.value!!.refRoulementAvant!![index] =
                            refRoul.text.toString()
                        specsRoul.adapter = roulementAdapter( fiche.typeRoulementAvant!!,fiche.refRoulementAvant!!) { item ->
                            viewModel.selection.value!!.typeRoulementAvant =
                                removeRef(item, viewModel.selection.value!!.typeRoulementAvant!!)
                            viewModel.selection.value!!.refRoulementAvant = removeRef(item, viewModel.selection.value!!.refRoulementAvant!!)
                            viewModel.getTime()
                            viewModel.localSave()
                        }
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }
        }
        //joints
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","simple lèvre","double lèvre"))
        var switchJoints = layout.findViewById<Switch>(R.id.switchJoints)
        var refJoints = layout.findViewById<EditText>(R.id.refJoints)
        if (fiche.status == 3L) {
            refJoints.isEnabled = false
            typeJoints.isEnabled = false
        }
        if (switchJoints.isChecked && fiche.typeJointArriere !== null){
            if (fiche.typeJointArriere!!) typeJoints.setSelection(1) else typeJoints.setSelection(2)
            refJoints.setText(fiche.refJointArriere)
        }
        if (fiche.typeJointAvant !== null) {
            typeJoints.setSelection(1)
            if (fiche.refJointAvant !== null) refJoints.setText(fiche.refJointAvant)
           // if (fiche.typeJointAvant!!) typeJoints.setSelection(2) else typeJoints.setSelection(1)
           // refJoints.setText(fiche.refJointAvant)
        }
        switchJoints.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var type = if (viewModel.selection.value!!.typeJointArriere == null) {
                    typeJoints.setSelection(0)
                } else { if (viewModel.selection.value!!.typeJointArriere!!) {
                    typeJoints.setSelection(2)
                    } else {
                    typeJoints.setSelection(1)
                 }
                }
                refJoints.setText(viewModel.selection.value!!.refJointArriere)
            } else {
                var type = 0
                if (viewModel.selection.value!!.typeJointAvant == null) {
                    typeJoints.setSelection(0)
                } else {
                    if (viewModel.selection.value!!.typeJointAvant!!) {
                        typeJoints.setSelection(2)
                    } else {
                        typeJoints.setSelection(1)
                    }
                }
                Log.i("INFO","position av ${typeJoints.selectedItemPosition.toString()} - type ${viewModel.selection.value!!.typeJointAvant}")
                refJoints.setText(viewModel.selection.value!!.refJointAvant)
            }
        }
        if( viewModel.selection.value!!.status!! !== 3L) {
        typeJoints.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selection = typeJoints.selectedItem.toString()
                if (switchJoints.isChecked) {
                    if (position == 2 ) {
                        viewModel.selection.value!!.typeJointArriere = true
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                    if (position == 1){
                        viewModel.selection.value!!.typeJointArriere = false
                        viewModel.getTime()
                        viewModel.localSave()
                    }

                } else {
                    if (position == 2 ) {viewModel.selection.value!!.typeJointAvant = true }
                    if (position == 1) { viewModel.selection.value!!.typeJointAvant = false}

                }
            }
        }

            refJoints.doAfterTextChanged {
                if (switchJoints.isChecked) {
                    viewModel.selection.value!!.refJointArriere = refJoints.text.toString()
                    viewModel.getTime()
                    viewModel.localSave()
                } else {

                    viewModel.selection.value!!.refJointAvant = refJoints.text.toString()
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
        }
        //capot ventilateur
        var cvent = layout.findViewById<Spinner>(R.id.spiCapot)
        cvent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Bon état","Cassé","Absent"))
           //if(viewModel.selection.value!!.capotV !== null)  cvent.setSelection(viewModel.selection.value!!.capotV!!)
        if (fiche.capotV !== null) cvent.setSelection(arrayOf<String>("Bon état","Cassé","Absent").indexOf(fiche.capotV.toString()))
        if (fiche.status!! == 3L) cvent.isEnabled = false
        if (fiche.status!! !== 3L) {
            cvent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if ( position > 0) {
                        viewModel.selection.value!!.capotV = position
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }
        }
        var vent = layout.findViewById<Spinner>(R.id.spiVentilateur)
        vent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Bon état","A changer","Absent"))
        if (fiche.status!! == 3L) vent.isEnabled = false
        if (fiche.ventilateur !== null) vent.setSelection(arrayOf<String>("Bon état","A changer","Absent").indexOf(fiche.ventilateur.toString()))
        if (fiche.status!! < 3L) {
            vent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        viewModel.selection.value!!.ventilateur = position
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }

            }
        }
        if (viewModel.selection.value!!.ventilateur !== null) vent.setSelection(viewModel.selection.value!!.ventilateur!! )
            var socle = layout.findViewById<Spinner>(R.id.spiSocle)
        socle.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.socleBoiteABorne !== null) socle.setSelection(viewModel.selection.value!!.socleBoiteABorne!! )
        if (fiche.status!! == 3L) socle.isEnabled = false
        if (fiche.status!! < 3L) {
            socle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        viewModel.selection.value!!.socleBoiteABorne = position
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }
        }
        var capot = layout.findViewById<Spinner>(R.id.spiCap)
        capot.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Bon état","Cassé","Absent"))
        if (viewModel.selection.value!!.capotBoiteABorne !== null) capot.setSelection(viewModel.selection.value!!.capotBoiteABorne!!)
        if (fiche.status!! == 3L) capot.isEnabled = false
        if (fiche.status!! < 3L) {
            capot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        viewModel.selection.value!!.capotBoiteABorne = position
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }

            }
        }
            var plaque = layout.findViewById<Spinner>(R.id.spiPla!!)
        plaque.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","Bon état","A changer","Sortie par câbles"))
        if (viewModel.selection.value!!.plaqueABorne !== null ) plaque.setSelection(viewModel.selection.value!!.plaqueABorne!!)
        if (fiche.status!! == 3L) plaque.isEnabled = false
        if (fiche.status!! !== 3L) {
            plaque.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        viewModel.selection.value!!.plaqueABorne = position
                        viewModel.getTime()
                        viewModel.localSave()
                    }
                }
            }
        }
        var sondes = layout.findViewById<Switch>(R.id.switchSondes)
        if (viewModel.selection.value!!.presenceSondes !== null && viewModel.selection.value!!.presenceSondes!!)
        {
           sondes.setChecked(true)
        }  else {
            sondes.setChecked(false)
        }
        if (fiche.status!! == 3L) sondes.isEnabled = false
        if (fiche.status!! < 3L) {
            sondes.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.presenceSondes = isChecked
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        var typeSondes = layout.findViewById<EditText>(R.id.typeSonde)
        if (fiche.status!! == 3L) typeSondes.isEnabled = false
        typeSondes.setText(viewModel.selection.value!!.typeSondes)
        if (fiche.status!! < 3L) {
            typeSondes.doAfterTextChanged {
                viewModel.selection.value!!.typeSondes = typeSondes.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        var equi = layout.findViewById<Switch>(R.id.swEqui)
        if (viewModel.selection.value!!.equilibrage !== null) equi.setChecked(viewModel.selection.value!!.equilibrage!!)
        if (fiche.status!! == 3L) equi.isEnabled = false
        if (fiche.status!! < 3L) {
            equi.setOnCheckedChangeListener { _, isChecked ->
                viewModel.selection.value!!.equilibrage = isChecked
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        var peint = layout.findViewById<EditText>(R.id.coul)
        if (viewModel.selection.value!!.peinture !== null) peint.setText(viewModel.selection.value!!.peinture)
        if (fiche.status!! == 3L) peint.isEnabled = false
        if (fiche.status!! < 3L) {
            peint.doAfterTextChanged {
                viewModel.selection.value!!.peinture = peint.text.toString()
                viewModel.getTime()
                viewModel.localSave()
            }
        }
        return layout
    }

    fun removeRef(i:Int, list:Array<String>):Array<String>{
        var tab = list.toMutableList()
        tab.removeAt(i)
        return tab.toTypedArray()
    }
}