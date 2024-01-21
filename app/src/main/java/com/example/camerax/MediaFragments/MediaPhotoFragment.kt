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
import com.example.camerax.MainActivity
import com.example.camerax.Data.Photo
import com.example.camerax.Adapters.PhotoAdapter
import com.example.camerax.Data.Video
import com.example.camerax.Adapters.VideoAdapter
import com.example.camerax.MAIN
import com.example.camerax.R
import com.example.camerax.databinding.FragmentMediaBinding


class MediaPhotoFragment : Fragment() {
    private var viewBinding: FragmentMediaBinding?=null
    private val binding get()=viewBinding!!
    private var recyclerView:RecyclerView?=null
    private var progressBar:ProgressBar?=null
    private var allPhotos:ArrayList<Photo>?=null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding= FragmentMediaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity=activity as MainActivity
        recyclerView=binding.mediaRecycler
        progressBar=binding.progressBar

        recyclerView?.layoutManager=GridLayoutManager(requireContext(),3)
        recyclerView?.setHasFixedSize(true)

        if(ContextCompat.checkSelfPermission(activity,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }


        allPhotos=ArrayList()


        if(allPhotos!!.isEmpty()){
            progressBar?.visibility=View.VISIBLE
            allPhotos=getAllPhotos()
            recyclerView?.adapter= PhotoAdapter(requireContext(),allPhotos!!)
            progressBar?.visibility=View.GONE
        }
        binding.toVideo.setOnClickListener{
            MAIN.navController.navigate(R.id.action_mediaFragment_to_mediaVideoFragment)
        }

    }




    private fun getAllPhotos(): ArrayList<Photo>? {
        val activity=activity as MainActivity
        val photos=ArrayList<Photo>()
        val allPhotoURI=MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val proj= arrayOf(MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME)
        val current=activity.contentResolver.query(allPhotoURI,proj,null,null,null)

        try {
            current!!.moveToFirst()
            do {
                val photo= Photo()
                photo.photoPath=current.getString(current.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                photo.photoName=current.getString(current.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                photos.add(photo)
            }while (current!!.moveToNext())
            current.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
        return photos


    }
}