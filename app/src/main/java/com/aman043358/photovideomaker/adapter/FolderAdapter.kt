package com.aman043358.photovideomaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aman043358.photovideomaker.databinding.ItemBinding
import com.aman043358.photovideomaker.model.Folder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FolderAdapter(
    private val folderList: List<Folder>,
    val onImageFolderClickListener: OnImageFolderClickListener
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    var currentItemId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(folderList[position])
    }

    override fun getItemCount(): Int = folderList.size

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: Folder) {
            Glide.with(binding.root).load(folder.firstPic).apply(RequestOptions().centerCrop())
                .into(binding.ivFirstImage)
            binding.tvFolderName.text = folder.folderName
            binding.cbSelect.isChecked = currentItemId == adapterPosition
            binding.cbSelect.setOnClickListener {
                onImageFolderClickListener.clicked(folder = folder, position = adapterPosition)
                currentItemId = adapterPosition
                this@FolderAdapter.notifyDataSetChanged()
            }
        }
    }

    interface OnImageFolderClickListener {
        fun clicked(folder: Folder, position: Int)
    }
}
