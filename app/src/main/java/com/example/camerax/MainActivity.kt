package com.example.camerax

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video
import android.provider.MediaStore.Video.Media
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoOutput
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.camerax.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        navController= Navigation.findNavController(this,R.id.nav_host_fragment)
        MAIN=this

    }


    companion object {
        const val TAG = "CameraX"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm"
        val REQUIRED_PERMISSION =
            mutableListOf(
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        fun hasPermission(context: Context) = REQUIRED_PERMISSION.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    }
}