package com.example.applicationstb.ui.ficheBobinage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}