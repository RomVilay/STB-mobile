package com.example.applicationstb.ui.ficheChantier

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private val viewModel: FicheChantierViewModel by activityViewModels()
    private val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1
    //


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.token.postValue(arguments?.get("Token") as String)
        viewModel.username = arguments?.get("username") as String
        val layout = inflater.inflate(R.layout.fiche_chantier_fragment, container, false)
        val lin = layout.findViewById<LinearLayout>(R.id.linearCh)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val materiel = layout.findViewById<EditText>(R.id.materiel)
        val objet = layout.findViewById<EditText>(R.id.objet)
        val observation = layout.findViewById<EditText>(R.id.observations)
        val selectButton = layout.findViewById<Button>(R.id.btnDemarrer)
        val client = layout.findViewById<TextView>(R.id.puissance)
        val vehicule = layout.findViewById<TextView>(R.id.vehicule)
        val contact = layout.findViewById<TextView>(R.id.marque)
        val numero = layout.findViewById<TextView>(R.id.type)
        val adresse = layout.findViewById<TextView>(R.id.adresse)
        val dateDebut = layout.findViewById<TextView>(R.id.dateDebut)
        val showDetails = layout.findViewById<TextView>(R.id.titreDetails)
        val quit = layout.findViewById<Button>(R.id.quit)
        val enregistrer = layout.findViewById<Button>(R.id.enregistrer)
        val term = layout.findViewById<Button>(R.id.termC)
        val dureeEssai = layout.findViewById<EditText>(R.id.dureeEch)
        viewModel.listeChantiers.observe(viewLifecycleOwner){
            spinner.adapter = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeChantiers.value!!.map { it.numFiche  })
        }
        var visibility = View.VISIBLE
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
        })
        selectButton.setOnClickListener {
            lin.visibility = View.VISIBLE
            var chantier = viewModel.listeChantiers.value!!.find { it.numFiche == spinner.selectedItem }
            viewModel.chantier.value = chantier
            viewModel.start.value = Date()
            if (viewModel.chantier.value!!.materiel !== null) materiel.setText(viewModel.chantier.value!!.materiel)
            if (viewModel.chantier.value!!.objet !== null) objet.setText(viewModel.chantier.value!!.objet)
            if (viewModel.chantier.value!!.observations !== null) observation.setText(viewModel.chantier.value!!.observations)
            if (viewModel.chantier.value!!.client !== null) client.setText(viewModel.chantier.value!!.client!!.enterprise)
            if (viewModel.chantier.value!!.vehicule !== null) viewModel.getVehicule(
                viewModel.chantier.value!!.vehicule!!,
                vehicule
            )
            if (viewModel.chantier.value!!.contact !== null) contact.setText(viewModel.chantier.value!!.contact)
            if (viewModel.chantier.value!!.dureeEssai !== null) dureeEssai.setText(viewModel.chantier.value!!.dureeEssai)
            if (viewModel.chantier.value!!.telContact !== null) numero.setText(viewModel.chantier.value!!.telContact)
            if (viewModel.chantier.value!!.adresseChantier !== null) adresse.setText(viewModel.chantier.value!!.adresseChantier)
            if (viewModel.chantier.value!!.dateDebut !== null) dateDebut.setText(viewModel.chantier.value!!.dateDebut!!.toLocaleString())
            lifecycleScope.launch(Dispatchers.IO) {
                var test = ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                if (!test) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 1
                    )
                }
                val listPhotos = chantier?.photos?.toMutableList()
                var iter = listPhotos?.listIterator()
                while (iter?.hasNext() == true) {
                    Log.i("INFO", iter.next())
                    //iter.set(viewModel.getPhotoFile(iter.toString())!!)
                }
                /*if (viewModel.isOnline(requireContext())) {

                }*/
                viewModel.photos.postValue(listPhotos)
            }
            sAdapter.update(viewModel.photos.value!!)
            viewModel.chantier.value?.status = 2
            viewModel.getTime()
            viewModel.quickSave()
        }
        val btnTech = layout.findViewById<Button>(R.id.signTech)
        val btnClient = layout.findViewById<Button>(R.id.signClient)
        //var stech: Bitmap? = sview.extraBitmap
        var btnPhoto = layout.findViewById<Button>(R.id.photo5)

        materiel.doAfterTextChanged {
            if (materiel.text.isNotEmpty()) viewModel.chantier.value?.materiel =
                materiel.text.toString()
            viewModel.getTime()
            viewModel.quickSave()
        }
        objet.doAfterTextChanged {
            if (objet.text.isNotEmpty()) viewModel.chantier.value?.objet = objet.text.toString()
            viewModel.getTime()
            viewModel.quickSave()
        }
        observation.doAfterTextChanged {
            if (observation.text.isNotEmpty()) viewModel.chantier.value?.observations =
                observation.text.toString()
            viewModel.getTime()
            viewModel.quickSave()
        }
        adresse.setOnClickListener {
            if (viewModel.chantier.value !== null && viewModel.chantier.value!!.adresseChantier !== null) {
                val gmmIntentUri =
                    Uri.parse("geo:0,0?q=${viewModel.chantier.value!!.adresseChantier!!}")
                //val gmmIntentUri = Uri.parse("google.navigation:q=${viewModel.chantier.value!!.adresseChantier!!}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

        }
        dureeEssai.doAfterTextChanged {
            if (dureeEssai.text.isNotEmpty()) viewModel.chantier.value?.dureeEssai = dureeEssai.text.toString()
            viewModel.getTime()
            viewModel.quickSave()
        }
        btnPhoto.setOnClickListener {
            runBlocking {
                var test = ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                Log.i("INFO", test.toString())
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
        }

        var gal = layout.findViewById<Button>(R.id.extPic)
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 6)
        }
        showDetails.setOnClickListener {
            if (visibility == View.GONE) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            client.visibility = visibility
            vehicule.visibility = visibility
            contact.visibility = visibility
            numero.visibility = visibility
            adresse.visibility = visibility
            dateDebut.visibility = visibility
            Log.i("INFO", "change")
        }
        btnClient.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            // set message of alert dialog
            dialogBuilder
                .setCancelable(true)
                .setView(inflater.inflate(R.layout.dawing_view, null))
                .setPositiveButton("Enregistrer", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Signature Client")
            // show alert dialog
            alert.show()
            alert.setOnDismissListener {
                var v = alert.findViewById<DawingView>(R.id.dawingView)
                var uri = v.showLog()
                Log.i(
                    "INFO",
                    "uri client: " + uri!!
                )
                viewModel.signatures.add(uri!!.removePrefix("/storage/emulated/0/Pictures/test_signatures/"))
                viewModel.chantier.value?.signatureClient =
                    uri!!.removePrefix("/storage/emulated/0/Pictures/test_signatures/")
                viewModel.getTime()
                viewModel.quickSave()
                if (viewModel.isOnline(requireContext())){
                    Log.i("info","signature ${viewModel.chantier.value!!.signatureClient!!}")
                    CoroutineScope(Dispatchers.IO).launch {
                       var s = async{viewModel.repositoryPhoto.sendSignature(viewModel.token.value!!, viewModel.chantier.value!!.signatureClient!!, requireContext())}
                        s.await()
                        withContext(Dispatchers.Main){
                            val mySnackbar = Snackbar.make(layout, "signature envoyée", 3600)
                            mySnackbar.show()
                        }
                    }
                }
            }
        }
        btnTech.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            dialogBuilder
                .setCancelable(true)
                .setView(inflater.inflate(R.layout.dawing_view, null))
                .setPositiveButton("Enregistrer", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
            val alert = dialogBuilder.create()
            alert.show()
            alert.setOnDismissListener {
                var v = alert.findViewById<DawingView>(R.id.dawingView)
                var uri = v.showLog()
                viewModel.signatures.add(uri!!.removePrefix("/storage/emulated/0/Pictures/test_signatures/"))
                viewModel.chantier.value?.signatureTech =
                    uri!!.removePrefix("/storage/emulated/0/Pictures/test_signatures/")
                //Log.i("INFO",sign.exists().toString())
                viewModel.getTime()
                viewModel.quickSave()
                if (viewModel.isOnline(requireContext())){
                    CoroutineScope(Dispatchers.IO).launch {
                        var s = async{viewModel.repositoryPhoto.sendSignature(viewModel.token.value!!, viewModel.chantier.value!!.signatureTech!!, requireContext())}
                        s.await()
                        withContext(Dispatchers.Main){
                            val mySnackbar = Snackbar.make(layout, "signature envoyée", 3600)
                            mySnackbar.show()
                        }
                    }
                }
            }
        }


        quit.setOnClickListener {
            lin.visibility = View.INVISIBLE
            viewModel.back(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.getTime()
            viewModel.quickSave()
            //if (viewModel.token.value == "") {
                viewModel.save(
                    requireContext(),
                    layout.findViewById<CoordinatorLayout>(R.id.FicheChantierLayout), viewModel.token.value!!)
           /* } else {
                viewModel.save(
                    requireContext(),
                    layout.findViewById<CoordinatorLayout>(R.id.FicheChantierLayout),
                    viewModel.token.value!!
                )
            }*/
        }
        term.setOnClickListener {
            viewModel.chantier.value?.status = 3
            viewModel.getTime()
            viewModel.quickSave()
            viewModel.save(
               requireContext(),
               layout.findViewById<CoordinatorLayout>(R.id.FicheChantierLayout),
                viewModel.token.value!!
            )
            if (viewModel.listeChantiers.value!!.size > 1 ) {
                lin.visibility = View.INVISIBLE
                viewModel.listeChantiers.value!!.remove(viewModel.chantier.value!!)
                spinner.adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.support_simple_spinner_dropdown_item,
                    viewModel.listeChantiers.value!!.map { it.numFiche })

            } else {
                viewModel.back(layout)
            }

        }
        return layout
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (resultCode < 0 || resultCode > 0) {
            if (requestCode == PHOTO_RESULT) {
                val photo: Bitmap = data?.extras?.get("data") as Bitmap
                val uri = context?.let { photo.saveImage(it.applicationContext) }
                if (uri != null) {
                    viewModel.galleryAddPic(uri.path)
                    /*var picture = File(uri.path)
                    try {
                        viewModel.sendPhoto(data?.extras?.get("data") as File)
                    } catch (e: java.lang.Exception) {
                        Log.e("EXCEPTION",e.message!!)
                    }*/
                }
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                viewModel.addPhoto(currentPhotoPath)
            }
            if (resultCode == Activity.RESULT_OK && requestCode == 6) {
                var file = viewModel.getRealPathFromURI(data?.data!!)
                CoroutineScope(Dispatchers.IO).launch {
                    var nfile = async { viewModel.sendExternalPicture(file!!) }
                    nfile.await()
                    if (nfile.isCompleted) {
                        var list = viewModel.chantier.value?.photos?.toMutableList()
                        list!!.removeAll { it == "" }
                        list.add(nfile.await()!!)
                        viewModel.chantier.value?.photos = list?.toTypedArray()
                        viewModel.photos.postValue(list!!)
                        viewModel.quickSave()
                    }
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getLocalFiches()
        }
        Log.i("info", "retour chantier")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (storageDir.exists()) {
            if (viewModel.isOnline(requireContext())) {
                return File.createTempFile(
                    viewModel.chantier.value?.numFiche + "_" + viewModel.chantier.value?.photos!!.size,/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", currentPhotoPath)
                }
            } else {
                return File.createTempFile(
                    viewModel.chantier.value?.numFiche + "_" + viewModel.chantier.value?.photos!!.size,/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                }
            }
        } else {
            makeFolder()
            if (viewModel.isOnline(requireContext())) {
                return File.createTempFile(
                    viewModel.imageName.value?.name.toString().removeSuffix(".jpg"),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", currentPhotoPath)
                }
            } else {
                return File.createTempFile(
                    viewModel.chantier.value!!.numFiche + "_" + SystemClock.uptimeMillis(),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", "local photo path:" + currentPhotoPath)
                }
            }
        }
    }

    fun makeFolder() {
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        storageDir.mkdir()
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        //Log.i("INFO",viewModel.imageName.value.toString())
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            viewModel.chantier.value?.numFiche + "_" + SystemClock.uptimeMillis()
        )

        val uri: Uri? =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.i("INFO", uri.toString())
            return uri
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /*fun writeToFile(fiche: Chantier, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("Fiche_${data}.txt", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }*/

}