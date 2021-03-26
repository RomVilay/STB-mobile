package com.example.applicationstb.ui.ficheBobinage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Section

class FillAdapter (private var list: ArrayList<Section>) :
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
        holder.longueur.text = list[position].longueur.toString()
        holder.brins.text = list[position].brins.toString()
    }

    fun update (sections: ArrayList<Section>){
        this.list =sections
    }
    override fun getItemCount(): Int {
        return list.size
    }
}