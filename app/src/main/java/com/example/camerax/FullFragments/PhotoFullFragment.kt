package com.example.camerax.FullFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.camerax.MAIN
import com.example.camerax.MainActivity
import com.example.camerax.R
import com.example.camerax.databinding.FragmentMediaBinding
import com.example.camerax.databinding.FragmentPhotoFullBinding


class PhotoFullFragment : Fragment() {

    private var viewBinding: FragmentPhotoFullBinding?=null
    private val binding get()=viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding= FragmentPhotoFullBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity=activity as MainActivity

        val photoPath=arguments?.getString("path")
        val photoName=arguments?.getString("name")

        activity.supportActionBar?.setTitle(photoName)
        Glide.with(this).load(photoPath).into(view.findViewById(R.id.image_view))

        binding.photoFullCapture.setOnClickListener{
            MAIN.navController.navigate(R.id.action_photoFullFragment_to_photoFragment)
        }
        binding.photoFullTakeVideo.setOnClickListener{
            MAIN.navController.navigate(R.id.action_photoFullFragment_to_videoFragment)
        }


    }

}