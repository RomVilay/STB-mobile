package com.example.applicationstb.ui.ficheBobinage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Section
import com.example.applicationstb.ui.ficheBobinage.FillAdapter

class FicheBobinage : Fragment() {

    companion object {
        fun newInstance() = FicheBobinage()
    }

    private lateinit var viewModel: FicheBobinageViewModel
    private lateinit var recycler:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
        viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)

        var non = layout.findViewById<TextView>(R.id.non)
        var oui = layout.findViewById<TextView>(R.id.oui)
        var freq = layout.findViewById<EditText>(R.id.frequence)
        var client = layout.findViewById<EditText>(R.id.client)
        var tension = layout.findViewById<EditText>(R.id.tension)
        var courant = layout.findViewById<EditText>(R.id.courant)
        var phase = layout.findViewById<EditText>(R.id.phase)
        var vitesse = layout.findViewById<EditText>(R.id.vitesse)
        var type = layout.findViewById<EditText>(R.id.type)
        var marque = layout.findViewById<EditText>(R.id.marque)
        var switch = layout.findViewById<Switch>(R.id.switch2)
        var dates = layout.findViewById<LinearLayout>(R.id.dates)
        var details = layout.findViewById<TextView>(R.id.details)
        val adapter = FillAdapter(viewModel.sections)
        var visibility = View.VISIBLE
        //champs fiche
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
        var schema = layout.findViewById<EditText>(R.id.schema)
        var obs = layout.findViewById<EditText>(R.id.observations)
        var spire = layout.findViewById<EditText>(R.id.spire)


        recycler = layout.findViewById<RecyclerView>(R.id.tab)
        recycler.layoutManager = GridLayoutManager(context,1)
        recycler.adapter = adapter

        details.setOnClickListener {
            if (visibility == View.GONE){
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
            non.visibility = visibility
            client.visibility = visibility
            oui.visibility = visibility
            freq.visibility = visibility
            tension.visibility = visibility
            courant.visibility = visibility
            phase.visibility = visibility
            vitesse.visibility = visibility
            type.visibility = visibility
            marque.visibility = visibility
            dates.visibility = visibility
            switch.visibility = visibility
            Log.i("INFO","change")
        }
        btnfils.setOnClickListener {
            viewModel.addSection(Integer.parseInt(nbfils.text.toString()),  inLong.text.toString().toDouble())
        }

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}