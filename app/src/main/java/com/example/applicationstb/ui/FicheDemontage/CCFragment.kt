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
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.databinding.ActivityMainBinding
import com.example.applicationstb.model.CourantContinu
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        var isopmu = layout.findViewById<EditText>(R.id.isopmU) //induit
        var isopmv = layout.findViewById<EditText>(R.id.isopmV) //pole principal
        var isopmw = layout.findViewById<EditText>(R.id.isopmW) //pole auxilliare
        var isoppU = layout.findViewById<EditText>(R.id.isoppU)// pôle compensatoire
        var isoppV = layout.findViewById<EditText>(R.id.isoppV)// pôle porte balais
        //resistances
        var rU = layout.findViewById<EditText>(R.id.rU)     //résistance Induit
        var rV =  layout.findViewById<EditText>(R.id.rV)    // résistance pôle principal
        var rI = layout.findViewById<EditText>(R.id.RI)     //resistance pôle auxilliaire
        var rPP = layout.findViewById<EditText>(R.id.RPP)   // resistance pôle compensatoire
        // essais dynamiques
        var vU = layout.findViewById<EditText>(R.id.vU)     //tension induit
        var vV = layout.findViewById<EditText>(R.id.vV)     //tension excitation
        var vUI = layout.findViewById<EditText>(R.id.vUI)   //intensité induit
        var vVI = layout.findViewById<EditText>(R.id.vVI)   //intensité excitation
        var enr = layout.findViewById<Button>(R.id.enregistrerCC)
        var ter = layout.findViewById<Button>(R.id.termCC)
        var btnPhoto = layout.findViewById<Button>(R.id.photo4)
        var observations = layout.findViewById<EditText>(R.id.observations)
        var retour = layout.findViewById<Button>(R.id.retourCC)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        var fiche = viewModel.selection.value!! as CourantContinu
            if(fiche.isolementMasseInduit !== null )isopmu.setText(fiche.isolementMasseInduit.toString())
            if(fiche.isolementMassePolesPrincipaux !== null ) isopmv.setText(fiche.isolementMassePolesPrincipaux.toString())  //pole principal
            if(fiche.isolementMassePolesAuxilliaires !== null ) isopmw.setText(fiche.isolementMassePolesAuxilliaires.toString())  //pole auxilliare
            if(fiche.isolementMassePolesCompensatoires !==null) isoppU.setText(fiche.isolementMassePolesCompensatoires.toString()) // pôle compensatoire
            if(fiche.isolementMassePorteBalais !== null) isoppV.setText(fiche.isolementMassePorteBalais.toString()) // pôle porte balais
            //resistances
             if (fiche.resistanceInduit !== null) rU.setText(fiche.resistanceInduit.toString())    //résistance Induit
             if (fiche.resistancePP !== null) rV.setText(fiche.resistancePP.toString())    // résistance pôle principal
             if (fiche.resistancePA !== null) rI.setText(fiche.resistancePA.toString())    //resistance pôle auxilliaire
             if (fiche.resistancePC !== null) rPP.setText(fiche.resistancePC.toString())   // resistance pôle compensatoire
            // essais dynamiques
             if (fiche.tensionInduit !== null) vU.setText(fiche.tensionInduit.toString())    //tension induit
             if (fiche.tensionExcitation !== null) vV.setText(fiche.tensionExcitation.toString())   //tension excitation
             if (fiche.intensiteInduit !== null ) vUI.setText(fiche.intensiteInduit.toString())   //intensité induit
             if (fiche.intensiteExcitation !== null )vVI.setText(fiche.intensiteExcitation.toString())
            if (fiche.observations !== null) observations.setText(fiche.observations.toString())
        viewModel.photos.value = fiche.photos!!.toMutableList()
        if (fiche.status!! < 3L) {
            isopmu.doAfterTextChanged {
                if (isopmu.text.isNotEmpty() && isopmu.hasFocus() && isopmu.text.matches(regexNombres)) {
                    fiche.isolementMasseInduit =
                        isopmu.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmv.doAfterTextChanged {
                if (isopmv.text.isNotEmpty() && isopmv.hasFocus() && isopmv.text.matches(regexNombres)) {
                    fiche.isolementMassePolesPrincipaux =
                        isopmv.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isopmw.doAfterTextChanged {
                if (isopmw.text.isNotEmpty() && isopmw.hasFocus() && isopmw.text.matches(regexNombres)) {
                    fiche.isolementMassePolesAuxilliaires =
                        isopmw.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isoppU.doAfterTextChanged {
                if (isoppU.text.isNotEmpty() && isoppU.hasFocus() && isoppU.text.matches(regexNombres)) {
                    fiche.isolementMassePolesCompensatoires =
                        isoppU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            isoppV.doAfterTextChanged {
                if (isoppV.text.isNotEmpty() && isoppV.hasFocus() && isoppV.text.matches(regexNombres)) {
                    fiche.isolementMassePorteBalais =
                        isoppV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rU.doAfterTextChanged {
                if (rU.text.isNotEmpty() && rU.hasFocus() && rU.text.matches(regexNombres)) {
                    fiche.resistanceInduit = rU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rI.doAfterTextChanged {
                if (rI.text.isNotEmpty() && rI.hasFocus() && rI.text.matches(regexNombres)) {
                    fiche.resistancePA = rI.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rV.doAfterTextChanged {
                if (rV.text.isNotEmpty() && rV.hasFocus() && rV.text.matches(regexNombres)) {
                    fiche.resistancePP = rV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            rPP.doAfterTextChanged {
                if (rPP.text.isNotEmpty() && rPP.hasFocus() && rPP.text.matches(regexNombres)) {
                    fiche.resistancePC = rPP.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            vU.doAfterTextChanged {
                if (vU.text.isNotEmpty() && vU.hasFocus() && vU.text.matches(regexNombres)) {
                    fiche.tensionInduit = vU.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            vV.doAfterTextChanged {
                if (vV.text.isNotEmpty() && vV.hasFocus() && vV.text.matches(regexNombres)) {
                    fiche.tensionExcitation = vV.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            vUI.doAfterTextChanged {
                if (vUI.text.isNotEmpty() && vUI.hasFocus() && vUI.text.matches(regexNombres)) {
                    fiche.intensiteInduit = vUI.text.toString().toFloat()
                    viewModel.selection.value = fiche
                    viewModel.getTime()
                    viewModel.localSave()
                }
            }
            vVI.doAfterTextChanged {
                if (vVI.text.isNotEmpty() && vVI.hasFocus() && vVI.text.matches(regexNombres)) {
                    fiche.intensiteExcitation = vVI.text.toString().toFloat()
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
            isopmu.isEnabled = false
            isopmv.isEnabled = false
            isopmw.isEnabled = false
            isoppU.isEnabled = false
            rU.isEnabled = false
            rI.isEnabled = false
            rV.isEnabled = false
            rPP.isEnabled = false
            vU.isEnabled = false
            vV.isEnabled = false
            vUI.isEnabled = false
            vVI.isEnabled = false
            observations.isEnabled = false
            enr.visibility = View.GONE
            ter.visibility = View.GONE
            btnPhoto.visibility = View.INVISIBLE
        }
        enr.setOnClickListener {
            viewModel.getTime()
            viewModel.selection.value!!.status = 2L
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
        var gal = layout.findViewById<Button>(R.id.g5)
        gal.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 6)
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