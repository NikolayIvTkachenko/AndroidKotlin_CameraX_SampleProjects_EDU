package com.rsh.tkachenkoni.app_photo005_camerax_full

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var btnCapture: Button
    private lateinit var textureView: TextureView

    private lateinit var cameraId: String
    private lateinit var cameraDevices: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var imageDimension: Size
    private lateinit var imageReader: ImageReader
    private lateinit var file: File

    private var mFlashSupport: Boolean = false
    private lateinit var mBackgroundHandler: Handler
    private lateinit var mBackgroundThread: HandlerThread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCapture = findViewById(R.id.btnCapture)
        textureView = findViewById(R.id.textureView)

        btnCapture.setOnClickListener {
            takePicture()
        }


    }



    private fun takePicture(){

        if(cameraDevices == null){
            return
        }

        var cameraManager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try{
            var cameraCharacteristics: CameraCharacteristics = cameraManager.getCameraCharacteristics(cameraDevices.id)

            var jpegSizes: Array<Size?>? = null

            if (cameraCharacteristics != null){
                jpegSizes = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(ImageFormat.JPEG)
            }

            var width = 640
            var height = 480
            if(jpegSizes != null && jpegSizes.size > 0){
                width = jpegSizes[0]!!.width
                height = jpegSizes[0]!!.height

            }

            var reader : ImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            var outputSurface: MutableList<Surface> = ArrayList(2)
            outputSurface.add(reader.surface)
            outputSurface.add(Surface(textureView.surfaceTexture))

            var captureBuilder : CaptureRequest.Builder = cameraDevices.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(reader.surface)

            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            var rotation: Int = windowManager.defaultDisplay.rotation
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation))

            file = File(Environment.getExternalStorageDirectory().toString() + "/" + UUID.randomUUID().toString() + ".jpeg")

            ImageReader.OnImageAvailableListener {
                var image : Image? = null
                try{

                    image = it.acquireLatestImage()
                    var buffer: ByteBuffer = image.planes[0].buffer

                    //var bytes: Array<Byte> = Array(buffer.capacity())

                }catch (e: java.lang.Exception){
                    e.printStackTrace()
                }



            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    private fun openCamera(){



    }


    companion object{
        var ORIENTATION: SparseIntArray = SparseIntArray().apply {
            append(Surface.ROTATION_0,90)
            append(Surface.ROTATION_90,0)
            append(Surface.ROTATION_180,270)
            append(Surface.ROTATION_270,180)
        }

        const val REQUEST_CAMERA_PERMISSION : Int = 200

    }


}