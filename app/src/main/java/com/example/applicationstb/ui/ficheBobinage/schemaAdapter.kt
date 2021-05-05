package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R

class schemaAdapter(var schemas: List<Uri>, ) :
    RecyclerView.Adapter<schemaAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var schema: ImageView
            init {
                schema = view.findViewById(R.id.schema)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schema, parent, false)
        /*val binding = DataBindingUtil.inflate<SchemaBinding>(LayoutInflater.from())
        view.setOnClickListener({
            callback(view.adapterPosition)
        })*/
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.schema.setImageURI(schemas[position])
    }
    fun update (list: MutableList<Uri>){
        this.schemas = list ///erreur du type sur l'objet transmis
        Log.i("INFO",schemas.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return schemas.size
    }
}