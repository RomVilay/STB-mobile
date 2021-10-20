package com.example.applicationstb.ui.ficheRemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R
import android.util.Log
import android.widget.*
import com.example.applicationstb.model.*
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageViewModel
import androidx.cardview.widget.CardView
import androidx.fragment.app.*
import java.util.*

class FicheRemontage : Fragment() {

    companion object {
        fun newInstance() = FicheRemontage()
    }

    private val viewModel: FicheRemontageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        var list = arguments?.get("listRemontages") as Array<Remontage>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeRemontages = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterRemontages = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeRemontages.map { it.numFiche  })
        spinner.adapter = adapterRemontages
        //var infoMoteur = layout.findViewById<CardView>(R.id.infoMoteur)
        var btnDemontage = layout.findViewById<Button>(R.id.btnDemarrer)
        //var essaisDynamiques = layout.findViewById<CardView>(R.id.essaisDynamiques)
        var essaisStats = layout.findViewById<FrameLayout>(R.id.essaisStatiqueslayout)
        val fragmentManager = childFragmentManager
        //infos moteur
        var titre = layout.findViewById<TextView>(R.id.titreRemontage)
        var spinnerMnt = layout.findViewById<Spinner>(R.id.spinnerMntRll)
        spinnerMnt.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","à la presse","douille de frappe","chauffe roulement"))
        var spinnerCPA = layout.findViewById<Spinner>(R.id.spinnerCPA)
        spinnerCPA.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","avant","arrière","aucun"))
        var spinnerCIF = layout.findViewById<Spinner>(R.id.spinnerCIF)
        spinnerCIF.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","avant","arrière","aucun"))
        var fixCouronne = layout.findViewById<RadioButton>(R.id.fixCouronne)
        var isoPBRB = layout.findViewById<RadioButton>(R.id.isoPBRB)
        var isoPBV = layout.findViewById<EditText>(R.id.isoPBV)
        var risoPBV = layout.findViewById<EditText>(R.id.RisoPBV)
        var tensionStatorInducteurs = layout.findViewById<Switch>(R.id.swTSI)
         var tensionStatorInducteursU = layout.findViewById<EditText>(R.id.TSVU)
         var tensionStatorInducteursV = layout.findViewById<EditText>(R.id.TSVV)
         var tensionStatorInducteursW = layout.findViewById<EditText>(R.id.TSVW)
         var intensiteStatorInducteurs = layout.findViewById<Switch>(R.id.swTSI2)
         var intensiteStatorInducteursU = layout.findViewById<EditText>(R.id.ISVU)
         var intensiteStatorInducteursV = layout.findViewById<EditText>(R.id.ISVV)
         var intensiteStatorInducteursW = layout.findViewById<EditText>(R.id.ISVW)
         var tensionInduitRotor = layout.findViewById<Switch>(R.id.swTSI3)
         var tensionInduitRotorU = layout.findViewById<EditText>(R.id.TIU)
         var tensionInduitRotorV = layout.findViewById<EditText>(R.id.TIV)
         var tensionInduitRotorW = layout.findViewById<EditText>(R.id.TIW)
         var intensiteInduit = layout.findViewById<RadioButton>(R.id.Iindcut)
         var intensiteInduitU = layout.findViewById<EditText>(R.id.IntIndu)
         var vitesseU = layout.findViewById<EditText>(R.id.vitesseRemo)
         var puissanceU = layout.findViewById<EditText>(R.id.pRemotri)
         var dureeEssai = layout.findViewById<EditText>(R.id.dureeEssaiRemoTri)
         var sensRotation = layout.findViewById<ToggleButton>(R.id.sensRotationS)
         var V1V = layout.findViewById<EditText>(R.id.V1V)
         var V1H = layout.findViewById<EditText>(R.id.V1H)
         var V2V = layout.findViewById<EditText>(R.id.V2V)
         var V2H = layout.findViewById<EditText>(R.id.V2H)
         var V2A = layout.findViewById<EditText>(R.id.V2A)
         var A1V = layout.findViewById<EditText>(R.id.A1V)
         var A1H = layout.findViewById<EditText>(R.id.A1H)
         var A2V = layout.findViewById<EditText>(R.id.A2V)
         var A2H = layout.findViewById<EditText>(R.id.A2H)
         var A2A = layout.findViewById<EditText>(R.id.A2A)
         var obs = layout.findViewById<EditText>(R.id.observations)

        btnDemontage.setOnClickListener {
            //Log.i("INFO","moteur ${viewModel.listeDemontages[spinner.selectedItemPosition].telContact}")
            viewModel.start.value = Date()
            var demo = viewModel.listeRemontages.find { it.numFiche == spinner.selectedItem }
            if ( demo!!.typeFicheRemontage == 1) {
                viewModel.selection.value = demo as RemontageTriphase
                fragmentManager.commit {
                    replace<essaisStatTriFragment>(R.id.essaisStatiqueslayout)
                }
            }
            if ( demo!!.typeFicheRemontage == 2) {
                viewModel.selection.value = demo as RemontageCourantC
                fragmentManager.commit {
                    replace<essaisStatCCFragment>(R.id.essaisStatiqueslayout)
                }
            }
            layout.findViewById<CardView>(R.id.infoMoteur).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisSats).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisDynamiques).visibility = View.VISIBLE
            layout.findViewById<CardView>(R.id.essaisVibratoires).visibility = View.VISIBLE
            layout.findViewById<EditText>(R.id.observations).visibility = View.VISIBLE
            layout.findViewById<LinearLayout>(R.id.btns).visibility = View.VISIBLE

            var fiche = viewModel.selection.value!!
            if(fiche.remontageRoulement !== null) spinnerMnt.setSelection(fiche.remontageRoulement!!)
            if(fiche.collageRoulementPorteeArbre !== null) spinnerCPA.setSelection(fiche.collageRoulementPorteeArbre!!)
            if(fiche.collageRoulementFlasque !== null) spinnerCIF.setSelection(fiche.collageRoulementFlasque!!)
            if(fiche.verificationFixationCouronne !== null) fixCouronne.setChecked(fiche.verificationFixationCouronne!!)
            if(fiche.verificationIsolementPorteBalais !== null) isoPBRB.setChecked(fiche.verificationIsolementPorteBalais!!)
            if(fiche.isolementPorteBalaisV !== null) isoPBV.setText(fiche.isolementPorteBalaisV!!.toString())
            if(fiche.isolementPorteBalaisOhm !== null) risoPBV.setText(fiche.isolementPorteBalaisOhm!!.toString())
            /*if(fiche.tensionStatorInducteurs !== null ) tensionStatorInducteurs.setChecked(fiche.tensionStatorInducteurs!!)
            if(fiche.tensionStatorInducteursU !== null) tensionStatorInducteursU.setText(fiche.tensionStatorInducteursU!!.toString())
            if(fiche.tensionStatorInducteursV !== null) tensionStatorInducteursV.setText(fiche.tensionStatorInducteursV!!.toString())
            if(fiche.tensionStatorInducteursW !== null) tensionStatorInducteursW.setText(fiche.tensionStatorInducteursW!!.toString())
            if(fiche.tensionInduitRotor !== null ) tensionInduitRotor.setChecked(fiche.tensionInduitRotor!!)
            if(fiche.intensiteStatorInducteursU !== null) intensiteStatorInducteursU.setText(fiche.intensiteStatorInducteursU!!.toString())
            if(fiche.intensiteStatorInducteursV !== null) intensiteStatorInducteursV.setText(fiche.intensiteStatorInducteursV!!.toString())
            if(fiche.intensiteStatorInducteursW !== null) intensiteStatorInducteursW.setText(fiche.intensiteStatorInducteursW!!.toString())
            if(fiche.intensiteInduit !== null ) tensionStatorInducteurs.setChecked(fiche.intensiteInduit)
            if(fiche.tensionInduitRotorU !== null) tensionInduitRotorU.setText(fiche.tensionInduitRotorU!!.toString())
            if(fiche.tensionInduitRotorV !== null) tensionInduitRotorV.setText(fiche.tensionInduitRotorV!!.toString())
            if(fiche.tensionInduitRotorW !== null) tensionInduitRotorW.setText(fiche.tensionInduitRotorW!!.toString())*/
            if(fiche.intensiteInduit !== null ) tensionStatorInducteurs.setChecked(fiche.intensiteInduit!!)
            if(fiche.intensiteInduitU !== null) intensiteInduitU.setText(fiche.intensiteInduitU!!.toString())
            if(fiche.vitesseU !== null) vitesseU.setText(fiche.vitesseU!!.toString())
            if(fiche.dureeEssai !== null) dureeEssai.setText(fiche.dureeEssai!!.toString())
            if(fiche.puissanceU !== null) puissanceU.setText(fiche.puissanceU!!.toString())
            if(fiche.observations !== null) obs.setText(fiche.observations!!.toString())
            if(fiche.vitesse1V !== null) V1V.setText(fiche.vitesse1V!!.toString())
            if(fiche.vitesse1H !== null) V1H.setText(fiche.vitesse1H!!.toString())
            if(fiche.vitesse2H !== null) V2H.setText(fiche.vitesse2H!!.toString())
            if(fiche.vitesse2V !== null) V2V.setText(fiche.vitesse2V!!.toString())
            if(fiche.vitesse2A !== null) V2A.setText(fiche.vitesse2A!!.toString())
            if(fiche.acceleration1V !== null) A1V.setText(fiche.acceleration1V!!.toString())
            if(fiche.acceleration1H !== null) A1H.setText(fiche.acceleration1H!!.toString())
            if(fiche.acceleration2V !== null) A2V.setText(fiche.acceleration2V!!.toString())
            if(fiche.acceleration2H !== null) A2H.setText(fiche.acceleration2H!!.toString())
            if(fiche.acceleration2A !== null) A2A.setText(fiche.acceleration2A!!.toString())
            if(fiche.sensRotation !== null && fiche.sensRotation == 2 ) sensRotation.setChecked(true) else sensRotation.setChecked(false)
            //infoMoteur.visibility = View.VISIBLE
            //essaisDynamiques.visibility = View.VISIBLE
        }

        titre.setOnClickListener{
        }
        var btnquitter = layout.findViewById<Button>(R.id.quit)
        var btnenregistrer = layout.findViewById<Button>(R.id.enregistrer)
        btnquitter.setOnClickListener {
            viewModel.retour(layout)
        }
        sensRotation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selection.value!!.sensRotation = 2
            } else {
                viewModel.selection.value!!.sensRotation = 1
            }
        }
        spinnerMnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.remontageRoulement = position
            }
        }
        spinnerCPA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.collageRoulementPorteeArbre = position
            }
        }
        spinnerCIF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selection.value!!.collageRoulementFlasque = position
            }
        }
        fixCouronne.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.verificationFixationCouronne = isChecked
        }
        isoPBRB.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.verificationIsolementPorteBalais = isChecked
        }
        /*tensionStatorInducteurs.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.tensionStatorInducteurs = isChecked
        }
        intensiteStatorInducteurs.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.intensiteStatorInducteurs = isChecked
        }
        tensionInduitRotor.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selection.value!!.tensionInduitRotor = isChecked
        }*/

        btnenregistrer.setOnClickListener {
            var fiche = viewModel.selection.value!!
            if (fiche.dureeTotale !== null) {
                fiche.dureeTotale =
                    (Date().time - viewModel.start.value!!.time) + viewModel.selection.value!!.dureeTotale!!
            } else {
                fiche.dureeTotale = Date().time - viewModel.start.value!!.time
            }
            fiche.status = 2L
            if (isoPBV.text.isNotEmpty()) fiche.isolementPorteBalaisV =  isoPBV.text.toString().toInt()
            if (risoPBV.text.isNotEmpty()) fiche.isolementPorteBalaisOhm =  risoPBV.text.toString().toInt()
            /*if (tensionStatorInducteursU.text.isNotEmpty()) fiche.tensionStatorInducteursU = tensionStatorInducteursU.text.toString().toFloat()
            if (tensionStatorInducteursV.text.isNotEmpty())fiche.tensionStatorInducteursV = tensionStatorInducteursV.text.toString().toFloat()
            if (tensionStatorInducteursW.text.isNotEmpty()) fiche.tensionStatorInducteursW = tensionStatorInducteursW.text.toString().toFloat()
            if (intensiteStatorInducteursV.text.isNotEmpty()) fiche.intensiteStatorInducteursU = intensiteStatorInducteursU.text.toString().toFloat()
            if (intensiteStatorInducteursU.text.isNotEmpty()) fiche.intensiteStatorInducteursV = intensiteStatorInducteursV.text.toString().toFloat()
            if (intensiteStatorInducteursW.text.isNotEmpty()) fiche.intensiteStatorInducteursW = intensiteStatorInducteursW.text.toString().toFloat()
            if (tensionInduitRotorU.text.isNotEmpty()) fiche.tensionInduitRotorU = tensionInduitRotorU.text.toString().toFloat()
            if (tensionInduitRotorV.text.isNotEmpty()) fiche.tensionInduitRotorV = tensionInduitRotorV.text.toString().toFloat()
            if (tensionInduitRotorW.text.isNotEmpty()) fiche.tensionInduitRotorW = tensionInduitRotorW.text.toString().toFloat()*/
            if (intensiteInduitU.text.isNotEmpty()) fiche.intensiteInduitU = intensiteInduitU.text.toString().toFloat()
            if (vitesseU.text.isNotEmpty()) fiche.vitesseU = vitesseU.text.toString().toFloat()
            if (puissanceU.text.isNotEmpty())fiche.puissanceU = puissanceU.text.toString().toFloat()
            if (dureeEssai.text.isNotEmpty()) fiche.dureeEssai = dureeEssai.text.toString().toFloat()
            if(V1V.text.isNotEmpty()) fiche.vitesse1V = V1V.text.toString().toFloat()
            if(V1H.text.isNotEmpty()) fiche.vitesse1H = V1H.text.toString().toFloat()
            if(V2V.text.isNotEmpty()) fiche.vitesse2V = V2V.text.toString().toFloat()
            if(V2H.text.isNotEmpty()) fiche.vitesse2H = V2H.text.toString().toFloat()
            if(V2A.text.isNotEmpty()) fiche.vitesse2A = V2A.text.toString().toFloat()
            if(A1V.text.isNotEmpty()) fiche.acceleration1V = A1V.text.toString().toFloat()
            if(A1H.text.isNotEmpty()) fiche.acceleration1H = A1H.text.toString().toFloat()
            if(A2V.text.isNotEmpty()) fiche.acceleration2V = A2V.text.toString().toFloat()
            if(A2H.text.isNotEmpty()) fiche.acceleration2H = A2H.text.toString().toFloat()
            if(A2A.text.isNotEmpty()) fiche.acceleration2A = A2A.text.toString().toFloat()
            if (obs.text.isNotEmpty()) fiche.observations = obs.text.toString()
            viewModel.selection.value = fiche
            viewModel.enregistrer(layout)
        }


        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}