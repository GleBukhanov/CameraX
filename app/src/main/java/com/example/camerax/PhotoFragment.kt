package com.example.camerax

import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.camerax.databinding.ActivityMainBinding
import com.example.camerax.databinding.FragmentPhotoBinding
import java.text.SimpleDateFormat
import java.util.Locale


class PhotoFragment : Fragment() {
    private var viewBinding: FragmentPhotoBinding?=null
    private val binding get()=viewBinding!!
    private lateinit var cameraController: LifecycleCameraController
    //lateinit var navController:NavController

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding=FragmentPhotoBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun startCamera() {

        val activity = activity as MainActivity
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding?.cameraPreviewPhoto?.surfaceProvider)
                }
            val previewView: PreviewView = viewBinding?.cameraPreviewPhoto!!
            cameraController = LifecycleCameraController(activity.baseContext)
            cameraController.bindToLifecycle(this)
            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            previewView.controller = cameraController
            }, ContextCompat.getMainExecutor(activity))
    }

    private fun takePhoto() {
        val activity = activity as MainActivity
        val name = SimpleDateFormat(MainActivity.FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            activity.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()
        cameraController.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(activity.baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(MainActivity.TAG, msg)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(MainActivity.TAG, "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // navController=findNavController()
        val activity = activity as MainActivity
        if (!MainActivity.hasPermission(activity.baseContext)) {
            activityResultLauncher.launch(MainActivity.REQUIRED_PERMISSION)
        } else {
            startCamera()
        }
        binding.capture.setOnClickListener {
            takePhoto()
        }
        binding.takeVideoCamera.setOnClickListener{
            MAIN.navController.navigate(R.id.action_photoFragment_to_videoFragment)
        }



    }
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in MainActivity.REQUIRED_PERMISSION && it.value == false)
                    permissionGranted = false
            }
            if (permissionGranted) {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG).show()
            } else {
                startCamera()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding=null
    }
}