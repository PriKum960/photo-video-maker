package com.aman043358.photovideomaker.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.aman043358.photovideomaker.model.Image
import com.aman043358.photovideomaker.model.Folder

object Utils {

    var allAlbum = HashMap<Folder, List<Image>>()

    fun init(ctx: Context) {
        val picPaths = ArrayList<String>()
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        val folderList = ctx.contentResolver.query(allImagesUri, projection, null, null, null)
            ?.use { cursor -> extractImageFolders(cursor, picPaths) } ?: arrayListOf()

        allAlbum.apply {
            folderList.forEach { folder ->
                this[folder] = getAllImagesByFolder(ctx, folder.path!!)
            }
        }
    }

    private fun extractImageFolders(
        cursor: Cursor,
        picPaths: ArrayList<String>
    ): ArrayList<Folder> {
        val folders = ArrayList<Folder>()

        if (cursor.moveToFirst()) {
            do {
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val folder =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val datapath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val folderPaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                    .let { "$it$folder/" }

                if (!picPaths.contains(folderPaths)) {
                    picPaths.add(folderPaths)
                    folders.add(createImageFolder(folderPaths, folder, datapath))
                } else {
                    updateExistingImageFolder(folders, folderPaths, datapath)
                }
            } while (cursor.moveToNext())
        }

        return folders
    }

    private fun createImageFolder(folderPath: String, folderName: String, firstPicPath: String) =
        Folder().apply {
            this.path = folderPath
            this.folderName = folderName
            this.firstPic = firstPicPath
            this.addPics()
        }

    private fun updateExistingImageFolder(
        folders: ArrayList<Folder>,
        folderPath: String,
        firstPicPath: String
    ) {
        for (imageFolder in folders) {
            if (imageFolder.path == folderPath) {
                imageFolder.firstPic = firstPicPath
                imageFolder.addPics()
            }
        }
    }

    fun getAllImagesByFolder(context: Context, path: String): List<Image> {
        val images = ArrayList<Image>()
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val selection = "${MediaStore.Images.Media.DATA} like ?"
        val selectionArgs = arrayOf("%$path%")
        val sortOrder = null

        context.contentResolver.query(
            allImagesUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                images.add(
                    Image(
                        name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                        siz = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    )
                )
            }
        }

        return images.reversed()
    }


/*    fun getAllImagesByFolder(ctx: Context, path: String): ArrayList<Image> {
        var images = ArrayList<Image>()
        val allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val cursor: Cursor = ctx.contentResolver.query(
            allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", arrayOf<String>(
                "%$path%"
            ), null
        )!!
        try {
            cursor.moveToFirst()
            do {
                images.add(Image().apply {
                    this.picturName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    this.picturePath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    this.pictureSize =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                })
            } while (cursor.moveToNext())
            cursor.close()
            val reSelection = ArrayList<Image>()
            for (i in images.size - 1 downTo -1 + 1) {
                reSelection.add(images[i])
            }
            images = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }*/
}