package com.example.applicationstb.ui.ficheChantier

import android.content.ContentValues
import android.content.Context
import android.graphics.*
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
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.app.AlertDialog
import android.content.DialogInterface

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private lateinit var viewModel: FicheChantierViewModel
    //


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
        val selectButton = layout.findViewById<Button>(R.id.btnDemarrer)
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
        //define signature area
        //val stech = layout.findViewById<DawingView>(R.id.signTech)
        //val scli = layout.findViewById<DawingView>(R.id.signclient)
        //stech.viewPlaceholder = "signature technicien"
        //scli.viewPlaceholder = "signature client"
        val btnTech = layout.findViewById<Button>(R.id.signTech)
        val btnClient = layout.findViewById<Button>(R.id.signClient)
        //var stech: Bitmap? = sview.extraBitmap

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
        btnClient.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            // set message of alert dialog
            dialogBuilder
                .setCancelable(true)
                .setView(inflater.inflate(R.layout.dawing_view, null))
                .setPositiveButton("Enregistrer", DialogInterface.OnClickListener{
                    dialog, id ->
                    dialog.dismiss()
                })
            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Signature Client")
            // show alert dialog
            alert.show()
            alert.setOnDismissListener {
                var v = alert.findViewById<DawingView>(R.id.dawingView)
                v.showLog()
            }
        }
        btnTech.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            // set message of alert dialog
            dialogBuilder.setMessage("Signature")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                        dialog, id ->
                            dialog.cancel()})
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("AlertDialogExample")
                // show alert dialog
            alert.show()
        }
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
            //stech = generateBitmapFromView(sview)

            //Log.i("INFO",stech?.let { it1 -> saveImageToInternalStorage(it1) }.toString())
            viewModel.back(layout)
        }
        enregistrer.setOnClickListener {

            /*val uriTech = stech.extraBitmap.saveImage(context!!.applicationContext)
            Log.i("INFO",uriTech.toString())
            val uriCli = scli.extraBitmap.saveImage(context!!.applicationContext)
            Log.i("INFO",uriCli.toString())
            //Log.i("INFO","vue convertie to bitmap")
            viewModel.signatures.add(uriTech)
            viewModel.signatures.add(uriCli)
            Log.i("INFO",viewModel.signatures.toString())*/
            viewModel.back(layout)
        }

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