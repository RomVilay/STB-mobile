package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.applicationstb.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class InfoMoteurFragment : Fragment() {

    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_info_moteur, container, false)
        var aspectExt = layout.findViewById<Spinner>(R.id.enumaspect)
        var aspectBte = layout.findViewById<Spinner>(R.id.enumboite)
        var optionsAsp = arrayOf<String>("propre","sale","très sale")
        var adaptExt = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,optionsAsp)
        aspectExt.adapter = adaptExt
        aspectBte.adapter = adaptExt


        return layout
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TriphaseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TriphaseFragment()
    }



}