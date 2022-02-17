package com.example.applicationstb.ui.FicheDemontage

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.applicationstb.R
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.model.DemontagePompe
import com.example.applicationstb.model.DemontageReducteur
import com.example.applicationstb.model.Joint
import com.example.applicationstb.model.Roulement
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ReducteurFragment : Fragment() {
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
        var layout = inflater.inflate(R.layout.fragment_reducteur, container, false)
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        var peinture = layout.findViewById<EditText>(R.id.peintureRed)
        var trMin = layout.findViewById<EditText>(R.id.trMin)
        var modele = layout.findViewById<EditText>(R.id.modele)
        var indiceReduction = layout.findViewById<EditText>(R.id.indRed)
        var typeHuile = layout.findViewById<EditText>(R.id.typeHuile)
        var quantiteHuile = layout.findViewById<EditText>(R.id.qteHuile)
        var typeRoulementAv = layout.findViewById<Spinner>(R.id.typeRav)
        var typeRoulementAr = layout.findViewById<Spinner>(R.id.typeRar)
        var refRoulementAv = layout.findViewById<EditText>(R.id.refRoulementAv)
        var refRoulementAr = layout.findViewById<EditText>(R.id.refRoulementAr)
        var typeJointAv = layout.findViewById<Spinner>(R.id.typeJav)
        var refJointAv = layout.findViewById<EditText>(R.id.refJointAv)
        var typeJointAr = layout.findViewById<Spinner>(R.id.typeJar)
        var refJointAr = layout.findViewById<EditText>(R.id.refJointsAr)
        var btnRoul = layout.findViewById<Button>(R.id.ajtTrain)
        var btnJoint = layout.findViewById<Button>(R.id.ajtJoint)
        var tabRoulements = layout.findViewById<RecyclerView>(R.id.tabRoullementsRed)
        var tabJoints = layout.findViewById<RecyclerView>(R.id.tabJointsred)
        var obs = layout.findViewById<EditText>(R.id.obs2)
        var termP = layout.findViewById<Button>(R.id.termP)
        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var roulements = MutableLiveData<MutableList<Roulement>?>()
        var joints = MutableLiveData<MutableList<Joint>?>()
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var fiche = viewModel.selection.value!! as DemontageReducteur
        //roulements
        if (fiche.roulements !== null) roulements.value = fiche.roulements else roulements.value = mutableListOf<Roulement>()
        var adapterRoulement = RoulementRedAdapter(mutableListOf<Roulement>(),{typeAv,refAv,typeAr,refAr,position ->
            roulements.value!!.set(position,Roulement("R${position}","${typeAv} - ${refAv}", "${typeAr} - ${refAr}"))
        }
        )
        roulements.observe(viewLifecycleOwner, {
            adapterRoulement.update(it!!)
            fiche.roulements = roulements.value
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.localSave()
        })
        tabRoulements.adapter = adapterRoulement
        tabRoulements.layoutManager = GridLayoutManager(context, 1)
        //joints
        if (fiche.joints !== null) joints.value = fiche.joints else joints.value = mutableListOf<Joint>()
        var adapterJoint = JointAdapter(mutableListOf<Joint>(), {typeAv,refAv,typeAr,refAr,position ->
            joints.value!!.set(position,
                Joint("J${position}","${typeAv} - ${refAv}", "${typeAr} - ${refAr}")
            )
        })
        joints.observe(viewLifecycleOwner, {
            adapterJoint.update(it!!)
            fiche.roulements = roulements.value
            viewModel.selection.value = fiche
            viewModel.getTime()
            viewModel.localSave()
        })
        tabJoints.adapter = adapterJoint
        tabJoints.layoutManager = GridLayoutManager(context, 1)
        if (fiche.peinture !== null) peinture.setText(fiche.peinture!!)
        if (fiche.trMinute !== null) trMin.setText(fiche.trMinute!!.toString())
        if (fiche.modele !== null) modele.setText(fiche.modele!!)
        if (fiche.indiceReduction !== null) indiceReduction.setText(fiche.indiceReduction!!)
        if (fiche.typeHuile !== null) typeHuile.setText(fiche.typeHuile!!)
        if (fiche.quantiteHuile !== null) quantiteHuile.setText(fiche.quantiteHuile!!.toString())
        if (fiche.observations !== null) obs.setText(fiche.observations!!)
        viewModel.photos.value = fiche.photos!!.toMutableList()
        var retour = layout.findViewById<Button>(R.id.retourPompe)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerPompe)
        if (fiche.status!! < 3L) {
            peinture.doAfterTextChanged {
                if(peinture.hasFocus() && peinture.text.isNotEmpty()) fiche.peinture = peinture.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            trMin.doAfterTextChanged {
                if(trMin.hasFocus() && trMin.text.isNotEmpty() && trMin.text.matches(regexNombres)) fiche.trMinute = trMin.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            modele.doAfterTextChanged {
                if(modele.hasFocus() && modele.text.isNotEmpty()) fiche.modele = modele.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            indiceReduction.doAfterTextChanged {
                if(indiceReduction.hasFocus() && indiceReduction.text.isNotEmpty()) fiche.indiceReduction = indiceReduction.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            typeHuile.doAfterTextChanged {
                if(typeHuile.hasFocus() && typeHuile.text.isNotEmpty() ) fiche.typeHuile = typeHuile.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            quantiteHuile.doAfterTextChanged {
                if(quantiteHuile.hasFocus() && quantiteHuile.text.isNotEmpty() && quantiteHuile.text.matches(regexNombres)) fiche.quantiteHuile = quantiteHuile.text.toString().toFloat()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
            btnRoul.setOnClickListener {
                var liste = roulements.value!!
                liste.add(Roulement("R${liste.size}","${typeRoulementAv.selectedItem} - ${refRoulementAv.text.toString()}", "${typeRoulementAr.selectedItem} - ${refRoulementAr.text.toString()}"))
                roulements.value = liste
                typeRoulementAv.setSelection(0)
                typeRoulementAr.setSelection(0)
                refRoulementAv.setText("")
                refRoulementAr.setText("")
            }
            btnJoint.setOnClickListener {
                var liste = joints.value!!
                liste.add(Joint("R${liste.size}","${typeJointAv.selectedItem} - ${refJointAv.text.toString()}", "${typeJointAr.selectedItem} - ${refJointAr.text.toString()}"))
                joints.value = liste
                typeJointAv.setSelection(0)
                typeJointAr.setSelection(0)
                refJointAv.setText("")
                refJointAr.setText("")
            }
            obs.doAfterTextChanged {
                fiche.observations = obs.text.toString()
                viewModel.selection.value = fiche
                viewModel.getTime()
                viewModel.localSave()
            }
        } else {

            obs.isEnabled = false
            btnPhoto.visibility = View.INVISIBLE
            enregistrer.visibility = View.GONE
        }

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

        typeRoulementAr.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M", "autre"))
        typeRoulementAv.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Sélectionnez un type","2Z/ECJ","2RS/ECP","C3","M", "autre"))
        //joints
        typeJointAr.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","simple lèvre","double lèvre"))
        typeJointAv.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("","simple lèvre","double lèvre"))
        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.getTime()
            fiche.status = 2L
            viewModel.selection.value = fiche
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getNameURI()
            }
            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
        }
        termP.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Terminer une fiche")
                    .setMessage("Êtes vous sûr de vouloir terminer la fiche? elle ne sera plus modifiable après")
                    .setPositiveButton("Terminer",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.getTime()
                            fiche.status = 3L
                            viewModel.selection.value = fiche
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.getNameURI()
                            }
                            viewModel.sendFiche(requireActivity().findViewById<CoordinatorLayout>(R.id.demoLayout))
                        })
                builder.create()
            }
            alertDialog?.show()
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
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (storageDir.exists()) {
            return File.createTempFile(
                viewModel.selection.value?.numFiche + "_" + SystemClock.uptimeMillis(),/* prefix */
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
    fun removeRef(i:Int, list:Array<String>):Array<String>{
        var tab = list.toMutableList()
        tab.removeAt(i)
        return tab.toTypedArray()
    }

}