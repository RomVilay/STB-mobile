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
import android.widget.*
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
import com.example.applicationstb.model.Triphase
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
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

    private val viewModel: FicheDemontageViewModel by activityViewModels()
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var layout = inflater.inflate(R.layout.fragment_triphase, container, false)
        var start : Date? = null;
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
        var fiche = viewModel.selection.value!! as Triphase
        viewModel.selection.observe(viewLifecycleOwner, {
             if (fiche.isolementPhaseMasseStatorUM !== null) UM.setText(fiche.isolementPhaseMasseStatorUM!!.toString()) else 0
            if (fiche.isolementPhaseMasseStatorVM !== null)VM.setText(fiche.isolementPhaseMasseStatorVM!!.toString()) else 0
            if (fiche.isolementPhaseMasseStatorWM !== null) WM.setText(fiche.isolementPhaseMasseStatorWM!!.toString()) else 0
            if (fiche.isolementPhasePhaseStatorUV !== null)UV.setText(fiche.isolementPhasePhaseStatorUV!!.toString())
            if (fiche.isolementPhasePhaseStatorUW !== null) UW.setText(fiche.isolementPhasePhaseStatorUW!!.toString()) else 0
            if (fiche.isolementPhasePhaseStatorVW!== null) iVW.setText(fiche.isolementPhasePhaseStatorVW!!.toString()) else 0
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
        })
        UM.doAfterTextChanged {
            fiche.isolementPhaseMasseStatorUM = UM.text.toString().toInt()
        }
        VM.doAfterTextChanged {
            fiche.isolementPhaseMasseStatorVM = VM.text.toString().toInt()
        }
        WM.doAfterTextChanged {
            fiche.isolementPhaseMasseStatorWM = WM.text.toString().toInt()
        }
        UV.doAfterTextChanged {
            fiche.isolementPhasePhaseStatorUV = UV.text.toString().toInt()
        }
        UW.doAfterTextChanged {
            fiche.isolementPhasePhaseStatorUW = UW.text.toString().toInt()
        }
        iVW.doAfterTextChanged {
            fiche.isolementPhasePhaseStatorVW = iVW.text.toString().toInt()
        }
        RU.doAfterTextChanged {
            fiche.resistanceStatorU = RU.text.toString().toInt()
        }
        RV.doAfterTextChanged {
            fiche.resistanceStatorV = RV.text.toString().toInt()
        }
        RW.doAfterTextChanged {
            fiche.resistanceStatorW = RW.text.toString().toInt()
        }
        VU.doAfterTextChanged {
            fiche.tensionU = VU.text.toString().toInt()
        }
        VV.doAfterTextChanged {
            fiche.tensionV = VV.text.toString().toInt()
        }
        VW.doAfterTextChanged {
            fiche.tensionW = VW.text.toString().toInt()
        }
        VUI.doAfterTextChanged {
            fiche.intensiteU = VUI.text.toString().toInt()
        }
        VVI.doAfterTextChanged {
            fiche.intensiteV = VVI.text.toString().toInt()
        }
        VWI.doAfterTextChanged {
            fiche.intensiteW = VWI.text.toString().toInt()
        }
        VU.doAfterTextChanged {
            fiche.tensionU = VU.text.toString().toInt()
        }
        dessai.doAfterTextChanged {
            fiche.dureeEssai = dessai.text.toString().toInt()
        }
        obs.doAfterTextChanged {
            fiche.observations = obs.text.toString()
        }
        dessai.doAfterTextChanged {
            fiche.dureeEssai = dessai.text.toString().toInt()
        }

        enr.setOnClickListener {
            if (viewModel.selection.value!!.dureeTotale !== null) {
                viewModel.selection.value!!.dureeTotale =
                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
            } else {
                viewModel.selection.value!!.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            viewModel.selection.value!!.status = 2L
            Log.i("INFO","duree:${viewModel.selection.value!!.dureeTotale}")
            viewModel.enregistrer(activity!!.findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        ter.setOnClickListener {
            if (viewModel.selection.value!!.dureeTotale !== null) {
                viewModel.selection.value!!.dureeTotale =
                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
            } else {
                viewModel.selection.value!!.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            viewModel.selection.value!!.status = 3L
            Log.i("INFO","duree:${viewModel.selection.value!!.dureeTotale}")
            viewModel.enregistrer(activity!!.findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        retour.setOnClickListener {
            viewModel.back(layout)
        }


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
            replace<InfoMoteurFragment>(R.id.infoLayout)
            setReorderingAllowed(true)
        }
        // Inflate the layout for this fragment
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