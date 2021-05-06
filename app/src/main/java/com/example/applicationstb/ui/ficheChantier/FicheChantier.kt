package com.example.applicationstb.ui.ficheChantier

import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.applicationstb.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private lateinit var viewModel: FicheChantierViewModel

    fun generateBitmapFromView(view: View): Bitmap {
        val specWidth = View.MeasureSpec.makeMeasureSpec(1324, View.MeasureSpec.AT_MOST)
        val specHeight = View.MeasureSpec.makeMeasureSpec(521, View.MeasureSpec.AT_MOST)
        view.measure(specWidth, specHeight)
        val width = view.measuredWidth
        val height = view.measuredHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }
    private fun saveImageToInternalStorage(bitmap :Bitmap):Uri{
        // Get the context wrapper instance
        val wrapper = ContextWrapper(requireActivity().application)
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }
        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FicheChantierViewModel::class.java)
        val layout = inflater.inflate(R.layout.fiche_chantier_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val materiel = layout.findViewById<EditText>(R.id.materiel)
        val objet = layout.findViewById<EditText>(R.id.objet)
        val observation = layout.findViewById<EditText>(R.id.observation)
        val selectButton = layout.findViewById<Button>(R.id.btnValider)
        val client = layout.findViewById<TextView>(R.id.puissance)
        val vehicule = layout.findViewById<TextView>(R.id.vehicule)
        val contact = layout.findViewById<TextView>(R.id.marque)
        val numero = layout.findViewById<TextView>(R.id.type)
        val adresse = layout.findViewById<TextView>(R.id.adresse)
        val dates = layout.findViewById<LinearLayout>(R.id.dates)
        val dateDebut = layout.findViewById<EditText>(R.id.DateFin)
        val dateFin = layout.findViewById<EditText>(R.id.DateDebut)
        val showDetails = layout.findViewById<TextView>(R.id.details)
        val quit = layout.findViewById<Button>(R.id.quit)
        val enregistrer = layout.findViewById<Button>(R.id.enregistrer)
        val adapter = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeChantiers.map { it.numDevis })
        var visibility = View.VISIBLE
        val swiew = layout.findViewById<View>(R.id.signTech)
        var stech: Bitmap? = null;



        showDetails.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            client.visibility = visibility
            vehicule.visibility = visibility
            contact.visibility = visibility
            numero.visibility = visibility
            adresse.visibility = visibility
            dates.visibility = visibility
            Log.i("INFO","change")
        }
        spinner.adapter = adapter
        var chantier = viewModel.listeChantiers.find{it.numFiche == spinner.selectedItem}
        selectButton.setOnClickListener {
            chantier = viewModel.listeChantiers.find{it.numFiche == spinner.selectedItem}
            materiel.setText(chantier?.materiel)
            objet.setText(chantier?.objet)
            observation.setText(chantier?.observations)
            client.setText(chantier?.client?.entreprise)
            vehicule.setText(chantier?.vehicule?.nom)
            contact.setText(chantier?.contact)
            numero.setText(chantier?.telContact.toString())
            adresse.setText(chantier?.adresse)
            var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dateDebut.setText(LocalDateTime.now().format(format))

        }
        quit.setOnClickListener {
            stech = generateBitmapFromView(swiew)

            Log.i("INFO",stech?.let { it1 -> saveImageToInternalStorage(it1) }.toString())
            viewModel.back(layout)
        }
        enregistrer.setOnClickListener {
            stech = generateBitmapFromView(swiew)
            Log.i("INFO","vue convertie to bitmap")
            context?.let { it1 -> stech!!.saveImage(it1) }
            viewModel.back(layout)
        }
        /*spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var x = position
                chantier = viewModel.listeChantiers[x]
            }
        }*/


        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

        val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.i("INFO",uri.toString())
            return uri
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}