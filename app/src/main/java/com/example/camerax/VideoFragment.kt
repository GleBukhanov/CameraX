package com.example.camerax

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
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.camerax.databinding.FragmentPhotoBinding
import com.example.camerax.databinding.FragmentVideoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class VideoFragment : Fragment() {
    private var viewBinding: FragmentVideoBinding?=null
    private val binding get()=viewBinding!!
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentVideoBinding.inflate(inflater,container,false)
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
                    it.setSurfaceProvider(binding/*viewBinding*/.cameraPreviewVideo.surfaceProvider)
                }
            val previewView: PreviewView = binding.cameraPreviewVideo

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider
                    .bindToLifecycle(this, cameraSelector, preview, videoCapture)
            } catch(exc: Exception) {
                Log.e(MainActivity.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()
        videoCapture = VideoCapture.withOutput(recorder)
    }

    private fun takeVideo() {
        val activity = activity as MainActivity
        val videoCapture=this.videoCapture?:return
        //viewBinding.takeVideo.isEnabled=false

        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }
        val name = SimpleDateFormat(MainActivity.FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }
        val mediaStoreOutputOptions= MediaStoreOutputOptions.Builder(activity.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).setContentValues(contentValues).build()
        recording=videoCapture.output.prepareRecording(activity,mediaStoreOutputOptions).apply {
            if(PermissionChecker.checkSelfPermission(activity,android.Manifest.permission.RECORD_AUDIO)== PermissionChecker.PERMISSION_GRANTED){
                withAudioEnabled()
            }
        }.start(ContextCompat.getMainExecutor(activity)){recordEvent->
            when(recordEvent){
                is VideoRecordEvent.Start->{
                    binding.takeVideo.apply {
                        isEnabled=true
                    }
                }
                is VideoRecordEvent.Finalize -> {
                    if (!recordEvent.hasError()) {
                        val msg = "Video capture succeeded: " +
                                "${recordEvent.outputResults.outputUri}"
                        Toast.makeText(activity.baseContext, msg, Toast.LENGTH_SHORT)
                            .show()
                        Log.d(MainActivity.TAG, msg)
                    } else {
                        recording?.close()
                        recording = null
                        Log.e(
                            MainActivity.TAG, "Video capture ends with error: " +
                                "${recordEvent.error}")
                    }
                    binding.takeVideo.apply {
                        isEnabled = true
                    }
                }
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController=findNavController()
        val activity = activity as MainActivity
        if (!MainActivity.hasPermission(activity.baseContext)) {
            activityResultLauncher.launch(MainActivity.REQUIRED_PERMISSION)
        } else {
            startCamera()
        }

        viewBinding!!.takeVideo.setOnClickListener {///////////////////////////////////////////////////////////////////////////////
            takeVideo()
        }
        binding.captureCamera.setOnClickListener{
        MAIN.navController.navigate(R.id.action_videoFragment_to_photoFragment)
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