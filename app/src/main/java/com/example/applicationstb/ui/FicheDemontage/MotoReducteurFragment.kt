package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.model.*
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MotoReducteurFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
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
        var layout = inflater.inflate(R.layout.fragment_motoreducteur, container, false)
        var trMin = layout.findViewById<EditText>(R.id.trMin)
        var modele = layout.findViewById<EditText>(R.id.modele)
        var indiceReduction = layout.findViewById<EditText>(R.id.indRed)
        var typeHuile = layout.findViewById<EditText>(R.id.typeHuile)
        var quantiteHuile = layout.findViewById<EditText>(R.id.qteHuile)
        var typeRoulementAv = layout.findViewById<Spinner>(R.id.typeRav)
        var typeRoulementAr = layout.findViewById<Spinner>(R.id.typeRar)
        var refRoulementAv = layout.findViewById<EditText>(R.id.refRoulementAv)
        var refRoulementAr = layout.findViewById<EditText>(R.id.refRoulementAr)
        var typeJointAv = layout.findViewById<Spinner>(R.id.typeJav)
        var refJointAv = layout.findViewById<EditText>(R.id.refJointAv)
        var typeJointAr = layout.findViewById<Spinner>(R.id.typeJar)
        var btnRoul = layout.findViewById<Button>(R.id.ajtTrain)
        var btnJoint = layout.findViewById<Button>(R.id.ajtJoint)
        var tabRoulements = layout.findViewById<RecyclerView>(R.id.tabRoullementsRed)
        var tabJoints = layout.findViewById<RecyclerView>(R.id.tabJointsred)
        var refJointAr = layout.findViewById<EditText>(R.id.refJointsAr)
        var infosMoteur = layout.findViewById<CardView>(R.id.info)
        var partMono = layout.findViewById<CardView>(R.id.moteur_mono)
        var partMeca = layout.findViewById<CardView>(R.id.mecamp)
        var partTri = layout.findViewById<CardView>(R.id.moteur_triphase)
        var roulements = MutableLiveData<MutableList<Roulement>?>()
        var joints = MutableLiveData<MutableList<Joint>?>()
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        var typemotoreducteur = layout.findViewById<Spinner>(R.id.typemotoreducteur)
        typemotoreducteur.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>("Sélectionnez un type de Motoreducteur", "Triphasé", "Monophasé")
        )
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var termP = layout.findViewById<Button>(R.id.termmp)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
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
        var fiche = viewModel.selection.value!!
        //roulements
        if (fiche.roulements !== null) roulements.value = fiche.roulements else roulements.value = mutableListOf<Roulement>()
        var adapterRoulement = RoulementRedAdapter(mutableListOf<Roulement>(),{typeAv,refAv,typeAr,refAr,position ->
            roulements.value!!.set(position,Roulement("R${position}","${typeAv} - ${refAv}", "${typeAr} - ${refAr}"))
        }
        )
        roulements.observe(viewLifecycleOwner, {
            adapterRoulement.update(it!!)
            fiche.roulements = roulements.value
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.localSave()
        })
        tabRoulements.adapter = adapterRoulement
        tabRoulements.layoutManager = GridLayoutManager(context, 1)
        //joints
        if (fiche.joints !== null) joints.value = fiche.joints else joints.value = mutableListOf<Joint>()
        var adapterJoint = JointAdapter(mutableListOf<Joint>(), {typeAv,refAv,typeAr,refAr,position ->
            joints.value!!.set(position,
                Joint("R${position}","${typeAv} - ${refAv}", "${typeAr} - ${refAr}")
            )
        })
        joints.observe(viewLifecycleOwner, {
            adapterJoint.update(it!!)
            fiche.roulements = roulements.value
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.localSave()
        })
        tabJoints.adapter = adapterJoint
        tabJoints.layoutManager = GridLayoutManager(context, 1)
        typemotoreducteur.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (typemotoreducteur.selectedItem.toString() == "Triphasé") {
                    partMeca.visibility = View.VISIBLE
                    partMono.visibility = View.GONE
                    partTri.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotoreducteur = 1
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
                }
                if (typemotoreducteur.selectedItem.toString() == "Monophasé") {
                    partMeca.visibility = View.VISIBLE
                    partTri.visibility = View.GONE
                    partMono.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotoreducteur = 2
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    if (fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail.toString())
                    if (fiche.resistanceDemarrage !== null) resistanceDemarrage.setText(fiche.resistanceDemarrage.toString())
                    if (fiche.valeurCondensateur !== null) valeurCondensateur.setText(fiche.valeurCondensateur.toString())
                }

            }
        }
        if (fiche.typeMotoreducteur !== null) typemotoreducteur.setSelection(fiche.typeMotoreducteur!!.toInt())
        if (fiche.trMinute !== null) trMin.setText(fiche.trMinute!!.toString())
        if (fiche.modele !== null) modele.setText(fiche.modele!!)
        if (fiche.indiceReduction !== null) indiceReduction.setText(fiche.indiceReduction!!)
        if (fiche.typeHuile !== null) typeHuile.setText(fiche.typeHuile!!)
        if (fiche.quantiteHuile !== null) quantiteHuile.setText(fiche.quantiteHuile!!.toString())
        if (fiche.observations !== null) obs.setText(fiche.observations!!)
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
        if(fiche.typeMotoreducteur == 2 ){
            if (fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail!!)
            if (fiche.resistanceDemarrage !== null) resistanceDemarrage.setText(fiche.resistanceDemarrage!!)
            if (fiche.valeurCondensateur !== null) valeurCondensateur.setText(fiche.valeurCondensateur!!)
            if (fiche.tensionU !== null) tensionMU.setText(fiche.tensionU)
            if (fiche.tensionV !== null) tensionMV.setText(fiche.tensionV)
            if (fiche.tensionW !== null) tensionMW.setText(fiche.tensionW)
        }
        if (fiche.observations !== null) obs.setText(fiche.observations!!)
        viewModel.photos.value = fiche.photos!!.toMutableList()
        var retour = layout.findViewById<Button>(R.id.retourmp)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrermp)
        if (fiche.status!! < 3L) {
            trMin.doAfterTextChanged {
                if(trMin.hasFocus() && trMin.text.isNotEmpty()) fiche.trMinute = trMin.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            modele.doAfterTextChanged {
                if(modele.hasFocus() && modele.text.isNotEmpty()) fiche.modele = modele.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            indiceReduction.doAfterTextChanged {
                if(indiceReduction.hasFocus() && indiceReduction.text.isNotEmpty()) fiche.indiceReduction = indiceReduction.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            typeHuile.doAfterTextChanged {
                if(typeHuile.hasFocus() && typeHuile.text.isNotEmpty() ) fiche.typeHuile = typeHuile.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            quantiteHuile.doAfterTextChanged {
                if(quantiteHuile.hasFocus() && quantiteHuile.text.isNotEmpty()) fiche.quantiteHuile = quantiteHuile.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            btnRoul.setOnClickListener {
                var liste = roulements.value!!
                liste.add(Roulement("R${liste.size}","${typeRoulementAv.selectedItem} - ${refRoulementAv.text.toString()}", "${typeRoulementAr.selectedItem} - ${refRoulementAr.text.toString()}"))
                roulements.value = liste
                typeRoulementAv.setSelection(0)
                typeRoulementAr.setSelection(0)
                refRoulementAv.setText("")
                refRoulementAr.setText("")
            }
            btnJoint.setOnClickListener {
                var liste = joints.value!!
                liste.add(Joint("R${liste.size}","${typeJointAv.selectedItem} - ${refJointAv.text.toString()}", "${typeJointAr.selectedItem} - ${refJointAr.text.toString()}"))
                joints.value = liste
                typeJointAv.setSelection(0)
                typeJointAr.setSelection(0)
                refJointAv.setText("")
                refJointAr.setText("")
            }
            //triphasé
                UM.doAfterTextChanged {
                    if (UM.text.isNotEmpty() && UM.hasFocus() ) fiche.isolementPhaseMasseStatorUM =
                        UM.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                VM.doAfterTextChanged {
                    if (VM.text.isNotEmpty() && VM.hasFocus()) fiche.isolementPhaseMasseStatorVM =
                        VM.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                WM.doAfterTextChanged {
                    if (WM.text.isNotEmpty() && WM.hasFocus()) fiche.isolementPhaseMasseStatorWM =
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
                    if (iVW.text.isNotEmpty() && iVW.hasFocus()) fiche.isolementPhasePhaseStatorVW =
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
                //monophase
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
                    if (valeurCondensateur.text.isNotEmpty() && valeurCondensateur.hasFocus()  ) fiche.valeurCondensateur = valeurCondensateur.text.toString()
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

            obs.doAfterTextChanged {
                fiche.observations = obs.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        } else {
            trMin.isEnabled = false
            modele.isEnabled = false
            indiceReduction.isEnabled = false
            typeHuile.isEnabled = false
            quantiteHuile.isEnabled = false
            btnRoul.visibility = View.INVISIBLE
            btnJoint.visibility = View.INVISIBLE
            resistanceTravail.isEnabled = false
            resistanceDemarrage.isEnabled = false
            valeurCondensateur.isEnabled = false
            tensionMU.isEnabled = false
            tensionMV.isEnabled = false
            tensionMW.isEnabled = false
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
            btnPhoto.visibility = View.INVISIBLE
            enregistrer.visibility = View.GONE
        }

        typeRoulementAr.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M", "autre"))
        typeRoulementAv.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M", "autre"))
        //joints
        typeJointAr.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","simple lèvre","double lèvre"))
        typeJointAv.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","simple lèvre","double lèvre"))

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
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.getTime()
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.localSave()
            if (viewModel.isOnline(requireContext())) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getNameURI()
                }
                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
            } else {
                val mySnackbar =
                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                mySnackbar.show()
            }
        }
        var gal = layout.findViewById<Button>(R.id.g9)
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, viewModel.GALLERY_CAPTURE)
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
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.getNameURI()
                                }
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
        if (storageDir.exists()) {
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + SystemClock.uptimeMillis(),/* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        } else {
            makeFolder()
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + SystemClock.uptimeMillis(), /* prefix */
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