package com.example.applicationstb.ui.ficheRemontage

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.applicationstb.model.*
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.*

class FicheRemontage : Fragment() {

    companion object {
        fun newInstance() = FicheRemontage()
    }

    private val viewModel: FicheRemontageViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        var list = arguments?.get("listRemontages") as Array<Remontage>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeRemontages = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterRemontages = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.listeRemontages.map { it.numFiche })
        spinner.adapter = adapterRemontages
        //var infoMoteur = layout.findViewById<CardView>(R.id.infoMoteur)
        var btnRemontage = layout.findViewById<Button>(R.id.btnDemarrer)
        //var essaisDynamiques = layout.findViewById<CardView>(R.id.essaisDynamiques)
        var essaisStats = layout.findViewById<FrameLayout>(R.id.essaisStatiqueslayout)
        val fragmentManager = childFragmentManager
        //infos moteur
        var titre = layout.findViewById<TextView>(R.id.titreRemontage)
        var btnDemo = layout.findViewById<Button>(R.id.btnFDemo)
        var spinnerMnt = layout.findViewById<Spinner>(R.id.spinnerMntRll)
        spinnerMnt.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>(" ", "à la presse", "douille de frappe", "chauffe roulement")
        )
        var spinnerCPA = layout.findViewById<Spinner>(R.id.spinnerCPA)
        spinnerCPA.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>(" ", "avant", "arrière", "aucun")
        )
        var spinnerCIF = layout.findViewById<Spinner>(R.id.spinnerCIF)
        spinnerCIF.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>(" ", "avant", "arrière", "aucun")
        )
        var fixCouronne = layout.findViewById<CheckBox>(R.id.fixCouronne)
        var isoPBRB = layout.findViewById<CheckBox>(R.id.isoPBRB)
        var isoPBV = layout.findViewById<EditText>(R.id.isoPBV)
        var risoPBV = layout.findViewById<EditText>(R.id.RisoPBV)
        var tensionStator = layout.findViewById<CheckBox>(R.id.radioButtonS1)
        var tensionStatorU = layout.findViewById<EditText>(R.id.TSVU)
        var tensionStatorV = layout.findViewById<EditText>(R.id.TSVV)
        var tensionStatorW = layout.findViewById<EditText>(R.id.TSVW)
        var tensionInducteurs = layout.findViewById<CheckBox>(R.id.radioButtonI1)
        var tensionInducteursU = layout.findViewById<EditText>(R.id.TIVU)
        var tensionInducteursV = layout.findViewById<EditText>(R.id.TIVV)
        var tensionInducteursW = layout.findViewById<EditText>(R.id.TIVW)
        var intensiteStator = layout.findViewById<CheckBox>(R.id.radioButtonS2)
        var intensiteStatorU = layout.findViewById<EditText>(R.id.ISVU)
        var intensiteStatorV = layout.findViewById<EditText>(R.id.ISVV)
        var intensiteStatorW = layout.findViewById<EditText>(R.id.ISVW)
        var intensiteInducteurs = layout.findViewById<CheckBox>(R.id.radioButtonI2)
        var intensiteInducteursU = layout.findViewById<EditText>(R.id.IIVU)
        var intensiteInducteursV = layout.findViewById<EditText>(R.id.IIVV)
        var intensiteInducteursW = layout.findViewById<EditText>(R.id.IIVW)
        var tensionInduit = layout.findViewById<CheckBox>(R.id.radioInduit)
        var tensionInduitU = layout.findViewById<EditText>(R.id.TIU)
        var tensionInduitV = layout.findViewById<EditText>(R.id.TIV)
        var tensionInduitW = layout.findViewById<EditText>(R.id.TIW)
        var tensionRotor = layout.findViewById<CheckBox>(R.id.radioRotor)
        var tensionRotorU = layout.findViewById<EditText>(R.id.TRU)
        var tensionRotorV = layout.findViewById<EditText>(R.id.TRV)
        var tensionRotorW = layout.findViewById<EditText>(R.id.TRW)
        var intensiteInduitU = layout.findViewById<EditText>(R.id.IntIndu)
        var vitesseU = layout.findViewById<EditText>(R.id.vitesseRemo)
        var puissanceU = layout.findViewById<EditText>(R.id.pRemotri)
        var dureeEssai = layout.findViewById<EditText>(R.id.dureeEssaiRemoTri)
        var sensRotation = layout.findViewById<ToggleButton>(R.id.sensRotationS)
        var V1V = layout.findViewById<EditText>(R.id.V1V)
        var V1H = layout.findViewById<EditText>(R.id.V1H)
        var V2V = layout.findViewById<EditText>(R.id.V2V)
        var V2H = layout.findViewById<EditText>(R.id.V2H)
        var V2A = layout.findViewById<EditText>(R.id.V2A)
        var A1V = layout.findViewById<EditText>(R.id.A1V)
        var A1H = layout.findViewById<EditText>(R.id.A1H)
        var A2V = layout.findViewById<EditText>(R.id.A2V)
        var A2H = layout.findViewById<EditText>(R.id.A2H)
        var A2A = layout.findViewById<EditText>(R.id.A2A)
        var obs = layout.findViewById<EditText>(R.id.observations)
        var term = layout.findViewById<Button>(R.id.termRemo)
        var btnFichesD = layout.findViewById<Button>(R.id.btnFichesD)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")

        btnRemontage.setOnClickListener {
            if (spinner.selectedItem == null) {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.RemontageLayout),
                    "Veuillez sélectionner une fiche",
                    3600
                )
                mySnackbar.show()
            } else {
                btnFichesD.visibility = View.VISIBLE
                viewModel.start.value = Date()
                var demo = viewModel.listeRemontages.find { it.numFiche == spinner.selectedItem }
                if (demo!!.typeFicheRemontage == 6 || demo!!.typeFicheRemontage == 7 ||demo!!.typeFicheRemontage == 9) {
                    viewModel.selection.value = demo as RemontageTriphase
                    viewModel.selection.value!!.status = 2L
                    fragmentManager.commit {
                        replace<essaisStatTriFragment>(R.id.essaisStatiqueslayout)
                    }
                    layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisSats).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisDynamiques).visibility = View.VISIBLE
                    if (demo.verificationIsolementPorteBalais !== null) isoPBRB.setChecked(
                        demo.verificationIsolementPorteBalais!!
                    )
                    if (demo.isolementPorteBalaisV !== null) isoPBV.setText(
                        demo.isolementPorteBalaisV!!.toString()
                    )
                    isoPBV.visibility = View.VISIBLE
                    if (demo.isolementPorteBalaisOhm !== null) risoPBV.setText(
                        demo.isolementPorteBalaisOhm!!.toString()
                    )
                    risoPBV.visibility = View.VISIBLE
                    if (demo.tensionStator !== null) tensionStator.setChecked(
                        demo.tensionStator!!
                    )

                    if (demo.tensionStatorU !== null) tensionStatorU.setText(
                        demo.tensionStatorU!!.toString()
                    )
                    if (demo.tensionStatorV !== null) tensionStatorV.setText(
                        demo.tensionStatorV!!.toString()
                    )
                    if (demo.tensionStatorW !== null) tensionStatorW.setText(
                        demo.tensionStatorW!!.toString()
                    )
                    if (demo.tensionInducteurs !== null) tensionInducteurs.setChecked(
                        demo.tensionInducteurs!!
                    )
                    if (demo.tensionInducteursU !== null) tensionInducteursU.setText(
                        demo.tensionInducteursU!!.toString()
                    )
                    if (demo.tensionInducteursV !== null) tensionInducteursV.setText(
                        demo.tensionInducteursV!!.toString()
                    )
                    if (demo.tensionInducteursW !== null) tensionInducteursW.setText(
                        demo.tensionInducteursW!!.toString()
                    )
                    if (demo.intensiteStator !== null) intensiteStator.setChecked(
                        demo.intensiteStator!!
                    )
                    if (demo.intensiteStatorU !== null) intensiteStatorU.setText(
                        demo.intensiteStatorU!!.toString()
                    )
                    if (demo.intensiteStatorV !== null) intensiteStatorV.setText(
                        demo.intensiteStatorV!!.toString()
                    )
                    if (demo.intensiteStatorW !== null) intensiteStatorW.setText(
                        demo.intensiteStatorW!!.toString()
                    )
                    if (demo.intensiteInducteurs !== null) intensiteInducteurs.setChecked(
                        demo.intensiteInducteurs!!
                    )
                    if (demo.intensiteInducteursU !== null) intensiteInducteursU.setText(
                        demo.intensiteInducteursU!!.toString()
                    )
                    if (demo.intensiteInducteursV !== null) intensiteInducteursV.setText(
                        demo.intensiteInducteursV!!.toString()
                    )
                    if (demo.intensiteInducteursW !== null) intensiteInducteursW.setText(
                        demo.intensiteInducteursW!!.toString()
                    )
                    if (demo.tensionInduit !== null) tensionInduit.setChecked(
                        demo.tensionInduit!!
                    )
                    if (demo.tensionInduitU !== null) tensionInduitU.setText(
                        demo.tensionInduitU!!.toString()
                    )
                    if (demo.tensionInduitV !== null) tensionInduitV.setText(
                        demo.tensionInduitV!!.toString()
                    )
                    if (demo.tensionInduitW !== null) tensionInduitW.setText(
                        demo.tensionInduitW!!.toString()
                    )
                    if (demo.tensionRotor !== null) tensionRotor.setChecked(
                        demo.tensionRotor!!
                    )
                    if (demo.tensionRotorU !== null) tensionRotorU.setText(
                        demo.tensionRotorU!!.toString()
                    )
                    if (demo.tensionRotorV !== null) tensionRotorV.setText(
                        demo.tensionRotorV!!.toString()
                    )
                    if (demo.tensionRotorW !== null) tensionRotorW.setText(
                        demo.tensionRotorW!!.toString()
                    )
                    if (demo.intensiteInduitU !== null) intensiteInduitU.setText(
                        demo.intensiteInduitU!!.toString()
                    )
                    if (demo.vitesseU !== null) vitesseU.setText(demo.vitesseU!!.toString())
                    if (demo.dureeEssai !== null) dureeEssai.setText(demo.dureeEssai!!.toString())
                    if (demo.puissanceU !== null) puissanceU.setText(demo.puissanceU!!.toString())

                    if (demo.vitesse1V !== null) V1V.setText(demo.vitesse1V!!.toString())
                    if (demo.vitesse1H !== null) V1H.setText(demo.vitesse1H!!.toString())
                    if (demo.vitesse2H !== null) V2H.setText(demo.vitesse2H!!.toString())
                    if (demo.vitesse2V !== null) V2V.setText(demo.vitesse2V!!.toString())
                    if (demo.vitesse2A !== null) V2A.setText(demo.vitesse2A!!.toString())
                    if (demo.acceleration1V !== null) A1V.setText(demo.acceleration1V!!.toString())
                    if (demo.acceleration1H !== null) A1H.setText(demo.acceleration1H!!.toString())
                    if (demo.acceleration2V !== null) A2V.setText(demo.acceleration2V!!.toString())
                    if (demo.acceleration2H !== null) A2H.setText(demo.acceleration2H!!.toString())
                    if (demo.acceleration2A !== null) A2A.setText(demo.acceleration2A!!.toString())
                    if (demo.sensRotation !== null && demo.sensRotation == 2) sensRotation.setChecked(
                        true
                    ) else sensRotation.setChecked(false)
                   /* sensRotation.setOnCheckedChangeListener { _, isChecked ->
                        if (sensRotation.hasFocus()) {
                            if (isChecked) {
                                viewModel.selection.value!!.sensRotation = 2
                                viewModel.getTime()
                                viewModel.quickSave()
                            } else {
                                viewModel.selection.value!!.sensRotation = 1
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                    }
                    fixCouronne.setOnCheckedChangeListener { _, isChecked ->
                        if (fixCouronne.hasFocus()) {
                            viewModel.selection.value!!.verificationFixationCouronne = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    isoPBRB.setOnCheckedChangeListener { _, isChecked ->
                        if (isoPBRB.hasFocus()) {
                            viewModel.selection.value!!.verificationIsolementPorteBalais = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    isoPBV.doAfterTextChanged {
                        if (isoPBV.hasFocus()) {
                            if (isoPBV.text.isNotEmpty() && isoPBRB.text.matches(regexNombres)) viewModel.selection.value!!.isolementPorteBalaisV =
                                isoPBV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    risoPBV.doAfterTextChanged {
                        if (risoPBV.hasFocus() && risoPBV.text.matches(regexNombres)) {
                            if (risoPBV.text.isNotEmpty()) viewModel.selection.value!!.isolementPorteBalaisOhm =
                                risoPBV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionStator.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (tensionStator.hasFocus() && tensionStator.text.matches(regexNombres)) {
                            viewModel.selection.value!!.tensionStator = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionStatorU.doAfterTextChanged {
                        if (tensionStatorU.text.isNotEmpty() && tensionStatorU.hasFocus() && tensionStatorU.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionStatorU =
                                tensionStatorU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionStatorV.doAfterTextChanged {
                        if (tensionStatorV.text.isNotEmpty() && tensionStatorV.hasFocus() && tensionStatorV.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionStatorV =
                                tensionStatorV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionStatorW.doAfterTextChanged {
                        if (tensionStatorW.text.isNotEmpty() && tensionStatorW.hasFocus() && tensionStatorV.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionStatorW =
                                tensionStatorW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInducteurs.setOnCheckedChangeListener { _, isChecked ->
                        if (tensionInducteurs.hasFocus()) {
                            viewModel.selection.value!!.tensionInducteurs = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInducteursU.doAfterTextChanged {
                        if (tensionInducteursU.text.isNotEmpty() && tensionInducteursU.hasFocus() && tensionInducteursU.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionInducteursU =
                                tensionInducteursU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInducteursV.doAfterTextChanged {
                        if (tensionInducteursV.text.isNotEmpty() && tensionInducteursV.hasFocus() && tensionInducteursV.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionInducteursV =
                                tensionInducteursV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInducteursW.doAfterTextChanged {
                        if (tensionInducteursW.text.isNotEmpty() && tensionInducteursW.hasFocus() && tensionInducteursW.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.tensionInducteursW =
                                tensionInducteursW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteStator.setOnCheckedChangeListener { _, isChecked ->
                        if (intensiteStator.hasFocus()) viewModel.selection.value!!.intensiteStator = isChecked
                    }
                    intensiteStatorU.doAfterTextChanged {
                        if (intensiteStatorU.text.isNotEmpty() && intensiteStatorU.hasFocus() && intensiteStatorU.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.intensiteStatorU =
                                intensiteStatorU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteStatorV.doAfterTextChanged {
                        if (intensiteStatorV.text.isNotEmpty() && intensiteStatorV.hasFocus() && intensiteStatorV.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.intensiteStatorV =
                                intensiteStatorV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInducteursW.doAfterTextChanged {
                        if (intensiteStatorW.text.isNotEmpty() && intensiteStatorW.hasFocus() && intensiteStatorW.text.matches(
                                regexNombres
                            )
                        ) {
                            viewModel.selection.value!!.intensiteStatorW =
                                intensiteStatorW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInducteurs.setOnCheckedChangeListener { _, isChecked ->
                        if (intensiteInducteurs.hasFocus()) {
                            viewModel.selection.value!!.intensiteInducteurs = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInducteursU.doAfterTextChanged {
                        if (intensiteInducteursU.text.isNotEmpty() && intensiteInducteursU.hasFocus()) {
                            viewModel.selection.value!!.intensiteInducteursU =
                                intensiteInducteursU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInducteursV.doAfterTextChanged {
                        if (intensiteInducteursV.text.isNotEmpty() && intensiteInducteursV.hasFocus()) {
                            viewModel.selection.value!!.intensiteInducteursV =
                                intensiteInducteursV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInducteursW.doAfterTextChanged {
                        if (intensiteInducteursW.text.isNotEmpty() && intensiteInducteursW.hasFocus()) {
                            viewModel.selection.value!!.intensiteInducteursW =
                                intensiteInducteursW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInduit.setOnCheckedChangeListener { _, isChecked ->
                        if (tensionInduit.hasFocus()) {
                            viewModel.selection.value!!.tensionInduit = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInduitU.doAfterTextChanged {
                        if (tensionInduitU.text.isNotEmpty() && tensionInduitU.hasFocus()) {
                            viewModel.selection.value!!.tensionInduitU =
                                tensionInduitU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInduitV.doAfterTextChanged {
                        if (tensionInduitV.text.isNotEmpty() && tensionInduitV.hasFocus()) {
                            viewModel.selection.value!!.tensionInduitV =
                                tensionInduitV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionInduitW.doAfterTextChanged {
                        if (tensionInduitW.text.isNotEmpty() && tensionInduitW.hasFocus()) {
                            viewModel.selection.value!!.tensionInduitW =
                                tensionInduitW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionRotor.setOnCheckedChangeListener { _, isChecked ->
                        if (tensionRotor.hasFocus()) {
                            viewModel.selection.value!!.tensionRotor = isChecked
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionRotorU.doAfterTextChanged {
                        if (tensionRotorU.text.isNotEmpty() && tensionRotorU.hasFocus()) {
                            viewModel.selection.value!!.tensionRotorU = tensionRotorU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionRotorV.doAfterTextChanged {
                        if (tensionRotorV.text.isNotEmpty() && tensionRotorV.hasFocus()) {
                            viewModel.selection.value!!.tensionRotorV = tensionRotorV.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    tensionRotorW.doAfterTextChanged {
                        if (tensionRotorW.text.isNotEmpty() && tensionRotorW.hasFocus()) {
                            viewModel.selection.value!!.tensionRotorW = tensionRotorW.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    intensiteInduitU.doAfterTextChanged {
                        if (intensiteInduitU.hasFocus()) {
                            if (intensiteInduitU.text.isNotEmpty()) {
                                viewModel.selection.value!!.intensiteInduitU =
                                    intensiteInduitU.text.toString().toFloat()
                                viewModel.selection.value!!.intensiteInduit = true
                            } else {
                                viewModel.selection.value!!.intensiteInduit = false
                            }
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    vitesseU.doAfterTextChanged {
                        if (vitesseU.text.isNotEmpty() && vitesseU.hasFocus()) {
                            viewModel.selection.value!!.vitesseU = vitesseU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    puissanceU.doAfterTextChanged {
                        if (puissanceU.text.isNotEmpty() && puissanceU.hasFocus()) {
                            viewModel.selection.value!!.puissanceU = puissanceU.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    dureeEssai.doAfterTextChanged {
                        if (dureeEssai.text.isNotEmpty() && dureeEssai.hasFocus()) {
                            viewModel.selection.value!!.dureeEssai = dureeEssai.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    V1V.doAfterTextChanged {
                        if (V1V.text.isNotEmpty() && V1V.hasFocus()) {
                            viewModel.selection.value!!.vitesse1V = V1V.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    V1H.doAfterTextChanged {
                        if (V1H.text.isNotEmpty() && V1H.hasFocus()) {
                            viewModel.selection.value!!.vitesse1H = V1H.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    V2V.doAfterTextChanged {
                        if (V2V.text.isNotEmpty() && V2V.hasFocus()) {
                            viewModel.selection.value!!.vitesse2V = V2V.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    V2H.doAfterTextChanged {
                        if (V2H.text.isNotEmpty() && V2H.hasFocus()) {
                            viewModel.selection.value!!.vitesse2H = V2H.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    V2A.doAfterTextChanged {
                        if (V2A.text.isNotEmpty() && V2A.hasFocus()) {
                            viewModel.selection.value!!.vitesse2A = V2A.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    A1V.doAfterTextChanged {
                        if (A1V.text.isNotEmpty() && A1V.hasFocus()) {
                            viewModel.selection.value!!.acceleration1V = A1V.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    A1H.doAfterTextChanged {
                        if (A1H.text.isNotEmpty() && A1H.hasFocus()) {
                            viewModel.selection.value!!.acceleration1H = A1H.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    A2V.doAfterTextChanged {
                        if (A2V.text.isNotEmpty() && A2V.hasFocus()) {
                            viewModel.selection.value!!.acceleration2V = A2V.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    A2H.doAfterTextChanged {
                        if (A2H.text.isNotEmpty() && A2H.hasFocus()) {
                            viewModel.selection.value!!.acceleration2H = A2H.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }
                    A2A.doAfterTextChanged {
                        if (A2A.text.isNotEmpty() && A2A.hasFocus()) {
                            viewModel.selection.value!!.acceleration2A = A2A.text.toString().toFloat()
                            viewModel.getTime()
                            viewModel.quickSave()
                        }
                    }*/
                    //layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE
                }
                if (demo!!.typeFicheRemontage == 5) {
                    viewModel.selection.value = demo as RemontageCourantC
                    viewModel.selection.value!!.status = 2L
                    fragmentManager.commit{
                        replace<essaisStatCCFragment>(R.id.essaisStatiqueslayout)
                    }
                    layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisSats).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisDynamiques).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE
                    if (demo.verificationIsolementPorteBalais !== null) isoPBRB.setChecked(
                        demo.verificationIsolementPorteBalais!!
                    )
                    if (demo.isolementPorteBalaisV !== null) isoPBV.setText(
                        demo.isolementPorteBalaisV!!.toString()
                    )
                    if (demo.isolementPorteBalaisOhm !== null) risoPBV.setText(
                        demo.isolementPorteBalaisOhm!!.toString()
                    )
                    if (demo.tensionStator !== null) tensionStator.setChecked(
                        demo.tensionStator!!
                    )
                    if (demo.tensionStatorU !== null) tensionStatorU.setText(
                        demo.tensionStatorU!!.toString()
                    )
                    if (demo.tensionStatorV !== null) tensionStatorV.setText(
                        demo.tensionStatorV!!.toString()
                    )
                    if (demo.tensionStatorW !== null) tensionStatorW.setText(
                        demo.tensionStatorW!!.toString()
                    )
                    if (demo.tensionInducteurs !== null) tensionInducteurs.setChecked(
                        demo.tensionInducteurs!!
                    )
                    if (demo.tensionInducteursU !== null) tensionInducteursU.setText(
                        demo.tensionInducteursU!!.toString()
                    )
                    if (demo.tensionInducteursV !== null) tensionInducteursV.setText(
                        demo.tensionInducteursV!!.toString()
                    )
                    if (demo.tensionInducteursW !== null) tensionInducteursW.setText(
                        demo.tensionInducteursW!!.toString()
                    )
                    if (demo.intensiteStator !== null) intensiteStator.setChecked(
                        demo.intensiteStator!!
                    )
                    if (demo.intensiteStatorU !== null) intensiteStatorU.setText(
                        demo.intensiteStatorU!!.toString()
                    )
                    if (demo.intensiteStatorV !== null) intensiteStatorV.setText(
                        demo.intensiteStatorV!!.toString()
                    )
                    if (demo.intensiteStatorW !== null) intensiteStatorW.setText(
                        demo.intensiteStatorW!!.toString()
                    )
                    if (demo.intensiteInducteurs !== null) intensiteInducteurs.setChecked(
                        demo.intensiteInducteurs!!
                    )
                    if (demo.intensiteInducteursU !== null) intensiteInducteursU.setText(
                        demo.intensiteInducteursU!!.toString()
                    )
                    if (demo.intensiteInducteursV !== null) intensiteInducteursV.setText(
                        demo.intensiteInducteursV!!.toString()
                    )
                    if (demo.intensiteInducteursW !== null) intensiteInducteursW.setText(
                        demo.intensiteInducteursW!!.toString()
                    )
                    if (demo.tensionInduit !== null) tensionInduit.setChecked(
                        demo.tensionInduit!!
                    )
                    if (demo.tensionInduitU !== null) tensionInduitU.setText(
                        demo.tensionInduitU!!.toString()
                    )
                    if (demo.tensionInduitV !== null) tensionInduitV.setText(
                        demo.tensionInduitV!!.toString()
                    )
                    if (demo.tensionInduitW !== null) tensionInduitW.setText(
                        demo.tensionInduitW!!.toString()
                    )
                    if (demo.tensionRotor !== null) tensionRotor.setChecked(
                        demo.tensionRotor!!
                    )
                    if (demo.tensionRotorU !== null) tensionRotorU.setText(
                        demo.tensionRotorU!!.toString()
                    )
                    if (demo.tensionRotorV !== null) tensionRotorV.setText(
                        demo.tensionRotorV!!.toString()
                    )
                    if (demo.tensionRotorW !== null) tensionRotorW.setText(
                        demo.tensionRotorW!!.toString()
                    )
                    if (demo.intensiteInduitU !== null) intensiteInduitU.setText(
                        demo.intensiteInduitU!!.toString()
                    )
                    if (demo.vitesseU !== null) vitesseU.setText(demo.vitesseU!!.toString())
                    if (demo.dureeEssai !== null) dureeEssai.setText(demo.dureeEssai!!.toString())
                    if (demo.puissanceU !== null) puissanceU.setText(demo.puissanceU!!.toString())

                    if (demo.vitesse1V !== null) V1V.setText(demo.vitesse1V!!.toString())
                    if (demo.vitesse1H !== null) V1H.setText(demo.vitesse1H!!.toString())
                    if (demo.vitesse2H !== null) V2H.setText(demo.vitesse2H!!.toString())
                    if (demo.vitesse2V !== null) V2V.setText(demo.vitesse2V!!.toString())
                    if (demo.vitesse2A !== null) V2A.setText(demo.vitesse2A!!.toString())
                    if (demo.acceleration1V !== null) A1V.setText(demo.acceleration1V!!.toString())
                    if (demo.acceleration1H !== null) A1H.setText(demo.acceleration1H!!.toString())
                    if (demo.acceleration2V !== null) A2V.setText(demo.acceleration2V!!.toString())
                    if (demo.acceleration2H !== null) A2H.setText(demo.acceleration2H!!.toString())
                    if (demo.acceleration2A !== null) A2A.setText(demo.acceleration2A!!.toString())
                    if (demo.sensRotation !== null && demo.sensRotation == 2) sensRotation.setChecked(
                        true
                    ) else sensRotation.setChecked(false)
                    if (demo.verificationFixationCouronne !== null) fixCouronne.setChecked(
                        demo.verificationFixationCouronne!!
                    )

                }
                if (demo!!.typeFicheRemontage == 1 || demo!!.typeFicheRemontage == 2 || demo!!.typeFicheRemontage == 3 || demo!!.typeFicheRemontage == 4) {
                    viewModel.selection.value = demo
                    viewModel.selection.value!!.status = 2L
                    layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisDynamiques).visibility = View.VISIBLE
                    layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE

                }
                if (viewModel.selection.value !== null && viewModel.isOnline(viewModel.context)) viewModel.getListeDemontage()
                layout.findViewById<EditText>(R.id.observations).visibility = View.VISIBLE
                layout.findViewById<LinearLayout>(R.id.btns).visibility = View.VISIBLE

                if (viewModel.selection.value!!.remontageRoulement !== null) spinnerMnt.setSelection(
                    viewModel.selection.value!!.remontageRoulement!!
                )
                if (viewModel.selection.value!!.collageRoulementPorteeArbre !== null) spinnerCPA.setSelection(
                    viewModel.selection.value!!.collageRoulementPorteeArbre!!
                )
                if (viewModel.selection.value!!.collageRoulementFlasque !== null) spinnerCIF.setSelection(
                    viewModel.selection.value!!.collageRoulementFlasque!!
                )
                if (viewModel.selection.value!!.observations !== null) obs.setText(viewModel.selection.value!!.observations!!.toString())


                //infoMoteur.visibility = View.VISIBLE
                //essaisDynamiques.visibility = View.VISIBLE
            }
        }

        titre.setOnClickListener {
        }
        var btnquitter = layout.findViewById<Button>(R.id.quit)
        var btnenregistrer = layout.findViewById<Button>(R.id.enregistrer)
        btnquitter.setOnClickListener {
            viewModel.retour(layout)
        }
        btnFichesD.setOnClickListener {
            if (context?.let { it1 -> viewModel.isOnline(it1) } == true) {
                viewModel.getListeDemontage()
            }
        }
        btnDemo.setOnClickListener {
            // Log.i("Info","nb fiche demo ${viewModel.listeDemo.value?.size}")
            if (viewModel.isOnline(viewModel.context)) {
                runBlocking {
                    viewModel.getListeDemontage()
                    var numFiches = arrayListOf<String>()
                    viewModel.listeDemo.value?.forEach {
                        if (!numFiches.contains(it.numFiche!!)) numFiches.add(it.numFiche!!)
                    }
                    val alertDialog: AlertDialog? = activity?.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle("Sélectionnez une fiche de démontage")
                            .setItems(
                                numFiches.toTypedArray(),
                                DialogInterface.OnClickListener { dialog, which ->
                                    Log.i(
                                        "INFO",
                                        "fiches existantes: ${viewModel.listeDemo.value!!.size} fiche envoyée ${
                                            viewModel.listeDemo.value!!.find { it.numFiche == numFiches[which] }!!.numFiche
                                        } index: ${which} - fiche: ${numFiches[which]}"
                                    )
                                    var fiche = viewModel.listeDemo.value!![which]
                                    viewModel.toFicheDemo(
                                        layout,
                                        viewModel.listeDemo.value!!.find { it.numFiche == numFiches[which] }!!
                                    )
                                })
                        builder.create()
                    }
                    if (viewModel.listeDemo.value!!.size > 0) {
                        alertDialog?.show()
                    }
                }
            } else {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.RemontageLayout),
                    "Vous devez être connecté à internet pour réccupérer les fiches de démontage",
                    3600
                )
                mySnackbar.show()
            }
        }


        spinnerMnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    if (position > 0) {
                        viewModel.selection.value!!.remontageRoulement = position
                        Log.i("info","roulement remontés ${spinnerMnt.selectedItem}")
                        viewModel.getTime()
                        viewModel.quickSave()
                    }
            }
        }
        spinnerCPA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    if (position > 0) {
                        viewModel.selection.value!!.collageRoulementPorteeArbre = position
                        viewModel.getTime()
                        viewModel.quickSave()
                    }
            }
        }
        spinnerCIF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    if (position > 0) {
                        viewModel.selection.value!!.collageRoulementFlasque = position
                        viewModel.getTime()
                        viewModel.quickSave()
                    }
            }
        }
        obs.doAfterTextChanged {
            if (obs.text.isNotEmpty() && obs.hasFocus()) {
                viewModel.selection.value!!.observations = obs.text.toString()
                viewModel.getTime()
                viewModel.quickSave()
            }
        }

        btnenregistrer.setOnClickListener {
            Log.i(
                "INFO",
                "collage inté flasque ${viewModel.selection.value!!.collageRoulementPorteeArbre}"
            )
            var fiche = viewModel.selection.value!!
            if (fiche.dureeTotale !== null) {
                fiche.dureeTotale =
                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
            } else {
                fiche.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.quickSave()
            viewModel.enregistrer(layout)
        }
        term.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Terminer une fiche")
                    .setMessage("Êtes vous sûr de vouloir terminer la fiche? elle ne sera plus modifiable après")
                    .setPositiveButton("Terminer",
                        DialogInterface.OnClickListener { dialog, id ->
                            var fiche = viewModel.selection.value!!
                            if (fiche.dureeTotale !== null) {
                                fiche.dureeTotale =
                                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
                            } else {
                                fiche.dureeTotale = Date().time - viewModel.start.value!!.time
                            }
                            fiche.status = 3L
                            viewModel.selection.value = fiche
                            viewModel.quickSave()
                            viewModel.enregistrer(layout)
                        })
                builder.create()
            }
            alertDialog?.show()
        }


        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}