package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.applicationstb.R
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.model.DemontagePompe
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class PompeFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_pompe, container, false)
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
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var fiche = viewModel.selection.value!! as DemontagePompe
        Log.i("INFO","sensRoation: ${fiche.sensRotation} - matiere joint: ${fiche.matiere}")
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
        var retour = layout.findViewById<Button>(R.id.retourPompe)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerPompe)
        marque.doAfterTextChanged {
            fiche.marque = marque.text.toString()
        }
        numSerie.doAfterTextChanged {
            fiche.numSerie = numSerie.text.toString().toInt()
        }
        fluide.doAfterTextChanged {
            fiche.fluide = fluide.text.toString()
        }
        sensRotation.setOnCheckedChangeListener { _, isChecked ->
            fiche.sensRotation = !isChecked
        }
        typeRessort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(typeRessort.selectedItem.toString() !== " ") fiche.typeRessort = position
            }
        }
        matiere.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(matiere.selectedItem.toString() !== " ") fiche.matiere = position
                Log.i("INFO","matiere ${fiche.matiere}")
            }
        }
        typeJoint.doAfterTextChanged {
            fiche.typeJoint = typeJoint.text.toString()
        }
        diametreArbre.doAfterTextChanged {
            fiche.diametreArbre = diametreArbre.text.toString().toFloat()
        }
        diametreExtPF.doAfterTextChanged {
            fiche.diametreExtPF = diametreExtPF.text.toString().toFloat()
        }
        diametreExtPR.doAfterTextChanged {
            fiche.diametreExtPR = diametreExtPR.text.toString().toFloat()
        }
        epaisseurPF.doAfterTextChanged {
            fiche.epaisseurPF = epaisseurPF.text.toString().toFloat()
        }
        longueurRotativeNonComprimee.doAfterTextChanged {
            fiche.longueurRotativeNonComprimee = longueurRotativeNonComprimee.text.toString().toFloat()
        }
        longueurRotativeComprimee.doAfterTextChanged {
            fiche.longueurRotativeComprimee = longueurRotativeComprimee.text.toString().toFloat()
        }
        longueurRotativeTravail.doAfterTextChanged {
            fiche.longueurRotativeTravail = longueurRotativeTravail.text.toString().toFloat()
        }
        obs.doAfterTextChanged {
            fiche.observations = obs.text.toString()
        }


        var schema = layout.findViewById<ImageView>(R.id.schemaPompe)
        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,viewModel.schema.value.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })



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
                cameraIntent.resolveActivity(activity!!.packageManager).also {
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
                            context!!,
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
            if (viewModel.selection.value!!.dureeTotale !== null) {
                fiche.dureeTotale =
                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
            } else {
                fiche.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.enregistrer(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
        }



        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
            viewModel.addPhoto(0,Uri.parse(currentPhotoPath))
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
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        } else {
            makeFolder()
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
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

}