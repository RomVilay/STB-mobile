package com.example.applicationstb.ui.FicheDemontage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.fragment.app.*
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import java.util.ArrayList

class FicheDemontage : Fragment() {

    companion object {
        fun newInstance() = FicheDemontage()
    }

    private val viewModel: FicheDemontageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var list = arguments?.get("listDemontages") as Array<DemontageMoteur>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeDemontages = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_demontage_fragment, container, false)
        val spinner = layout.findViewById<Spinner>(R.id.spinnerDemontage)
        val adapterDemontages = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeDemontages.map { it.numFiche  })
        spinner.adapter = adapterDemontages
        var btnDemontage = layout.findViewById<Button>(R.id.selectDemontage)
        val cfragment = layout.findViewById<FrameLayout>(R.id.fragmentContainer)
        val fragmentManager = childFragmentManager
        btnDemontage.setOnClickListener {
            var demo = viewModel.listeDemontages.find { it.numFiche == spinner.selectedItem }
            viewModel.selection.value = demo
            //viewModel.photos.value = demo!!.photo!!.toMutableList()

            //Log.i("INFO","moteur ${viewModel.listeDemontages[spinner.selectedItemPosition].telContact}")
            when (viewModel.selection.value){
                is CourantContinu -> fragmentManager.commit {
                    replace<CCFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                is Triphase -> fragmentManager.commit {
                    replace<TriphaseFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                /*is Monophase -> fragmentManager.commit {
                    replace<MonophaseFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
               is RotorBobine -> fragmentManager.commit {
                    replace<RotorBobineFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }

                is Alternateur ->fragmentManager.commit {
                    replace<AlternateurFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                is DemontagePompe -> fragmentManager.commit {
                    replace<PompeFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }*/
            }
        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
   /* private fun chargerFiche(){
        var selection: Fragment = FicheMonophase();
        var transaction: FragmentTransaction = childFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view_tag, childFragment).commit();
    }*/

}
class FicheMonophase: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monophase,container,false)
    }
}