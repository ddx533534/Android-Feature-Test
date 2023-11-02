package com.example.androidfeature.arch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfeature.bean.PhotoFolder
import com.example.androidfeature.databinding.FragmentPhotoBinding
import com.squareup.picasso.Picasso
import java.io.File

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PhotoItemRecyclerViewAdapter(private var photos: MutableList<String>?) :
    RecyclerView.Adapter<PhotoItemRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = photos?.getOrNull(position)
//        holder.picName.text = path
        Picasso.get().load(File(path)).into(holder.imgView)
    }

    override fun getItemCount(): Int = photos?.size ?: 0


    fun submit(chosenFolder: PhotoFolder?) {
        if (chosenFolder != null) {
            photos?.clear()
            photos?.addAll(chosenFolder.photoList)
            this.notifyDataSetChanged()
        }
    }

    inner class ViewHolder(binding: FragmentPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgView: ImageView = binding.itemPic
//        val picName: TextView = binding.picName
    }
}