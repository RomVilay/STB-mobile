package com.example.applicationstb.ui.FicheDemontage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlternteurFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlternateurFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  val PHOTO_RESULT = 1888
    private val viewModel: FicheDemontageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_alternateur, container, false)
        var btnPhoto = layout.findViewById<Button>(R.id.photo2)
        var retour = layout.findViewById<Button>(R.id.retourAlt)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerAlt)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,viewModel.schema.value.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })

        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.enregistrer(layout)
        }
        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.partMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlternteurFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AlternateurFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}