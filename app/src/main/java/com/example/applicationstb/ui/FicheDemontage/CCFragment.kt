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
import android.widget.EditText
import android.widget.FrameLayout
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
        var retour = layout.findViewById<Button>(R.id.retourCC)
        retour.setOnClickListener {
            viewModel.back(layout)
        }
       /* if (viewModel.selection.value.javaClass is CourantContinu){
            var fiche: CourantContinu? = viewModel.selection.value as CourantContinu
            isopmu.setText(fiche.isoMass[0])
            isopmv.setText(fiche.isoMass[1])
            isopmw.setText(fiche.isoMass[2])

        }*/
        var fiche = viewModel.selection.value!! as CourantContinu
        viewModel.selection.observe(viewLifecycleOwner, {
            if(fiche.isolationMasseInduit !== null )isopmu.setText(fiche.isolationMasseInduit.toString())
            if(fiche.isolationMassePolesPrincipaux !== null ) isopmv.setText(fiche.isolationMassePolesPrincipaux.toString())  //pole principal
            if(fiche.isolationMassePolesAuxilliaires !== null ) isopmw.setText(fiche.isolationMassePolesAuxilliaires.toString())  //pole auxilliare
            if(fiche.isolationMassePolesCompensatoires !==null) isoppU.setText(fiche.isolationMassePolesCompensatoires.toString()) // pôle compensatoire
            if(fiche.isolationMassePorteBalais !== null) isoppV.setText(fiche.isolationMassePorteBalais.toString()) // pôle porte balais
            //resistances
             if (fiche.resistanceInduit !== null) rU.setText(fiche.resistanceInduit.toString())    //résistance Induit
             if (fiche.resistancePP !== null) rV.setText(fiche.resistancePP.toString())    // résistance pôle principal
             if (fiche.resistancePA !== null) rI.setText(fiche.resistancePA.toString())    //resistance pôle auxilliaire
             if (fiche.resistancePC !== null) rPP.setText(fiche.resistancePC.toString())   // resistance pôle compensatoire
            // essais dynamiques
             if (fiche.tensionInduit !== null) vU.setText(fiche.tensionInduit.toString())    //tension induit
             if (fiche.tensionExcitation !== null) vV.setText(fiche.tensionExcitation.toString())   //tension excitation
             if (fiche.intensiteInduit !== null ) vUI.setText(fiche.intensiteInduit.toString())   //intensité induit
             if (fiche.intensiteExcitation != null )vVI.setText(fiche.intensiteExcitation.toString())
        })
        isopmu.doAfterTextChanged {
            fiche.isolationMasseInduit = isopmu.text.toString().toInt()
        }
        isopmv.doAfterTextChanged {
            fiche.isolationMassePolesPrincipaux = isopmv.text.toString().toInt()
        }
        isopmw.doAfterTextChanged {
            fiche.isolationMassePolesAuxilliaires = isopmw.text.toString().toInt()
        }
        isoppU.doAfterTextChanged {
            fiche.isolationMassePolesCompensatoires = isoppU.text.toString().toInt()
        }
        isoppV.doAfterTextChanged {
            fiche.isolationMassePorteBalais = isoppV.text.toString().toInt()
        }
         rU.doAfterTextChanged {
            fiche.resistanceInduit = rU.text.toString().toInt()
        }
        rI.doAfterTextChanged {
            fiche.resistancePA = rI.text.toString().toInt()
        }
        rV.doAfterTextChanged {
            fiche.resistancePP = rV.text.toString().toInt()
        }
        rPP.doAfterTextChanged {
            fiche.resistancePC = rPP.text.toString().toInt()
        }
        vU.doAfterTextChanged {
            fiche.tensionInduit = vU.text.toString().toInt()
        }
        vV.doAfterTextChanged {
            fiche.tensionExcitation = vV.text.toString().toInt()
        }
        vUI.doAfterTextChanged {
            fiche.intensiteInduit = vUI.text.toString().toInt()
        }
        vVI.doAfterTextChanged {
            fiche.intensiteExcitation = vVI.text.toString().toInt()
        }
        enr.setOnClickListener {
            if (viewModel.selection.value!!.dureeTotale !== null) {
                viewModel.selection.value!!.dureeTotale =
                    ( Date().time -  viewModel.start.value!!.time ) + viewModel.selection.value!!.dureeTotale!!
            } else {
                viewModel.selection.value!!.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            viewModel.selection.value!!.status = 2L
            viewModel.enregistrer(activity!!.findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        ter.setOnClickListener {
            if (viewModel.selection.value!!.dureeTotale !== null) {
                viewModel.selection.value!!.dureeTotale =
                    ( Date().time -  viewModel.start.value!!.time ) + viewModel.selection.value!!.dureeTotale!!
            } else {
                viewModel.selection.value!!.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            viewModel.selection.value!!.status = 3L
            viewModel.enregistrer(activity!!.findViewById<CoordinatorLayout>(R.id.demoLayout))
        }


        var btnPhoto = layout.findViewById<Button>(R.id.photo4)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto3)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,viewModel.schema.value.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
            photos.visibility == View.VISIBLE
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