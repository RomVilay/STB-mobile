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
import androidx.constraintlayout.widget.ConstraintLayout
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
        viewModel.token = arguments?.get("token") as String
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterRemontages = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.listeRemontages.map { it.numFiche })
        spinner.adapter = adapterRemontages
        var btnRemontage = layout.findViewById<Button>(R.id.btnDemarrer)
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
        var dureeEssai = layout.findViewById<EditText>(R.id.dureeEssai)
        //tout sauf reducteur et pompe
        var isolementPhaseMasseU = layout.findViewById<EditText>(R.id.isoPMU)
        var isolementPhaseMasseV = layout.findViewById<EditText>(R.id.isoPMV)
        var isolementPhaseMasseW = layout.findViewById<EditText>(R.id.isoPMW)
        var isolementPhasePhaseU = layout.findViewById<EditText>(R.id.isoPPU)
        var isolementPhasePhaseV = layout.findViewById<EditText>(R.id.isoPPV)
        var isolementPhasePhaseW = layout.findViewById<EditText>(R.id.isoPPW)
        var resistanceStatorU = layout.findViewById<EditText>(R.id.rSU)
        var resistanceStatorV = layout.findViewById<EditText>(R.id.rSV)
        var resistanceStatorW = layout.findViewById<EditText>(R.id.rSW)
        var tension = layout.findViewById<EditText>(R.id.tension)
        var intensiteU = layout.findViewById<EditText>(R.id.iU)
        var intensiteV = layout.findViewById<EditText>(R.id.iV)
        var intensiteW = layout.findViewById<EditText>(R.id.iW)
        //tout sauf réducteur/pompe/mono
        var tisoPhase = layout.findViewById<TableLayout>(R.id.tisoPhase)
        var isolementPhase = layout.findViewById<EditText>(R.id.isoPhase)
        // seulement mono (2)
        var tmono = layout.findViewById<TableLayout>(R.id.tmono)
        var resistanceTravail = layout.findViewById<EditText>(R.id.rTrav)
        var resistanceDemarrage = layout.findViewById<EditText>(R.id.rDem)
        //seulement alter (3)
        var tAlterStat = layout.findViewById<TableLayout>(R.id.tAlterStat)
        var tAlterDyn = layout.findViewById<TableLayout>(R.id.tAlterDyn)
        var isolementMasseStatorPrincipalU = layout.findViewById<EditText>(R.id.isoMSPU)
        var isolementMasseStatorPrincipalV = layout.findViewById<EditText>(R.id.isoMSPV)
        var isolementMasseStatorPrincipalW = layout.findViewById<EditText>(R.id.isoMSPW)
        var isolementMasseRotorPrincipal = layout.findViewById<EditText>(R.id.isoMRP)
        var isolementMasseStatorExcitation = layout.findViewById<EditText>(R.id.isoMSE)
        var isolementMasseRotorExcitation = layout.findViewById<EditText>(R.id.isoMRE)
        var resistanceRotorPrincipal = layout.findViewById<EditText>(R.id.rRP)
        var resistanceStatorExcitation = layout.findViewById<EditText>(R.id.rSE)
        var resistanceRotorExcitation = layout.findViewById<EditText>(R.id.rRE)
        var tensionExcitation = layout.findViewById<EditText>(R.id.TE)
        var intensiteExcitation = layout.findViewById<EditText>(R.id.IE)
        //seulement Courant continu
        var ccStat = layout.findViewById<TableLayout>(R.id.CCstat)
        var ccDyna = layout.findViewById<TableLayout>(R.id.CCDyna)
        var tvf = layout.findViewById<TextView>(R.id.tVF)
        var fixCouronne = layout.findViewById<CheckBox>(R.id.fixCouronne)
        var isolementPhaseMasseRotorU = layout.findViewById<EditText>(R.id.isoPMRU)
        var isolementPhaseMasseRotorV = layout.findViewById<EditText>(R.id.isoPMRV)
        var isolementPhaseMasseRotorW = layout.findViewById<EditText>(R.id.isoPMRW)
        var isolementPhaseRotorUV = layout.findViewById<EditText>(R.id.isoPRUV)
        var isolementPhaseRotorVW = layout.findViewById<EditText>(R.id.isoPRVW)
        var isolementPhaseRotorUW = layout.findViewById<EditText>(R.id.isoPRUW)
        var isolementPolePrincipal = layout.findViewById<EditText>(R.id.isoPP)
        var isolementPoleAuxillaire = layout.findViewById<EditText>(R.id.isoPA)
        var isolementPoleCompensatoire = layout.findViewById<EditText>(R.id.isoPC)
        var isolementPorteBalais = layout.findViewById<EditText>(R.id.iPB)
        var isolementInduit = layout.findViewById<EditText>(R.id.isoI)
        var resistanceInduit = layout.findViewById<EditText>(R.id.rInduit)
        var resistancePolePrincipal = layout.findViewById<EditText>(R.id.rPP)
        var resistancePoleAuxilliaire = layout.findViewById<EditText>(R.id.rPA)
        var resistancePoleCompensatoire = layout.findViewById<EditText>(R.id.rPC)
        var tensionInducteursU = layout.findViewById<EditText>(R.id.tIU)
        var tensionInducteursV = layout.findViewById<EditText>(R.id.tIV)
        var tensionInducteursW = layout.findViewById<EditText>(R.id.tIW)
        var intensiteInducteursU = layout.findViewById<EditText>(R.id.iIU)
        var intensiteInducteursV = layout.findViewById<EditText>(R.id.iIV)
        var intensiteInducteursW = layout.findViewById<EditText>(R.id.iIW)
        var tensionInduitU = layout.findViewById<EditText>(R.id.tiU)
        var tensionInduitV = layout.findViewById<EditText>(R.id.tiV)
        var tensionInduitW = layout.findViewById<EditText>(R.id.tiW)
        var tensionRotorU = layout.findViewById<EditText>(R.id.trU)
        var tensionRotorV = layout.findViewById<EditText>(R.id.trV)
        var tensionRotorW = layout.findViewById<EditText>(R.id.trW)
        var intensiteInduit = layout.findViewById<EditText>(R.id.iI)
        var tensionECC = layout.findViewById<EditText>(R.id.tE)
        var intensiteECC = layout.findViewById<EditText>(R.id.iE)
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
                //btnFichesD.visibility = View.VISIBLE
                viewModel.start.value = Date()
                var demo = viewModel.listeRemontages.find { it.numFiche == spinner.selectedItem }.apply {
                    this!!.status = 2L
                }
                layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
                if (demo != null) {
                    tmono.visibility = View.GONE
                    tisoPhase.visibility = View.GONE
                    tAlterStat.visibility = View.GONE
                    tAlterDyn.visibility = View.GONE
                    ccDyna.visibility = View.GONE
                    ccStat.visibility = View.GONE
                    tvf.visibility = View.GONE
                    fixCouronne.visibility = View.GONE
                    if (demo.dureeEssai !== null) dureeEssai.setText(demo.dureeEssai!!.toString())
                    if (demo.remontageRoulement !== null) spinnerMnt.setSelection(demo.remontageRoulement!!)
                    if (demo.collageRoulementPorteeArbre !== null) spinnerCPA.setSelection(demo.collageRoulementPorteeArbre!!)
                    if (demo.collageRoulementFlasque !== null) spinnerCIF.setSelection(demo.collageRoulementFlasque!!)
                    spinnerMnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if ((position > 0) ) {
                                demo.remontageRoulement = position
                                viewModel.selection.value = demo
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
                            if ((position > 0) ) {
                                demo.collageRoulementPorteeArbre = position
                                viewModel.selection.value = demo
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
                            if ((position > 0) ) {
                                demo.collageRoulementFlasque = position
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                    }
                    if (demo.observations !== null) obs.setText(demo.observations)
                    if (demo.subtype !== 1 || demo.subtype !== 8) {
                        tisoPhase.visibility = View.VISIBLE
                        if (layout.findViewById<CardView>(R.id.essaisStatiques).visibility == View.GONE) layout.findViewById<CardView>(R.id.essaisStatiques).visibility = View.VISIBLE
                        if (layout.findViewById<ConstraintLayout>(R.id.essaisDynamiques).visibility == View.GONE) layout.findViewById<ConstraintLayout>(R.id.essaisDynamiques).visibility = View.VISIBLE
                        if (demo.isolementPhaseMasseU !== null) isolementPhaseMasseU.setText(
                            demo.isolementPhaseMasseU!!
                        )
                        if (demo.isolementPhaseMasseV !== null) isolementPhaseMasseV.setText(
                            demo.isolementPhaseMasseV!!
                        )
                        if (demo.isolementPhaseMasseW !== null) isolementPhaseMasseW.setText(
                            demo.isolementPhaseMasseW!!
                        )
                        if (demo.isolementPhasePhaseU !== null) isolementPhasePhaseU.setText(
                            demo.isolementPhasePhaseU!!
                        )
                        if (demo.isolementPhasePhaseV !== null) isolementPhasePhaseV.setText(
                            demo.isolementPhasePhaseV!!
                        )
                        if (demo.isolementPhasePhaseW !== null) isolementPhasePhaseW.setText(
                            demo.isolementPhasePhaseW!!
                        )
                        if (demo.resistanceStatorU !== null) resistanceStatorU.setText(
                            demo.resistanceStatorU!!
                        )
                        if (demo.resistanceStatorV !== null) resistanceStatorV.setText(
                            demo.resistanceStatorV!!
                        )
                        if (demo.resistanceStatorW !== null) resistanceStatorW.setText(
                            demo.resistanceStatorW!!
                        )
                        if (demo.tension !== null) tension.setText(
                            demo.tension!!
                        )
                        if (demo.intensiteU !== null) intensiteU.setText(
                            demo.intensiteU!!
                        )
                        if (demo.intensiteV !== null) intensiteV.setText(
                            demo.intensiteV!!
                        )
                        if (demo.intensiteW !== null) intensiteW.setText(
                            demo.intensiteW!!
                        )
                        if (demo.isolementPhase !== null) isolementPhase.setText(
                            demo.isolementPhase
                        )
                        if(demo!!.subtype == 2) {
                            tmono.visibility = View.VISIBLE
                            if (demo.resistanceTravail !== null) resistanceTravail.setText(
                                demo.resistanceTravail!!
                            )
                            if (demo.resistanceDemarrage !== null) resistanceDemarrage.setText(
                                demo.resistanceDemarrage!!
                            )
                            tisoPhase.visibility = View.GONE
                        }
                        if(demo!!.subtype == 3) {
                            tAlterStat.visibility = View.VISIBLE
                            tAlterDyn.visibility = View.VISIBLE

                            if (demo.isolementMasseStatorPrincipalU !== null) isolementMasseStatorPrincipalU.setText(
                                demo.isolementMasseStatorPrincipalU!!
                            )
                            if (demo.isolementMasseStatorPrincipalV !== null) isolementMasseStatorPrincipalV.setText(
                                demo.isolementMasseStatorPrincipalV!!
                            )
                            if (demo.isolementMasseStatorPrincipalW !== null) isolementMasseStatorPrincipalW.setText(
                                demo.isolementMasseStatorPrincipalW!!
                            )
                            if (demo.isolementMasseRotorPrincipal !== null) isolementMasseRotorPrincipal.setText(
                                demo.isolementMasseRotorPrincipal!!
                            )
                            if (demo.isolementMasseStatorExcitation !== null) isolementMasseStatorExcitation.setText(
                                demo.isolementMasseStatorExcitation!!
                            )
                            if (demo.isolementMasseRotorExcitation !== null) isolementMasseRotorExcitation.setText(
                                demo.isolementMasseRotorExcitation!!
                            )
                            if (demo.resistanceRotorPrincipal !== null) resistanceRotorPrincipal.setText(
                                demo.resistanceRotorPrincipal!!
                            )
                            if (demo.resistanceStatorExcitation !== null) resistanceStatorExcitation.setText(
                                demo.resistanceStatorExcitation!!
                            )
                            if (demo.resistanceRotorExcitation !== null) resistanceRotorExcitation.setText(
                                demo.resistanceRotorExcitation!!
                            )
                            if (demo.tensionExcitation !== null) tensionExcitation.setText(
                                demo.tensionExcitation!!
                            )
                            if (demo.intensiteExcitation !== null) intensiteExcitation.setText(
                                demo.intensiteExcitation!!
                            )
                        }
                        if(demo!!.subtype == 5) {
                            ccDyna.visibility = View.VISIBLE
                            ccStat.visibility = View.VISIBLE
                            tvf.visibility = View.VISIBLE
                            fixCouronne.visibility = View.VISIBLE
                            fixCouronne.setChecked(demo.verificationFixationCouronne!!)
                            fixCouronne.setOnCheckedChangeListener { _, isChecked ->
                                    demo.verificationFixationCouronne = isChecked//viewModel.selection.value!!.verificationFixationCouronne = isChecked
                                    viewModel.selection.value = demo
                                    viewModel.getTime()
                                    viewModel.quickSave()
                            }
                            if (demo.isolementPorteBalais !== null) isolementPorteBalais.setText(
                                demo.isolementPorteBalais!!
                            )
                            if (demo.isolementPhaseMasseRotorU !== null) isolementPhaseMasseRotorU.setText(
                                demo.isolementPhaseMasseRotorU!!
                            )
                            if (demo.isolementPhaseMasseRotorV !== null) isolementPhaseMasseRotorV.setText(
                                demo.isolementPhaseMasseRotorV!!
                            )
                            if (demo.isolementPhaseMasseRotorW !== null) isolementPhaseMasseRotorW.setText(
                                demo.isolementPhaseMasseRotorW!!
                            )
                            if (demo.isolementPhaseRotorUV !== null) isolementPhaseRotorUV.setText(
                                demo.isolementPhaseRotorUV!!
                            )
                            if (demo.isolementPhaseRotorVW !== null) isolementPhaseRotorVW.setText(
                                demo.isolementPhaseRotorVW!!
                            )
                            if (demo.isolementInduit !== null) isolementInduit.setText(
                                demo.isolementInduit!!
                            )
                            if (demo.isolementPolePrincipal !== null) isolementPolePrincipal.setText(
                                demo.isolementPolePrincipal!!
                            )
                            if (demo.isolementPoleAuxilliaire !== null) isolementPoleAuxillaire.setText(
                                demo.isolementPoleAuxilliaire!!
                            )
                            if (demo.isolementPoleCompensatoire !== null) isolementPoleCompensatoire.setText(
                                demo.isolementPoleCompensatoire!!
                            )
                            if (demo.resistanceInduit !== null) resistanceInduit.setText(
                                demo.resistanceInduit!!
                            )
                            if (demo.resistancePolePrincipal !== null) resistancePolePrincipal.setText(
                                demo.resistancePolePrincipal!!
                            )
                            if (demo.resistancePoleAuxilliaire !== null) resistancePoleAuxilliaire.setText(
                                demo.resistancePoleAuxilliaire!!
                            )
                            if (demo.resistancePoleCompensatoire !== null) resistancePoleCompensatoire.setText(
                                demo.resistancePoleCompensatoire!!
                            )
                            if (demo.tensionInducteursU !== null) tensionInducteursU.setText(
                                demo.tensionInducteursU!!
                            )
                            if (demo.tensionInducteursV !== null) tensionInducteursV.setText(
                                demo.tensionInducteursV!!
                            )
                            if (demo.tensionInducteursW !== null) tensionInducteursW.setText(
                                demo.tensionInducteursW!!
                            )
                            if (demo.intensiteInducteursU !== null) intensiteInducteursU.setText(
                                demo.intensiteInducteursU!!
                            )
                            if (demo.intensiteInducteursV !== null) intensiteInducteursV.setText(
                                demo.intensiteInducteursV!!
                            )
                            if (demo.intensiteInducteursW !== null) intensiteInducteursW.setText(
                                demo.intensiteInducteursW!!
                            )
                            if (demo.tensionInduitU!== null) tensionInduitU.setText(
                                demo.tensionInduitU!!
                            )
                            if (demo.tensionInduitV !== null) tensionInduitV.setText(
                                demo.tensionInduitV!!
                            )
                            if (demo.tensionInduitW !== null) tensionInduitW.setText(
                                demo.tensionInducteursW!!
                            )
                            if (demo.intensiteInduit !== null) intensiteInduit.setText(
                                demo.intensiteInduit!!
                            )
                            if (demo.tensionRotorU !== null) tensionRotorU.setText(
                                demo.tensionRotorU!!
                            )
                            if (demo.tensionRotorV !== null) tensionRotorV.setText(
                                demo.tensionRotorV!!
                            )
                            if (demo.tensionRotorW !== null) tensionRotorW.setText(
                                demo.tensionRotorW!!
                            )
                            if (demo.tensionExcitation !== null) tensionECC.setText(
                                demo.tensionExcitation!!
                            )
                            if (demo.intensiteExcitation !== null) intensiteECC.setText(
                                demo.intensiteExcitation!!
                            )
                        }
                        if(demo!!.subtype == 7 && demo!!.typeMotopompe == 2) {
                            tmono.visibility = View.VISIBLE
                            tisoPhase.visibility = View.GONE
                            if (demo.resistanceTravail !== null) resistanceTravail.setText(
                                demo.resistanceTravail!!
                            )
                            if (demo.resistanceDemarrage !== null) resistanceDemarrage.setText(
                                demo.resistanceDemarrage!!
                            )
                        }
                        if(demo!!.subtype == 9 && demo!!.typeMotoreducteur == 2) {
                            tisoPhase.visibility = View.GONE
                            tmono.visibility = View.VISIBLE
                            if (demo.resistanceTravail !== null) resistanceTravail.setText(
                                demo.resistanceTravail!!
                            )
                            if (demo.resistanceDemarrage !== null) resistanceDemarrage.setText(
                                demo.resistanceDemarrage!!
                            )
                        }
                        dureeEssai.doAfterTextChanged {
                            if (dureeEssai.hasFocus() && dureeEssai.text.isNotEmpty()) {
                                demo.dureeEssai = dureeEssai.text.toString().toInt()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseU.doAfterTextChanged {
                            if (isolementPhaseMasseU.hasFocus() && isolementPhaseMasseU.text.isNotEmpty()) {
                                demo.isolementPhaseMasseU = isolementPhaseMasseU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseV.doAfterTextChanged {
                            if (isolementPhaseMasseV.hasFocus() && isolementPhaseMasseV.text.isNotEmpty()) {
                                demo.isolementPhaseMasseV = isolementPhaseMasseV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseW.doAfterTextChanged {
                            if (isolementPhaseMasseW.hasFocus() && isolementPhaseMasseW.text.isNotEmpty()) {
                                demo.isolementPhaseMasseW = isolementPhaseMasseW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhasePhaseU.doAfterTextChanged {
                            if (isolementPhasePhaseU.hasFocus() && isolementPhasePhaseU.text.isNotEmpty()) {
                                demo.isolementPhasePhaseU = isolementPhasePhaseU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhasePhaseV.doAfterTextChanged {
                            if (isolementPhasePhaseV.hasFocus() && isolementPhasePhaseV.text.isNotEmpty()) {
                                demo.isolementPhasePhaseV = isolementPhasePhaseV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhasePhaseW.doAfterTextChanged {
                            if (isolementPhasePhaseW.hasFocus() && isolementPhasePhaseW.text.isNotEmpty()) {
                                demo.isolementPhasePhaseW = isolementPhasePhaseW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceStatorU.doAfterTextChanged {
                            if (resistanceStatorU.text.isNotEmpty() && resistanceStatorU.hasFocus()
                            ) {
                                demo.resistanceStatorU = resistanceStatorU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceStatorV.doAfterTextChanged {
                            if (resistanceStatorV.text.isNotEmpty() && resistanceStatorV.hasFocus()) {
                                demo.resistanceStatorV = resistanceStatorV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceStatorW.doAfterTextChanged {
                            if (resistanceStatorW.text.isNotEmpty() && resistanceStatorW.hasFocus()) {
                                demo.resistanceStatorW = resistanceStatorW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tension.doAfterTextChanged {
                            if (tension.text.isNotEmpty() && tension.hasFocus()) {
                                demo.tension = tension.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteU.doAfterTextChanged {
                            if (intensiteU.text.isNotEmpty() && intensiteU.hasFocus()) {
                                demo.intensiteU =
                                    intensiteU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteV.doAfterTextChanged {
                            if (intensiteV.text.isNotEmpty() && intensiteV.hasFocus()) {
                                demo.intensiteV =
                                    intensiteV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteW.doAfterTextChanged {
                            if (intensiteW.text.isNotEmpty() && intensiteW.hasFocus()) {
                                demo.intensiteW =
                                    intensiteW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhase.doAfterTextChanged {
                            if (isolementPhase.text.isNotEmpty() && isolementPhase.hasFocus()) {
                                demo.isolementPhase =
                                    isolementPhase.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceTravail.doAfterTextChanged {
                            if (resistanceTravail.text.isNotEmpty() && resistanceTravail.hasFocus()) {
                                demo.resistanceTravail =
                                    resistanceTravail.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceDemarrage.doAfterTextChanged {
                            if (resistanceDemarrage.text.isNotEmpty() && resistanceDemarrage.hasFocus()) {
                                demo.resistanceDemarrage =
                                    resistanceDemarrage.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseStatorPrincipalU.doAfterTextChanged {
                            if (isolementMasseStatorPrincipalU.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus()) {
                                demo.isolementMasseStatorPrincipalU =
                                    isolementMasseStatorPrincipalU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseStatorPrincipalV.doAfterTextChanged {
                            if (isolementMasseStatorPrincipalV.text.isNotEmpty() && isolementMasseStatorPrincipalV.hasFocus()) {
                                demo.isolementMasseStatorPrincipalV =
                                    isolementMasseStatorPrincipalV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseStatorPrincipalW.doAfterTextChanged {
                            if (isolementMasseStatorPrincipalW.text.isNotEmpty() && isolementMasseStatorPrincipalW.hasFocus()) {
                                demo.isolementMasseStatorPrincipalW =
                                    isolementMasseStatorPrincipalW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseRotorPrincipal.doAfterTextChanged {
                            if (isolementMasseRotorPrincipal.text.isNotEmpty() && isolementMasseRotorPrincipal.hasFocus()) {
                                demo.isolementMasseRotorPrincipal =
                                    isolementMasseRotorPrincipal.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseStatorExcitation.doAfterTextChanged {
                            if (isolementMasseStatorExcitation.text.isNotEmpty() && isolementMasseStatorExcitation.hasFocus()) {
                                demo.isolementMasseStatorExcitation =
                                    isolementMasseStatorExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementMasseRotorExcitation.doAfterTextChanged {
                            if (isolementMasseRotorExcitation.text.isNotEmpty() && isolementMasseRotorExcitation.hasFocus()) {
                                demo.isolementMasseRotorExcitation =
                                    isolementMasseRotorExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceRotorPrincipal.doAfterTextChanged {
                            if (resistanceRotorPrincipal.text.isNotEmpty() && resistanceRotorPrincipal.hasFocus()) {
                                demo.resistanceRotorPrincipal =
                                    resistanceRotorPrincipal.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceStatorExcitation.doAfterTextChanged {
                            if (resistanceStatorExcitation.text.isNotEmpty() && resistanceStatorExcitation.hasFocus()) {
                                demo.resistanceStatorExcitation =
                                    resistanceStatorExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceRotorExcitation.doAfterTextChanged {
                            if (resistanceRotorExcitation.text.isNotEmpty() && resistanceRotorExcitation.hasFocus()) {
                                demo.resistanceRotorExcitation =
                                    resistanceRotorExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionExcitation.doAfterTextChanged {
                            if (tensionExcitation.text.isNotEmpty() && tensionExcitation.hasFocus()) {
                                demo.tensionExcitation = tensionExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteExcitation.doAfterTextChanged {
                            if (intensiteExcitation.text.isNotEmpty() && intensiteExcitation.hasFocus()) {
                                demo.intensiteExcitation = intensiteExcitation.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPorteBalais.doAfterTextChanged {
                            if (isolementPorteBalais.text.isNotEmpty() && isolementPorteBalais.hasFocus()) {
                                demo.isolementPorteBalais = isolementPorteBalais.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseRotorU.doAfterTextChanged {
                            if (isolementPhaseMasseRotorU.text.isNotEmpty() && isolementPhaseMasseRotorU.hasFocus()) {
                                demo.isolementPhaseMasseRotorU = isolementPhaseMasseRotorU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseRotorV.doAfterTextChanged {
                            if (isolementPhaseMasseRotorV.text.isNotEmpty() && isolementPhaseMasseRotorV.hasFocus()) {
                                demo.isolementPhaseMasseRotorV = isolementPhaseMasseRotorV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseMasseRotorW.doAfterTextChanged {
                            if (isolementPhaseMasseRotorW.text.isNotEmpty() && isolementPhaseMasseRotorW.hasFocus()) {
                                demo.isolementPhaseMasseRotorW = isolementPhaseMasseRotorW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseRotorUV.doAfterTextChanged {
                            if (isolementPhaseRotorUV.text.isNotEmpty() && isolementPhaseRotorUV.hasFocus()) {
                                demo.isolementPhaseRotorUV = isolementPhaseRotorUV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseRotorVW.doAfterTextChanged {
                            if (isolementPhaseRotorVW.text.isNotEmpty() && isolementPhaseRotorVW.hasFocus()) {
                                demo.isolementPhaseRotorVW = isolementPhaseRotorVW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPhaseRotorUW.doAfterTextChanged {
                            if (isolementPhaseRotorUW.text.isNotEmpty() && isolementPhaseRotorUW.hasFocus()) {
                                demo.isolementPhaseRotorUW = isolementPhaseRotorUW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementInduit.doAfterTextChanged {
                            if (isolementInduit.text.isNotEmpty() && isolementInduit.hasFocus()) {
                                demo.isolementInduit =
                                    isolementInduit.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPolePrincipal.doAfterTextChanged {
                            if (isolementPolePrincipal.text.isNotEmpty() && isolementPolePrincipal.hasFocus()) {
                                demo.isolementPolePrincipal =
                                    isolementPolePrincipal.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPoleAuxillaire.doAfterTextChanged {
                            if (isolementPoleAuxillaire.text.isNotEmpty() && isolementPoleAuxillaire.hasFocus()) {
                                demo.isolementPoleAuxilliaire =
                                    isolementPoleAuxillaire.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        isolementPoleCompensatoire.doAfterTextChanged {
                            if (isolementPoleCompensatoire.text.isNotEmpty() && isolementPoleCompensatoire.hasFocus()) {
                                demo.isolementPoleCompensatoire =
                                    isolementPoleCompensatoire.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistanceInduit.doAfterTextChanged {
                            if (resistanceInduit.text.isNotEmpty() && resistanceInduit.hasFocus()) {
                                demo.resistanceInduit =
                                    resistanceInduit.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistancePolePrincipal.doAfterTextChanged {
                            if (resistancePolePrincipal.text.isNotEmpty() && resistancePolePrincipal.hasFocus()) {
                                demo.resistancePolePrincipal =
                                    resistancePolePrincipal.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistancePoleAuxilliaire.doAfterTextChanged {
                            if (resistancePoleAuxilliaire.text.isNotEmpty() && resistancePoleAuxilliaire.hasFocus()) {
                                demo.resistancePoleAuxilliaire =
                                    resistancePoleAuxilliaire.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        resistancePoleCompensatoire.doAfterTextChanged {
                            if (resistancePoleCompensatoire.text.isNotEmpty() && resistancePoleCompensatoire.hasFocus()) {
                                demo.resistancePoleCompensatoire =
                                    resistancePoleCompensatoire.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursU.doAfterTextChanged {
                            if (tensionInducteursU.text.isNotEmpty() && tensionInducteursU.hasFocus()) {
                                demo.tensionInducteursU = tensionInducteursU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursV.doAfterTextChanged {
                            if (tensionInducteursV.text.isNotEmpty() && tensionInducteursV.hasFocus() ) {
                                demo.tensionInducteursV =
                                    tensionInducteursV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursW.doAfterTextChanged {
                            if (tensionInducteursW.text.isNotEmpty() && tensionInducteursW.hasFocus() ) {
                                demo.tensionInducteursW =
                                    tensionInducteursW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteInducteursU.doAfterTextChanged {
                            if (intensiteInducteursU.text.isNotEmpty() && intensiteInducteursU.hasFocus()) {
                                demo.intensiteInducteursU = intensiteInducteursU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteInducteursV.doAfterTextChanged {
                            if (intensiteInducteursV.text.isNotEmpty() && intensiteInducteursV.hasFocus()) {
                                demo.intensiteInducteursV = intensiteInducteursV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteInducteursW.doAfterTextChanged {
                            if (intensiteInducteursW.text.isNotEmpty() && intensiteInducteursW.hasFocus()) {
                                demo.intensiteInducteursW = intensiteInducteursW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursU.doAfterTextChanged {
                            if (tensionInducteursU.text.isNotEmpty() && tensionInducteursU.hasFocus()) {
                                demo.tensionInducteursU = tensionInducteursU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursV.doAfterTextChanged {
                            if (tensionInducteursV.text.isNotEmpty() && tensionInducteursV.hasFocus()) {
                                demo.tensionInducteursV = tensionInducteursV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInducteursW.doAfterTextChanged {
                            if (tensionInducteursW.text.isNotEmpty() && tensionInducteursW.hasFocus()) {
                                demo.tensionInducteursW = tensionInducteursW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInduitU.doAfterTextChanged {
                            if (tensionInduitU.text.isNotEmpty() && tensionInduitU.hasFocus()) {
                                demo.tensionInduitU = tensionInduitU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInduitV.doAfterTextChanged {
                            if (tensionInduitV.text.isNotEmpty() && tensionInduitV.hasFocus()) {
                                demo.tensionInduitV = tensionInduitV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionInduitW.doAfterTextChanged {
                            if (tensionInduitW.text.isNotEmpty() && tensionInduitW.hasFocus()) {
                                demo.tensionInduitW = tensionInduitW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteInduit.doAfterTextChanged {
                            if (intensiteInduit.text.isNotEmpty() && intensiteInduit.hasFocus()) {
                                demo.intensiteInduit = intensiteInduit.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionRotorU.doAfterTextChanged {
                            if (tensionRotorU.text.isNotEmpty() && tensionRotorU.hasFocus()) {
                                demo.tensionRotorU = tensionRotorU.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionRotorV.doAfterTextChanged {
                            if (tensionRotorV.text.isNotEmpty() && tensionRotorV.hasFocus()) {
                                demo.tensionRotorV = tensionRotorV.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionRotorW.doAfterTextChanged {
                            if (tensionRotorW.text.isNotEmpty() && tensionRotorW.hasFocus()) {
                                demo.tensionRotorW = tensionRotorW.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        tensionECC.doAfterTextChanged {
                            if (tensionECC.text.isNotEmpty() && tensionECC.hasFocus()) {
                                demo.tensionExcitation = tensionECC.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        intensiteECC.doAfterTextChanged {
                            if (intensiteECC.text.isNotEmpty() && intensiteECC.hasFocus()) {
                                demo.intensiteExcitation = intensiteECC.text.toString()
                                viewModel.selection.value = demo
                                viewModel.getTime()
                                viewModel.quickSave()
                            }
                        }
                        //layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE
                    }

                    layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE

                    layout.findViewById<EditText>(R.id.observations).visibility = View.VISIBLE
                    layout.findViewById<LinearLayout>(R.id.btns).visibility = View.VISIBLE
                    //infoMoteur.visibility = View.VISIBLE
                    //essaisDynamiques.visibility = View.VISIBLE
                }
            }
        }

        titre.setOnClickListener {
        }
        var btnquitter = layout.findViewById<Button>(R.id.quit)
        var btnenregistrer = layout.findViewById<Button>(R.id.enregistrer)
        btnquitter.setOnClickListener {
            viewModel.retour(layout)
        }
        /*btnFichesD.setOnClickListener {
            if (context?.let { it1 -> viewModel.isOnline(it1) } == true) {
                viewModel.getListeDemontage()
            }
        }*/
        /* btnDemo.setOnClickListener {
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
         }*/



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