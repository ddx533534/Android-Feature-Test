package com.example.androidfeature.bean

import java.io.Serializable

/**
 * 相册数据 bean
 */
class PhotoFolder(
    var folderName: String? = null,
    val photoList: MutableList<String> = mutableListOf()
) : Serializable {

    companion object {
        const val serialVersionUID = 1L
    }

}
