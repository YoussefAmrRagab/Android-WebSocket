package com.example.websocket

import android.util.Log
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class WebSocketConnection(private var socket: Socket) {

    fun sendMessage(
        message: JSONObject,
        event: String,
        onError: (errorMessage: String) -> Unit
    ) = try {
        Log.d("TAG", message.toString())
        socket.emit(event, message)
    } catch (e: URISyntaxException) {
        e.printStackTrace()
        e.message?.let {
            onError(it)
        }
    }

    fun startWebSocketConnection(
        username: String,
        onConnected: (message: Message) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) = try {
        // Set up event listeners
        socket.on(
            username, onNewMessage({
                onConnected(it)
            }, {
                onError(it)
            })
        )
        socket.connect()
    } catch (e: URISyntaxException) {
        e.printStackTrace()
        e.message?.let {
            onError(it)
        }
    }

    private fun onNewMessage(
        onReceivedMessage: (message: Message) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            Log.d("TAG", data.toString())
            val sender: String = data.getString("sender")
            val message: String = data.getString("message")
            val to: String = data.getString("to")
            onReceivedMessage(Message(sender, message, to))
        } catch (e: JSONException) {
            e.printStackTrace()
            e.message?.let {
                onError(it)
            }
        }
    }

}