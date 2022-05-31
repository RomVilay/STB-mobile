package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_monophase, container, false)
        var resistanceTravail = layout.findViewById<EditText>(R.id.isopmV)
        var resistanceDemarrage	= layout.findViewById<EditText>(R.id.rdem)
        var valeurCondensateur	= layout.findViewById<EditText>(R.id.condens)
        var tensionU	= layout.findViewById<EditText>(R.id.tU)
        var tensionV	= layout.findViewById<EditText>(R.id.tV)
        var tensionW	= layout.findViewById<EditText>(R.id.tW)
        var observations = layout.findViewById<EditText>(R.id.obs2)
        var retour = layout.findViewById<Button>(R.id.retourTri)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerTRi)
        var terminer = layout.findViewById<Button>(R.id.termMo)
        var btnPhoto = layout.findViewById<Button>(R.id.photo2)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var fiche = viewModel.selection.value!!
        if( fiche.resistanceTravail !== null) resistanceTravail.setText(fiche.resistanceTravail.toString())
        if( fiche.resistanceDemarrage !== null ) resistanceDemarrage.setText(fiche.resistanceDemarrage.toString())
        if( fiche.valeurCondensateur !== null ) valeurCondensateur.setText(fiche.valeurCondensateur.toString())
        if( fiche.tensionU !== null ) tensionU.setText(fiche.tensionU.toString())
        if( fiche.tensionV !== null ) tensionV.setText(fiche.tensionV.toString())
        if( fiche.tensionW !== null ) tensionW.setText(fiche.tensionW.toString())
        if (fiche.status!! < 3) {

            resistanceTravail.doAfterTextChanged {
                if (resistanceTravail.text.isNotEmpty()) fiche.resistanceTravail = resistanceTravail.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceDemarrage.doAfterTextChanged {
                if (resistanceDemarrage.text.isNotEmpty() ) fiche.resistanceDemarrage = resistanceDemarrage.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            valeurCondensateur.doAfterTextChanged {
                if (valeurCondensateur.text.isNotEmpty()  ) fiche.valeurCondensateur = valeurCondensateur.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionU.doAfterTextChanged {
                if (tensionU.text.isNotEmpty() ) fiche.tensionU = tensionU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty() )fiche.tensionV = tensionV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() )fiche.tensionW = tensionV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            observations.doAfterTextChanged {
                fiche.observations = observations.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        }  else {
            resistanceTravail.isEnabled = false
            resistanceDemarrage.isEnabled = false
            valeurCondensateur.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            observations.isEnabled = false
            enregistrer.visibility = View.GONE
            terminer.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
        }


        //

        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)

        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto2)
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
        viewModel.photos.value = fiche.photos!!.toMutableList()
        sAdapter.update(fiche.photos!!.toMutableList())
        btnPhoto.setOnClickListener {
            var test = ActivityCompat.checkSelfPermission(requireContext(),
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
        terminer.setOnClickListener {
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

        var gal = layout.findViewById<Button>(R.id.g3)
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 6)
        }

        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.PartMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }

        return layout
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
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
                viewModel.selection.value?.numFiche + "_" + SystemClock.uptimeMillis(), /* prefix */
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


}