package com.example.camerax.MediaFragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camerax.Adapters.VideoAdapter
import com.example.camerax.Data.Video
import com.example.camerax.MAIN
import com.example.camerax.MainActivity
import com.example.camerax.R
import com.example.camerax.databinding.FragmentMediaVideoBinding


class MediaVideoFragment : Fragment() {
    private var viewBinding: FragmentMediaVideoBinding? = null
    private val binding get() = viewBinding!!
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var allVideos: ArrayList<Video>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentMediaVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        recyclerView = binding.mediaVideoRecycler
        progressBar = binding.videoProgressBar
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView?.setHasFixedSize(true)
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                101
            )
        }
        allVideos = ArrayList()

        binding.toPhotoButton.setOnClickListener{
            MAIN.navController.navigate(R.id.action_mediaVideoFragment_to_mediaFragment)}

        if(allVideos!!.isEmpty()){
            progressBar?.visibility=View.VISIBLE
            allVideos=getAllVideos()
            recyclerView?.adapter= VideoAdapter(requireContext(),allVideos!!)
            progressBar?.visibility=View.GONE
        }

    }
    private fun getAllVideos(): ArrayList<Video>? {
        val activity=activity as MainActivity
        val videos=ArrayList<Video>()
        val allVideoURI= MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val proj= arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME)
        val current=activity.contentResolver.query(allVideoURI,proj,null,null,null)
        try {
            current!!.moveToFirst()
            do {
                val video= Video()
                video.videoPath=current.getString(current.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                video.videoName=current.getString(current.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                videos.add(video)
            }while (current!!.moveToNext())
            current.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
        return videos
    }
}