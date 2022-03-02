package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.MediaStoreFileLoader
import com.example.applicationstb.R
import com.example.applicationstb.model.Triphase
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TriphaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriphaseFragment : Fragment() {

    val REQUEST_CODE = 100
    private val viewModel: FicheDemontageViewModel by activityViewModels()
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
        var layout = inflater.inflate(R.layout.fragment_triphase, container, false)
        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        var infos = layout.findViewById<FrameLayout>(R.id.infoLayout)
        var btnPhoto = layout.findViewById<Button>(R.id.photo)
        var UM = layout.findViewById<EditText>(R.id.isopmU)
        var VM = layout.findViewById<EditText>(R.id.isopmV)
        var WM = layout.findViewById<EditText>(R.id.isopmW)
        var UV = layout.findViewById<EditText>(R.id.isoppU)
        var UW = layout.findViewById<EditText>(R.id.isoppV)
        var iVW = layout.findViewById<EditText>(R.id.isoppW)
        var RU = layout.findViewById<EditText>(R.id.rU)
        var RV = layout.findViewById<EditText>(R.id.rV)
        var RW = layout.findViewById<EditText>(R.id.rW)
        var VU = layout.findViewById<EditText>(R.id.vU)
        var VV = layout.findViewById<EditText>(R.id.vV)
        var VW = layout.findViewById<EditText>(R.id.vW)
        var VUI = layout.findViewById<EditText>(R.id.vUI)
        var VVI = layout.findViewById<EditText>(R.id.vVI)
        var VWI = layout.findViewById<EditText>(R.id.vWI)
        var dessai = layout.findViewById<EditText>(R.id.tpse)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var enr = layout.findViewById<Button>(R.id.enregistrerTRi)
        var retour = layout.findViewById<Button>(R.id.retourTri)
        var ter = layout.findViewById<Button>(R.id.termTri)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var fiche = viewModel.selection.value!! as Triphase
        if (fiche.isolementPhaseMasseStatorUM !== null) UM.setText(fiche.isolementPhaseMasseStatorUM!!.toString()) else 0
        if (fiche.isolementPhaseMasseStatorVM !== null) VM.setText(fiche.isolementPhaseMasseStatorVM!!.toString()) else 0
        if (fiche.isolementPhaseMasseStatorWM !== null) WM.setText(fiche.isolementPhaseMasseStatorWM!!.toString()) else 0
        if (fiche.isolementPhasePhaseStatorUV !== null) UV.setText(fiche.isolementPhasePhaseStatorUV!!.toString())
        if (fiche.isolementPhasePhaseStatorUW !== null) UW.setText(fiche.isolementPhasePhaseStatorUW!!.toString()) else 0
        if (fiche.isolementPhasePhaseStatorVW !== null) iVW.setText(fiche.isolementPhasePhaseStatorVW!!.toString()) else 0
        if (fiche.resistanceStatorU !== null) RU.setText(fiche.resistanceStatorU!!.toString()) else 0
        if (fiche.resistanceStatorV !== null) RV.setText(fiche.resistanceStatorV!!.toString()) else 0
        if (fiche.resistanceStatorW !== null) RW.setText(fiche.resistanceStatorW!!.toString()) else 0
        if (fiche.tensionU !== null) VU.setText(fiche.tensionU!!.toString()) else 0
        if (fiche.tensionV !== null) VV.setText(fiche.tensionV!!.toString()) else 0
        if (fiche.tensionW !== null) VW.setText(fiche.tensionW!!.toString()) else 0
        if (fiche.intensiteU !== null) VUI.setText(fiche.intensiteU!!.toString()) else 0
        if (fiche.intensiteV !== null) VVI.setText(fiche.intensiteV!!.toString()) else 0
        if (fiche.intensiteW !== null) VWI.setText(fiche.intensiteW!!.toString()) else 0
        if (fiche.dureeEssai !== null) dessai.setText(fiche.dureeEssai!!.toString()) else 0
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
                if (UM.text.isNotEmpty() && UM.hasFocus() && UM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorUM =
                    UM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VM.doAfterTextChanged {
                if (VM.text.isNotEmpty() && VM.hasFocus() && VM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorVM =
                    VM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            WM.doAfterTextChanged {
                if (WM.text.isNotEmpty() && WM.hasFocus() && WM.text.matches(regexNombres)) fiche.isolementPhaseMasseStatorWM =
                    WM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UV.doAfterTextChanged {
                if (UV.text.isNotEmpty() && UV.hasFocus() && UV.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorUV =
                    UV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            UW.doAfterTextChanged {
                if (UW.text.isNotEmpty() && UW.hasFocus() && UW.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorUW =
                    UW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            iVW.doAfterTextChanged {
                if (iVW.text.isNotEmpty() && iVW.hasFocus() && iVW.text.matches(regexNombres)) fiche.isolementPhasePhaseStatorVW =
                    iVW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RU.doAfterTextChanged {
                if (RU.text.isNotEmpty() && RU.hasFocus() && RU.text.matches(regexNombres)) fiche.resistanceStatorU =
                    RU.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RV.doAfterTextChanged {
                if (RV.text.isNotEmpty() && RV.hasFocus() && RV.text.matches(regexNombres)) fiche.resistanceStatorV =
                    RV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            RW.doAfterTextChanged {
                if (RW.text.isNotEmpty() && RW.hasFocus() && RW.text.matches(regexNombres)) fiche.resistanceStatorW =
                    RW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VU.doAfterTextChanged {
                if (VU.text.isNotEmpty() && VU.hasFocus() && VU.text.matches(regexNombres)) fiche.tensionU =
                    VU.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VV.doAfterTextChanged {
                if (VV.text.isNotEmpty() && VV.hasFocus() && VV.text.matches(regexNombres)) fiche.tensionV =
                    VV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VW.doAfterTextChanged {
                if (VW.text.isNotEmpty() && VW.hasFocus() && VW.text.matches(regexNombres)) fiche.tensionW =
                    VW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VUI.doAfterTextChanged {
                if (VUI.text.isNotEmpty() && VUI.hasFocus() && VUI.text.matches(regexNombres)) fiche.intensiteU =
                    VUI.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VVI.doAfterTextChanged {
                if (VVI.text.isNotEmpty() && VVI.hasFocus() && VVI.text.matches(regexNombres)) fiche.intensiteV =
                    VVI.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            VWI.doAfterTextChanged {
                if (VWI.text.isNotEmpty() && VWI.hasFocus() && VWI.text.matches(regexNombres)) fiche.intensiteW =
                    VWI.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            dessai.doAfterTextChanged {
                if (dessai.text.isNotEmpty() && dessai.hasFocus() && dessai.text.matches(
                        regexNombres
                    )
                ) fiche.dureeEssai = dessai.text.toString().toFloat()
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
            VU.isEnabled = false
            VV.isEnabled = false
            VW.isEnabled = false
            VUI.isEnabled = false
            VVI.isEnabled = false
            VWI.isEnabled = false
            dessai.isEnabled = false
            obs.isEnabled = false
            enr.visibility = View.GONE
            ter.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
        }

        enr.setOnClickListener {
            viewModel.getTime()
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.localSave()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getNameURI()
            }
            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        ter.setOnClickListener {
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
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.getNameURI()
                            }
                            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                        })
                builder.create()
            }
            alertDialog?.show()
        }
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        var gal = layout.findViewById<Button>(R.id.gallerie)
        gal.setOnClickListener {
            openGalleryForImage()
        }
        btnPhoto.setOnClickListener {
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Log.i("info", "photo camera: ${currentPhotoPath}")
            viewModel.addPhoto(currentPhotoPath)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var file = viewModel.getRealPathFromURI(data?.data!!)
            //viewModel.sendExternalPicture(File(file!!))
            viewModel.addPhoto(file!!)
        }

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
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

    fun makeFolder() {
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        storageDir.mkdir()
    }



}