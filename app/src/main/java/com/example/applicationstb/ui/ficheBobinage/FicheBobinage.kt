package com.example.applicationstb.ui.ficheBobinage

import android.Manifest
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.marginTop
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Bobinage
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class FicheBobinage : Fragment() {

    companion object {
        fun newInstance() = FicheBobinage()
    }

    private val viewModel: FicheBobinageViewModel by activityViewModels()
    private lateinit var recycler:RecyclerView
    private lateinit var schemas:RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("INFO", "tableau fiches: ${arguments?.get("listBobinage")} - token: ${arguments?.get("token")} ")
        var list = arguments?.get("listBobinage") as Array<Bobinage>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeBobinage = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
        var scrollBobi = layout.findViewById<ScrollView>(R.id.scrollBobi)
        //viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)

        //champs détails
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterDevis = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeBobinage.map { it.numFiche })
        spinner.adapter = adapterDevis
        var btnSelect = layout.findViewById<Button>(R.id.btnDemarrer)
        var non = layout.findViewById<TextView>(R.id.non)
        var oui = layout.findViewById<TextView>(R.id.oui)
        var frequence = layout.findViewById<EditText>(R.id.frequence)
        var client = layout.findViewById<EditText>(R.id.client)
        var puissance= layout.findViewById<EditText>(R.id.puissance)
        var courant = layout.findViewById<EditText>(R.id.courant)
        var phases = layout.findViewById<EditText>(R.id.phase)
        var vitesse = layout.findViewById<EditText>(R.id.vitesse)
        var type = layout.findViewById<EditText>(R.id.type)
        var marque = layout.findViewById<EditText>(R.id.marque)
        var switch = layout.findViewById<Switch>(R.id.callage)
        var dates = layout.findViewById<LinearLayout>(R.id.dates)
        var dated = layout.findViewById<TextView>(R.id.DateDebut)
        var details = layout.findViewById<TextView>(R.id.titreDetails)
        val adapter = FillAdapter(viewModel.sections!!.value!!)
        val sAdapter = schemaAdapter(viewModel.photos.value!! ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(
                layout,
                "/storage/emulated/0/Pictures/test_pictures/" + item.toString()
            )
        })
        var visibility = View.VISIBLE
        //champs fils
        var linscroll = layout.findViewById<LinearLayout>(R.id.linscroll)
        var btnfils = layout.findViewById<Button>(R.id.ajoutFil)
        var diam = layout.findViewById<EditText>(R.id.diam)
        var nbBrins = layout.findViewById<EditText>(R.id.nbfils)
        var poids = layout.findViewById<EditText>(R.id.poids)
        var RU = layout.findViewById<EditText>(R.id.RU)
        var RV = layout.findViewById<EditText>(R.id.RV)
        var RW = layout.findViewById<EditText>(R.id.RW)
        var IU = layout.findViewById<EditText>(R.id.IU)
        var IV = layout.findViewById<EditText>(R.id.IV)
        var IW = layout.findViewById<EditText>(R.id.IW)
        var IIU = layout.findViewById<EditText>(R.id.IIU)
        var IIV = layout.findViewById<EditText>(R.id.IIV)
        var IIW = layout.findViewById<EditText>(R.id.IIW)
        var addschema = layout.findViewById<Button>(R.id.addschema)
        var tension = layout.findViewById<EditText>(R.id.tension)
        var obs = layout.findViewById<EditText>(R.id.observations)
        var som = layout.findViewById<TextView>(R.id.somme)
        var spire = layout.findViewById<EditText>(R.id.spire)
        var enrg = layout.findViewById<Button>(R.id.enregistrer)
        var quit = layout.findViewById<Button>(R.id.quit)
        var term = layout.findViewById<Button>(R.id.termB)
        if (activity?.let { ContextCompat.checkSelfPermission(it.applicationContext, Manifest.permission.CAMERA) }
                == PackageManager.PERMISSION_DENIED)
            this.activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PHOTO_RESULT) }
        recycler = layout.findViewById(R.id.tab)
        recycler.layoutManager = GridLayoutManager(context, 1)
        recycler.adapter = adapter
        viewModel.sections.observe(viewLifecycleOwner, {
            adapter.update(it)
        })

        schemas = layout.findViewById(R.id.schemas)
        schemas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        schemas.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
           
            if (viewModel.photos.value !== null) {
                sAdapter.update(viewModel.photos.value!!)
                if (viewModel.photos.value?.size == 0) {
                    schemas.visibility = View.GONE
                } else {
                    schemas.visibility = View.VISIBLE
                }
            } else {
                viewModel.photos.value = mutableListOf()
            }
        })

            /*var format = DateTimeFormatter.ofPattern("DD-MM-YYYY")
            dateDebut.setText(LocalDateTime.now().format(format))*/

        btnSelect.setOnClickListener {
            if (spinner.selectedItem == null) {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout),
                    "Veuillez sélectionner une fiche",
                    3600
                )
                mySnackbar.show()
            } else {
                scrollBobi.visibility = View.VISIBLE
                viewModel.start.value = Date()
                var bobinage = viewModel.listeBobinage.find { it.numFiche == spinner.selectedItem }
                viewModel.bobinage.value = bobinage
                viewModel.sections.value = bobinage?.sectionsFils
                viewModel.photos.value = bobinage?.photos!!.toMutableList()
                if (bobinage != null) {
                   if (bobinage.photos !== null) sAdapter.update(bobinage.photos!!.toMutableList())
                    marque.setText(bobinage?.marqueMoteur)
                    if (bobinage.courant !== null) {
                        courant.setText(bobinage?.courant.toString())
                    } //
                    if (bobinage.vitesse !== null) {
                        vitesse.setText(bobinage?.vitesse.toString())
                    } //
                    type.setText(bobinage?.typeBobinage)
                    client.setText(bobinage?.client?.enterprise)
                    if (bobinage.puissance !== null) {
                        puissance.setText(bobinage?.puissance.toString())
                    } //
                    if (bobinage.phases !== null) {
                        phases.setText(bobinage?.phases.toString())
                    } //
                    if (bobinage.frequences !== null) {
                        frequence.setText(bobinage?.frequences.toString())
                    } //
                    if (bobinage.calageEncoches !== null) {
                        switch.setChecked(bobinage.calageEncoches!!)
                    } else switch.setChecked(false)
                    if (bobinage.tension !== null) tension.setText(bobinage?.tension.toString())
                    adapter.list = bobinage.sectionsFils!!
                    if (bobinage.nbSpires !== null) {
                        spire.setText(bobinage?.nbSpires.toString())
                    }
                    if (bobinage.poids !== null) poids.setText(bobinage?.poids.toString())
                    if (bobinage.resistanceU !== null) {
                        RU.setText(bobinage?.resistanceU.toString())
                    }
                    if (bobinage.resistanceV !== null) RV.setText(bobinage?.resistanceV.toString())
                    if (bobinage.resistanceW !== null) RW.setText(bobinage?.resistanceW.toString())
                    if (bobinage.isolementUT !== null) IU.setText(bobinage?.isolementUT.toString())
                    if (bobinage.isolementVT !== null) IV.setText(bobinage?.isolementVT.toString())
                    if (bobinage.isolementWT !== null) IW.setText(bobinage?.isolementWT.toString())
                    if (bobinage.isolementUV !== null) IIU.setText(bobinage?.isolementUV.toString())
                    if (bobinage.isolementUW !== null) IIV.setText(bobinage?.isolementUW.toString())
                    if (bobinage.isolementVW !== null) IIW.setText(bobinage?.isolementVW.toString())
                    if (bobinage.observations !== null) obs.setText(bobinage?.observations.toString())
                    var formater = DateTimeFormatter.ofPattern("DD-MM-YYYY HH:mm")
                    if (bobinage.dateDebut !== null) dated.setText(bobinage?.dateDebut!!.toLocaleString())
                }
                //viewModel.selectBobinage(bobinage!!._id)
                /*var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dated.setText(LocalDateTime.now().format(format))*/
            }
        }

        details.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
                details.setText("masquer les détails")
            } else {
                visibility = View.GONE
                details.setText("afficher les détails")
            }
            non.visibility = visibility
            client.visibility = visibility
            oui.visibility = visibility
            frequence.visibility = visibility
            puissance.visibility = visibility
            courant.visibility = visibility
            phases.visibility = visibility
            vitesse.visibility = visibility
            type.visibility = visibility
            marque.visibility = visibility
            dates.visibility = visibility
            tension.visibility = visibility
            switch.visibility = visibility
            poids.visibility = visibility
            Log.i("INFO", "change")
        }
        btnfils.setOnClickListener {
            if (nbBrins.text.isNotEmpty() && diam.text.isNotEmpty()) {
                viewModel.addSection(
                    nbBrins.text.toString().toLong(),
                    diam.text.toString().toDouble()
                )
                var s = viewModel.somme(viewModel.sections.value!!)
                som.setText(s.toString())
                viewModel.quickSave()
            } else {
                val mySnackbar = Snackbar.make(layout,"Veuillez préciser le nombre de brins et le diamètre de la section", 3600)
                mySnackbar.show()
            }
        }
        addschema.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            Log.i("INFO",test.toString())
            if (test == false) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
                // Ensure that there's a camera activity to handle the intent
                cameraIntent.resolveActivity(requireActivity().packageManager).also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException ) {
                        // Error occurred while creating the File
                        Log.i("INFO","error while creating file: "+ ex)
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
                        Log.i("INFO",currentPhotoPath)
                    }
                }
            }
        }
        marque.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 120
                linscroll.layoutParams = margins
            } else{
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        courant.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 120
                linscroll.layoutParams = margins
            } else{
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        client.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 120
                linscroll.layoutParams = margins
            } else{
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        type.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 120
                linscroll.layoutParams = margins
            } else{
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        marque.doAfterTextChanged {
            if (marque.text.isNotEmpty() && marque.hasFocus()) {
                viewModel.bobinage.value!!.marqueMoteur = marque.text.toString()
                viewModel.quickSave()
            }
        }
        courant.doAfterTextChanged {
            if (courant.text.isNotEmpty()) viewModel.bobinage.value!!.courant = courant.text.toString().toFloat()
            viewModel.quickSave()
        }
        vitesse.doAfterTextChanged {
            if (vitesse.text.isNotEmpty()) viewModel.bobinage.value!!.vitesse = vitesse.text.toString().toFloat()
            viewModel.quickSave()
        }
        type.doAfterTextChanged {
            if (type.text.isNotEmpty()) viewModel.bobinage.value!!.typeBobinage = type.text.toString()
            viewModel.quickSave()
        }
        phases.doAfterTextChanged {
            if (phases.text.isNotEmpty()) viewModel.bobinage.value!!.phases = phases.text.toString().toLong()
            viewModel.quickSave()
        }
        frequence.doAfterTextChanged {
            if (frequence.text.isNotEmpty()) viewModel.bobinage.value!!.frequences = frequence.text.toString().toFloat()
            viewModel.quickSave()
        }
        puissance.doAfterTextChanged {
            if (puissance.text.isNotEmpty()) viewModel.bobinage.value!!.puissance = puissance.text.toString().toFloat()
            viewModel.quickSave()
        }
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.bobinage.value!!.calageEncoches = isChecked
            viewModel.quickSave()
        }
        tension.doAfterTextChanged {
            if (tension.text.isNotEmpty()) viewModel.bobinage.value!!.tension = tension.text.toString().toLong()
        }

        spire.doAfterTextChanged {
            if (spire.text.isNotEmpty()) viewModel.bobinage.value!!.nbSpires = spire.text.toString().toLong()
            viewModel.quickSave()
        }
        poids.doAfterTextChanged {
            if (poids.text.isNotEmpty()) viewModel.bobinage.value!!.poids = poids.text.toString().toFloat()
            viewModel.quickSave()
        }
        RU.doAfterTextChanged {
            if(RU.text.isNotEmpty()) viewModel.bobinage.value!!.resistanceU = RU.text.toString().toFloat()
            viewModel.quickSave()
        }
        RV.doAfterTextChanged {
            if(RV.text.isNotEmpty()) viewModel.bobinage.value!!.resistanceV = RV.text.toString().toFloat()
            viewModel.quickSave()
        }
        RW.doAfterTextChanged {
            if (RW.text.isNotEmpty()) viewModel.bobinage.value!!.resistanceW = RW.text.toString().toFloat()
            viewModel.quickSave()
        }
        IU.doAfterTextChanged {
            if (IU.text.isNotEmpty()) viewModel.bobinage.value!!.isolementUT = IU.text.toString().toFloat()
            viewModel.quickSave()
        }
        IV.doAfterTextChanged {
             if (IV.text.isNotEmpty()) viewModel.bobinage.value!!.isolementVT = IV.text.toString().toFloat()
            viewModel.quickSave()
        }
        IW.doAfterTextChanged {
            if (IW.text.isNotEmpty()) viewModel.bobinage.value!!.isolementWT = IW.text.toString().toFloat()
            viewModel.quickSave()
        }
         IIU.doAfterTextChanged {
             if (IIU.text.isNotEmpty()) viewModel.bobinage.value!!.isolementUV =  IIU.text.toString().toFloat()
             viewModel.quickSave()
         }
        IIV.doAfterTextChanged {
            if (IIV.text.isNotEmpty()) viewModel.bobinage.value!!.isolementUW =   IIV.text.toString().toFloat()
            viewModel.quickSave()
        }
         IIW.doAfterTextChanged {
             if (IIW.text.isNotEmpty()) viewModel.bobinage.value!!.isolementVW =  IIW.text.toString().toFloat()
             viewModel.quickSave()
         }
        obs.doAfterTextChanged {
            viewModel.bobinage.value!!.observations = obs.text.toString()
            viewModel.quickSave()
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.bobinage.value!!.calageEncoches = isChecked
            viewModel.quickSave()
        }
        quit.setOnClickListener {
            viewModel.back(layout)
        }
        enrg.setOnClickListener {
            viewModel.bobinage.value!!.status = 2L
            viewModel.bobinage.value!!.sectionsFils = viewModel.sections.value
            viewModel.getTime()
            Log.i("INFO","duree: ${viewModel.bobinage.value!!.dureeTotale}")
            viewModel.save(requireContext(), layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout))
        }

        term.setOnClickListener {
            viewModel.bobinage.value!!.status = 3L
            viewModel.bobinage.value!!.sectionsFils = viewModel.sections.value
            viewModel.getTime()
            Log.i("INFO","duree: ${viewModel.bobinage.value!!.dureeTotale}")
            viewModel.save(requireContext(), layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout))
        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES+"/test_pictures")
        if (storageDir.exists()) {
            return File.createTempFile(
                viewModel.bobinage.value?.numFiche + "_" + SystemClock.uptimeMillis(), /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
                schemas.visibility = View.VISIBLE
            }
        } else {
            makeFolder()
            return File.createTempFile(
                viewModel.bobinage.value?.numFiche + "_" + SystemClock.uptimeMillis(), /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
                schemas.visibility = View.VISIBLE
            }
        }
    }

    fun makeFolder(){
        val storageDir: File = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES+"/test_pictures")
        storageDir.mkdir()
    }

    /*private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroad (mediaScanIntent)
        }
    }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == PHOTO_RESULT) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
            val uri = context?.let { photo.saveImage(it.applicationContext) }
            if (uri != null) {
                viewModel.addSchema(0,uri)
            }
            Log.i("INFO",uri.toString())
        }*/
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
            viewModel.addSchema(currentPhotoPath)
            //galleryAddPic()
        }
    }

}