package com.aman043358.photovideomaker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aman043358.photovideomaker.adapter.FolderAdapter
import com.aman043358.photovideomaker.adapter.ImageAdapter
import com.aman043358.photovideomaker.databinding.FragmentImageSelectionBinding
import com.aman043358.photovideomaker.model.Folder
import com.aman043358.photovideomaker.utils.Utils

class ImageSelectionFragment : Fragment(), FolderAdapter.OnImageFolderClickListener {

    lateinit var binding: FragmentImageSelectionBinding
    lateinit var folderAdapter: FolderAdapter
    lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageSelectionBinding.inflate(inflater, container, false)

        return binding.root
    }

    lateinit var check: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext()

        if (Utils.allAlbum.isEmpty()) {
            Utils.init(requireContext())
        }

        Log.d("sgdjfgjdfgds", "${Utils.allAlbum}")

        val allAlbum = Utils.allAlbum

        folderAdapter = FolderAdapter(allAlbum.keys.toList(), this)
        imageAdapter = ImageAdapter(allAlbum[allAlbum.keys.first()]!!)

        binding.rvAlbum.adapter = folderAdapter
        binding.rvImageAlbum.adapter = imageAdapter

        binding.rvAlbum.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.rvImageAlbum.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun clicked(folder: Folder, position: Int) {
        imageAdapter.imageList = Utils.allAlbum[folder] ?: emptyList()
        imageAdapter.notifyDataSetChanged()
    }
}