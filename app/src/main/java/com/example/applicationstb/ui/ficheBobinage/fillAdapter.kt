package com.example.applicationstb.ui.ficheBobinage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R

class FillAdapter (private var list: ArrayList<Any>) :
    RecyclerView.Adapter<FillAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var longueur: TextView
        var brins: TextView

        init {
            // Define click listener for the ViewHolder's View.
            longueur = view.findViewById(R.id.lg)
            brins = view.findViewById(R.id.brin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var i = list.iterator()

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}