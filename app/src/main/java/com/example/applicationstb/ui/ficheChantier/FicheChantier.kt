package com.example.applicationstb.ui.ficheChantier

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.applicationstb.R
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Fiche
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private lateinit var viewModel: FicheChantierViewModel
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1
    //


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FicheChantierViewModel::class.java)
        var list = arguments?.get("listChantiers") as Array<Fiche>
        viewModel.token = arguments?.get("Token") as String
        viewModel.listeChantiers = list.toCollection(ArrayList())
        val layout = inflater.inflate(R.layout.fiche_chantier_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val materiel = layout.findViewById<EditText>(R.id.materiel)
        val objet = layout.findViewById<EditText>(R.id.objet)
        val observation = layout.findViewById<EditText>(R.id.observation)
        val selectButton = layout.findViewById<Button>(R.id.btnDemarrer)
        val client = layout.findViewById<TextView>(R.id.puissance)
        val vehicule = layout.findViewById<TextView>(R.id.vehicule)
        val contact = layout.findViewById<TextView>(R.id.marque)
        val numero = layout.findViewById<TextView>(R.id.type)
        val adresse = layout.findViewById<TextView>(R.id.adresse)
        val dates = layout.findViewById<LinearLayout>(R.id.dates)
        val dateDebut = layout.findViewById<EditText>(R.id.DateFin)
        val dateFin = layout.findViewById<EditText>(R.id.DateDebut)
        val showDetails = layout.findViewById<TextView>(R.id.details)
        val quit = layout.findViewById<Button>(R.id.quit)
        val enregistrer = layout.findViewById<Button>(R.id.enregistrer)
        val adapter = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeChantiers.map { it.numFiche })
        var visibility = View.VISIBLE
        //define signature area
        //val stech = layout.findViewById<DawingView>(R.id.signTech)
        //val scli = layout.findViewById<DawingView>(R.id.signclient)
        //stech.viewPlaceholder = "signature technicien"
        //scli.viewPlaceholder = "signature client"
        val btnTech = layout.findViewById<Button>(R.id.signTech)
        val btnClient = layout.findViewById<Button>(R.id.signClient)
        //var stech: Bitmap? = sview.extraBitmap
        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,item.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })
        viewModel.chantier.observe(viewLifecycleOwner, {
            materiel.setText(it.materiel)
            objet.setText(it.objet)
            //observation.setText(chantier?.observations)
            client.setText(it.client.enterprise)
            vehicule.setText(it.vehicule)
            contact.setText(it.contact)
            numero.setText(it.telContact)
            adresse.setText(it.adresseChantier)
            var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dateDebut.setText(LocalDateTime.now().format(format))
        })

        btnPhoto.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(getContext()!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            Log.i("INFO",test.toString())
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
        showDetails.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            client.visibility = visibility
            vehicule.visibility = visibility
            contact.visibility = visibility
            numero.visibility = visibility
            adresse.visibility = visibility
            dates.visibility = visibility
            Log.i("INFO","change")
        }
        spinner.adapter = adapter
        btnClient.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            // set message of alert dialog
            dialogBuilder
                .setCancelable(true)
                .setView(inflater.inflate(R.layout.dawing_view, null))
                .setPositiveButton("Enregistrer", DialogInterface.OnClickListener{
                    dialog, id ->
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
                viewModel.signatures.add(0,uri)
            }
        }
        btnTech.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            dialogBuilder
                .setCancelable(true)
                .setView(inflater.inflate(R.layout.dawing_view, null))
                .setPositiveButton("Enregistrer", DialogInterface.OnClickListener{
                        dialog, id ->
                    dialog.dismiss()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Signature Responsable")
            alert.show()
            alert.setOnDismissListener {
                var v = alert.findViewById<DawingView>(R.id.dawingView)
                var uri = v.showLog()
                viewModel.signatures.add(1,uri)
            }
        }
        selectButton.setOnClickListener {
            var chantier = viewModel.listeChantiers.find{it.numFiche == spinner.selectedItem}
            viewModel.selectChantier(viewModel.token as String , chantier!!._id)
            chantier = viewModel.chantier.value
            //Log.i("INFO", "client: ${chantier!!.client} ")
            /*materiel.setText(chantier?.materiel)
            objet.setText(chantier?.objet)
            //observation.setText(chantier?.observations)
            client.setText(chantier?.client?.enterprise)
            vehicule.setText(chantier?.vehicule?.nom)
            contact.setText(chantier?.contact)
            numero.setText(chantier?.telContact.toString())
            //adresse.setText(chantier?.adresse)
            var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dateDebut.setText(LocalDateTime.now().format(format))*/
        }
        quit.setOnClickListener {
            //stech = generateBitmapFromView(sview)

            //Log.i("INFO",stech?.let { it1 -> saveImageToInternalStorage(it1) }.toString())
            viewModel.back(layout)
        }
        enregistrer.setOnClickListener {

            /*val uriTech = stech.extraBitmap.saveImage(context!!.applicationContext)
            Log.i("INFO",uriTech.toString())
            val uriCli = scli.extraBitmap.saveImage(context!!.applicationContext)
            Log.i("INFO",uriCli.toString())
            //Log.i("INFO","vue convertie to bitmap")
            viewModel.signatures.add(uriTech)
            viewModel.signatures.add(uriCli)
            Log.i("INFO",viewModel.signatures.toString())*/

            Log.i("info", viewModel.signatures.toString())
            viewModel.back(layout)
        }

        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_RESULT) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val uri = context?.let { photo.saveImage(it.applicationContext) }
            if (uri != null) {
                Log.i("INFO",uri.toString())
                viewModel.addPhoto(0,uri)
            }
            Log.i("INFO",uri.toString())
        }
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
        val storageDir: File = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES+"/test_pictures")
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

        val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.i("INFO",uri.toString())
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

}