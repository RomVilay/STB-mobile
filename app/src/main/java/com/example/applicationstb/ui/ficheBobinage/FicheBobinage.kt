package com.example.applicationstb.ui.ficheBobinage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Bobinage
import com.example.applicationstb.model.Fiche
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

    /*private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }*/


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
        var start : Date? = null;
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
        var details = layout.findViewById<TextView>(R.id.details)
        val adapter = FillAdapter(viewModel.sections!!.value!!)
        val sAdapter = schemaAdapter(viewModel.schemas.value!! ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,item.toString())
        })
        var visibility = View.VISIBLE
        //champs fils
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
        viewModel.schemas.observe(viewLifecycleOwner, {
            Log.i("INFO",viewModel.schemas.value?.size.toString())
            if (viewModel.schemas.value !== null) {
                sAdapter.update(viewModel.schemas.value!!)
                if (viewModel.schemas.value?.size == 0) {
                    schemas.visibility = View.GONE
                } else {
                    schemas.visibility = View.VISIBLE
                }
            } else {
                viewModel.schemas.value = mutableListOf()
            }
        })
        viewModel.bobinage.observe(viewLifecycleOwner,{
            var bobinage = viewModel.bobinage.value
            if (bobinage != null) {
                marque.setText(bobinage?.marqueMoteur)
                if (bobinage.courant!== null) {courant.setText(bobinage?.courant.toString())} //
                if (bobinage.vitesse!== null) {vitesse.setText(bobinage?.vitesse.toString())} //
                type.setText(bobinage?.typeBobinage)
                client.setText(bobinage?.client?.enterprise)
                if (bobinage.puissance!== null) {puissance.setText(bobinage?.puissance.toString())} //
                if (bobinage.phases!== null) {phases.setText(bobinage?.phases.toString())} //
                if (bobinage.frequences!== null) {frequence.setText(bobinage?.frequences.toString())} //
                if(bobinage.calageEncoches !== null) {switch.setChecked(bobinage.calageEncoches!!)} else switch.setChecked(false)
                tension.setText(bobinage?.tension.toString())
                adapter.list = bobinage.sectionsFils!!
                if (bobinage.nbSpires!== null) {spire.setText(bobinage?.nbSpires.toString())}
                poids.setText(bobinage?.poids.toString())
                RU.setText(bobinage?.resistanceU.toString())
                RV.setText(bobinage?.resistanceV.toString())
                RW.setText(bobinage?.resistanceW.toString())
                IU.setText(bobinage?.isolementUT.toString())
                IV.setText(bobinage?.isolementVT.toString())
                IW.setText(bobinage?.isolementWT.toString())
                IIU.setText(bobinage?.isolementUV.toString())
                IIV.setText(bobinage?.isolementUW.toString())
                IIW.setText(bobinage?.isolementVW.toString())
                obs.setText(bobinage?.observations.toString())
                var formater = DateTimeFormatter.ofPattern("DD-MM-YYYY HH:mm")
                dated.setText( bobinage?.dateDebut!!.toLocaleString())
            }
            /*var format = DateTimeFormatter.ofPattern("DD-MM-YYYY")
            dateDebut.setText(LocalDateTime.now().format(format))*/
        })

        btnSelect.setOnClickListener {
            start = Date()
            var bobinage = viewModel.listeBobinage.find{it.numFiche == spinner.selectedItem}
            viewModel.bobinage.value = bobinage
            viewModel.sections.value = bobinage?.sectionsFils
            viewModel.schemas.value = bobinage?.schemas
            //viewModel.selectBobinage(bobinage!!._id)
            /*var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dated.setText(LocalDateTime.now().format(format))*/
        }

        details.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
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
            viewModel.addSection(nbBrins.text.toString().toLong(),diam.text.toString().toDouble())
            var s = viewModel.somme(viewModel.sections.value!!)
            som.setText(s.toString())
            //Log.i("INFO",viewModel.somme(viewModel.sections.value!!).toString())
        }
        addschema.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(getContext()!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            Log.i("INFO",test.toString())
            if (test == false) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
                // Ensure that there's a camera activity to handle the intent
                cameraIntent.resolveActivity(activity!!.packageManager).also {
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
                            context!!,
                            "com.example.applicationstb.fileprovider",
                            it
                        )
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                        //viewModel.addSchema(photoURI)
                        Log.i("INFO",currentPhotoPath)
                    }
                }
            }
            /*if ()
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        cameraIntent.resolveActivity(activity!!.packageManager).also {
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
                                    context!!,
                                    "com.example.applicationstb.fileprovider",
                                    it
                                )
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                                //viewModel.addSchema(photoURI)
                                Log.i("INFO",currentPhotoPath)
                            }
                        } ()
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )  -> {
                        //requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }

                else -> {
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                }
            }


            }
            //startActivityForResult(cameraIntent, PHOTO_RESULT)*/
        }
        quit.setOnClickListener {
            viewModel.back(layout)
        }
        enrg.setOnClickListener {
            /*if (IU == null) {
                Snackbar.make(
                    layout.findViewById(R.id.FicheBobinageLayout),
                    "veuilleza définir une valeur pour la résistance",
                    Snackbar.LENGTH_SHORT
                ).show()
            }*/
            var bobi = viewModel.bobinage.value
            bobi!!.marqueMoteur = marque.text.toString()
            bobi!!.typeBobinage = type.text.toString()
            bobi!!.vitesse = if (vitesse.text.isNotEmpty()) vitesse.text.toString().toFloat() else bobi!!.vitesse
            bobi!!.puissance = if (puissance.text.isNotEmpty()) puissance.text.toString().toFloat() else bobi!!.puissance
            bobi!!.phases = if (phases.text.isNotEmpty()) phases.text.toString().toLong() else bobi!!.phases
            bobi!!.frequences = if (frequence.text.isNotEmpty()) frequence.text.toString().toFloat() else bobi!!.frequences
            bobi!!.courant = if (courant.text.isNotEmpty()) courant.text.toString().toFloat() else bobi!!.courant
            bobi!!.nbSpires = if (spire.text.isNotEmpty()) spire.text.toString().toLong() else bobi!!.nbSpires
            bobi!!.poids = if (poids.text.isNotEmpty()) poids.text.toString().toFloat() else bobi!!.poids
            bobi!!.resistanceU = if(RU.text.isNotEmpty()) RU.text.toString().toFloat() else bobi!!.resistanceU
            bobi!!.resistanceV = if(RV.text.isNotEmpty()) RV.text.toString().toFloat() else bobi!!.resistanceV
            bobi!!.resistanceW = if (RW.text.isNotEmpty()) RW.text.toString().toFloat() else bobi!!.resistanceW
            bobi!!.isolementUT = if (IU.text.isNotEmpty()) IU.text.toString().toFloat() else bobi!!.isolementUT
            bobi!!.isolementVT = if (IV.text.isNotEmpty()) IV.text.toString().toFloat() else bobi!!.isolementVT
            bobi!!.isolementWT = if (IW.text.isNotEmpty()) IW.text.toString().toFloat() else bobi!!.isolementWT
            bobi!!.isolementUV = if (IIU.text.isNotEmpty()) IIU.text.toString().toFloat() else bobi!!.isolementUV
            bobi!!.isolementUW =  if (IIV.text.isNotEmpty()) IIV.text.toString().toFloat() else bobi!!.isolementUW
            bobi!!.isolementVW = if (IIW.text.isNotEmpty()) IIW.text.toString().toFloat() else bobi!!.isolementVW
            bobi!!.observations = obs.text.toString()
            bobi!!.status = 2L
            bobi!!.calageEncoches = switch.isChecked()
            bobi!!.sectionsFils = viewModel.sections.value
            if (bobi!!.dureeTotale !== null) {
               bobi!!.dureeTotale =
                    (Date().time - start!!.time ) + bobi!!.dureeTotale!!
            } else {
                bobi!!.dureeTotale = Date().time - start!!.time
            }
            viewModel.bobinage.value = bobi
            Log.i("INFO","duree: ${bobi.dureeTotale}")
            viewModel.save(context!!, layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout))
        }

        term.setOnClickListener {
                /*if (IU == null) {
                    Snackbar.make(
                        layout.findViewById(R.id.FicheBobinageLayout),
                        "veuilleza définir une valeur pour la résistance",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }*/
                var bobi = viewModel.bobinage.value
                bobi!!.marqueMoteur = marque.text.toString()
                bobi!!.typeBobinage = type.text.toString()
                bobi!!.vitesse = if (vitesse.text.isNotEmpty()) vitesse.text.toString().toFloat() else bobi!!.vitesse
                bobi!!.puissance = if (puissance.text.isNotEmpty()) puissance.text.toString().toFloat() else bobi!!.puissance
                bobi!!.phases = if (phases.text.isNotEmpty()) phases.text.toString().toLong() else bobi!!.phases
                bobi!!.frequences = if (frequence.text.isNotEmpty()) frequence.text.toString().toFloat() else bobi!!.frequences
                bobi!!.courant = if (courant.text.isNotEmpty()) courant.text.toString().toFloat() else bobi!!.courant
                bobi!!.nbSpires = if (spire.text.isNotEmpty()) spire.text.toString().toLong() else bobi!!.nbSpires
                bobi!!.poids = if (poids.text.isNotEmpty()) poids.text.toString().toFloat() else bobi!!.poids
                bobi!!.resistanceU = if(RU.text.isNotEmpty()) RU.text.toString().toFloat() else bobi!!.resistanceU
                bobi!!.resistanceV = if(RV.text.isNotEmpty()) RV.text.toString().toFloat() else bobi!!.resistanceV
                bobi!!.resistanceW = if (RW.text.isNotEmpty()) RW.text.toString().toFloat() else bobi!!.resistanceW
                bobi!!.isolementUT = if (IU.text.isNotEmpty()) IU.text.toString().toFloat() else bobi!!.isolementUT
                bobi!!.isolementVT = if (IV.text.isNotEmpty()) IV.text.toString().toFloat() else bobi!!.isolementVT
                bobi!!.isolementWT = if (IW.text.isNotEmpty()) IW.text.toString().toFloat() else bobi!!.isolementWT
                bobi!!.isolementUV = if (IIU.text.isNotEmpty()) IIU.text.toString().toFloat() else bobi!!.isolementUV
                bobi!!.isolementUW =  if (IIV.text.isNotEmpty()) IIV.text.toString().toFloat() else bobi!!.isolementUW
                bobi!!.isolementVW = if (IIW.text.isNotEmpty()) IIW.text.toString().toFloat() else bobi!!.isolementVW
                bobi!!.observations = obs.text.toString()
                bobi!!.status = 3L
                bobi!!.calageEncoches = switch.isChecked()
                bobi!!.sectionsFils = viewModel.sections.value
                if (bobi!!.dureeTotale !== null) {
                    bobi!!.dureeTotale =
                        (Date().time - start!!.time ) + bobi!!.dureeTotale!!
                } else {
                    bobi!!.dureeTotale = Date().time - start!!.time
                }
                viewModel.bobinage.value = bobi
                Log.i("INFO","duree: ${bobi.dureeTotale}")
                viewModel.save(context!!, layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout))
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
                "JPEG_${timeStamp}_", /* prefix */
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
                "JPEG_${timeStamp}_", /* prefix */
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
            viewModel.addSchema(0,Uri.parse(currentPhotoPath))
            //galleryAddPic()
        }
    }

}