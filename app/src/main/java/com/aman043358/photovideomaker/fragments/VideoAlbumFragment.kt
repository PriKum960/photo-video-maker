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
import com.aman043358.photovideomaker.databinding.FragmentVideoAlbumBinding
import java.io.File

class VideoAlbumFragment : Fragment(R.layout.fragment_video_album) {

    lateinit var binding: FragmentVideoAlbumBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVideoAlbumBinding.bind(view)

        val videoPathArr = arrayListOf<String>()
        val internalStoragePath = File(Environment.getExternalStorageDirectory().absolutePath)
        val folderPath = File(internalStoragePath, "Test")

        if (folderPath.exists() && folderPath.isDirectory) {
            val listFiles = folderPath.listFiles()

            if (listFiles != null) {
                for (file in listFiles) {
                    if (file.name.endsWith(".mp4"))
                        videoPathArr.add(file.path)

                }
            } else
                Toast.makeText(binding.root.context, "no files found", Toast.LENGTH_SHORT).show()

        } else
            Toast.makeText(binding.root.context, "folder not found", Toast.LENGTH_SHORT).show()



        binding.rcv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rcv.adapter = MyAlbumAdapter(videoPathArr)
    }

}