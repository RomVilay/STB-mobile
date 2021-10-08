package com.example.applicationstb.ui.ficheRemontage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import com.example.applicationstb.R


class essaisStatTriFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        private var viewModel: FicheRemontageViewModel by activityViewModels()
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_essais_stat_tri, container, false)
        val spiIsoPM = layout.findViewById<Spinner>(R.id.spinnerIsoPMT)
        spiIsoPM.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiIsoP = layout.findViewById<Spinner>(R.id.spinnerIsoP)
        spiIsoP.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        return layout
    }

    companion object {

        @JvmStatic
        fun newInstance() = essaisStatTriFragment()
    }
}