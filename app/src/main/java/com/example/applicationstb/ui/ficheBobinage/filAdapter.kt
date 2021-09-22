package com.example.applicationstb.ui.ficheBobinage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Bobinage
import com.example.applicationstb.model.Section

class FillAdapter (var list: MutableList<Section>) :
    RecyclerView.Adapter<FillAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var longueur: TextView
        var brins: TextView
        init {
            longueur = view.findViewById(R.id.lg)
            brins = view.findViewById(R.id.br)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.longueur.text = list[position].longueur.toString()
        holder.brins.text = list[position].nbBrins.toString()
    }

    fun update (sections: MutableList<Section>){
        this.list = sections
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }
}