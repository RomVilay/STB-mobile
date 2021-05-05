package com.example.applicationstb.ui.ficheBobinage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.applicationstb.R


/**
 * A simple [Fragment] subclass.
 * Use the [schema_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class schema_fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var layout = inflater.inflate(R.layout.schema_fragment, container, false)
        // Inflate the layout for this fragment
        return layout
    }


}