package com.example.applicationstb.ui.ficheBobinage

import android.animation.Animator
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.applicationstb.R
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import kotlin.math.max
import kotlin.math.min


/**
 * A simple [Fragment] subclass.
 * Use the [SchemaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchemaFragment : Fragment() {

    private val viewModel: FicheBobinageViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var layout = inflater.inflate(R.layout.schema_fragment, container, false)
        scaleGestureDetector = ScaleGestureDetector(this.context, ScaleListener())
        var retour = layout.findViewById<Button>(R.id.retour)
        imageView = layout.findViewById<ImageView>(R.id.fond)
        Log.i("INFO", arguments?.get("schemaUri").toString())
        if (arguments?.get("schemaUri") !== null){
            imageView.setImageURI(Uri.parse(arguments?.get("schemaUri").toString()) )
        }
        retour.setOnClickListener{
            activity?.onBackPressed()
           // viewModel.backFs(layout)
        }
        // Inflate the layout for this fragment
        layout.setOnTouchListener { view, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
        }
        return layout
    }
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }


}