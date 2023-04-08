package com.aman043358.photovideomaker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman043358.photovideomaker.databinding.ImageItemBinding
import com.aman043358.photovideomaker.databinding.ItemBinding
import com.aman043358.photovideomaker.model.Image
import com.aman043358.photovideomaker.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageAdapter(
    var imageList: List<Image>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size

    inner class ViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {
            Glide.with(binding.root).load(image.path).apply(RequestOptions().centerCrop())
                .into(binding.ivFirstImage)
            binding.ivFirstImage.setOnClickListener {
                if (binding.ivSelected.visibility == View.VISIBLE) {
                    binding.ivSelected.visibility = View.GONE
                    Utils.images.remove(image)
                } else {
                    binding.ivSelected.visibility = View.VISIBLE
                    Utils.images.add(image)
                }
            }
        }
    }
}