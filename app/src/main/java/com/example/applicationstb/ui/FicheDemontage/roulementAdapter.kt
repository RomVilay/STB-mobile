package com.example.applicationstb.ui.FicheDemontage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import java.util.*

class roulementAdapter(var typeRoulement: Array<String>, var refRoulement: Array<String>, var callback: (Int)->Unit):
    RecyclerView.Adapter<roulementAdapter.ViewHolder>(){
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val type: TextView
            val ref: TextView
            val suppr: Button
            init {
                type = view.findViewById<TextView>(R.id.typeR)
                ref = view.findViewById<TextView>(R.id.refR)
                suppr = view.findViewById<Button>(R.id.supp)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.infos_roulement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.type.text = typeRoulement[position]
        var test = refRoulement[position].length
        Log.i("INFO","${test} -${refRoulement[position]} ")
        if(refRoulement[position].length !== 0) holder.ref.text = refRoulement[position] else holder.ref.text ="N/A"
        holder.suppr.setOnClickListener {
            var tab = typeRoulement.toMutableList()
            var tab2 = refRoulement.toMutableList()
            tab.removeAt(position)
            tab2.removeAt(position)
            callback(position)
            notifyItemRemoved(position)
            update(tab.toTypedArray(),tab2.toTypedArray())
        }
    }

    override fun getItemCount(): Int {
        return typeRoulement.size
    }
    fun update(types:Array<String>,refs:Array<String>){
        this.typeRoulement = types
        this.refRoulement = refs
        Log.i("INFO","taille ${typeRoulement.size}")
        notifyDataSetChanged()
    }
}