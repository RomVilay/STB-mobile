package com.example.applicationstb.ui.FicheDemontage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Joint
import com.example.applicationstb.model.Roulement
import com.example.applicationstb.model.Section


class JointAdapter(var list: MutableList<Joint>?, var callback: (String,String,String,String,Int) -> Unit, var callback2:(Int) ->Unit) :
    RecyclerView.Adapter<JointAdapter.ViewHolder>() {
    var regexNombres = Regex("^\\d*\\.?\\d*\$")


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var typeAv: EditText
        var refAv: EditText
        var typeAr: EditText
        var refAr: EditText
        var suppr:Button

        init {
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
        var tab1 = list?.get(position)?.jointAvant.toString().split(" - ")
        var tab2 = list?.get(position)?.jointArriere.toString().split(" - ")
        holder.typeAv.setText(tab1[0])
        holder.refAv.setText(tab1[1])
        holder.typeAr.setText(tab1[0])
        holder.refAr.setText(tab2[1])

        holder.typeAv.doAfterTextChanged {
            if (holder.typeAv.text.toString()
                    .matches(regexNombres) && holder.typeAv.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.typeAr.doAfterTextChanged {
            if (holder.typeAr.text.toString()
                    .matches(regexNombres) && holder.typeAr.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.refAr.doAfterTextChanged {
            if (holder.refAr.text.toString()
                    .matches(regexNombres) && holder.refAr.text.isNotEmpty()
            ) callback(
                holder.typeAv.text.toString(),
                holder.refAv.text.toString(),
                holder.typeAr.text.toString(),
                holder.refAr.text.toString(),
                position
            )
        }
        holder.refAv.doAfterTextChanged {
            if (holder.refAv.text.toString()
                    .matches(regexNombres) && holder.refAv.text.isNotEmpty()
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
            callback2(position)
        }

    }


    fun update(joints: MutableList<Joint>) {
        this.list = joints
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}