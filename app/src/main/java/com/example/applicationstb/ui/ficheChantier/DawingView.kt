package com.example.applicationstb.ui.ficheChantier

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewConfiguration
import com.example.applicationstb.ui.ficheBobinage.FicheBobinageViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * TODO: document your custom view class.
 */
class DawingView : View {

    private val paintColor = Color.BLACK
    private lateinit var extraCanvas: Canvas
    lateinit var extraBitmap: Bitmap
    private val STROKE_WIDTH = 5f
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame: RectF
    val paint = Paint().apply {
        color = paintColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    val path: Path = Path()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        /*init(attrs, 0)
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();*/
    }

    /*private fun setupPaint() {
        // Setup paint with color and stroke styles
        drawPaint = Paint()
        drawPaint!!.setColor(paintColor)
        drawPaint!!.setAntiAlias(true)
        drawPaint!!.setStrokeWidth(5f)
        drawPaint!!.setStyle(Paint.Style.STROKE)
        drawPaint!!.setStrokeJoin(Paint.Join.ROUND)
        drawPaint!!.setStrokeCap(Paint.Cap.ROUND)
    }*/

    private fun init(attrs: AttributeSet?, defStyle: Int) {
    }
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(Color.WHITE)
        val inset = 0
        frame = RectF(inset+0f, inset+0f, width - inset + 0f, height - inset +0f)
    }
    override fun onDraw(canvas: Canvas) {
        super .onDraw(canvas)
        //drawPaint?.let { canvas.drawPath(path, it) };
        canvas.drawBitmap(extraBitmap,0f,0f,null)
        canvas.drawRoundRect(frame, 20f, 20f, paint)
        paint.setTextSize(40f)
        paint.setStyle(Paint.Style.STROKE)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_signatures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "sign_${SystemClock.uptimeMillis()}")

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
    fun showLog(): Uri? {
        val uriTech = extraBitmap.saveImage(context!!.applicationContext)
        Log.i("INFO",uriTech.toString())
        return uriTech
    }
}