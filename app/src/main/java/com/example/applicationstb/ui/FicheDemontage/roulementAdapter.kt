package com.example.applicationstb.ui.FicheDemontage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import java.util.*

class roulementAdapter(var typeRoulement: Array<String>, var refRoulement: Array<String>,var posRoulement: Array<String>, var callback: (Int)->Unit , var callback2:(String,String,Int)->Unit):
    RecyclerView.Adapter<roulementAdapter.ViewHolder>(){
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val type: EditText
            val ref: EditText
            val suppr: Button
            val position: TextView
            init {
                type = view.findViewById<EditText>(R.id.typeR)
                ref = view.findViewById<EditText>(R.id.refR)
                position = view.findViewById<EditText>(R.id.poseR)
                suppr = view.findViewById<Button>(R.id.supp)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.infos_roulement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (typeRoulement.size > 0 ) {
            holder.type.setText(typeRoulement[position])
            if (refRoulement[position].length > 0 && refRoulement[position] !== "") holder.ref.setText(
                refRoulement[position]) else holder.ref.setText("N/A")
            holder.position.setText(posRoulement[position])
            holder.type.doAfterTextChanged {
                callback2(
                    holder.ref.text.toString(),
                    holder.type.text.toString(),
                    position
                )
            }
            holder.ref.doAfterTextChanged {
                callback2(
                    holder.ref.text.toString(),
                    holder.type.text.toString(),
                    position
                )
            }
            holder.position.doAfterTextChanged {
                callback2(
                    holder.ref.text.toString(),
                    holder.type.text.toString(),
                    position
                )
            }

            holder.suppr.setOnClickListener {
                var tab = typeRoulement.toMutableList()
                var tab2 = refRoulement.toMutableList()
                var tab3 = posRoulement.toMutableList()
                tab.removeAt(position)
                tab2.removeAt(position)
                tab3.removeAt(position)
                callback(position)
                notifyItemRemoved(position)
                update(tab.toTypedArray(), tab2.toTypedArray(), tab3.toTypedArray())
            }
        }
    }

    override fun getItemCount(): Int {
        return typeRoulement.size
    }
    fun update(types:Array<String>,refs:Array<String>,pos:Array<String>){
        this.typeRoulement = types
        this.refRoulement = refs
        this.posRoulement = pos
        notifyDataSetChanged()
    }
}