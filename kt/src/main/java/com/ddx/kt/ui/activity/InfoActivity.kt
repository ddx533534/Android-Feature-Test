package com.ddx.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.ddx.kt.ui.compose.NavGraph
import com.ddx.kt.ui.compose.REQUEST_CODE_ALBUM
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R


const val HOME = "home"
const val ORDER = "order"
const val PROFILE = "profile"
val PageIcon =
    mapOf(R.drawable.order to ORDER, R.drawable.home to HOME, R.drawable.profile to PROFILE)

class InfoActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContent {
            mainContent()
        }
    }

    

    @Preview
    @Composable
    fun mainContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavGraph(userViewModel = userViewModel)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK) {
        }
    }
}