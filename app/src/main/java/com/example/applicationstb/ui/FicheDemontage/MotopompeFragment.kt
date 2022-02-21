
package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.model.DemontageMotopompe
import com.example.applicationstb.model.DemontagePompe
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MotopompeFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private  val PHOTO_RESULT = 1888
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
        var layout = inflater.inflate(R.layout.fragment_motopompe, container, false)
        var fluide = layout.findViewById<EditText>(R.id.typeFluide)
        var sensRotation = layout.findViewById<Switch>(R.id.sensRotation)
        var typeRessort = layout.findViewById<Spinner>(R.id.typePompe)
        var typeJoint = layout.findViewById<EditText>(R.id.tjoint)
        typeRessort.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","entraînement par vis","ressort coaxial conique","ressort coaxial cylindrique","soufflet"))
        var matiere = layout.findViewById<Spinner>(R.id.matJoint)
        matiere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","ceramique","carbone scilicium","carbone","tungstène"))
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
        typemotopompe.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","Triphasé","Monophasé"))
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
        var resistanceDemarrage	= layout.findViewById<EditText>(R.id.rdem)
        var valeurCondensateur	= layout.findViewById<EditText>(R.id.condens)
        var tension	= layout.findViewById<EditText>(R.id.vVe)
        var intensite	= layout.findViewById<EditText>(R.id.vWe)
        var fiche = viewModel.selection.value!! as DemontageMotopompe
        typemotopompe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (typemotopompe.selectedItem.toString() == "Triphasé") {
                    partMeca.visibility = View.VISIBLE
                    partMono.visibility = View.GONE
                    partTri.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotopompe = "1"
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
                if (typemotopompe.selectedItem.toString() == "Monophasé") {
                    partMeca.visibility = View.VISIBLE
                    partTri.visibility = View.GONE
                    partMono.visibility = View.VISIBLE
                    val fmanager = childFragmentManager
                    fmanager.commit {
                        replace<MecaFragment>(R.id.partMeca)
                        setReorderingAllowed(true)
                    }
                    fiche.typeMotopompe = "2"
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
        if (fiche.typeMotopompe !== null) typemotopompe.setSelection(fiche.typeMotopompe!!.toInt())
        if (fiche.fluide !== null) fluide.setText(fiche.fluide!!.toString())
        if (fiche.sensRotation !== null ) sensRotation.setChecked(!fiche.sensRotation!!)
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
                    if (matiere.selectedItem.toString() !== " ") fiche.matiere = position
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                    Log.i("INFO", "matiere ${fiche.matiere}")
                }
            }
            typeJoint.doAfterTextChanged {
                if (typeJoint.text.isNotEmpty()) fiche.typeJoint = typeJoint.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreArbre.doAfterTextChanged {
                if (diametreArbre.text.isNotEmpty()  && diametreArbre.hasFocus()) fiche.diametreArbre =
                    diametreArbre.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreExtPF.doAfterTextChanged {
                if (diametreExtPF.text.isNotEmpty()  && diametreExtPF.hasFocus()) fiche.diametreExtPF =
                    diametreExtPF.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            diametreExtPR.doAfterTextChanged {
                if (diametreExtPR.text.isNotEmpty()  && diametreExtPR.hasFocus()) fiche.diametreExtPR =
                    diametreExtPR.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            epaisseurPF.doAfterTextChanged {
                if (epaisseurPF.text.isNotEmpty()  && epaisseurPF.hasFocus()) fiche.epaisseurPF =
                    epaisseurPF.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeNonComprimee.doAfterTextChanged {
                if (longueurRotativeNonComprimee.text.isNotEmpty() && longueurRotativeNonComprimee.hasFocus()) fiche.longueurRotativeNonComprimee =
                    longueurRotativeNonComprimee.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeComprimee.doAfterTextChanged {
                if (longueurRotativeComprimee.text.isNotEmpty() && longueurRotativeComprimee.hasFocus()) fiche.longueurRotativeComprimee =
                    longueurRotativeComprimee.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            longueurRotativeTravail.doAfterTextChanged {
                if (longueurRotativeTravail.text.isNotEmpty()  && longueurRotativeTravail.hasFocus()) fiche.longueurRotativeTravail =
                    longueurRotativeTravail.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
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
                isolementPhaseMasse.doAfterTextChanged {
                    if (isolementPhaseMasse.text.isNotEmpty() && isolementPhaseMasse.hasFocus() && isolementPhaseMasse.text.matches(regexNombres)) fiche.isolementPhaseMasse =
                        isolementPhaseMasse.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                resistanceTravail.doAfterTextChanged {
                    if (resistanceTravail.text.isNotEmpty() && resistanceTravail.hasFocus() && resistanceTravail.text.matches(regexNombres) ) fiche.resistanceTravail = resistanceTravail.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                resistanceDemarrage.doAfterTextChanged {
                    if (resistanceDemarrage.text.isNotEmpty() && resistanceDemarrage.hasFocus() && resistanceDemarrage.text.matches(regexNombres) ) fiche.resistanceDemarrage = resistanceDemarrage.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                valeurCondensateur.doAfterTextChanged {
                    if (valeurCondensateur.text.isNotEmpty() && valeurCondensateur.hasFocus() && valeurCondensateur.text.matches(regexNombres) ) fiche.valeurCondensateur = valeurCondensateur.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                tension.doAfterTextChanged {
                    if (tension.text.isNotEmpty() && tension.hasFocus() && tension.text.matches(regexNombres)) fiche.tension = tension.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
                intensite.doAfterTextChanged {
                    if (intensite.text.isNotEmpty() && intensite.hasFocus() && intensite.text.matches(regexNombres) )fiche.intensite = intensite.text.toString().toFloat()
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


        var schema = layout.findViewById<ImageView>(R.id.schemaPompe)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
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
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        d7.setOnClickListener {
            diametreExtPF.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }

        diametreArbre.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d1)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }

        diametreExtPR.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d3select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d3.setOnClickListener {
            diametreExtPR.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        longueurRotativeTravail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_i3_select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        i3.setOnClickListener {
            longueurRotativeTravail.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        diametreExtPF.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d7)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d1.setOnClickListener {
            diametreArbre.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }

        btnPhoto.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            if (test == false) {
                requestPermissions(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
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
                        Log.i("INFO","error while creating file")
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
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getNameURI()
            }
            viewModel.localSave()
            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
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
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.getNameURI()
                            }
                            viewModel.localSave()
                            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
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
    fun makeFolder(){
        val storageDir: File = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES+"/test_pictures")
        storageDir.mkdir()
    }
    fun removeRef(i:Int, list:Array<String>):Array<String>{
        var tab = list.toMutableList()
        tab.removeAt(i)
        return tab.toTypedArray()
    }

}