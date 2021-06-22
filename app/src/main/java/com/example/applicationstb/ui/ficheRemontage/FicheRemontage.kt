package com.example.applicationstb.ui.ficheRemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R

class FicheRemontage : Fragment() {

    companion object {
        fun newInstance() = FicheRemontage()
    }

    private lateinit var viewModel: FicheRemontageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fiche_remontage_fragment, container, false)


        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheRemontageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}