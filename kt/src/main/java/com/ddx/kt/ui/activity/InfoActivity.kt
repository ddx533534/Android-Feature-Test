package com.ddx.kt.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ddx.kt.ui.compose.Home
import com.ddx.kt.ui.compose.NavGraph
import com.ddx.kt.ui.compose.Order
import com.ddx.kt.ui.compose.Profile
import com.ddx.kt.ui.compose.REQUEST_CODE_ALBUM
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R
import com.zhihu.matisse.Matisse


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
            val uri = Matisse.obtainResult(data)
            Toast.makeText(this,"$uri",Toast.LENGTH_SHORT).show()
        }
    }
}