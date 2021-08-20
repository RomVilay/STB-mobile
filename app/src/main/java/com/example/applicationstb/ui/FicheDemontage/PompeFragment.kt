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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.applicationstb.R
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import java.io.OutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PompeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  val PHOTO_RESULT = 1888
    private val viewModel: FicheDemontageViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_pompe, container, false)
        var i4 = layout.findViewById<TextView>(R.id.i4)
        var d7 = layout.findViewById<TextView>(R.id.d7)
        var d3 = layout.findViewById<TextView>(R.id.d3)
        var i3 = layout.findViewById<TextView>(R.id.i3)
        var d1 = layout.findViewById<TextView>(R.id.titreD1)
        var epf = layout.findViewById<EditText>(R.id.ePF)
        var arbre = layout.findViewById<EditText>(R.id.dArbre)
        var schema = layout.findViewById<ImageView>(R.id.schemaPompe)
        var depr = layout.findViewById<EditText>(R.id.dEPR)
        var lprt = layout.findViewById<EditText>(R.id.LPRT)
        var dext = layout.findViewById<EditText>(R.id.dExt)
        var btnPhoto = layout.findViewById<Button>(R.id.photo5)
        var retour = layout.findViewById<Button>(R.id.retourPompe)
        var enregistrer = layout.findViewById<Button>(R.id.enregistrerPompe)

        epf.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                schema.setImageResource(R.drawable.detourage_pompe_i4)
                i4.setBackgroundResource(R.color.bleu_stb)
                //i4.setTextColor(Color.WHITE)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        i4.setOnClickListener {
            epf.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        d7.setOnClickListener {
            dext.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }

        arbre.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d1)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }

        depr.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d3select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d3.setOnClickListener {
            depr.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        lprt.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_i3_select)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        i3.setOnClickListener {
            lprt.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }
        dext.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                schema.setImageResource(R.drawable.detourage_pompe_d7)
            } else {
                schema.setImageResource(R.drawable.detourage_pompe)
            }
        }
        d1.setOnClickListener {
            arbre.requestFocus()
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(activity?.currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }

        btnPhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, PHOTO_RESULT)
        }


        retour.setOnClickListener {
            viewModel.retour(layout)
        }
        enregistrer.setOnClickListener {
            viewModel.enregistrer(layout)
        }



        return layout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_RESULT) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(photo)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PompeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PompeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}