package com.aman043358.photovideomaker.model

data class Folder(
    var path: String? = null,
    var folderName: String? = null,
    var numberOfPics: Int = 0,
    var firstPic: String? = null
){
    fun addPics() {
        numberOfPics++
    }
}