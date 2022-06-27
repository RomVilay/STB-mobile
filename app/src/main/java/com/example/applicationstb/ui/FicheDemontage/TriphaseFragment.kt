package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TriphaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriphaseFragment : Fragment() {

    private val viewModel: FicheDemontageViewModel by activityViewModels()
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_triphase, container, false)
        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        var infos = layout.findViewById<FrameLayout>(R.id.infoLayout)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
        var UM = layout.findViewById<EditText>(R.id.isopmPA)
        var VM = layout.findViewById<EditText>(R.id.isopmPP)
        var WM = layout.findViewById<EditText>(R.id.isopmI)
        var UV = layout.findViewById<EditText>(R.id.isoppPC)
        var UW = layout.findViewById<EditText>(R.id.isoppPB)
        var iVW = layout.findViewById<EditText>(R.id.isoppW)
        var RU = layout.findViewById<EditText>(R.id.rInduit)
        var RV = layout.findViewById<EditText>(R.id.rPP)
        var RW = layout.findViewById<EditText>(R.id.rW)
        var tensionU = layout.findViewById<EditText>(R.id.u)
        var tensionV = layout.findViewById<EditText>(R.id.v)
        var tensionW = layout.findViewById<EditText>(R.id.w)
        var isolementPhase = layout.findViewById<EditText>(R.id.isoPM)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var enr = layout.findViewById<Button>(R.id.enregistrerTRi)
        var retour = layout.findViewById<Button>(R.id.retourTri)
        var ter = layout.findViewById<Button>(R.id.termTri)
        var gal = layout.findViewById<Button>(R.id.gallerie)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var fiche = viewModel.selection.value!!
        if (fiche.isolementPhaseMasseStatorUM !== null) UM.setText(fiche.isolementPhaseMasseStatorUM!!.toString()) else 0
        if (fiche.isolementPhaseMasseStatorVM !== null) VM.setText(fiche.isolementPhaseMasseStatorVM!!.toString()) else 0
        if (fiche.isolementPhaseMasseStatorWM !== null) WM.setText(fiche.isolementPhaseMasseStatorWM!!.toString()) else 0
        if (fiche.isolementPhasePhaseStatorUV !== null) UV.setText(fiche.isolementPhasePhaseStatorUV!!.toString())
        if (fiche.isolementPhasePhaseStatorUW !== null) UW.setText(fiche.isolementPhasePhaseStatorUW!!.toString()) else 0
        if (fiche.isolementPhasePhaseStatorVW !== null) iVW.setText(fiche.isolementPhasePhaseStatorVW!!.toString()) else 0
        if (fiche.resistanceStatorU !== null) RU.setText(fiche.resistanceStatorU!!.toString()) else 0
        if (fiche.resistanceStatorV !== null) RV.setText(fiche.resistanceStatorV!!.toString()) else 0
        if (fiche.resistanceStatorW !== null) RW.setText(fiche.resistanceStatorW!!.toString()) else 0
        if (fiche.tensionU !== null) tensionU.setText(fiche.tensionU!!.toString()) else 0
        if (fiche.tensionV !== null) tensionV.setText(fiche.tensionV!!.toString()) else 0
        if (fiche.tensionW !== null) tensionW.setText(fiche.tensionW!!.toString()) else 0
        if (fiche.observations !== null) obs.setText(fiche.observations)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        viewModel.photos.value = fiche.photos!!.toMutableList()
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
        if (fiche.photos !== null) sAdapter.update(viewModel.photos.value!!)

        if (fiche.status!! < 3L) {
            UM.doAfterTextChanged {
                if (UM.text.isNotEmpty() && UM.hasFocus() ) fiche.isolementPhaseMasseStatorUM =
                    UM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VM.doAfterTextChanged {
                if (VM.text.isNotEmpty() && VM.hasFocus() ) fiche.isolementPhaseMasseStatorVM =
                    VM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            WM.doAfterTextChanged {
                if (WM.text.isNotEmpty() && WM.hasFocus()) fiche.isolementPhaseMasseStatorWM =
                    WM.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UV.doAfterTextChanged {
                if (UV.text.isNotEmpty() && UV.hasFocus() ) fiche.isolementPhasePhaseStatorUV =
                    UV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UW.doAfterTextChanged {
                if (UW.text.isNotEmpty() && UW.hasFocus() ) fiche.isolementPhasePhaseStatorUW =
                    UW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            iVW.doAfterTextChanged {
                if (iVW.text.isNotEmpty() && iVW.hasFocus() ) fiche.isolementPhasePhaseStatorVW =
                    iVW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RU.doAfterTextChanged {
                if (RU.text.isNotEmpty() && RU.hasFocus() ) fiche.resistanceStatorU =
                    RU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RV.doAfterTextChanged {
                if (RV.text.isNotEmpty() && RV.hasFocus() ) fiche.resistanceStatorV =
                    RV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RW.doAfterTextChanged {
                if (RW.text.isNotEmpty() && RW.hasFocus()) fiche.resistanceStatorW =
                    RW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionU.doAfterTextChanged {
                if (tensionU.text.isNotEmpty() && tensionU.hasFocus() ) fiche.tensionU =
                    tensionU.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty() && tensionV.hasFocus() ) fiche.tensionV =
                    tensionV.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() && tensionW.hasFocus() ) fiche.tensionW =
                    tensionW.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhase.doAfterTextChanged {
                if (isolementPhase.text.isNotEmpty() && isolementPhase.hasFocus() ) fiche.isolementPhase =
                    isolementPhase.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            obs.doAfterTextChanged {
                if (obs.text.isNotEmpty()) fiche.observations = obs.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        } else {
            UM.isEnabled = false
            VM.isEnabled = false
            WM.isEnabled = false
            UV.isEnabled = false
            UW.isEnabled = false
            iVW.isEnabled = false
            RU.isEnabled = false
            RV.isEnabled = false
            RW.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            isolementPhase.isEnabled = false
            obs.isEnabled = false
            enr.visibility = View.GONE
            ter.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
            gal.visibility = View.INVISIBLE
        }
        enr.setOnClickListener {
            viewModel.getTime()
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.localSave()
            if (viewModel.isOnline(requireContext()) && viewModel.token !== "") {
                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
            } else {
                val mySnackbar =
                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                mySnackbar.show()
            }
        }
        ter.setOnClickListener {
            layout.findViewById<CardView>(R.id.loadTriD).visibility = View.VISIBLE
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
                                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                            } else {
                                val mySnackbar =
                                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                                mySnackbar.show()
                            }
                        })
                    .setOnDismissListener {
                        layout.findViewById<CardView>(R.id.loadTriD).visibility = View.GONE
                    }
                builder.create()
            }
            alertDialog?.show()
        }
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        gal.setOnClickListener {
            openGalleryForImage()
        }
        btnPhoto.setOnClickListener {
        var test = ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
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
                        startActivityForResult(cameraIntent, viewModel.CAMERA_CAPTURE)
                        //viewModel.addSchema(photoURI)
                    }
                }
            }
        }
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.partMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infoLayout)
            setReorderingAllowed(true)
        }
        // Inflate the layout for this fragment
        return layout
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("info", "resultcode: ${resultCode}")
        if (requestCode == viewModel.CAMERA_CAPTURE){
            if (resultCode == Activity.RESULT_OK) {
                viewModel.addPhoto(currentPhotoPath)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                File(currentPhotoPath).delete()
            }
        }
        if (requestCode == viewModel.GALLERY_CAPTURE) {
            if (resultCode == Activity.RESULT_OK ) {
                var file = viewModel.getRealPathFromURI(data?.data!!)
                CoroutineScope(Dispatchers.IO).launch {
                    var nfile = async { viewModel.sendExternalPicture(file!!) }
                    nfile.await()
                    if (nfile.isCompleted) {
                        var list = viewModel.selection.value?.photos?.toMutableList()
                        list!!.removeAll { it == "" }
                        list.add(nfile.await()!!)
                        viewModel.selection.value?.photos = list?.toTypedArray()
                        viewModel.photos.postValue(list!!)
                        viewModel.localSave()
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("info", "data: ${data}")
            }
        }

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, viewModel.GALLERY_CAPTURE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (storageDir.exists()) {
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + viewModel.selection.value?.photos!!.size, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        } else {
            makeFolder()
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + viewModel.selection.value?.photos!!.size, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }
    }

    fun makeFolder() {
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        storageDir.mkdir()
    }



}