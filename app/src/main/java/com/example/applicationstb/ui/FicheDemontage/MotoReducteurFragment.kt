package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.applicationstb.R
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MotoReducteurFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1


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
            arrayOf<String>(" ", "Triphasé", "Monophasé")
        )
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var termP = layout.findViewById<Button>(R.id.termmp)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        //triphase
        var UM = layout.findViewById<EditText>(R.id.isopmU)
        var VM = layout.findViewById<EditText>(R.id.isopmV)
        var WM = layout.findViewById<EditText>(R.id.isopmW)
        var UV = layout.findViewById<EditText>(R.id.isoppU)
        var UW = layout.findViewById<EditText>(R.id.isoppV)
        var iVW = layout.findViewById<EditText>(R.id.isoppW)
        var RU = layout.findViewById<EditText>(R.id.rU)
        var RV = layout.findViewById<EditText>(R.id.rV)
        var RW = layout.findViewById<EditText>(R.id.rW)
        //mono
        var isolementPhaseMasse = layout.findViewById<EditText>(R.id.isopmUe)
        var resistanceTravail = layout.findViewById<EditText>(R.id.isopmVe)
        var resistanceDemarrage = layout.findViewById<EditText>(R.id.rdem)
        var valeurCondensateur = layout.findViewById<EditText>(R.id.condens)
        var tension = layout.findViewById<EditText>(R.id.vVe)
        var intensite = layout.findViewById<EditText>(R.id.vWe)
        var fiche = viewModel.selection.value!! as DemontageMotoreducteur
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
                    fiche.typeMotoreducteur = "1"
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
                    fiche.typeMotoreducteur = "2"
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    if (fiche.isolementPhaseMasse !== null) isolementPhaseMasse.setText(fiche.isolementPhaseMasse.toString())
                    if (fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail.toString())
                    if (fiche.resistanceDemarrage !== null) resistanceDemarrage.setText(fiche.resistanceDemarrage.toString())
                    if (fiche.valeurCondensateur !== null) valeurCondensateur.setText(fiche.valeurCondensateur.toString())
                    if (fiche.tension !== null) tension.setText(fiche.tension.toString())
                    if (fiche.intensite !== null) intensite.setText(fiche.intensite.toString())
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


        if (fiche.observations !== null) obs.setText(fiche.observations!!)
        viewModel.photos.value = fiche.photos!!.toMutableList()
        var retour = layout.findViewById<Button>(R.id.retourmp)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrermp)
        if (fiche.status!! < 3L) {
            trMin.doAfterTextChanged {
                if(trMin.hasFocus() && trMin.text.isNotEmpty() && trMin.text.matches(regexNombres)) fiche.trMinute = trMin.text.toString().toFloat()
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
                if(quantiteHuile.hasFocus() && quantiteHuile.text.isNotEmpty() && quantiteHuile.text.matches(regexNombres)) fiche.quantiteHuile = quantiteHuile.text.toString().toFloat()
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
                UM.doAfterTextChanged {
                    if (UM.text.isNotEmpty() && UM.hasFocus() && UM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorUM =
                        UM.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                VM.doAfterTextChanged {
                    if (VM.text.isNotEmpty() && VM.hasFocus() && VM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorVM =
                        VM.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                WM.doAfterTextChanged {
                    if (WM.text.isNotEmpty() && WM.hasFocus() && WM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorWM =
                        WM.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                UV.doAfterTextChanged {
                    if (UV.text.isNotEmpty() && UV.hasFocus() && UV.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorUV =
                        UV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                UW.doAfterTextChanged {
                    if (UW.text.isNotEmpty() && UW.hasFocus() && UW.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorUW =
                        UW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                iVW.doAfterTextChanged {
                    if (iVW.text.isNotEmpty() && iVW.hasFocus() && iVW.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorVW =
                        iVW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                RU.doAfterTextChanged {
                    if (RU.text.isNotEmpty() && RU.hasFocus() && RU.text.matches(regexNombres)) fiche.resistanceStatorU =
                        RU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                RV.doAfterTextChanged {
                    if (RV.text.isNotEmpty() && RV.hasFocus() && RV.text.matches(regexNombres)) fiche.resistanceStatorV =
                        RV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                RW.doAfterTextChanged {
                    if (RW.text.isNotEmpty() && RW.hasFocus() && RW.text.matches(regexNombres)) fiche.resistanceStatorW =
                        RW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            //if (fiche.typeMotoreducteur == "2" || typemotoreducteur.selectedItem == "Monophasé") {
               // Log.i("info","is mono")
                isolementPhaseMasse.doAfterTextChanged {
                    if (isolementPhaseMasse.text.isNotEmpty() && isolementPhaseMasse.text.matches(
                            regexNombres
                        )
                    ) fiche.isolementPhaseMasse =
                        isolementPhaseMasse.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                resistanceTravail.doAfterTextChanged {
                    if (resistanceTravail.text.isNotEmpty() && resistanceTravail.hasFocus() && resistanceTravail.text.matches(
                            regexNombres
                        )
                    ) fiche.resistanceTravail = resistanceTravail.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                resistanceDemarrage.doAfterTextChanged {
                    if (resistanceDemarrage.text.isNotEmpty() && resistanceDemarrage.hasFocus() && resistanceDemarrage.text.matches(
                            regexNombres
                        )
                    ) fiche.resistanceDemarrage = resistanceDemarrage.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                valeurCondensateur.doAfterTextChanged {
                    if (valeurCondensateur.text.isNotEmpty() && valeurCondensateur.hasFocus() && valeurCondensateur.text.matches(
                            regexNombres
                        )
                    ) fiche.valeurCondensateur = valeurCondensateur.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                tension.doAfterTextChanged {
                    if (tension.text.isNotEmpty() && tension.hasFocus() && tension.text.matches(
                            regexNombres
                        )
                    ) fiche.tension = tension.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                intensite.doAfterTextChanged {
                    if (intensite.text.isNotEmpty() && intensite.hasFocus() && intensite.text.matches(
                            regexNombres
                        )
                    ) fiche.intensite = intensite.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            //}
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
            isolementPhaseMasse.isEnabled = false
            resistanceTravail.isEnabled = false
            resistanceDemarrage.isEnabled = false
            valeurCondensateur.isEnabled = false
            tension.isEnabled = false
            intensite.isEnabled = false
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
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
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
            startActivityForResult(intent, 6)
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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
            viewModel.addPhoto(currentPhotoPath)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 6) {
            var file = viewModel.getRealPathFromURI(data?.data!!)
            CoroutineScope(Dispatchers.IO).launch {
                if (viewModel.isOnline(requireContext())) viewModel.getNameURI()
                var nfile = viewModel.sendExternalPicture(file!!)
                if (nfile !== null) {
                    var list = viewModel.selection.value?.photos?.toMutableList()
                    if (list != null) {
                        list.add(nfile)
                    }
                    viewModel.selection.value?.photos = list?.toTypedArray()
                    viewModel.photos.postValue(list!!)
                }
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