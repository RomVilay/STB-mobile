package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R

class schemaAdapter(var schemas: List<String>, var callback: (String)->Unit) :
    RecyclerView.Adapter<schemaAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var schema: ImageView
            var uri: Uri? = null
            init {
                schema = view.findViewById(R.id.schema)
            }
            fun bind(photo:String){
                if (photo !== "") {
                uri = Uri.parse("/storage/emulated/0/Pictures/test_pictures/"+photo)
                schema.setImageURI(Uri.parse("/storage/emulated/0/Pictures/test_pictures/"+photo))
                    }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schema, parent, false)
        val holder = ViewHolder(view as ConstraintLayout)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schema = schemas[position]
        holder.bind(schema)
        holder.itemView.setOnClickListener{callback(schema)}
    }
    fun update (list: MutableList<String>){
        this.schemas = list ///erreur du type sur l'objet transmis
        //Log.i("INFO",schemas.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return schemas.size
    }
}