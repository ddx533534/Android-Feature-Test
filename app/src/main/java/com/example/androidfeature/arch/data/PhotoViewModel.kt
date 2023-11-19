package com.example.androidfeature.arch.data

import android.app.Application
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.androidfeature.activity.TAG
import com.example.androidfeature.bean.PhotoFolder
import com.example.androidfeature.utils.hasGalleryPermission
import kotlin.math.roundToInt

class PhotoViewModel(application: Application) : AndroidViewModel(application) {


    val _progress = MutableLiveData<Int>()
    val _folders = MutableLiveData<List<PhotoFolder>>()
    val _chosenFolder = MutableLiveData<PhotoFolder>()


    // 初始化
//    init {
//        _folders.value = PhotoRepository.syncLoad(application).filter {
//            it.photoList.isNotEmpty()
//        }
//        _chosenFolder.value = _folders.value?.firstOrNull()
//
//    }

    init {
        _progress.value = 0
        _folders.value = PhotoRepository.aSyncLoad(application, _progress)
        _chosenFolder.value = _folders.value?.firstOrNull()
    }

    fun PhotoRepository.aSyncLoad(
        application: Application,
        progress: MutableLiveData<Int>
    ): List<PhotoFolder> {

        // 1.创建可变图片列表结果
        val photoFolders = mutableListOf<PhotoFolder>()

        // 2.校验权限
        if (!hasGalleryPermission(application)) {
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
            application.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            STORE_IMAGES,
            null,
            MediaStore.Images.Media.DATE_ADDED
        )?.use { cursor ->
            var count = cursor.count
            var preprogress = 0
            var curProgress = 0
            var interval = 5
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

                curProgress = (cursor.position.toDouble() / count * 100).roundToInt()
                Log.d(TAG,"""$curProgress""")
                if (curProgress - preprogress >= interval) {
                    progress.postValue(curProgress)
                    preprogress = curProgress
                }
            }
            progress.postValue(100)
        }
        photoFolders.addAll(photoFolderMap.values)
        return photoFolders.toList()
    }

    // 随机文件
    fun randomFolder() {
        _chosenFolder.value = _folders.value?.randomOrNull()
    }

    fun getChosenFolder(): PhotoFolder? {
        return _chosenFolder.value
    }

    fun getProgress(): Int {
        return _progress.value ?: 0
    }

}