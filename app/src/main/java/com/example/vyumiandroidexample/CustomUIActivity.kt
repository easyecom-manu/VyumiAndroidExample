package com.example.vyumiandroidexample

import ai.vyumi.android.livecall.VyumiLiveCall
import ai.vyumi.android.livecall.model.Message
import ai.vyumi.android.livecall.utils.CallInteractionCallback
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CustomUIActivity : AppCompatActivity() {
    lateinit var remoteView: TextureView
    lateinit var localView: TextureView

    var mic = true
    var camera = false
    var useBackCamera = false
    private var messageAdapter: MessageAdapter? = null
    private val messageList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_uiactivity)
        remoteView = findViewById(R.id.remoteView)
        localView = findViewById(R.id.localView)

        val btnMic = findViewById<MaterialButton>(R.id.btnMic)
        val btnCamera = findViewById<MaterialButton>(R.id.btnCamera)
        val btnCameraFlip = findViewById<MaterialButton>(R.id.btnCameraFlip)
        val endCall = findViewById<MaterialButton>(R.id.endCall)
        val btnMessage = findViewById<MaterialButton>(R.id.btnMessage)

        btnCameraFlip.setOnClickListener {
            useBackCamera = !useBackCamera
            VyumiLiveCall.useBackCamera(useBackCamera)
        }

        btnCamera.setOnClickListener {
            camera = !camera
            VyumiLiveCall.enableCamera(camera)
        }

        btnMic.setOnClickListener {
            mic = !mic
            VyumiLiveCall.muteMicrophone(mic)
        }

        endCall.setOnClickListener {
            VyumiLiveCall.endCall()
            finish()
        }

        btnMessage.setOnClickListener {
            showChatBottomSheet()
        }

        VyumiLiveCall.joinCall(
            application,
            localView,
            remoteView,
            object : CallInteractionCallback {
                override fun onReceivingMessage(message: Message) {
                    Log.e("onReceivingMessage", message.message ?: "")
                    messageList.add(message)
                }

                override fun onRemoteMicUpdate(muted: Boolean) {
                    Log.e("onRemoteMicUpdate", "${muted}")
                }

                override fun onRemoteCameraUpdate(enabled: Boolean) {
                    Log.e("onRemoteMicUpdate", "${enabled}")
                }

                override fun onCallEnded() {
                    VyumiLiveCall.endCall()
                    finish()
                }
            })


    }

    private fun showChatBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.message_bottomsheet)

        val btnCancel = bottomSheetDialog.findViewById<ImageButton>(R.id.btn_cancel)
        val btnSend = bottomSheetDialog.findViewById<ImageButton>(R.id.btnSend)
        val recyclerView = bottomSheetDialog.findViewById<RecyclerView>(R.id.shortPickRecycler)
        val etMessage = bottomSheetDialog.findViewById<TextInputEditText>(R.id.etMessage)


        messageAdapter = MessageAdapter(messageList)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = messageAdapter


        btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }


        btnSend?.setOnClickListener {
            val text = etMessage?.text?.toString()?.trim()
            if (!text.isNullOrEmpty()) {
                val msg = Message(text, "Me")
                messageList.add(msg)
                messageAdapter?.notifyItemInserted(messageList.size - 1)
                VyumiLiveCall.sendMessage(text)
                recyclerView?.scrollToPosition(messageList.size - 1)
                etMessage.setText("")
            }
        }

        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        VyumiLiveCall.endCall()
    }
}