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

/**
 * A simple [Fragment] subclass.
 * Use the [CCFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CCFragment : Fragment() {
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos: RecyclerView
    private  val PHOTO_RESULT = 1888
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
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_c_c, container, false)
        //isolement phase/masse
        var isopmI = layout.findViewById<EditText>(R.id.isopmI) //induit
        var isopmPP = layout.findViewById<EditText>(R.id.isopmPP) //pole principal
        var isopmPA = layout.findViewById<EditText>(R.id.isopmPA) //pole auxilliare
        var isopmPC = layout.findViewById<EditText>(R.id.isoppPC)// pôle compensatoire
        var isopmPB = layout.findViewById<EditText>(R.id.isoppPB)// pôle porte balais
        //resistances
        var rI = layout.findViewById<EditText>(R.id.rInduit)     //résistance Induit
        var rPP =  layout.findViewById<EditText>(R.id.rPP)    // résistance pôle principal
        var rPA = layout.findViewById<EditText>(R.id.rPA)     //resistance pôle auxilliaire
        var rPC = layout.findViewById<EditText>(R.id.rPC)   // resistance pôle compensatoire
        // essais dynamiques
        var tensionU = layout.findViewById<EditText>(R.id.u)     //tension u
        var tensionV = layout.findViewById<EditText>(R.id.v)     //tension V
        var tensionW = layout.findViewById<EditText>(R.id.w)   //tension W
        var isoPM = layout.findViewById<EditText>(R.id.isoPM)   // isolement phase masse
        var enr = layout.findViewById<Button>(R.id.enregistrerCC)
        var ter = layout.findViewById<Button>(R.id.termCC)
        var btnPhoto = layout.findViewById<Button>(R.id.photo4)
        var observations = layout.findViewById<EditText>(R.id.observations)
        var gal = layout.findViewById<Button>(R.id.g5)
        var retour = layout.findViewById<Button>(R.id.retourCC)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        var fiche = viewModel.selection.value!!
            if(fiche.isolementMasseInduit !== null )isopmI.setText(fiche.isolementMasseInduit.toString())
            if(fiche.isolementMassePolesPrincipaux !== null ) isopmPP.setText(fiche.isolementMassePolesPrincipaux.toString())  //pole principal
            if(fiche.isolementMassePolesAuxilliaires !== null ) isopmPA.setText(fiche.isolementMassePolesAuxilliaires.toString())  //pole auxilliare
            if(fiche.isolementMassePolesCompensatoires !==null) isopmPC.setText(fiche.isolementMassePolesCompensatoires.toString()) // pôle compensatoire
            if(fiche.isolementMassePorteBalais !== null) isopmPB.setText(fiche.isolementMassePorteBalais.toString()) // pôle porte balais
            //resistances
             if (fiche.resistanceInduit !== null) rI.setText(fiche.resistanceInduit.toString())    //résistance Induit
             if (fiche.resistancePP !== null) rPP.setText(fiche.resistancePP.toString())    // résistance pôle principal
             if (fiche.resistancePA !== null) rPA.setText(fiche.resistancePA.toString())    //resistance pôle auxilliaire
             if (fiche.resistancePC !== null) rPC.setText(fiche.resistancePC.toString())   // resistance pôle compensatoire
             if (fiche.tensionU !== null ) tensionU.setText(fiche.tensionU)
             if (fiche.tensionV !== null ) tensionV.setText(fiche.tensionV)
             if (fiche.tensionW !== null ) tensionW.setText(fiche.tensionW)
            // essais dynamiques
            if (fiche.isolementPhase !== null) isoPM.setText(fiche.isolementPhase)
            if (fiche.observations !== null) observations.setText(fiche.observations.toString())
        viewModel.photos.value = fiche.photos!!.toMutableList()
        if (fiche.status!! < 3L) {
            isopmI.doAfterTextChanged {
                if (isopmI.text.isNotEmpty() && isopmI.hasFocus()) {
                    fiche.isolementMasseInduit =
                        isopmI.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmPP.doAfterTextChanged {
                if (isopmPP.text.isNotEmpty() && isopmPP.hasFocus() ) {
                    fiche.isolementMassePolesPrincipaux =
                        isopmPP.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmPA.doAfterTextChanged {
                if (isopmPA.text.isNotEmpty() && isopmPA.hasFocus() ) {
                    fiche.isolementMassePolesAuxilliaires =
                        isopmPA.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmPC.doAfterTextChanged {
                if (isopmPC.text.isNotEmpty() && isopmPC.hasFocus() ) {
                    fiche.isolementMassePolesCompensatoires =
                        isopmPC.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmPB.doAfterTextChanged {
                if (isopmPB.text.isNotEmpty() && isopmPB.hasFocus() ) {
                    fiche.isolementMassePorteBalais =
                        isopmPB.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rI.doAfterTextChanged {
                if (rI.text.isNotEmpty() && rI.hasFocus()) {
                    fiche.resistanceInduit = rI.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rPA.doAfterTextChanged {
                if (rPA.text.isNotEmpty() && rPA.hasFocus()) {
                    fiche.resistancePA = rPA.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rPP.doAfterTextChanged {
                if (rPP.text.isNotEmpty() && rPP.hasFocus() ) {
                    fiche.resistancePP = rPP.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rPC.doAfterTextChanged {
                if (rPC.text.isNotEmpty() && rPC.hasFocus() ) {
                    fiche.resistancePC = rPC.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isoPM.doAfterTextChanged {
                if (isoPM.text.isNotEmpty() && isoPM.hasFocus() ) {
                    fiche.isolementPhase = isoPM.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionU.doAfterTextChanged {
                if (tensionU.hasFocus()) {
                    fiche.tensionU = tensionU.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionV.doAfterTextChanged {
                if (tensionV.hasFocus()) {
                    fiche.tensionV = tensionV.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            tensionW.doAfterTextChanged {
                if (tensionW.hasFocus()) {
                    fiche.tensionW = tensionW.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            observations.doAfterTextChanged {
                if (observations.hasFocus()) {
                    fiche.observations = observations.text.toString()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
        } else {
            isopmI.isEnabled = false
            isopmPP.isEnabled = false
            isopmPA.isEnabled = false
            isopmPC.isEnabled = false
            isopmPB.isEnabled = false
            rI.isEnabled = false
            rPP.isEnabled = false
            rPA.isEnabled = false
            rPC.isEnabled = false
            tensionU.isEnabled = false
            tensionV.isEnabled = false
            tensionW.isEnabled = false
            isoPM.isEnabled = false
            gal.visibility = View.INVISIBLE
            observations.isEnabled = false
            enr.visibility = View.GONE
            ter.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
        }
        enr.setOnClickListener {
            viewModel.getTime()
            viewModel.selection.value!!.status = 2L
            viewModel.localSave()
            if (viewModel.isOnline(requireContext()) && viewModel.token !== "") {
                viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
            } else {
                val mySnackbar =
                    Snackbar.make(layout, "fiche enregistrée localement", 3600)
                mySnackbar.show()
            }
        }
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, viewModel.GALLERY_CAPTURE)
        }
        ter.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Terminer une fiche")
                    .setMessage("Êtes vous sûr de vouloir terminer la fiche? elle ne sera plus modifiable après")
                    .setPositiveButton("Terminer",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.getTime()
                            viewModel.selection.value!!.status = 3L
                            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                        })
                builder.create()
            }
                alertDialog?.show()
        }
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto3)
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
            photos.visibility == View.VISIBLE
        })
        if (fiche.photos !== null) sAdapter.update(viewModel.photos.value!!)

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
                        startActivityForResult(cameraIntent,viewModel.CAMERA_CAPTURE)
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
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        return layout
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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