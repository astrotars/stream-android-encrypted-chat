package io.getstream.encryptedchat

import com.getstream.sdk.chat.rest.Message
import com.getstream.sdk.chat.rest.response.ChannelState

class EncryptedMessageChannelState(channelState: ChannelState) :
    ChannelState(channelState.channel) {
    override fun getLastMessage(): Message? {
        var lastMessage = super.getLastMessage()
        if (lastMessage != null) {
            lastMessage = EncryptedMessage(lastMessage).decrypt()
        }
        return lastMessage
    }
}
