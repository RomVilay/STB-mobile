package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.applicationstb.R


/**
 * A simple [Fragment] subclass.
 * Use the [SchemaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchemaFragment : Fragment() {

    private val viewModel: FicheBobinageViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var layout = inflater.inflate(R.layout.schema_fragment, container, false)
        var retour = layout.findViewById<Button>(R.id.retour)
        var schema = layout.findViewById<ImageView>(R.id.fond)
        viewModel.schema.observe(viewLifecycleOwner, Observer<Uri> {
            schema.setImageURI(it)
        })
        retour.setOnClickListener{
            viewModel.backFs(layout)
        }
        // Inflate the layout for this fragment
        return layout
    }


}