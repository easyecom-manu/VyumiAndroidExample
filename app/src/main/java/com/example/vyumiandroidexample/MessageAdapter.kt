package com.example.vyumiandroidexample

import ai.vyumi.android.livecall.model.Message

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bubble: TextView = itemView.findViewById(R.id.messageBubble)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        holder.bubble.text = msg.message
        holder.userName.text = msg.user

        if (msg.user?.lowercase() == "me") {
            holder.bubble.background.setTint("#2196F3".toColorInt()) // blue bubble
            holder.bubble.setTextColor(Color.WHITE)
        } else {
            holder.bubble.background.setTint("#E0E0E0".toColorInt()) // gray bubble
            holder.bubble.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = messages.size
}