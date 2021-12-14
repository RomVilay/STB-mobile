package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
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
import com.example.applicationstb.model.DemontageRotorBobine
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RotorBobineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RotorBobineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private  val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_rotor_bobine, container, false)
        var isolementPhaseMasseStatorUM = layout.findViewById<EditText>(R.id.upm)
        var isolementPhaseMasseStatorVM = layout.findViewById<EditText>(R.id.vpm)
        var isolementPhaseMasseStatorWM = layout.findViewById<EditText>(R.id.wpm)
        var isolementPhaseMasseRotorB1M = layout.findViewById<EditText>(R.id.impRP)
        var isolementPhaseMasseRotorB2M = layout.findViewById<EditText>(R.id.b2)
        var isolementPhaseMasseRotorB3M = layout.findViewById<EditText>(R.id.b3)
        var isolementPhaseMassePorteBalaisM = layout.findViewById<EditText>(R.id.ipmSE)
        var isolementPhasePhaseStatorUV = layout.findViewById<EditText>(R.id.u)
        var isolementPhasePhaseStatorVW = layout.findViewById<EditText>(R.id.v2)
        var isolementPhasePhaseStatorUW = layout.findViewById<EditText>(R.id.w)
        var resistanceStatorU = layout.findViewById<EditText>(R.id.U)
        var resistanceStatorV = layout.findViewById<EditText>(R.id.V)
        var resistanceStatorW = layout.findViewById<EditText>(R.id.W)
        var resistanceRotorB1B2 = layout.findViewById<EditText>(R.id.b1b2)
        var resistanceRotorB2B2 = layout.findViewById<EditText>(R.id.b2b3)
        var resistanceRotorB1B3 = layout.findViewById<EditText>(R.id.b1b3)
        var tensionU = layout.findViewById<EditText>(R.id.tu)
        var tensionV = layout.findViewById<EditText>(R.id.tv)
        var tensionW = layout.findViewById<EditText>(R.id.tw)
        var tensionRotor = layout.findViewById<EditText>(R.id.tr)
        var intensiteU = layout.findViewById<EditText>(R.id.iu)
        var intensiteV = layout.findViewById<EditText>(R.id.iv)
        var intensiteW = layout.findViewById<EditText>(R.id.iw)
        var intensiteRotor = layout.findViewById<EditText>(R.id.irotor)
        var dureeEssai = layout.findViewById<EditText>(R.id.tpse)
        var observations = layout.findViewById<EditText>(R.id.obs2)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var btnPhoto = layout.findViewById<Button>(R.id.photo3)
        var fiche = viewModel.selection.value!! as DemontageRotorBobine
        if (fiche.isolementPhaseMasseStatorUM !== null) isolementPhaseMasseStatorUM.setText(fiche.isolementPhaseMasseStatorUM.toString())
        if (fiche.isolementPhaseMasseStatorVM !== null) isolementPhaseMasseStatorVM.setText(fiche.isolementPhaseMasseStatorVM.toString())
        if (fiche.isolementPhaseMasseStatorWM !== null) isolementPhaseMasseStatorWM.setText(fiche.isolementPhaseMasseStatorWM.toString())
        if (fiche.isolementPhaseMasseRotorB1M !== null) isolementPhaseMasseRotorB1M.setText(fiche.isolementPhaseMasseRotorB1M.toString())
        if (fiche.isolementPhaseMasseRotorB2M !== null) isolementPhaseMasseRotorB2M.setText(fiche.isolementPhaseMasseRotorB2M.toString())
        if (fiche.isolementPhaseMasseRotorB3M !== null) isolementPhaseMasseRotorB3M.setText(fiche.isolementPhaseMasseRotorB3M.toString())
        if (fiche.isolementPhaseMassePorteBalaisM !== null) isolementPhaseMassePorteBalaisM.setText(fiche.isolementPhaseMassePorteBalaisM.toString())
        if (fiche.isolementPhasePhaseStatorUV !== null) isolementPhasePhaseStatorUV.setText(fiche.isolementPhasePhaseStatorUV.toString())
        if (fiche.isolementPhasePhaseStatorVW !== null) isolementPhasePhaseStatorVW.setText(fiche.isolementPhasePhaseStatorVW.toString())
        if (fiche.isolementPhasePhaseStatorUW !== null) isolementPhasePhaseStatorUW.setText(fiche.isolementPhasePhaseStatorUW.toString())
        if (fiche.resistanceStatorU !== null) resistanceStatorU.setText(fiche.resistanceStatorU.toString())
        if (fiche.resistanceStatorV !== null) resistanceStatorV.setText(fiche.resistanceStatorV.toString())
        if (fiche.resistanceStatorW !== null) resistanceStatorW.setText(fiche.resistanceStatorW.toString())
        if (fiche.resistanceRotorB1B2 !== null) resistanceRotorB1B2.setText(fiche.resistanceRotorB1B2.toString())
        if (fiche.resistanceRotorB2B2 !== null) resistanceRotorB2B2.setText(fiche.resistanceRotorB2B2.toString())
        if (fiche.resistanceRotorB1B3 !== null) resistanceRotorB1B3.setText(fiche.resistanceRotorB1B3.toString())
        if (fiche.tensionU !== null) tensionU.setText(fiche.tensionU.toString())
        if (fiche.tensionV !== null) tensionV.setText(fiche.tensionV.toString())
        if (fiche.tensionW !== null) tensionW.setText(fiche.tensionW.toString())
        if (fiche.tensionRotor !== null) tensionRotor.setText(fiche.tensionRotor.toString())
        if (fiche.intensiteU !== null) intensiteU.setText(fiche.intensiteU.toString())
        if (fiche.intensiteV !== null) intensiteV.setText(fiche.intensiteV.toString())
        if (fiche.intensiteW !== null) intensiteW.setText(fiche.intensiteW.toString())
        if (fiche.intensiteRotor !== null) intensiteRotor.setText(fiche.intensiteRotor.toString())
        if (fiche.dureeEssai !== null) dureeEssai.setText(fiche.dureeEssai.toString())
        if (fiche.observations !== null) observations.setText(fiche.observations.toString())
        var enr = layout.findViewById<Button>(R.id.enregistrerTRi)
        var retour = layout.findViewById<Button>(R.id.retourTri)
        var term = layout.findViewById<Button>(R.id.termRB)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto3)
        viewModel.photos.value = fiche.photos!!.toMutableList()
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
        if (fiche.status!! < 3L) {
            isolementPhaseMasseStatorUM.doAfterTextChanged {
                if (isolementPhaseMasseStatorUM.text.isNotEmpty() && isolementPhaseMasseStatorUM.text.matches(regexNombres) && isolementPhaseMasseStatorUM.hasFocus()) fiche.isolementPhaseMasseStatorUM =
                    isolementPhaseMasseStatorUM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMasseStatorVM.doAfterTextChanged {
                if (isolementPhaseMasseStatorVM.text.isNotEmpty()  && isolementPhaseMasseStatorVM.text.matches(regexNombres) && isolementPhaseMasseStatorVM.hasFocus()) fiche.isolementPhaseMasseStatorVM =
                    isolementPhaseMasseStatorVM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMasseStatorWM.doAfterTextChanged {
                if (isolementPhaseMasseStatorWM.text.isNotEmpty()  && isolementPhaseMasseStatorWM.text.matches(regexNombres) && isolementPhaseMasseStatorWM.hasFocus()) fiche.isolementPhaseMasseStatorWM =
                    isolementPhaseMasseStatorWM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMasseRotorB1M.doAfterTextChanged {
                if (isolementPhaseMasseRotorB1M.text.isNotEmpty()  && isolementPhaseMasseRotorB1M.text.matches(regexNombres) && isolementPhaseMasseRotorB1M.hasFocus()) fiche.isolementPhaseMasseRotorB1M =
                    isolementPhaseMasseRotorB1M.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMasseRotorB2M.doAfterTextChanged {
                if (isolementPhaseMasseRotorB2M.text.isNotEmpty() && isolementPhaseMasseRotorB2M.text.matches(regexNombres) && isolementPhaseMasseRotorB2M.hasFocus()) fiche.isolementPhaseMasseRotorB2M =
                    isolementPhaseMasseRotorB2M.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMasseRotorB3M.doAfterTextChanged {
                if (isolementPhaseMasseRotorB3M.text.isNotEmpty() && isolementPhaseMasseRotorB3M.text.matches(regexNombres) && isolementPhaseMasseRotorB3M.hasFocus()) fiche.isolementPhaseMasseRotorB3M =
                    isolementPhaseMasseRotorB3M.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhaseMassePorteBalaisM.doAfterTextChanged {
                if (isolementPhaseMassePorteBalaisM.text.isNotEmpty() && isolementPhaseMassePorteBalaisM.text.matches(regexNombres) && isolementPhaseMassePorteBalaisM.hasFocus()) fiche.isolementPhaseMassePorteBalaisM =
                    isolementPhaseMassePorteBalaisM.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhasePhaseStatorUV.doAfterTextChanged {
                if (isolementPhasePhaseStatorUV.text.isNotEmpty() && isolementPhasePhaseStatorUV.text.matches(regexNombres) && isolementPhasePhaseStatorUV.hasFocus()) fiche.isolementPhasePhaseStatorUV =
                    isolementPhasePhaseStatorUV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhasePhaseStatorUW.doAfterTextChanged {
                if (isolementPhasePhaseStatorUW.text.isNotEmpty() && isolementPhasePhaseStatorUW.text.matches(regexNombres) && isolementPhasePhaseStatorUW.hasFocus()) fiche.isolementPhasePhaseStatorUW =
                    isolementPhasePhaseStatorUW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            isolementPhasePhaseStatorVW.doAfterTextChanged {
                if (isolementPhasePhaseStatorVW.text.isNotEmpty() && isolementPhasePhaseStatorVW.text.matches(regexNombres) && isolementPhasePhaseStatorVW.hasFocus()) fiche.isolementPhasePhaseStatorVW =
                    isolementPhasePhaseStatorVW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceStatorU.doAfterTextChanged {
                if (resistanceStatorU.text.isNotEmpty() && resistanceStatorU.text.matches(regexNombres) && resistanceStatorU.hasFocus()) fiche.resistanceStatorU =
                    resistanceStatorU.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceStatorV.doAfterTextChanged {
                if (resistanceStatorV.text.isNotEmpty() && resistanceStatorV.text.matches(regexNombres) && resistanceStatorV.hasFocus()) fiche.resistanceStatorV =
                    resistanceStatorV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceStatorW.doAfterTextChanged {
                if (resistanceStatorW.text.isNotEmpty() && resistanceStatorW.text.matches(regexNombres) && resistanceStatorW.hasFocus()) fiche.resistanceStatorW =
                    resistanceStatorW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceRotorB1B2.doAfterTextChanged {
                if (resistanceRotorB1B2.text.isNotEmpty() && resistanceRotorB1B2.text.matches(regexNombres) && resistanceRotorB1B2.hasFocus()) fiche.resistanceRotorB1B2 =
                    resistanceRotorB1B2.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceRotorB1B3.doAfterTextChanged {
                if (resistanceRotorB1B3.text.isNotEmpty()&& resistanceRotorB1B3.text.matches(regexNombres) && resistanceRotorB1B3.hasFocus()) fiche.resistanceRotorB1B3 =
                    resistanceRotorB1B3.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            resistanceRotorB2B2.doAfterTextChanged {
                if (resistanceRotorB2B2.text.isNotEmpty() && resistanceRotorB2B2.text.matches(regexNombres) && resistanceRotorB2B2.hasFocus()) fiche.resistanceRotorB2B2 =
                    resistanceRotorB2B2.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionU.doAfterTextChanged {
                if (tensionU.text.isNotEmpty() && tensionU.text.matches(regexNombres) && tensionU.hasFocus()) fiche.tensionU = tensionU.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionV.doAfterTextChanged {
                if (tensionV.text.isNotEmpty()) fiche.tensionV = tensionV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionW.doAfterTextChanged {
                if (tensionW.text.isNotEmpty() && tensionW.text.matches(regexNombres) && tensionW.hasFocus()) fiche.tensionW = tensionW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            tensionRotor.doAfterTextChanged {
                if (tensionRotor.text.isNotEmpty() && tensionRotor.text.matches(regexNombres) && tensionRotor.hasFocus()) fiche.tensionRotor =
                    tensionRotor.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            intensiteU.doAfterTextChanged {
                if (intensiteU.text.isNotEmpty() && intensiteU.text.matches(regexNombres) && intensiteU.hasFocus()) fiche.intensiteU =
                    intensiteU.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            intensiteV.doAfterTextChanged {
                if (intensiteV.text.isNotEmpty() && intensiteV.text.matches(regexNombres) && intensiteV.hasFocus()) fiche.intensiteV =
                    intensiteV.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            intensiteW.doAfterTextChanged {
                if (intensiteW.text.isNotEmpty() && intensiteW.text.matches(regexNombres) && intensiteW.hasFocus()) fiche.intensiteW =
                    intensiteW.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            intensiteRotor.doAfterTextChanged {
                if (intensiteRotor.text.isNotEmpty() && intensiteRotor.text.matches(regexNombres) && intensiteRotor.hasFocus()) fiche.intensiteRotor =
                    intensiteRotor.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            dureeEssai.doAfterTextChanged {
                if (dureeEssai.text.isNotEmpty() && dureeEssai.text.matches(regexNombres) && dureeEssai.hasFocus()) fiche.dureeEssai =
                    dureeEssai.text.toString().toInt()
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
        } else {
            isolementPhaseMasseStatorUM.isEnabled = false
            isolementPhaseMasseStatorVM.isEnabled = false
            isolementPhaseMasseStatorWM.isEnabled = false
            isolementPhaseMasseRotorB1M.isEnabled = false
            isolementPhaseMasseRotorB2M.isEnabled = false
            isolementPhaseMasseRotorB3M.isEnabled = false
            isolementPhaseMassePorteBalaisM.isEnabled = false
            isolementPhasePhaseStatorUV.isEnabled = false
            isolementPhasePhaseStatorUW.isEnabled = false
            isolementPhasePhaseStatorVW.isEnabled = false
            resistanceStatorU.isEnabled = false
            resistanceStatorV.isEnabled = false
            resistanceStatorW.isEnabled = false
            resistanceRotorB1B2.isEnabled = false
            resistanceRotorB1B3.isEnabled = false
            resistanceRotorB2B2.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            tensionRotor.isEnabled = false
            intensiteU.isEnabled = false
            intensiteV.isEnabled = false
            intensiteW.isEnabled = false
            intensiteRotor.isEnabled = false
            dureeEssai.isEnabled = false
            observations.isEnabled = false
            enr.visibility = View.GONE
            term.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
        }
        enr.setOnClickListener {
            fiche.status = 2L
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.enregistrer(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
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
                            viewModel.enregistrer(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                        })
                builder.create()
            }
            alertDialog?.show()
        }
        retour.setOnClickListener {
            if (viewModel.selection.value?.status == 3L){
                activity?.onBackPressed()
            } else {
                viewModel.retour(layout)
            }
        }
        // Inflate the layout for this fragment
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
            viewModel.addPhoto(currentPhotoPath)
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