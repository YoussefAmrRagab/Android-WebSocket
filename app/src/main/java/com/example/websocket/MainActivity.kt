package com.example.websocket

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.websocket.databinding.ActivityMainBinding
import io.socket.client.IO


class MainActivity : AppCompatActivity() {

    private val username: String = "user1" // user1
    private val client: String = "user2" // user2
    private val event: String = "sendToUser"
    private lateinit var webSocketConnection: WebSocketConnection
    private lateinit var adapter: ChatAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ChatAdapter(username)
        val messageList = mutableListOf<Message>()

        webSocketConnection = WebSocketConnection(IO.socket("http://192.168.1.8:8878/"))
        webSocketConnection.startWebSocketConnection(username, {
            messageList.add(it)
            adapter.isUserSending = false
            runOnUiThread {
                submitAdapter(messageList)
            }
        }, {
            toast(it)
        })

        binding.button.setOnClickListener {
            val editText = binding.message.text
            val message = Message(username, editText.toString(), client)
            webSocketConnection.sendMessage(message.toJson(), event) {
                adapter.isUserSending = true
                toast(it)
            }
            messageList.add(message)
            submitAdapter(messageList)
            editText.clear()
        }
    }

    private fun submitAdapter(messageList: List<Message>) {
        adapter.submitList(messageList)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.smoothScrollToPosition(messageList.size)
    }

    private fun toast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        Log.d("TAG", message)
    }

}