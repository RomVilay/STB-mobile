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
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MecaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MecaFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_meca, container, false)
        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)
        couplage.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Y","Δ","Autre"))
        var etatFlasqueAvant = layout.findViewById<Spinner>(R.id.spiFA)
        etatFlasqueAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var etatFlasqueArrière = layout.findViewById<Spinner>(R.id.spiFAr)
        etatFlasqueArrière.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var roulementAvant = layout.findViewById<Spinner>(R.id.spiRAv)
        roulementAvant.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var roulementArriere = layout.findViewById<Spinner>(R.id.spiRAr)
        roulementArriere.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("OK","A contrôler","A rebaguer"))
        var typeRoulement = layout.findViewById<Spinner>(R.id.spiRoul)
        typeRoulement.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("2Z/ECJ","2RS/ECP","C3","M"))
        var typeJoints = layout.findViewById<Spinner>(R.id.spiJoints)
        typeJoints.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("simple lèvre","double lèvre"))
        var cvent = layout.findViewById<Spinner>(R.id.spiCapot)
        cvent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        var vent = layout.findViewById<Spinner>(R.id.spiVentilateur)
        vent.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Absent"))
        var socle = layout.findViewById<Spinner>(R.id.spiSocle)
        socle.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        var capot = layout.findViewById<Spinner>(R.id.spiCap)
        capot.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","Cassé","Absent"))
        var plaque = layout.findViewById<Spinner>(R.id.spiPla)
        plaque.adapter = ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item, arrayOf<String>("Bon état","A changer","Sortie par câbles"))
        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MecaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MecaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}