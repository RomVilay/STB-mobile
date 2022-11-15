package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.applicationstb.R
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*


class MotopompeFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_motopompe, container, false)
        var fluide = layout.findViewById<EditText>(R.id.typeFluide)
        var sensRotation = layout.findViewById<Switch>(R.id.sensRotation)
        var typeRessort = layout.findViewById<Spinner>(R.id.typePompe)
        var typeJoint = layout.findViewById<AutoCompleteTextView>(R.id.tjoint)
        ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>("joint torique", "joint enveloppant", "passage anti-rotation")
        ).also { adapter -> typeJoint.setAdapter(adapter) }
        typeRessort.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>(
                " ",
                "entraînement par vis",
                "ressort coaxial conique",
                "ressort coaxial cylindrique",
                "soufflet"
            )
        )
        var matiere = layout.findViewById<Spinner>(R.id.matJoint)
        matiere.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>(" ", "ceramique", "carbone scilicium", "carbone", "tungstène")
        )
        var i4 = layout.findViewById<TextView>(R.id.i4)
        var d7 = layout.findViewById<TextView>(R.id.d7)
        var d3 = layout.findViewById<TextView>(R.id.d3)
        var i3 = layout.findViewById<TextView>(R.id.i3)
        var d1 = layout.findViewById<TextView>(R.id.titreD1)
        var diametreArbre = layout.findViewById<EditText>(R.id.dArbre)
        var diametreExtPR = layout.findViewById<EditText>(R.id.dEPR)
        var longueurRotativeComprimee = layout.findViewById<EditText>(R.id.LC)
        var longueurRotativeTravail = layout.findViewById<EditText>(R.id.LPRT)
        var diametreExtPF = layout.findViewById<EditText>(R.id.dExt)
        var longueurRotativeNonComprimee = layout.findViewById<EditText>(R.id.LNC)
        var epaisseurPF = layout.findViewById<EditText>(R.id.ePF)
        var infosMoteur = layout.findViewById<CardView>(R.id.info)
        var partMono = layout.findViewById<CardView>(R.id.moteur_mono)
        var partMeca = layout.findViewById<CardView>(R.id.mecamp)
        var partTri = layout.findViewById<CardView>(R.id.moteur_triphase)
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        var typemotopompe = layout.findViewById<Spinner>(R.id.typemotopompe)
        typemotopompe.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>("Sélectionnez un type de motopompe", "Triphasé", "Monophasé")
        )
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var termP = layout.findViewById<Button>(R.id.termmp)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
        var switchGarniture = layout.findViewById<ToggleButton>(R.id.switchGarniture2)

        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        //triphase
        var UM = layout.findViewById<EditText>(R.id.isopmPA)
        var VM = layout.findViewById<EditText>(R.id.isopmPP)
        var WM = layout.findViewById<EditText>(R.id.isopmI)
        var UV = layout.findViewById<EditText>(R.id.isoppPC)
        var UW = layout.findViewById<EditText>(R.id.isoppPB)
        var iVW = layout.findViewById<EditText>(R.id.isoppW)
        var RU = layout.findViewById<EditText>(R.id.rInduit)
        var RV = layout.findViewById<EditText>(R.id.rPP)
        var RW = layout.findViewById<EditText>(R.id.rW)
        var isolementPhase = layout.findViewById<EditText>(R.id.vUI)
        var tensionU = layout.findViewById<EditText>(R.id.tensionU)
        var tensionV = layout.findViewById<EditText>(R.id.vVtri)
        var tensionW = layout.findViewById<EditText>(R.id.vWtri)
        //mono
        var resistanceTravail = layout.findViewById<EditText>(R.id.isopmVe)
        var resistanceDemarrage = layout.findViewById<EditText>(R.id.rdem)
        var valeurCondensateur = layout.findViewById<EditText>(R.id.condens)
        var tensionMU = layout.findViewById<EditText>(R.id.tensionV)
        var tensionMV = layout.findViewById<EditText>(R.id.tensionW)
        var tensionMW = layout.findViewById<EditText>(R.id.tW)
        var gal = layout.findViewById<Button>(R.id.gallerie2)
        var fiche = viewModel.selection.value!!
        switchGarniture.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (fiche.matiere2 !== null) matiere.setSelection(fiche.matiere2!!) else matiere.setSelection(
                    0
                )
                if (fiche.typeJoint2 !== null) typeJoint.setText(fiche.typeJoint2.toString()) else typeJoint.setText(
                    ""
                )
                if (fiche.diametreArbre2 !== null) diametreArbre.setText(fiche.diametreArbre2!!.toString()) else diametreArbre.setText(
                    ""
                )
                if (fiche.diametreExtPR2 !== null) diametreExtPR.setText(fiche.diametreExtPR2!!.toString()) else diametreExtPR.setText(
                    ""
                )
                if (fiche.diametreExtPF2 !== null) diametreExtPF.setText(fiche.diametreExtPF2!!.toString()) else diametreExtPF.setText(
                    ""
                )
                if (fiche.epaisseurPF2 !== null) epaisseurPF.setText(fiche.epaisseurPF2!!.toString()) else epaisseurPF.setText(
                    ""
                )
                if (fiche.longueurRotativeNonComprimee2 !== null) longueurRotativeNonComprimee.setText(
                    fiche.longueurRotativeNonComprimee2!!.toString()
                ) else longueurRotativeNonComprimee.setText("")
                if (fiche.longueurRotativeComprimee2 !== null) longueurRotativeComprimee.setText(
                    fiche.longueurRotativeComprimee2!!.toString()
                ) else longueurRotativeComprimee.setText("")
                if (fiche.longueurRotativeTravail2 !== null) longueurRotativeTravail.setText(fiche.longueurRotativeTravail2!!.toString()) else longueurRotativeTravail.setText(
                    ""
                )
            } else {
                if (fiche.matiere !== null) matiere.setSelection(fiche.matiere!!) else matiere.setSelection(
                    0
                )
                if (fiche.typeJoint !== null) typeJoint.setText(fiche.typeJoint.toString()) else typeJoint.setText(
                    ""
                )
                if (fiche.diametreArbre !== null) diametreArbre.setText(fiche.diametreArbre!!.toString()) else diametreArbre.setText(
                    ""
                )
                if (fiche.diametreExtPR !== null) diametreExtPR.setText(fiche.diametreExtPR!!.toString()) else diametreExtPR.setText(
                    ""
                )
                if (fiche.diametreExtPF !== null) diametreExtPF.setText(fiche.diametreExtPF!!.toString()) else diametreExtPF.setText(
                    ""
                )
                if (fiche.epaisseurPF !== null) epaisseurPF.setText(fiche.epaisseurPF!!.toString()) else epaisseurPF.setText(
                    ""
                )
                if (fiche.longueurRotativeNonComprimee !== null) longueurRotativeNonComprimee.setText(
                    fiche.longueurRotativeNonComprimee!!.toString()
                ) else longueurRotativeNonComprimee.setText("")
                if (fiche.longueurRotativeComprimee !== null) longueurRotativeComprimee.setText(
                    fiche.longueurRotativeComprimee!!.toString()
                ) else longueurRotativeComprimee.setText("")
                if (fiche.longueurRotativeTravail !== null) longueurRotativeTravail.setText(fiche.longueurRotativeTravail!!.toString()) else longueurRotativeTravail.setText(
                    ""
                )
            }
        }
        typemotopompe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (typemotopompe.selectedItemPosition == 0){
                    typemotopompe.setBackgroundResource(R.drawable.dropdown_background_type)
                } else {
                    typemotopompe.setBackgroundResource(R.drawable.dropdown_background)
                }
                if (typemotopompe.selectedItem.toString() == "Triphasé") {
                    partMeca.visibility = View.VISIBLE
                    partMono.visibility = View.GONE
                    partTri.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotopompe = 1
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    if (fiche.isolementPhaseMasseStatorUM !== null) UM.setText(fiche.isolementPhaseMasseStatorUM!!.toString()) else 0
                    if (fiche.isolementPhaseMasseStatorVM !== null) VM.setText(fiche.isolementPhaseMasseStatorVM!!.toString()) else 0
                    if (fiche.isolementPhaseMasseStatorWM !== null) WM.setText(fiche.isolementPhaseMasseStatorWM!!.toString()) else 0
                    if (fiche.isolementPhasePhaseStatorUV !== null) UV.setText(fiche.isolementPhasePhaseStatorUV!!.toString())
                    if (fiche.isolementPhasePhaseStatorUW !== null) UW.setText(fiche.isolementPhasePhaseStatorUW!!.toString()) else 0
                    if (fiche.isolementPhasePhaseStatorVW !== null) iVW.setText(fiche.isolementPhasePhaseStatorVW!!.toString()) else 0
                    if (fiche.resistanceStatorU !== null) RU.setText(fiche.resistanceStatorU!!.toString()) else 0
                    if (fiche.resistanceStatorV !== null) RV.setText(fiche.resistanceStatorV!!.toString()) else 0
                    if (fiche.resistanceStatorW !== null) RW.setText(fiche.resistanceStatorW!!.toString()) else 0
                    if (fiche.isolementPhase !== null) isolementPhase.setText(fiche.isolementPhase!!.toString()) else 0
                }
                if (typemotopompe.selectedItem.toString() == "Monophasé") {
                    partMeca.visibility = View.VISIBLE
                    partTri.visibility = View.GONE
                    partMono.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotopompe = 2
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    if (fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail.toString())
                    if (fiche.resistanceDemarrage !== null) resistanceDemarrage.setText(fiche.resistanceDemarrage.toString())
                    if (fiche.valeurCondensateur !== null) valeurCondensateur.setText(fiche.valeurCondensateur.toString())
                }

            }
        }
        if (fiche.typeMotopompe !== null) typemotopompe.setSelection(fiche.typeMotopompe!!.toInt())
        if (fiche.fluide !== null) fluide.setText(fiche.fluide!!.toString())
        if (fiche.sensRotation !== null) sensRotation.setChecked(!fiche.sensRotation!!)
        if (fiche.typeRessort !== null) typeRessort.setSelection(fiche.typeRessort!!)
        if (fiche.matiere !== null) matiere.setSelection(fiche.matiere!!)
        if (fiche.typeJoint !== null) typeJoint.setText(fiche.typeJoint.toString())
        if (fiche.diametreArbre !== null) diametreArbre.setText(fiche.diametreArbre!!.toString())
        if (fiche.diametreExtPR !== null) diametreExtPR.setText(fiche.diametreExtPR!!.toString())
        if (fiche.diametreExtPF !== null) diametreExtPF.setText(fiche.diametreExtPF!!.toString())
        if (fiche.epaisseurPF !== null) epaisseurPF.setText(fiche.epaisseurPF!!.toString())
        if (fiche.longueurRotativeNonComprimee !== null) longueurRotativeNonComprimee.setText(fiche.longueurRotativeNonComprimee!!.toString())
        if (fiche.longueurRotativeComprimee !== null) longueurRotativeComprimee.setText(fiche.longueurRotativeComprimee!!.toString())
        if (fiche.longueurRotativeTravail !== null) longueurRotativeTravail.setText(fiche.longueurRotativeTravail!!.toString())
        if(fiche.typeMotopompe == 2 ){
            if (fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail!!)
            if (fiche.resistanceDemarrage !== null) resistanceDemarrage.setText(fiche.resistanceDemarrage!!)
            if (fiche.valeurCondensateur !== null) valeurCondensateur.setText(fiche.valeurCondensateur!!)
            if (fiche.tensionU !== null) tensionMU.setText(fiche.tensionU)
            if (fiche.tensionV !== null) tensionMV.setText(fiche.tensionV)
            if (fiche.tensionW !== null) tensionMW.setText(fiche.tensionW)
        }
        if(fiche.typeMotopompe == 1 ){
            if (fiche.isolementPhaseMasseStatorUM !== null) UM.setText(fiche.isolementPhaseMasseStatorUM!!)
            if (fiche.isolementPhaseMasseStatorVM !== null) VM.setText(fiche.isolementPhaseMasseStatorVM!!)
            if (fiche.isolementPhaseMasseStatorWM !== null) WM.setText(fiche.isolementPhaseMasseStatorWM!!)
            if (fiche.isolementPhasePhaseStatorUV !== null) UV.setText(fiche.isolementPhasePhaseStatorUV!!)
            if (fiche.isolementPhasePhaseStatorUW !== null) UW.setText(fiche.isolementPhasePhaseStatorUW!!)
            if (fiche.isolementPhasePhaseStatorVW !== null) iVW.setText(fiche.isolementPhasePhaseStatorVW!!)
            if (fiche.resistanceStatorU !== null) RU.setText(fiche.resistanceStatorU)
            if (fiche.resistanceStatorV !== null) RV.setText(fiche.resistanceStatorV)
            if (fiche.resistanceStatorW !== null) RW.setText(fiche.resistanceStatorW)
            if (fiche.tensionU !== null) tensionU.setText(fiche.tensionU)
            if (fiche.tensionV !== null) tensionV.setText(fiche.tensionV)
            if (fiche.tensionW !== null) tensionW.setText(fiche.tensionW)
            if (fiche.isolementPhase !== null) isolementPhase.setText(fiche.isolementPhase)
        }
        if (fiche.observations !== null) obs.setText(fiche.observations!!)
        viewModel.photos.value = fiche.photos!!.toMutableList()
        var retour = layout.findViewById<Button>(R.id.retourmp)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrermp)
        if (fiche.status!! < 3L) {
            fluide.doAfterTextChanged {
                fiche.fluide = fluide.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            sensRotation.setOnCheckedChangeListener { _, isChecked ->
                fiche.sensRotation = !isChecked
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            typeRessort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (typeRessort.selectedItem.toString() !== " ") fiche.typeRessort = position
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            matiere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (matiere.selectedItem.toString() !== " ") {
                        if (switchGarniture.isChecked) fiche.matiere2 =
                            position else fiche.matiere = position
                    }
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    Log.i("INFO", "matiere ${fiche.matiere}")
                }
            }
            typeJoint.doAfterTextChanged {
                if (typeJoint.text.isNotEmpty()) {
                    if (switchGarniture.isChecked) {
                        fiche.typeJoint2 = Normalizer
                            .normalize(typeJoint.text.toString(), Normalizer.Form.NFD)
                            .replace("[^\\p{ASCII}]", "")
                    } else {
                        fiche.typeJoint = Normalizer
                            .normalize(typeJoint.text.toString(), Normalizer.Form.NFD)
                            .replace("[^\\p{ASCII}]", "")
                    }
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreArbre.doAfterTextChanged {
                if (diametreArbre.text.isNotEmpty() && diametreArbre.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.diametreArbre2 =
                        diametreArbre.text.toString() else fiche.diametreArbre =
                        diametreArbre.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreExtPF.doAfterTextChanged {
                if (diametreExtPF.text.isNotEmpty() && diametreExtPF.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.diametreExtPF2 =
                        diametreExtPF.text.toString() else fiche.diametreExtPF =
                        diametreExtPF.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreExtPR.doAfterTextChanged {
                if (diametreExtPR.text.isNotEmpty() && diametreExtPR.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.diametreExtPR2 =
                        diametreExtPR.text.toString() else fiche.diametreExtPR =
                        diametreExtPR.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            epaisseurPF.doAfterTextChanged {
                if (epaisseurPF.text.isNotEmpty() && epaisseurPF.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.epaisseurPF2 =
                        epaisseurPF.text.toString() else fiche.epaisseurPF =
                        epaisseurPF.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeNonComprimee.doAfterTextChanged {
                if (longueurRotativeNonComprimee.text.isNotEmpty() && longueurRotativeNonComprimee.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.longueurRotativeNonComprimee2 =
                        longueurRotativeNonComprimee.text.toString() else fiche.longueurRotativeNonComprimee =
                        longueurRotativeNonComprimee.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeComprimee.doAfterTextChanged {
                if (longueurRotativeComprimee.text.isNotEmpty() && longueurRotativeComprimee.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.longueurRotativeComprimee2 =
                        longueurRotativeComprimee.text.toString() else fiche.longueurRotativeComprimee =
                        longueurRotativeComprimee.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeTravail.doAfterTextChanged {
                if (longueurRotativeTravail.text.isNotEmpty() && longueurRotativeTravail.hasFocus()) {
                    if (switchGarniture.isChecked) fiche.longueurRotativeTravail2 =
                        longueurRotativeTravail.text.toString() else fiche.longueurRotativeTravail =
                        longueurRotativeTravail.text.toString()
                }
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UM.doAfterTextChanged {
                if (UM.text.isNotEmpty() && UM.hasFocus()) fiche.isolementPhaseMasseStatorUM =
                    UM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VM.doAfterTextChanged {
                if (VM.text.isNotEmpty() && VM.hasFocus() ) fiche.isolementPhaseMasseStatorVM =
                    VM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            WM.doAfterTextChanged {
                if (WM.text.isNotEmpty() && WM.hasFocus() ) fiche.isolementPhaseMasseStatorWM =
                    WM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UV.doAfterTextChanged {
                if (UV.text.isNotEmpty() && UV.hasFocus() ) fiche.isolementPhasePhaseStatorUV =
                    UV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UW.doAfterTextChanged {
                if (UW.text.isNotEmpty() && UW.hasFocus() ) fiche.isolementPhasePhaseStatorUW =
                    UW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            iVW.doAfterTextChanged {
                if (iVW.text.isNotEmpty() && iVW.hasFocus() ) fiche.isolementPhasePhaseStatorVW =
                    iVW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RU.doAfterTextChanged {
                if (RU.text.isNotEmpty() && RU.hasFocus() ) fiche.resistanceStatorU =
                    RU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RV.doAfterTextChanged {
                if (RV.text.isNotEmpty() && RV.hasFocus() ) fiche.resistanceStatorV =
                    RV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RW.doAfterTextChanged {
                if (RW.text.isNotEmpty() && RW.hasFocus() ) fiche.resistanceStatorW =
                    RW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceTravail.doAfterTextChanged {
                if (resistanceTravail.text.isNotEmpty() && resistanceTravail.hasFocus()) fiche.resistanceTravail = resistanceTravail.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceDemarrage.doAfterTextChanged {
                if (resistanceDemarrage.text.isNotEmpty() && resistanceDemarrage.hasFocus() ) fiche.resistanceDemarrage = resistanceDemarrage.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            valeurCondensateur.doAfterTextChanged {
                if (valeurCondensateur.text.isNotEmpty() && valeurCondensateur.hasFocus() ) fiche.valeurCondensateur = valeurCondensateur.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionU.doAfterTextChanged {
                if (tensionU.text.isNotEmpty() && tensionU.hasFocus() ) fiche.tensionU = tensionU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty() && tensionV.hasFocus()) fiche.tensionV = tensionV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() && tensionW.hasFocus()  ) fiche.tensionW = tensionW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionMU.doAfterTextChanged {
                if (tensionMU.text.isNotEmpty() && tensionMU.hasFocus() ) fiche.tensionU = tensionMU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionMV.doAfterTextChanged {
                if (tensionMV.text.isNotEmpty() && tensionMV.hasFocus()) fiche.tensionV = tensionMV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionMW.doAfterTextChanged {
                if (tensionMW.text.isNotEmpty() && tensionMW.hasFocus()  ) fiche.tensionW = tensionMW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhase.doAfterTextChanged {
                if (isolementPhase.text.isNotEmpty() && isolementPhase.hasFocus() ) fiche.isolementPhase =
                    isolementPhase.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            obs.doAfterTextChanged {
                fiche.observations = obs.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        } else {
            fluide.isEnabled = false
            sensRotation.isEnabled = false
            typeRessort.isEnabled = false
            matiere.isEnabled = false
            typeJoint.isEnabled = false
            diametreArbre.isEnabled = false
            diametreExtPF.isEnabled = false
            diametreExtPR.isEnabled = false
            epaisseurPF.isEnabled = false
            longueurRotativeNonComprimee.isEnabled = false
            longueurRotativeComprimee.isEnabled = false
            longueurRotativeTravail.isEnabled = false
            resistanceTravail.isEnabled = false
            resistanceDemarrage.isEnabled = false
            valeurCondensateur.isEnabled = false
            tensionMU.isEnabled = false
            tensionMV.isEnabled = false
            tensionMW.isEnabled = false
            tensionMU.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            UM.isEnabled = false
            VM.isEnabled = false
            WM.isEnabled = false
            UV.isEnabled = false
            UW.isEnabled = false
            iVW.isEnabled = false
            RU.isEnabled = false
            RV.isEnabled = false
            RW.isEnabled = false
            obs.isEnabled = false
            gal.visibility = View.INVISIBLE
            btnPhoto.visibility = View.INVISIBLE
            enregistrer.visibility = View.GONE
        }


        var schema = layout.findViewById<ImageView>(R.id.schemaPompe)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList(), { item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(
                layout,
                "/storage/emulated/0/Pictures/test_pictures/" + item.toString()
            )
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
            fiche.photos = it.toTypedArray()
        })
        if (fiche.photos !== null) sAdapter.update(viewModel.photos.value!!)
        epaisseurPF.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_i4)
                //i4.setTextColor(Color.WHITE)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        i4.setOnClickListener {
            epaisseurPF.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }
        d7.setOnClickListener {
            diametreExtPF.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }

        diametreArbre.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_d1)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }

        diametreExtPR.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_d3select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d3.setOnClickListener {
            diametreExtPR.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }
        longueurRotativeTravail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_i3_select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        i3.setOnClickListener {
            longueurRotativeTravail.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }
        diametreExtPF.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_d7)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d1.setOnClickListener {
            diametreArbre.requestFocus()
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                activity?.currentFocus!!.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }

        btnPhoto.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (test == false) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            }
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
                // Ensure that there's a camera activity to handle the intent
                cameraIntent.resolveActivity(requireActivity().packageManager).also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.i("INFO", "error while creating file")
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.example.applicationstb.fileprovider",
                            it
                        )
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(cameraIntent, viewModel.CAMERA_CAPTURE)
                        //viewModel.addSchema(photoURI)
                    }
                }
            }
        }
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, viewModel.GALLERY_CAPTURE)
        }
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.getTime()
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.localSave()
            if (viewModel.isOnline(requireContext())) {
                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
            } else {
                val mySnackbar =
                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                mySnackbar.show()
            }
        }
        termP.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Terminer une fiche")
                    .setMessage("Êtes vous sûr de vouloir terminer la fiche? elle ne sera plus modifiable après")
                    .setPositiveButton("Terminer",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.getTime()
                            fiche.status = 3L
                            viewModel.selection.value = fiche
                            viewModel.localSave()
                            if (viewModel.isOnline(requireContext())) {
                                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                            } else {
                                val mySnackbar =
                                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                                mySnackbar.show()
                            }
                        })
                builder.create()
            }
            alertDialog?.show()
        }



        return layout
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel.CAMERA_CAPTURE){
            if (resultCode == Activity.RESULT_OK) {
                viewModel.addPhoto(currentPhotoPath)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                File(currentPhotoPath).delete()
            }
        }
        if (requestCode == viewModel.GALLERY_CAPTURE) {
            if (resultCode == Activity.RESULT_OK ) {
                var file = viewModel.getRealPathFromURI(data?.data!!)
                CoroutineScope(Dispatchers.IO).launch {
                    var nfile = async { viewModel.sendExternalPicture(file!!) }
                    nfile.await()
                    if (nfile.isCompleted) {
                        var list = viewModel.selection.value?.photos?.toMutableList()
                        list!!.removeAll { it == "" }
                        list.add(nfile.await()!!)
                        viewModel.selection.value?.photos = list?.toTypedArray()
                        viewModel.photos.postValue(list!!)
                        viewModel.localSave()
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("info", "data: ${data}")
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        var num = if (viewModel.selection.value?.photos?.size!! == 1 && viewModel.selection.value?.photos!![0] == "")
            "${viewModel.selection.value?.numFiche}_${viewModel.selection.value?.photos?.size!!}"
        else
            "${viewModel.selection.value?.numFiche}_${viewModel.selection.value?.photos?.size!!+1}"
        if (storageDir.exists()) {
            return File.createTempFile(
                num,/* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        } else {
            makeFolder()
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + num, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }
    }

    fun makeFolder() {
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        storageDir.mkdir()
    }

    fun removeRef(i: Int, list: Array<String>): Array<String> {
        var tab = list.toMutableList()
        tab.removeAt(i)
        return tab.toTypedArray()
    }

}