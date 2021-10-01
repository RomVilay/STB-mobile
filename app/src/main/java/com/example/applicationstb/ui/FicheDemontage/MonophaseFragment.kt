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
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class MonophaseFragment : Fragment() {

    companion object {
        fun newInstance() = MonophaseFragment()
    }
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos:RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_monophase, container, false)
        var titre1 = layout.findViewById<TextView>(R.id.titreMono)
        var titre2 = layout.findViewById<TextView>(R.id.titre2)
        var titre3 = layout.findViewById<TextView>(R.id.titre3)

        //
        var infos = layout.findViewById<CardView>(R.id.infoMoteur)
        var essais = layout.findViewById<CardView>(R.id.essais)
        var meca = layout.findViewById<CardView>(R.id.meca)
        var retour = layout.findViewById<Button>(R.id.retourMono)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerMono)

        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)

        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        var btnPhoto = layout.findViewById<Button>(R.id.photo2)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto2)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,viewModel.schema.value.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })

        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.enregistrer(layout)
        }
        /*var listePhotos = layout.findViewById<Button>(R.id.recyclerPhoto2)
        listePhotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listePhotos.adapter = schemaAdapter(viewModel.photos.schemas,{ item ->
           Log.i("INFO","photo 1")
        })*/

        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.PartMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        titre1.setOnClickListener {
            var layout = infos.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            infos.layoutParams = layout
        }
        titre2.setOnClickListener {
            var layout = essais.layoutParams
            if (layout.height == 130){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 130
                Log.i("INFO","in")
            }
            essais.layoutParams = layout
        }
        titre3.setOnClickListener {
            var layout = meca.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            meca.layoutParams = layout
        }
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


}