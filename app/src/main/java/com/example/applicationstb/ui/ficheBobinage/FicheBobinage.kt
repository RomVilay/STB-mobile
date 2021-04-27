package com.example.applicationstb.ui.ficheBobinage

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Section
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FicheBobinage : Fragment() {

    companion object {
        fun newInstance() = FicheBobinage()
    }

    private lateinit var viewModel: FicheBobinageViewModel
    private lateinit var recycler:RecyclerView
    private lateinit var imageView: ImageView
    private  val PHOTO_RESULT = 1888

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var layout = inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
        viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)

        //champs détails
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterDevis = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeBobinage.map { it.numDevis })
        spinner.adapter = adapterDevis
        var btnSelect = layout.findViewById<Button>(R.id.btnValider)
        var non = layout.findViewById<TextView>(R.id.non)
        var oui = layout.findViewById<TextView>(R.id.oui)
        var frequence = layout.findViewById<EditText>(R.id.frequence)
        var client = layout.findViewById<EditText>(R.id.client)
        var tension = layout.findViewById<EditText>(R.id.tension)
        var courant = layout.findViewById<EditText>(R.id.courant)
        var phases = layout.findViewById<EditText>(R.id.phase)
        var vitesse = layout.findViewById<EditText>(R.id.vitesse)
        var type = layout.findViewById<EditText>(R.id.type)
        var marque = layout.findViewById<EditText>(R.id.marque)
        var switch = layout.findViewById<Switch>(R.id.switch2)
        var dates = layout.findViewById<LinearLayout>(R.id.dates)
        var details = layout.findViewById<TextView>(R.id.details)
        val adapter = FillAdapter(viewModel.listeBobinage[0].sectionsFils)
        var visibility = View.VISIBLE
        //champs fils
        var btnfils = layout.findViewById<Button>(R.id.ajoutFil)
        var nbfils = layout.findViewById<EditText>(R.id.nbfils)
        var inLong = layout.findViewById<EditText>(R.id.inputlongueur)
        var RU = layout.findViewById<EditText>(R.id.RU)
        var RV = layout.findViewById<EditText>(R.id.RV)
        var RW = layout.findViewById<EditText>(R.id.RW)
        var IU = layout.findViewById<EditText>(R.id.IU)
        var IV = layout.findViewById<EditText>(R.id.IV)
        var IW = layout.findViewById<EditText>(R.id.IW)
        var IIU = layout.findViewById<EditText>(R.id.IIU)
        var IIV = layout.findViewById<EditText>(R.id.IIV)
        var IIW = layout.findViewById<EditText>(R.id.IIW)
        var schema = layout.findViewById<Button>(R.id.schema)
        var obs = layout.findViewById<EditText>(R.id.observations)
        var som = layout.findViewById<TextView>(R.id.somme)
        var spire = layout.findViewById<EditText>(R.id.spire)
        imageView = layout.findViewById(R.id.photoSchema)
        var enrg = layout.findViewById<Button>(R.id.enregistrer)
        var quit = layout.findViewById<Button>(R.id.quit)
        if (activity?.let { ContextCompat.checkSelfPermission(it.applicationContext, Manifest.permission.CAMERA) }
                == PackageManager.PERMISSION_DENIED)
            this.activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PHOTO_RESULT) }

        recycler = layout.findViewById(R.id.tab)
        recycler.layoutManager = GridLayoutManager(context, 1)
        recycler.adapter = adapter
        viewModel.sections.observe(viewLifecycleOwner, {
            adapter.update(it)
        })

        btnSelect.setOnClickListener {
            var bobinage = viewModel.listeBobinage.find{it.numDevis == spinner.selectedItem}
            marque.setText(bobinage?.marque)
            type.setText(bobinage?.type)
            vitesse.setText(bobinage?.vitesse.toString())
            client.setText(bobinage?.client.toString())
            tension.setText(bobinage?.tension.toString())
            phases.setText(bobinage?.phases)
            frequence.setText(bobinage?.frequence.toString())
            courant.setText(bobinage?.courant)
            if (bobinage != null) {
                switch.setChecked(bobinage.callage)
                adapter.list = bobinage.sectionsFils
            }
            /*var format = DateTimeFormatter.ofPattern("DD-MM-YYYY")
            dateDebut.setText(LocalDateTime.now().format(format))*/

        }

        details.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            non.visibility = visibility
            client.visibility = visibility
            oui.visibility = visibility
            frequence.visibility = visibility
            tension.visibility = visibility
            courant.visibility = visibility
            phases.visibility = visibility
            vitesse.visibility = visibility
            type.visibility = visibility
            marque.visibility = visibility
            dates.visibility = visibility
            switch.visibility = visibility
            Log.i("INFO", "change")
        }
        btnfils.setOnClickListener {
            viewModel.addSection(Integer.parseInt(nbfils.text.toString()), inLong.text.toString().toDouble())
            var bobinage = viewModel.listeBobinage.find{it.numDevis == spinner.selectedItem}
            if (bobinage != null) {
                som.setText(viewModel.somme(bobinage.sectionsFils).toString())
            }
        }
        schema.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, PHOTO_RESULT)
        }
        quit.setOnClickListener {
            viewModel.back(layout)
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_RESULT) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
        }
    }


}