package com.example.messagingtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenResumed {
            val loginResponse = apolloClient(this@MainActivity).mutation(
                LoginMutation(
                    "student02@univerlive.com",
                    "123456"
                )
            ).execute()
            User.setToken(this@MainActivity, loginResponse.data!!.authenticate!!.token)
        }

        lifecycleScope.launchWhenResumed {
            apolloClient(this@MainActivity).subscription(MessageSubscription()).toFlow()
                .retryWhen { _, attempt ->
                    delay(attempt * 1000)
                    true
                }
                .collect {
                    Log.d("createdMessage", it.data?.Message.toString())
                }
        }

    }
}