package io.getstream.encryptedchat

import com.getstream.sdk.chat.adapter.MessageListItem

class EncryptedMessageListItem(messageListItem: MessageListItem) : MessageListItem(
    EncryptedMessage(messageListItem.message).decrypt(),
    messageListItem.positions,
    messageListItem.isMine
) {
    init {
        messageReadBy.addAll(messageListItem.messageReadBy)
    }
}
