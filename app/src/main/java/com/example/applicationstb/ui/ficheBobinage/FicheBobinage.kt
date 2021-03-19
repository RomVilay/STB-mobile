package com.example.applicationstb.ui.ficheBobinage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.example.applicationstb.R

class FicheBobinage : Fragment() {

    companion object {
        fun newInstance() = FicheBobinage()
    }

    private lateinit var viewModel: FicheBobinageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
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
        var visibility = View.VISIBLE

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

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}