package com.example.applicationstb.ui.FicheDemontage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Joint
import com.example.applicationstb.model.Roulement
import com.example.applicationstb.model.Section

class RoulementRedAdapter(var list: MutableList<Roulement>?, var callback: (String,String,String,String,Int) -> Unit) :
    RecyclerView.Adapter<RoulementRedAdapter.ViewHolder>() {
    var regexNombres = Regex("^\\d*\\.?\\d*\$")


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titre: TextView
        var typeAv: EditText
        var refAv: EditText
        var typeAr: EditText
        var refAr: EditText
        var suppr:Button

        init {
            titre = view.findViewById(R.id.titretrain)
            typeAv = view.findViewById(R.id.typeAv)
            refAv = view.findViewById(R.id.refAv)
            typeAr = view.findViewById(R.id.typeAr)
            refAr = view.findViewById(R.id.refAr)
            suppr = view.findViewById(R.id.supprRlt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowreducteur, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var tab1 = list?.get(position)?.roulementAvant.toString().split(" - ")
        var tab2 = list?.get(position)?.roulementArriere.toString().split(" - ")
        holder.titre.setText(list?.get(position)!!.title)
        holder.typeAv.setText(tab1[0])
        holder.refAv.setText(tab1[1])
        holder.typeAr.setText(tab2[0])
        holder.refAr.setText(tab2[1])
        holder.typeAv.doAfterTextChanged {
            if ( holder.typeAv.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.typeAr.doAfterTextChanged {
            if ( holder.typeAr.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.refAr.doAfterTextChanged {
            if (holder.refAr.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.refAv.doAfterTextChanged {
            if ( holder.refAv.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }


        holder.suppr.setOnClickListener {
            list?.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }

    }


    fun update(roulements: MutableList<Roulement>) {
        this.list = roulements
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}
