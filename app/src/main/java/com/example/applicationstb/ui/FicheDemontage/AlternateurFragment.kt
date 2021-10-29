package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.content.Intent
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
import com.example.applicationstb.model.DemontageAlternateur
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


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_alternateur, container, false)
        var isolementMasseStatorPrincipalU = layout.findViewById<EditText>(R.id.upm)
        var isolementMasseStatorPrincipalV = layout.findViewById<EditText>(R.id.vpm)
        var isolementMasseStatorPrincipalW	 = layout.findViewById<EditText>(R.id.wpm)
        var isolementMasseRotorPrincipal	 = layout.findViewById<EditText>(R.id.impRP)
        var isolementMasseStatorExcitation	 = layout.findViewById<EditText>(R.id.ipmSE)
        var isolementMasseRotorExcitation	 = layout.findViewById<EditText>(R.id.ipmSE3)
        var resistanceStatorPrincipalU	 = layout.findViewById<EditText>(R.id.U)
        var resistanceStatorPrincipalV	 = layout.findViewById<EditText>(R.id.V)
        var resistanceStatorPrincipalW	 = layout.findViewById<EditText>(R.id.W)
        var resistanceRotorPrincipal	 = layout.findViewById<EditText>(R.id.b1b3)
        var resistanceStatorExcitation	 = layout.findViewById<EditText>(R.id.b1b2)
        var resistanceRotorExcitation	 = layout.findViewById<EditText>(R.id.b2b3)
        var isolementPhasePhaseStatorPrincipalUV	 = layout.findViewById<EditText>(R.id.u)
        var isolementPhasePhaseStatorPrincipalVW	 = layout.findViewById<EditText>(R.id.v2)
        var isolementPhasePhaseStatorPrincipalUW	 = layout.findViewById<EditText>(R.id.w)
        var testDiode  = layout.findViewById<Switch>(R.id.switchDiode)
        var tensionU	 = layout.findViewById<EditText>(R.id.tu)
        var tensionV	 = layout.findViewById<EditText>(R.id.tv)
        var tensionW	 = layout.findViewById<EditText>(R.id.tw)
        var intensiteU	 = layout.findViewById<EditText>(R.id.iu)
        var intensiteV	 = layout.findViewById<EditText>(R.id.iv)
        var intensiteW	 = layout.findViewById<EditText>(R.id.iw)
        var tensionUExcitation	 = layout.findViewById<EditText>(R.id.tw2)
        var tensionVExcitation	 = layout.findViewById<EditText>(R.id.tw3)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var fiche = viewModel.selection.value!! as DemontageAlternateur
        if (fiche.isolementMasseStatorPrincipalU !== null) isolementMasseStatorPrincipalU.setText(fiche.isolementMasseStatorPrincipalU.toString())
        if (fiche.isolementMasseStatorPrincipalV !== null) isolementMasseStatorPrincipalV.setText(fiche.isolementMasseStatorPrincipalV.toString())
        if (fiche.isolementMasseStatorPrincipalW !== null) isolementMasseStatorPrincipalW.setText(fiche.isolementMasseStatorPrincipalW.toString())
        if (fiche.isolementMasseRotorPrincipal !== null) isolementMasseRotorPrincipal.setText(fiche.isolementMasseRotorPrincipal.toString())
        if (fiche.isolementMasseStatorExcitation !== null) isolementMasseStatorExcitation.setText(fiche.isolementMasseStatorExcitation.toString())
        if (fiche.isolementMasseRotorExcitation !== null) isolementMasseRotorExcitation.setText(fiche.isolementMasseRotorExcitation.toString())
        if (fiche.resistanceStatorPrincipalU !== null) resistanceStatorPrincipalU.setText(fiche.resistanceStatorPrincipalU.toString())
        if (fiche.resistanceStatorPrincipalV !== null) resistanceStatorPrincipalV.setText(fiche.resistanceStatorPrincipalV.toString())
        if (fiche.resistanceStatorPrincipalW !== null) resistanceStatorPrincipalW.setText(fiche.resistanceStatorPrincipalW.toString())
        if (fiche.resistanceRotorPrincipal !== null) resistanceRotorPrincipal.setText(fiche.resistanceRotorPrincipal.toString())
        if (fiche.resistanceRotorExcitation !== null) resistanceRotorExcitation.setText(fiche.resistanceRotorExcitation.toString())
        if (fiche.resistanceStatorExcitation !== null) resistanceStatorExcitation.setText(fiche.resistanceStatorExcitation.toString())
        if (fiche.isolementPhasePhaseStatorPrincipalUV !== null) isolementPhasePhaseStatorPrincipalUV.setText(fiche.isolementPhasePhaseStatorPrincipalUV.toString())
        if (fiche.isolementPhasePhaseStatorPrincipalVW !== null) isolementPhasePhaseStatorPrincipalVW.setText(fiche.isolementPhasePhaseStatorPrincipalVW.toString())
        if (fiche.isolementPhasePhaseStatorPrincipalUW !== null) isolementPhasePhaseStatorPrincipalUW.setText(fiche.isolementPhasePhaseStatorPrincipalUW.toString())
        if (fiche.testDiode !== null) testDiode.setChecked(fiche.testDiode!!)
        if (fiche.tensionU !== null) tensionU.setText(fiche.tensionU.toString())
        if (fiche.tensionV !== null) tensionV.setText(fiche.tensionV.toString())
        if (fiche.tensionW !== null) tensionW.setText(fiche.tensionW.toString())
        if (fiche.intensiteU !== null) intensiteU.setText(fiche.intensiteU.toString())
        if (fiche.intensiteV !== null) intensiteV.setText(fiche.intensiteV.toString())
        if (fiche.intensiteW !== null) intensiteW.setText(fiche.intensiteW.toString())
        if (fiche.tensionExcitation !== null) tensionUExcitation.setText(fiche.tensionExcitation.toString())
        if (fiche.intensiteExcitation !== null) tensionVExcitation.setText(fiche.intensiteExcitation.toString())
        if (fiche.observations !== null) obs.setText(fiche.observations!!.toString())
        if (fiche.status!! < 3L) {
            isolementMasseStatorPrincipalU.doAfterTextChanged {
                if (isolementMasseStatorPrincipalU.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus()) {
                    fiche.isolementMasseStatorPrincipalU =
                        isolementMasseStatorPrincipalU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorPrincipalV.doAfterTextChanged {
                if (isolementMasseStatorPrincipalV.text.isNotEmpty() && isolementMasseStatorPrincipalV.hasFocus()) {
                    fiche.isolementMasseStatorPrincipalV =
                        isolementMasseStatorPrincipalV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorPrincipalW.doAfterTextChanged {
                if (isolementMasseStatorPrincipalW.text.isNotEmpty() && isolementMasseStatorPrincipalW.hasFocus()) {
                    fiche.isolementMasseStatorPrincipalW =
                        isolementMasseStatorPrincipalW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseStatorExcitation.doAfterTextChanged {
                if (isolementMasseStatorExcitation.text.isNotEmpty() && isolementMasseStatorExcitation.hasFocus()) {
                    fiche.isolementMasseStatorExcitation =
                        isolementMasseStatorExcitation.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseRotorPrincipal.doAfterTextChanged {
                if (isolementMasseRotorPrincipal.text.isNotEmpty() && isolementMasseRotorPrincipal.hasFocus()) {
                    fiche.isolementMasseRotorPrincipal =
                        isolementMasseRotorPrincipal.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementMasseRotorExcitation.doAfterTextChanged {
                if (isolementMasseRotorExcitation.text.isNotEmpty() && isolementMasseRotorExcitation.hasFocus()) {
                    fiche.isolementMasseRotorExcitation =
                        isolementMasseRotorExcitation.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceStatorPrincipalU.doAfterTextChanged {
                if (resistanceStatorPrincipalU.text.isNotEmpty() && resistanceStatorPrincipalU.hasFocus()) {
                    fiche.resistanceStatorPrincipalU =
                        resistanceStatorPrincipalU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceStatorPrincipalV.doAfterTextChanged {
                if (resistanceStatorPrincipalV.text.isNotEmpty() && resistanceStatorPrincipalV.hasFocus()) {
                    fiche.resistanceStatorPrincipalV =
                        resistanceStatorPrincipalV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceStatorPrincipalW.doAfterTextChanged {
                if (resistanceStatorPrincipalW.text.isNotEmpty() && resistanceStatorPrincipalW.hasFocus()) {
                    fiche.resistanceStatorPrincipalW =
                        resistanceStatorPrincipalW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceRotorPrincipal.doAfterTextChanged {
                if (resistanceRotorPrincipal.text.isNotEmpty() && resistanceRotorPrincipal.hasFocus()) {
                    fiche.resistanceRotorPrincipal =
                        resistanceRotorPrincipal.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceStatorExcitation.doAfterTextChanged {
                if (resistanceStatorExcitation.text.isNotEmpty() && resistanceStatorExcitation.hasFocus()) {
                    fiche.resistanceStatorExcitation =
                        resistanceStatorExcitation.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            resistanceRotorExcitation.doAfterTextChanged {
                if (resistanceRotorExcitation.text.isNotEmpty() && resistanceRotorExcitation.hasFocus()) {
                    fiche.resistanceRotorExcitation =
                        resistanceRotorExcitation.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementPhasePhaseStatorPrincipalUV.doAfterTextChanged {
                if (isolementPhasePhaseStatorPrincipalUV.text.isNotEmpty() && isolementPhasePhaseStatorPrincipalUV.hasFocus()) {
                    fiche.isolementPhasePhaseStatorPrincipalUV =
                        isolementPhasePhaseStatorPrincipalUV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementPhasePhaseStatorPrincipalUW.doAfterTextChanged {
                if (isolementPhasePhaseStatorPrincipalUW.text.isNotEmpty() && isolementPhasePhaseStatorPrincipalUW.hasFocus()) {
                    fiche.isolementPhasePhaseStatorPrincipalUW =
                        isolementPhasePhaseStatorPrincipalUW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isolementPhasePhaseStatorPrincipalVW.doAfterTextChanged {
                if (isolementPhasePhaseStatorPrincipalVW.text.isNotEmpty() && isolementPhasePhaseStatorPrincipalVW.hasFocus()) {
                    fiche.isolementPhasePhaseStatorPrincipalVW =
                        isolementPhasePhaseStatorPrincipalVW.text.toString().toFloat()
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
                    fiche.tensionU = tensionU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty() && tensionV.hasFocus()) {
                    fiche.tensionV = tensionV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() && tensionW.hasFocus()) {
                    fiche.tensionW = tensionW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            intensiteU.doAfterTextChanged {
                if (intensiteU.text.isNotEmpty() && intensiteU.hasFocus()) {
                    fiche.intensiteU = intensiteU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            intensiteV.doAfterTextChanged {
                if (intensiteV.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus()) {
                    fiche.intensiteV = intensiteV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            intensiteW.doAfterTextChanged {
                if (intensiteW.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus()) {
                    fiche.intensiteW = intensiteW.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionUExcitation.doAfterTextChanged {
                if (tensionUExcitation.text.isNotEmpty() && isolementMasseStatorPrincipalU.hasFocus()) {
                    fiche.tensionExcitation = tensionUExcitation.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionVExcitation.doAfterTextChanged {
                if (tensionVExcitation.text.isNotEmpty() && tensionVExcitation.hasFocus()) fiche.intensiteExcitation =
                    tensionVExcitation.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
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
            resistanceStatorPrincipalU.isEnabled = false
            resistanceStatorPrincipalV.isEnabled = false
            resistanceStatorPrincipalW.isEnabled = false
            resistanceRotorPrincipal.isEnabled = false
            resistanceStatorExcitation.isEnabled = false
            resistanceRotorExcitation.isEnabled = false
            isolementPhasePhaseStatorPrincipalUV.isEnabled = false
            isolementPhasePhaseStatorPrincipalUW.isEnabled = false
            isolementPhasePhaseStatorPrincipalVW.isEnabled = false
            testDiode.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            intensiteU.isEnabled = false
            intensiteV.isEnabled = false
            intensiteW.isEnabled = false
            tensionUExcitation.isEnabled = false
            tensionVExcitation.isEnabled = false
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
            viewModel.fullScreen(layout,item.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })
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

        retour.setOnClickListener {
            if (viewModel.selection.value?.status == 3L){
                activity?.onBackPressed()
            } else {
                viewModel.back(layout)
            }
        }
        enregistrer.setOnClickListener {
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.enregistrer(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        term.setOnClickListener {
            fiche.status = 3L
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.enregistrer(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
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
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (storageDir.exists()) {
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
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
            }
        }
    }
    fun makeFolder(){
        val storageDir: File = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES+"/test_pictures")
        storageDir.mkdir()
    }
}