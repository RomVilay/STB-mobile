package com.example.applicationstb.ui

import android.content.Context
import android.graphics.*
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * TODO: document your custom view class.
 */
class DawingView : View {
    private val paintColor = Color.BLACK
    private var drawPaint: Paint? = null
    private val path: Path = Path()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    private fun setupPaint() {
        // Setup paint with color and stroke styles
        drawPaint = Paint()
        drawPaint!!.setColor(paintColor)
        drawPaint!!.setAntiAlias(true)
        drawPaint!!.setStrokeWidth(5f)
        drawPaint!!.setStyle(Paint.Style.STROKE)
        drawPaint!!.setStrokeJoin(Paint.Join.ROUND)
        drawPaint!!.setStrokeCap(Paint.Cap.ROUND)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

    }
    fun save (context: Context) {
        val b: Bitmap? = null

//create directory if not exist
        val dir = File("/sdcard/tempfolder/")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val output = File(dir, "tempfile.jpg")
        var os: OutputStream? = null

        try {
            os = FileOutputStream(output)
            b!!.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()

            //this code will scan the image so that it will appear in your gallery when you open next time
            MediaScannerConnection.scanFile(context, arrayOf(output.toString()), null,
                    OnScanCompletedListener { path, uri -> Log.d("appname", "image is saved in gallery and gallery is refreshed.") }
            )
        } catch (e: Exception) {
        }
    }
    override fun onDraw(canvas: Canvas) {
        drawPaint?.let { canvas.drawPath(path, it) };
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointX = event!!.x
        val pointY = event.y
        // Checks for the event that occurs
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(pointX, pointY)
                return true
            }
            MotionEvent.ACTION_MOVE -> path.lineTo(pointX, pointY)
            else -> return false
        }
        // Force a view to draw again
        // Force a view to draw again
        postInvalidate()
        return true
    }
}