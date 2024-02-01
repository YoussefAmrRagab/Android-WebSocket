package com.example.websocket


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.websocket.databinding.ChatItemBinding

class ChatAdapter(private val username: String) :
    ListAdapter<Message, ChatAdapter.ViewHolder>(MessageDiffCallBack()) {
    var isUserSending = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.binding(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, username, isUserSending)
    }

    class ViewHolder private constructor(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun binding(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChatItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Message, username: String, isUserSending: Boolean) {
            binding.messageContent = item
            binding.isClient = item.to == username
            binding.isClientName = item.to != username && isUserSending
            binding.executePendingBindings()
            Log.d("TAG", binding.username.text.toString())
        }
    }

    class MessageDiffCallBack : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

}
