package com.example.camerax.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.camerax.Data.Video
import com.example.camerax.MAIN
import com.example.camerax.R


class VideoAdapter(private var context: Context, private var videoList: ArrayList<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var video:ImageView?=null


        init {
            video = itemView.findViewById(R.id.custom_item_view)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_recycler_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentVideo = videoList[position]
        Glide.with(context).load(currentVideo.videoPath).apply(RequestOptions().centerCrop())
            .into(holder?.video!!)

        holder.video!!.setOnClickListener {
            val bundle= bundleOf("path" to currentVideo.videoPath,"name" to currentVideo.videoName)
            MAIN.navController.navigate(R.id.action_mediaVideoFragment_to_videoFullFragment,args=bundle)

        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }



}