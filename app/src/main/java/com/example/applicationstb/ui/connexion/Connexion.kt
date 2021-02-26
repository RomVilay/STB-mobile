package com.example.applicationstb.ui.connexion

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.applicationstb.R

class Connexion : Fragment() {
    companion object {
        fun newInstance() = Connexion()
    }

    private lateinit var viewModel: ConnexionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ConnexionViewModel::class.java)
        val user = viewModel.user
        val layout = inflater.inflate(R.layout.connexion_fragment, container, false)
        val username = layout.findViewById<EditText>(R.id.username)
        val password = layout.findViewById<EditText>(R.id.psw)
        val button = layout.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            viewModel.toAccueil(layout)
            Log.i("INFO","cliqué")
        }
        username.setText(user[0])
        password.setText(user[1])
        Log.i("INFO","init")
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}