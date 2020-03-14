package io.getstream.encryptedchat

import com.getstream.sdk.chat.rest.Message

class EncryptedMessage(private val encryptedMessage: Message) {
    fun decrypt() : Message {
        val message = encryptedMessage.copy()
        message.text = "decrypted text"
        return message
    }
}
