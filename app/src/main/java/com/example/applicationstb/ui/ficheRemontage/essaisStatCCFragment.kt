package com.example.applicationstb.ui.ficheRemontage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R
import android.widget.*
import androidx.fragment.app.activityViewModels


class essaisStatCCFragment : Fragment() {
    private val viewModel: FicheRemontageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_essais_stat_c_c, container, false)
        val spiInductMasse = layout.findViewById<Spinner>(R.id.spinnerIsoInduMasse)
        spiInductMasse.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiInduiMasse = layout.findViewById<Spinner>(R.id.spinnerIsoIndui)
        spiInduiMasse.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        val spiInduiInduc = layout.findViewById<Spinner>(R.id.spinnerIntuiInduc)
        spiInduiInduc.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>(" ","500","1000","2500","5000"))
        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment essaisStatCCFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = essaisStatTriFragment()
    }
}