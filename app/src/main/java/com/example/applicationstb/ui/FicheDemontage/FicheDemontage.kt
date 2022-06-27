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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import com.example.applicationstb.R
import com.example.applicationstb.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher
import java.util.*
import kotlin.collections.ArrayList

class FicheDemontage : Fragment() {

    companion object {
        fun newInstance() = FicheDemontage()
    }

    private val viewModel: FicheDemontageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.token = arguments?.get("token") as String
        //viewModel.listeDemontages.value = viewModel.listeDemontages.value!!.filter { it.status!! < 3 }.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_demontage_fragment, container, false)
        var spinner = layout.findViewById<Spinner>(R.id.spinnerDemontage)
/*        val adapterDemontages = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeDemontages.value!!.map { it.numFiche  })
        spinner.adapter = adapterDemontages*/
        var btnDemontage = layout.findViewById<Button>(R.id.selectDemontage)
        val cfragment = layout.findViewById<FrameLayout>(R.id.fragmentContainer)
        val fragmentManager = childFragmentManager
        viewModel.listeDemontages.observe(viewLifecycleOwner){
            spinner!!.adapter = ArrayAdapter(requireActivity(),R.layout.support_simple_spinner_dropdown_item,viewModel.listeDemontages.value!!.map { it.numFiche  })
        }
        if (arguments?.get("ficheID") !== null ){
                CoroutineScope(Dispatchers.IO).launch{
                    var fiches = async {  viewModel.repository.demontageRepository!!.getAllDemontageLocalDatabase()}.await().map { it.toFicheDemontage() }.filter { it.status!! >= 2 }.toMutableList()
                        //var list = fiche.await().map { it.toFicheDemontage() }.filter { it.status!! >= 3 }.toMutableList()
                        withContext(Dispatchers.Main){
                            viewModel.listeDemontages.value = fiches
                            spinner.setSelection(fiches.indexOfFirst { it._id == arguments?.get("ficheID") })
                        }
                }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
               viewModel.getLocalFiches()
            }
        }
        viewModel.selection.observe(viewLifecycleOwner) {
            if (viewModel.selection.value !== null){
                Log.i("info","position ${viewModel.listeDemontages.value!!.indexOf(viewModel.selection.value)}")
                spinner.setSelection(viewModel.listeDemontages.value!!.indexOf(viewModel.selection.value))
            }

        }
        btnDemontage.setOnClickListener {
            viewModel.start.value = Date()
            var demo = viewModel.listeDemontages.value!!.find { it.numFiche == spinner!!.selectedItem }
            viewModel.selection.value = demo
            viewModel.photos.value = demo!!.photos!!.toMutableList()
            if (viewModel.selection.value!!.status ==  1L) viewModel.selection.value!!.status = 2L
            var tab = viewModel.selection.value!!.typeRoulementAvant!!.toMutableList().filter { it == "" }
            viewModel.selection.value!!.typeRoulementAvant = tab.toTypedArray()
            var tab2 = viewModel.selection.value!!.typeRoulementArriere!!.toMutableList().filter { it == "" }
            viewModel.selection.value!!.typeRoulementArriere = tab2.toTypedArray()

            when (viewModel.selection.value!!.subtype){
                1 -> fragmentManager.commit {
                    replace<PompeFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                2 -> fragmentManager.commit {
                    replace<MonophaseFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                3 ->fragmentManager.commit {
                    replace<AlternateurFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                5 -> fragmentManager.commit {
                    replace<CCFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                6 -> fragmentManager.commit {
                    replace<TriphaseFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                7 ->fragmentManager.commit {
                    replace<MotopompeFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                8 ->fragmentManager.commit {
                    replace<ReducteurFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
                9 ->fragmentManager.commit {
                    replace<MotoReducteurFragment>(R.id.fragmentContainer)
                    setReorderingAllowed(true)
                }
            }
        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }


    override fun onResume() {
        super.onResume()


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