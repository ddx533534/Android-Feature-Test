package com.example.androidfeature.arch.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.androidfeature.bean.PhotoFolder

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    val _folders = MutableLiveData<List<PhotoFolder>>()
    val _chosenFolder = MutableLiveData<PhotoFolder>()


    // 初始化
    init {
        _folders.value = PhotoRepository.syncLoad(application).filter {
            it.photoList.isNotEmpty()
        }
        _chosenFolder.value = _folders.value?.firstOrNull()
    }

    // 更新文件列表
    fun updateFolders(folders: List<PhotoFolder>) {
        _folders.value = folders
    }

    // 更新选中文件
    fun updateChosenFolder(folder: PhotoFolder) {
        _chosenFolder.value = folder
    }

    // 随机文件
    fun randomFolder() {
        _chosenFolder.value = _folders.value?.randomOrNull()
    }

    fun getChosenFolder(): PhotoFolder? {
        return _chosenFolder.value
    }

}