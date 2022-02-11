
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
        var marque = layout.findViewById<TextView>(R.id.marc)
        var numSerie = layout.findViewById<TextView>(R.id.numSerie)
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
        var typemotopompe = layout.findViewById<Spinner>(R.id.typemotopompe)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var termP = layout.findViewById<Button>(R.id.termmp)
        var partMono = layout.findViewById<CardView>(R.id.moteur_mono)
        var partMeca = layout.findViewById<CardView>(R.id.mecamp)
        var partTri = layout.findViewById<CardView>(R.id.moteur_triphase)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var fiche = viewModel.selection.value!! as DemontageMotopompe
        if (fiche.numSerie !== null) numSerie.setText(fiche.numSerie!!.toString()) else 0
        if (fiche.marque !== null) marque.setText(fiche.marque!!.toString())
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
        typemotopompe.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","Triphasé","Monophasé"))
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
                    fmanager.commit {
                        replace<InfoMoteurFragment>(R.id.infoLayout)
                        setReorderingAllowed(true)
                    }
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
                    fmanager.commit {
                        replace<InfoMoteurFragment>(R.id.infosLayout)
                        setReorderingAllowed(true)
                    }
                }

            }
        }
        /*if (fiche.status!! < 3L) {
            marque.doAfterTextChanged {
                fiche.marque = marque.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            numSerie.doAfterTextChanged {
                fiche.numSerie = numSerie.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
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
            obs.doAfterTextChanged {
                fiche.observations = obs.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        } else {
            marque.isEnabled = false
            numSerie.isEnabled = false
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
            obs.isEnabled = false
            btnPhoto.visibility = View.INVISIBLE
            enregistrer.visibility = View.GONE
        }*/


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
        })
        if (fiche.photos !== null) sAdapter.update(viewModel.photos.value!!)
       /* epaisseurPF.setOnFocusChangeListener { view, hasFocus ->
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
        }*/

      /*  var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M"))
        var switchRoullements = layout.findViewById<Switch>(R.id.switchRoullements)
        var refRoul = layout.findViewById<EditText>(R.id.refRoullement)
        if (fiche.typeRoulementAvant!!.size > 0) {
            typeRoulement.setSelection(arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M").indexOf(fiche.typeRoulementAvant!![0]))
        }
        if (fiche.refRoulementAvant !== null && fiche.refRoulementAvant!!.size > 0) {
            refRoul.setText(fiche.refRoulementAvant!![0])
        }
        var specsRoul = layout.findViewById<RecyclerView>(R.id.specsRoul)
        if (fiche.status == 3L) {
            refRoul.isEnabled = false
            specsRoul.isEnabled = false
            typeRoulement.isEnabled = false
        }
        specsRoul.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false )
        var adapter = roulementAdapter( viewModel.selection.value!!.typeRoulementArriere!!,viewModel.selection.value!!.refRoulementArriere!!) { item ->
            viewModel.selection.value!!.typeRoulementArriere = removeRef(item, viewModel.selection.value!!.typeRoulementArriere!!)
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
                if (position > 0 && selection !== "") {
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
        }*/


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