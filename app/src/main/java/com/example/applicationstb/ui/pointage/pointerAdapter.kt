package com.example.applicationstb.ui.pointage

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Pointage
import com.example.applicationstb.model.Section
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class pointerAdapter(var list: List<Pointage>) :
    RecyclerView.Adapter<pointerAdapter.ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    var dateFomater = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    var timeFomater = DateTimeFormatter.ofPattern("HH:mm")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView
        var debut: TextView

        init {
            date = view.findViewById(R.id.dateP)
            debut = view.findViewById(R.id.dbtPnt)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pointage, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.setText(list[position].timestamp?.format(dateFomater).toString())
        holder.debut.setText(list[position].timestamp?.format(timeFomater).toString())
    }


    fun update(pointages: List<Pointage>) {
        this.list = pointages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}