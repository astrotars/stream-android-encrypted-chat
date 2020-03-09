package io.getstream.encryptedchat

import android.content.Context
import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.adapter.MessageListItemViewHolder
import com.getstream.sdk.chat.rest.response.ChannelState

class EncryptedMessageViewHolder(resId: Int, viewGroup: ViewGroup?) :
    MessageListItemViewHolder(resId, viewGroup) {

    override fun bind(
        context: Context,
        channelState: ChannelState,
        messageListItem: MessageListItem,
        position: Int
    ) {
        super.bind(context, channelState, EncryptedMessageListItem(messageListItem), position)
    }
}