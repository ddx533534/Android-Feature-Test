package com.ddx.kt.ui.compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ddx.kt.ui.activity.LoginActivity
import com.ddx.kt.viewmodel.UserInfo

class LoginContract : ActivityResultContract<Any, UserInfo?>() {

    companion object {
        const val Key_Request = "key_request"
        const val Key_Response = "key_response"

        internal fun buildResult(userInfo: UserInfo) : Intent{
            val intent = Intent()
            intent.putExtra(Key_Response,userInfo)
            return intent
        }

    }

    override fun createIntent(context: Context, input: Any?): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): UserInfo? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            intent.getParcelableExtra(Key_Response)
        } else {
            null
        }
    }

}