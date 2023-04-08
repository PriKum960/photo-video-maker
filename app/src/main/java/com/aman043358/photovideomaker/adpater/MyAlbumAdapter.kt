package com.aman043358.photovideomaker.adpater

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.aman043358.photovideomaker.R
import com.aman043358.photovideomaker.databinding.MyAlbumItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class MyAlbumAdapter(val pathArr: ArrayList<String>) :
    RecyclerView.Adapter<MyAlbumAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: MyAlbumItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            Glide.with(binding.root)
                .load(File(pathArr[pos]))
                .apply(RequestOptions().centerCrop())
                .into(binding.iv)

            binding.tv.text = File(pathArr[pos]).name
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(pathArr[pos]), "video/*")
                startActivity(
                    binding.root.context,
                    Intent.createChooser(intent, "play video using"),
                    null
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = MyAlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return pathArr.size
    }
}