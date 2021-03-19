package com.example.applicationstb.ui.ficheChantier

import android.graphics.Bitmap
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.applicationstb.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private lateinit var viewModel: FicheChantierViewModel

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
        val diagnostic = layout.findViewById<EditText>(R.id.diagnostic)
        val selectButton = layout.findViewById<Button>(R.id.btnValider)
        val client = layout.findViewById<TextView>(R.id.puissance)
        val vehicule = layout.findViewById<TextView>(R.id.vehicule)
        val contact = layout.findViewById<TextView>(R.id.marque)
        val numero = layout.findViewById<TextView>(R.id.type)
        val adresse = layout.findViewById<TextView>(R.id.adresse)
        val dates = layout.findViewById<LinearLayout>(R.id.dates)
        val dateDebut = layout.findViewById<EditText>(R.id.DateArrivee)
        val dateFin = layout.findViewById<EditText>(R.id.DateDepart)
        val showDetails = layout.findViewById<TextView>(R.id.details)
        val quit = layout.findViewById<Button>(R.id.quit)
        val enregistrer = layout.findViewById<Button>(R.id.enregistrer)
        val adapter = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeChantiers.map { it.numDevis })
        var visibility = View.VISIBLE
        val swiew = layout.findViewById<View>(R.id.signTech)
        val sclient: Bitmap? = null;
        val stech: Bitmap? = null;


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
            diagnostic.setText(chantier?.diagnostic)
            client.setText(chantier?.client?.entreprise)
            vehicule.setText(chantier?.vehicule?.nom)
            contact.setText(chantier?.contact)
            numero.setText(chantier?.telContact.toString())
            adresse.setText(chantier?.adresse)
            var format = DateTimeFormatter.ofPattern("DD-MM-YYYY")
            dateDebut.setText(LocalDateTime.now().format(format))

        }
        quit.setOnClickListener {
            viewModel.back(layout)
        }
        enregistrer.setOnClickListener {
            swiew
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

}