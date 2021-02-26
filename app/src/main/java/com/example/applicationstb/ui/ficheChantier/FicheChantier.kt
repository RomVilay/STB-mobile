package com.example.applicationstb.ui.ficheChantier

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.applicationstb.R

class FicheChantier : Fragment() {

    companion object {
        fun newInstance() = FicheChantier()
    }

    private lateinit var viewModel: FicheChantierViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FicheChantierViewModel::class.java)
        val layout = inflater.inflate(R.layout.fiche_chantier_fragment, container, false)
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}