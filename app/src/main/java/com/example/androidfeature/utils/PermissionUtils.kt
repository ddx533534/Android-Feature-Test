package com.example.androidfeature.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * 是否有存储访问权限
 */
fun hasStoragePermission(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE
    val result = ContextCompat.checkSelfPermission(context, storagePermission)
    return result == PackageManager.PERMISSION_GRANTED
}

/**
 * 是否有图片访问权限
 */
fun hasGalleryPermission(context: Context?): Boolean {
    return hasStoragePermission(context)
}
