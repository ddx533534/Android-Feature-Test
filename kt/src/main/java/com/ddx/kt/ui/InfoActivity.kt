package com.ddx.kt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    @Composable
    fun Page() {

        var screenController = rememberNavController()

        Column {
            NavHost(
                navController = screenController,
                startDestination = HOME,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
            ) {
                composable(ORDER) {
                    Order()
                }
                composable(HOME) {
                    Home(navHostController = screenController, userViewModel = userViewModel)
                }
                composable(PROFILE) {
                    Profile(userViewModel = userViewModel)
                }
            }

            BottomNavigation(screenController = screenController)
        }

    }

    @Composable
    fun BottomNavigation(screenController: NavHostController) {
        val current =
            screenController.currentBackStackEntryAsState().value?.destination?.route ?: HOME
        Row(
            Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(4.dp))
                .padding(top = 5.dp)
        ) {
            for (i in PageIcon.keys) {
                Image(
                    painter = painterResource(id = i),
                    contentDescription = PageIcon[i],
                    Modifier
                        .size(if (current != PageIcon[i]) 30.dp else 50.dp)
                        .weight(1.0f)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            screenController.navigate(PageIcon[i] ?: HOME) {
                                // 在导航到新页面之前清空后退栈
                                popUpTo(screenController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // 避免在后退栈中创建多个相同的目标
                                launchSingleTop = true
                                // 将状态保存到新的目标
                                restoreState = true
                            }
                        }
                )
            }
        }
    }

    @Preview
    @Composable
    fun mainContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Page()
        }
    }


}