package com.example.applicationstb.ui.FicheDemontage

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.ui.ficheBobinage.schemaAdapter
import java.io.OutputStream


class MonophaseFragment : Fragment() {

    companion object {
        fun newInstance() = MonophaseFragment()
    }
    private val viewModel: FicheDemontageViewModel by activityViewModels()
    private lateinit var photos:RecyclerView
    private  val PHOTO_RESULT = 1888

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_monophase, container, false)
        var titre1 = layout.findViewById<TextView>(R.id.titreMono)
        var titre2 = layout.findViewById<TextView>(R.id.titre2)
        var titre3 = layout.findViewById<TextView>(R.id.titre3)

        //
        var infos = layout.findViewById<CardView>(R.id.infoMoteur)
        var essais = layout.findViewById<CardView>(R.id.essais)
        var meca = layout.findViewById<CardView>(R.id.meca)
        var retour = layout.findViewById<Button>(R.id.retourMono)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerMono)

        var couplage = layout.findViewById<Spinner>(R.id.spiCouplage)

        var partM = layout.findViewById<FrameLayout>(R.id.PartMeca)
        var btnPhoto = layout.findViewById<Button>(R.id.photo2)
        var photos = layout.findViewById<RecyclerView>(R.id.recyclerPhoto2)
        photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val sAdapter = schemaAdapter(viewModel.photos.value!!.toList() ,{ item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(layout,viewModel.schema.value.toString())
        })
        photos.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            sAdapter.update(it)
        })

        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.enregistrer(layout)
        }
        /*var listePhotos = layout.findViewById<Button>(R.id.recyclerPhoto2)
        listePhotos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        listePhotos.adapter = schemaAdapter(viewModel.photos.schemas,{ item ->
           Log.i("INFO","photo 1")
        })*/

        val fmanager = childFragmentManager
        fmanager.commit {
            replace<MecaFragment>(R.id.PartMeca)
            setReorderingAllowed(true)
        }
        fmanager.commit {
            replace<InfoMoteurFragment>(R.id.infosLayout)
            setReorderingAllowed(true)
        }
        titre1.setOnClickListener {
            var layout = infos.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            infos.layoutParams = layout
        }
        titre2.setOnClickListener {
            var layout = essais.layoutParams
            if (layout.height == 130){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 130
                Log.i("INFO","in")
            }
            essais.layoutParams = layout
        }
        titre3.setOnClickListener {
            var layout = meca.layoutParams
            if (layout.height == 100){
                layout.height = WRAP_CONTENT
                Log.i("INFO","out")
            } else{
                layout.height = 100
                Log.i("INFO","in")
            }
            meca.layoutParams = layout
        }
        btnPhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, PHOTO_RESULT)
        }




        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_RESULT) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val uri = context?.let { photo.saveImage(it.applicationContext) }
            if (uri != null) {
                Log.i("INFO",uri.toString())
                viewModel.addPhoto(0,uri)
            }
            Log.i("INFO",uri.toString())
        }
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

        val uri: Uri? =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            return uri
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}