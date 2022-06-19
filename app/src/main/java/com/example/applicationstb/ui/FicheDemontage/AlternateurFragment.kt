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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
class AlternateurFragment : Fragment() {

    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos:RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_alternateur, container, false)
        var isolementMasseStatorPrincipalU = layout.findViewById<EditText>(R.id.upm)
        var isolementMasseStatorPrincipalV = layout.findViewById<EditText>(R.id.vpm)
        var isolementMasseStatorPrincipalW	 = layout.findViewById<EditText>(R.id.wpm)
        var isolementMasseRotorPrincipal	 = layout.findViewById<EditText>(R.id.impRP)
        var isolementMasseStatorExcitation	 = layout.findViewById<EditText>(R.id.ipmSE)
        var isolementMasseRotorExcitation	 = layout.findViewById<EditText>(R.id.ipmRE)
        var resistanceRotorPrincipal	 = layout.findViewById<EditText>(R.id.RRP)
        var resistanceStatorExcitation	 = layout.findViewById<EditText>(R.id.RSE)
        var resistanceRotorExcitation	 = layout.findViewById<EditText>(R.id.RRE)
        var testDiode  = layout.findViewById<Switch>(R.id.switchDiode)
        var tensionU	 = layout.findViewById<EditText>(R.id.u)
        var tensionV	 = layout.findViewById<EditText>(R.id.v)
        var tensionW	 = layout.findViewById<EditText>(R.id.w)
        var isolementPhases = layout.findViewById<EditText>(R.id.isoP)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var regexNombres = Regex("/[+-]?([0-9]*[.])?[0-9]+/")
        var regexInt = Regex("^\\d+")
        var fiche = viewModel.selection.value!!
        viewModel.photos.value = fiche.photos!!.toMutableList()
        if (fiche.isolementMasseStatorPrincipalU !== null) isolementMasseStatorPrincipalU.setText(fiche.isolementMasseStatorPrincipalU.toString())
        if (fiche.isolementMasseStatorPrincipalV !== null) isolementMasseStatorPrincipalV.setText(fiche.isolementMasseStatorPrincipalV.toString())
        if (fiche.isolementMasseStatorPrincipalW !== null) isolementMasseStatorPrincipalW.setText(fiche.isolementMasseStatorPrincipalW.toString())
        if (fiche.isolementMasseRotorPrincipal !== null) isolementMasseRotorPrincipal.setText(fiche.isolementMasseRotorPrincipal.toString())
        if (fiche.isolementMasseStatorExcitation !== null) isolementMasseStatorExcitation.setText(fiche.isolementMasseStatorExcitation.toString())
        if (fiche.isolementMasseRotorExcitation !== null) isolementMasseRotorExcitation.setText(fiche.isolementMasseRotorExcitation.toString())
        if (fiche.resistanceRotorPrincipal !== null) resistanceRotorPrincipal.setText(fiche.resistanceRotorPrincipal.toString())
        if (fiche.resistanceRotorExcitation !== null) resistanceRotorExcitation.setText(fiche.resistanceRotorExcitation.toString())
        if (fiche.resistanceStatorExcitation !== null) resistanceStatorExcitation.setText(fiche.resistanceStatorExcitation.toString())
        if (fiche.testDiode !== null) testDiode.setChecked(fiche.testDiode!!)
        if (fiche.tensionU !== null) tensionU.setText(fiche.tensionU.toString())
        if (fiche.tensionV !== null) tensionV.setText(fiche.tensionV.toString())
        if (fiche.tensionW !== null) tensionW.setText(fiche.tensionW.toString())
        if (fiche.isolementPhase !== null) isolementPhases.setText(fiche.isolementPhase.toString())
        if (fiche.observations !== null) obs.setText(fiche.observations!!.toString())
        if (fiche.status!! < 3L) {
            isolementMasseStatorPrincipalU.doAfterTextChanged {
                if (isolementMasseStatorPrincipalU.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus() ) {
                    fiche.isolementMasseStatorPrincipalU =
                        isolementMasseStatorPrincipalU.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorPrincipalV.doAfterTextChanged {
                if (isolementMasseStatorPrincipalV.text.isNotEmpty() && isolementMasseStatorPrincipalV.hasFocus() ) {
                    fiche.isolementMasseStatorPrincipalV =
                        isolementMasseStatorPrincipalV.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorPrincipalW.doAfterTextChanged {
                if (isolementMasseStatorPrincipalW.text.isNotEmpty() && isolementMasseStatorPrincipalW.hasFocus() ) {
                    fiche.isolementMasseStatorPrincipalW =
                        isolementMasseStatorPrincipalW.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorExcitation.doAfterTextChanged {
                if (isolementMasseStatorExcitation.text.isNotEmpty() && isolementMasseStatorExcitation.hasFocus() ) {
                    fiche.isolementMasseStatorExcitation =
                        isolementMasseStatorExcitation.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseRotorPrincipal.doAfterTextChanged {
                if (isolementMasseRotorPrincipal.text.isNotEmpty() && isolementMasseRotorPrincipal.hasFocus()  ) {
                    fiche.isolementMasseRotorPrincipal =
                        isolementMasseRotorPrincipal.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseRotorExcitation.doAfterTextChanged {
                if (isolementMasseRotorExcitation.text.isNotEmpty() && isolementMasseRotorExcitation.hasFocus() ) {
                    fiche.isolementMasseRotorExcitation =
                        isolementMasseRotorExcitation.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceRotorPrincipal.doAfterTextChanged {
                if (resistanceRotorPrincipal.text.isNotEmpty() && resistanceRotorPrincipal.hasFocus() ) {
                    fiche.resistanceRotorPrincipal =
                        resistanceRotorPrincipal.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceStatorExcitation.doAfterTextChanged {
                if (resistanceStatorExcitation.text.isNotEmpty() && resistanceStatorExcitation.hasFocus() ) {
                    fiche.resistanceStatorExcitation =
                        resistanceStatorExcitation.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceRotorExcitation.doAfterTextChanged {
                if (resistanceRotorExcitation.text.isNotEmpty() && resistanceRotorExcitation.hasFocus() ) {
                    fiche.resistanceRotorExcitation =
                        resistanceRotorExcitation.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            testDiode.setOnCheckedChangeListener { _, isChecked ->
                fiche.testDiode = isChecked
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionU.doAfterTextChanged {
                if (tensionU.text.isNotEmpty() && tensionU.hasFocus()) {
                    fiche.tensionU = tensionU.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty() && tensionV.hasFocus() ) {
                    fiche.tensionV = tensionV.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() && tensionW.hasFocus() ) {
                    fiche.tensionW = tensionW.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementPhases.doAfterTextChanged {
                if (isolementPhases.text.isNotEmpty() && isolementPhases.hasFocus() ) {
                    fiche.isolementPhase =
                        isolementPhases.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            obs.doAfterTextChanged {
                if (obs.hasFocus()) {
                    fiche.observations = obs.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
        } else{
            isolementMasseStatorPrincipalU.isEnabled = false
            isolementMasseStatorPrincipalV.isEnabled = false
            isolementMasseStatorPrincipalW.isEnabled = false
            isolementMasseStatorExcitation.isEnabled = false
            isolementMasseRotorPrincipal.isEnabled = false
            isolementMasseRotorExcitation.isEnabled = false
            testDiode.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            obs.isEnabled = false
        }

        var term = layout.findViewById<Button>(R.id.termA)

        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var retour = layout.findViewById<Button>(R.id.retourAlt)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerAlt)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
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
        if (fiche.photos !== null) sAdapter.update(viewModel.photos.value!!)
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
        var gal = layout.findViewById<Button>(R.id.g4)
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 6)
        }

        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.localSave()
            if (viewModel.isOnline(requireContext())) {
                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
            } else {
                val mySnackbar =
                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                mySnackbar.show()
            }
        }
        term.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Terminer une fiche")
                    .setMessage("Êtes vous sûr de vouloir terminer la fiche? elle ne sera plus modifiable après")
                    .setPositiveButton("Terminer",
                        DialogInterface.OnClickListener { dialog, id ->
                            fiche.status = 3L
                            viewModel.selection.value = fiche
                            viewModel.getTime()
                            viewModel.localSave()
                            if (viewModel.isOnline(requireContext())) {
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


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            //val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
            viewModel.addPhoto(currentPhotoPath)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 6) {
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