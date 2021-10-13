package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AlternteurFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlternateurFragment : Fragment() {

    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos:RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_alternateur, container, false)
        /*var isolementMasseStatorPrincipalU = layout.findViewById<>()
        var isolementMasseStatorPrincipalV: Float?,
        var isolementMasseStatorPrincipalW	: Float?,
        var isolementMasseRotorPrincipal	: Float?,
        var isolementMasseStatorExcitation	: Float?,
        var resistanceStatorPrincipalU	: Float?,
        var resistanceStatorPrincipalV	: Float?,
        var resistanceStatorPrincipalW	: Float?,
        var resistanceRotorPrincipal	: Float?,
        var resistanceStatorExcitation	: Float?,
        var resistanceRotorExcitation	: Float?,
        var isolementPhasePhaseStatorPrincipalUV	: Float?,
        var isolementPhasePhaseStatorPrincipalVW	: Float?,
        var isolementPhasePhaseStatorPrincipalUW	: Float?,
        var testDiode : Boolean?,
        var tensionU	: Float?,
        var tensionV	: Float?,
        var tensionW	: Float?,
        var intensiteU	: Float?,
        var intensiteV	: Float?,
        var intensiteW	: Float?,
        var tensionUExcitation	: Float?,
        var tensionVExcitation	: Float?,
        var tensionWExcitation	: Float?,
        var intensiteUExcitation	: Float?,
        var intensiteVExcitation	: Float?,
        var intensiteWExcitation	: Float?*/



        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var retour = layout.findViewById<Button>(R.id.retourAlt)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerAlt)
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

        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {

        }
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.partMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
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