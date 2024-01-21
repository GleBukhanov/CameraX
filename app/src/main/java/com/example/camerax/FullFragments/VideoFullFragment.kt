package com.example.camerax.FullFragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.camerax.MAIN
import com.example.camerax.MainActivity
import com.example.camerax.R
import com.example.camerax.databinding.FragmentPhotoFullBinding
import com.example.camerax.databinding.FragmentVideoFullBinding

class VideoFullFragment : Fragment() {
    private var viewBinding: FragmentVideoFullBinding? = null
    private val binding get()=viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding= FragmentVideoFullBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity

        val videoPath = arguments?.getString("path")
        val videoName = arguments?.getString("name")

        activity.supportActionBar?.setTitle(videoName)
        val mediaController: MediaController = MediaController(requireContext())
        val imageView = view.findViewById<VideoView>(R.id.video_full_screen)
        imageView.setMediaController(mediaController)
        imageView.setVideoURI(Uri.parse(videoPath))

        binding.videoFullCapture.setOnClickListener {
            MAIN.navController.navigate(R.id.action_videoFullFragment_to_photoFragment)
        }
        binding.videoFullTakeVideo.setOnClickListener {
            MAIN.navController.navigate(R.id.action_videoFullFragment_to_videoFragment)
        }

    }
}