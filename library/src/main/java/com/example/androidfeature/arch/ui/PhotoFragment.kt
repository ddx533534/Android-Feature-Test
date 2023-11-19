package com.example.androidfeature.arch.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfeature.R
import com.example.androidfeature.arch.data.BlankViewModelFactory
import com.example.androidfeature.arch.data.PhotoViewModel
import com.example.androidfeature.utils.hasGalleryPermission

/**
 * A fragment representing a list of Items.
 */
const val STORAGE_PERMISSION_REQUEST_CODE = 12

class PhotoFragment : Fragment() {

    private lateinit var mAdapter: PhotoItemRecyclerViewAdapter
    private lateinit var viewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            BlankViewModelFactory(activity!!.application)
        ).get(PhotoViewModel::class.java)
        if (!hasGalleryPermission(context)) {
            requestStoragePermission(activity as Activity)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_list, container, false)
        val progressView = view.findViewById<TextView>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val folder = view.findViewById<TextView>(R.id.folder)
        val button = view.findViewById<Button>(R.id.next_folder)

        mAdapter = PhotoItemRecyclerViewAdapter(viewModel.getChosenFolder()?.photoList)
        recyclerView.layoutManager = GridLayoutManager(context, COLUMN_COUNT)
        recyclerView.adapter = mAdapter

        viewModel._chosenFolder.observe(this, Observer {
            it?.let {
                mAdapter.submit(it)
                // 数据变更后向上传递，UI 更新
                folder.text = it.folderName
            }
        })
        viewModel._progress.observe(this, Observer {
            progressView.text = """$it%"""
        })

        button.setOnClickListener {
            // 事件向下传递，数据变更
            viewModel.randomFolder()
        }
        return view
    }


    private fun requestStoragePermission(activity: Activity) {
        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (activity.shouldShowRequestPermissionRationale(storagePermission)) {
            // 如果用户之前拒绝了权限请求，弹出一个对话框解释为什么需要该权限
            AlertDialog.Builder(activity)
                .setTitle("权限请求")
                .setMessage("需要存储权限来访问您的文件")
                .setPositiveButton("确定") { dialog, which ->
                    activity.requestPermissions(
                        arrayOf(storagePermission),
                        STORAGE_PERMISSION_REQUEST_CODE
                    )
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            // 直接请求权限
            activity.requestPermissions(
                arrayOf(storagePermission),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了存储权限
                // 在这里处理权限被授予后的逻辑

            } else {
                // 用户拒绝了存储权限
                // 在这里处理权限被拒绝后的逻辑
                activity?.finish()
            }
        }
    }


    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        const val COLUMN_COUNT = 4

        @JvmStatic
        fun newInstance(columnCount: Int) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}