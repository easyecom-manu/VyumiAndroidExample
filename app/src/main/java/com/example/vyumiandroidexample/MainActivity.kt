package com.example.vyumiandroidexample

import ai.vyumi.android.livecall.VyumiLiveCall
import ai.vyumi.android.livecall.utils.CallSessionCallback
import ai.vyumi.android.livecall.utils.DataCallBack
import android.Manifest
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val allGranted = permissions.entries.all { it.value }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        VyumiLiveCall.initialize("258e52a321")
        requestCameraAndMicPermissions()

        val buttonInstantCallPrebuild = findViewById<MaterialButton>(R.id.buttonInstantCallPrebuild)
        val buttonInstantCallCustomUi = findViewById<MaterialButton>(R.id.buttonInstantCallCustomUi)
        val buttonTimeSlots = findViewById<MaterialButton>(R.id.buttonTimeSlots)
        val buttonSchedule = findViewById<MaterialButton>(R.id.buttonSchedule)

        buttonInstantCallPrebuild.setOnClickListener {
            initiateInstantCall(true)
        }

        buttonInstantCallCustomUi.setOnClickListener {
            initiateInstantCall(false)
        }

        buttonTimeSlots.setOnClickListener {
            VyumiLiveCall.getTimeSlots("Asia/Calcutta", object : DataCallBack<JSONObject?> {
                override fun onSuccess(data: JSONObject?) {
                    Log.i("SCHEDULES", data?.toString() ?: "")

                }

                override fun onError(error: String) {
                    Log.i("SCHEDULES", error)
                }
            })
        }


    }

    private fun requestCameraAndMicPermissions() {

        val permissionsToRequest = mutableListOf<String>()

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }

        if (permissionsToRequest.isNotEmpty()) {

            permissionLauncher.launch(
                permissionsToRequest.toTypedArray()
            )

        }
    }

    fun initiateInstantCall(isPreBuildUi: Boolean) {
        VyumiLiveCall.setProductURL("https://getepik.in/products/ecovacs-n30-white")
        val userPayload = JSONObject().apply {
            put("name", "Test User")
            put("mobile_no", "0988767854")
            put("country_code", "+91")
        }
        VyumiLiveCall.makeInstantCall(userPayload, "video", object : CallSessionCallback {
            override fun onRoomReady(message: String) {
                Log.e("onRoomReady", message)
                if (isPreBuildUi) {
                    applicationContext.startActivity(
                        Intent(
                            applicationContext,
                            PrebuildUIActivity::class.java
                        ).addFlags(FLAG_ACTIVITY_NEW_TASK)
                    )

                } else {
                    applicationContext.startActivity(
                        Intent(
                            applicationContext,
                            CustomUIActivity::class.java
                        ).addFlags(FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }

            override fun onAgentNotAvailable(message: String) {
                Log.e("onAgentNotAvailable", message)
            }

            override fun onError(message: String) {
                Log.e("onError", message)
            }

            override fun onAgentAssigned(message: String) {
                Log.e("onAgentAssigned", message)
            }

            override fun onQueueUpdate(message: String, quePosition: Int) {
                Log.e("onAgentAssigned", "${message} position ${quePosition}")
            }

            override fun onConnectingToAgent(message: String) {
                Log.e("onConnectingToAgent", message)
            }
        })

    }

    fun getTimeSlots() {}

}

