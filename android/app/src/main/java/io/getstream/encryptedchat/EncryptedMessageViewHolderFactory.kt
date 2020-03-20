package io.getstream.encryptedchat

import android.view.ViewGroup
import com.getstream.sdk.chat.MarkdownImpl
import com.getstream.sdk.chat.R
import com.getstream.sdk.chat.adapter.BaseMessageListItemViewHolder
import com.getstream.sdk.chat.adapter.MessageListItemAdapter
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree

class EncryptedMessageViewHolderFactory(private val eThree: EThree) : MessageViewHolderFactory() {
    override fun createMessageViewHolder(
        adapter: MessageListItemAdapter?,
        parent: ViewGroup?,
        viewType: Int
    ): BaseMessageListItemViewHolder {
        if (viewType == MESSAGEITEM_MESSAGE) {
            val holder = EncryptedMessageViewHolder(R.layout.stream_item_message, parent, eThree)
            holder.setViewHolderFactory(this)
            holder.setStyle(adapter!!.style)
            holder.setMarkdownListener(MarkdownImpl.getMarkdownListener())
            holder.setMessageClickListener(adapter.messageClickListener)
            holder.setMessageLongClickListener(adapter.messageLongClickListener)
            holder.setAttachmentClickListener(adapter.attachmentClickListener)
            holder.setReactionViewClickListener(adapter.reactionViewClickListener)
            holder.setUserClickListener(adapter.userClickListener)
            holder.setReadStateClickListener(adapter.readStateClickListener)
            holder.setGiphySendListener(adapter.giphySendListener)
            return holder
        } else {
            return super.createMessageViewHolder(adapter, parent, viewType)
        }
    }
}

