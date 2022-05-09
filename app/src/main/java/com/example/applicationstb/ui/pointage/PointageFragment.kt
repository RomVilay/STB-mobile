package com.example.applicationstb.ui.pointage

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Pointage
import com.example.applicationstb.ui.FicheDemontage.FicheDemontageViewModel
import com.example.applicationstb.ui.accueil.AccueilViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration

class PointageFragment : Fragment() {

    companion object {
        fun newInstance() = PointageFragment()
    }

    private lateinit var viewModel: PointageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.pointage_fragment, container, false)
        viewModel = ViewModelProvider(this).get(PointageViewModel::class.java)

        var tabPointage = layout.findViewById<RecyclerView>(R.id.tabPointage)
        viewModel.pointages.observe(viewLifecycleOwner, {
            tabPointage.adapter = pointerAdapter(viewModel.pointages.value!!)
        })
        tabPointage.layoutManager = GridLayoutManager(context, 1)
        var totalHeures = layout.findViewById<TextView>(R.id.totalHeure)
        viewModel.total.observe(viewLifecycleOwner, {
            totalHeures.setText(viewModel.total.value.toString())
        })
        var retour = layout.findViewById<TextView>(R.id.exit)
        retour.setOnClickListener {
            viewModel.toAccueil(layout)
        }
        return layout
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getListePointages()
    }
}