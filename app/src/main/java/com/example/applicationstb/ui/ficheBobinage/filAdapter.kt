package com.example.applicationstb.ui.ficheBobinage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Section

class FillAdapter(var list: MutableList<Section>, var callback: (Double, Long, Int) -> Unit) :
    RecyclerView.Adapter<FillAdapter.ViewHolder>() {
    var regexNombres = Regex("^\\d*\\.?\\d*\$")


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var diametre: EditText
        var nbBrins: EditText

        init {
            diametre = view.findViewById(R.id.dia)
            nbBrins = view.findViewById(R.id.br)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.diametre.setText(list[position].diametre.toString())
        holder.nbBrins.setText(list[position].nbBrins.toString())
        holder.diametre.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                holder.diametre.doAfterTextChanged {
                    if (holder.diametre.text.toString().matches(regexNombres)) callback(
                        holder.diametre.text.toString().toDouble(),
                        list[position].nbBrins,
                        position
                    )
                }
            }
        }
        holder.nbBrins.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                if (holder.nbBrins.text.toString().matches(regexNombres)) callback(
                    list[position].diametre,
                    holder.nbBrins.text.toString().toLong(),
                    position
                )
            }
        }

    }


    fun update(sections: MutableList<Section>) {
        this.list = sections
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}