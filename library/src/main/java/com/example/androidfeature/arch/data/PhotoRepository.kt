package com.example.androidfeature.arch.data

import android.app.Activity
import android.content.Context
import android.provider.MediaStore
import com.example.androidfeature.bean.PhotoFolder
import com.example.androidfeature.utils.hasGalleryPermission

object PhotoRepository : DataLoad<PhotoFolder> {

    override fun syncLoad(context: Context): List<PhotoFolder> {

        // 1.创建可变图片列表结果
        val photoFolders = mutableListOf<PhotoFolder>()

        // 2.校验权限
        if (!hasGalleryPermission(context)) {
            return photoFolders
        }
        // 3.创建 map
        val photoFolderMap = mutableMapOf<String, PhotoFolder>()

        // 4.创建 contentProvider 解析
        val STORE_IMAGES = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,  // 文件目录 id
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // 文件目录名称
        )
        MediaStore.Images.Media.query(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            STORE_IMAGES,
            null,
            MediaStore.Images.Media.DATE_ADDED
        )?.use { cursor ->
            while (cursor!!.moveToNext()) {
                var photoPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                var folderID =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
                var folderName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val photoFolder =
                    photoFolderMap.getOrPut(folderID) { PhotoFolder(folderName = folderName) }
                photoFolder.photoList.add(photoPath)
            }
        }
        photoFolders.addAll(photoFolderMap.values)
        return photoFolders.toList()
    }

    override suspend fun asyncLoad(context: Context): List<PhotoFolder> {
        return syncLoad(context)
    }

}