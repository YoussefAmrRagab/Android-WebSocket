package com.example.websocket

import org.json.JSONObject

data class Message(val sender: String, val message: String, val to: String) {

    fun toJson(): JSONObject {
        val messageObject = JSONObject()
        messageObject.put("sender", sender)
        messageObject.put("message", message)
        messageObject.put("to", to)
        return messageObject
    }

}
