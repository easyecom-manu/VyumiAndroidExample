package com.example.vyumiandroidexample

import ai.vyumi.android.livecall.VyumiLiveCall
import ai.vyumi.android.livecall.prebuild.VyumiLiveCallView
import ai.vyumi.android.livecall.utils.VyumiLiveCallCallback
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrebuildUIActivity : AppCompatActivity() {
    var liveCallView: VyumiLiveCallView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prebuild_uiactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        liveCallView = findViewById<VyumiLiveCallView>(R.id.liveCallView)

        liveCallView?.initCall(application, object : VyumiLiveCallCallback {
            override fun onCallEnded() {
                finish()

            }

            override fun onError() {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
                finish()

            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        liveCallView?.endCall()
    }
}