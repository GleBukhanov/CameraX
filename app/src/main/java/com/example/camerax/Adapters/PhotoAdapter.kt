package com.example.camerax.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.camerax.Data.Photo
import com.example.camerax.MAIN
import com.example.camerax.R

class PhotoAdapter(private var context: Context, private var photoList: ArrayList<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView? = null

       // preview ImageView
       // video VideoView

        init {
            photo = itemView.findViewById(R.id.custom_item_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_recycler_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = photoList[position]
        Glide.with(context).load(currentPhoto.photoPath).apply(RequestOptions().centerCrop())
            .into(holder?.photo!!)

        holder.photo?.setOnClickListener {
            val bundle= bundleOf("path" to currentPhoto.photoPath,"name" to currentPhoto.photoName)
            MAIN.navController.navigate(R.id.action_mediaFragment_to_photoFullFragment,args=bundle)

        }
    }
}