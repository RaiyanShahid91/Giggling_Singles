package com.dil.singles.fragments.chats

class Message {
    var messageId: String? = null
    var message: String? = null
    var senderId: String? = null
    var imageUrl: String? = null
    var timestamp: Long = 0
    var feeling = -1

    constructor()

    constructor(message: String?, senderId: String?, timestamp: Long) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
    }

    @JvmName("setMessageId1")
    fun setMessageId(messageId: String) {
        this.messageId = messageId
    }

    @JvmName("getMessage1")
    fun getMessage(): String? {
        return message
    }
}
