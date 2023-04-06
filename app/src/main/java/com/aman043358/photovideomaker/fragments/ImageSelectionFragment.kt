package com.aman043358.photovideomaker.fragments

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman043358.photovideomaker.R
import com.aman043358.photovideomaker.adpater.MyAlbumAdapter
import com.aman043358.photovideomaker.databinding.FragmentImageSelectionBinding
import java.io.File

class ImageSelectionFragment : Fragment(R.layout.fragment_image_selection) {


    lateinit var binding: FragmentImageSelectionBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageSelectionBinding.bind(view)







    }


    fun getVideoFilesPath(): ArrayList<String> {

        val videoPathArr = arrayListOf<String>()
        val internalStoragePath = File(Environment.getExternalStorageDirectory().absolutePath)
        val folderPath = File(internalStoragePath, "Test")

        if (folderPath.exists() && folderPath.isDirectory) {
            val listFiles = folderPath.listFiles()

            if (listFiles != null) {
                for (file in listFiles) {
                    if (file.name.endsWith(".mp4")) {
                        videoPathArr.add(file.path)

                    }
                }
                return videoPathArr
            } else {
                Toast.makeText(binding.root.context, "no files found", Toast.LENGTH_SHORT).show()
                return arrayListOf()

            }

        } else {
            Toast.makeText(binding.root.context, "folder not found", Toast.LENGTH_SHORT).show()
            return arrayListOf()
        }
    }

}