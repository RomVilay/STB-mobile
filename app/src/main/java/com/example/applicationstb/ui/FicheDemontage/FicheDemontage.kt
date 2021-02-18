package com.example.applicationstb.ui.FicheDemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R

class FicheDemontage : Fragment() {

    companion object {
        fun newInstance() = FicheDemontage()
    }

    private lateinit var viewModel: FicheDemontageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fiche_demontage_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheDemontageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}