package io.getstream.encryptedchat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.getstream.sdk.chat.MarkdownImpl
import com.getstream.sdk.chat.adapter.BaseChannelListItemViewHolder
import com.getstream.sdk.chat.adapter.ChannelListItemAdapter
import com.getstream.sdk.chat.adapter.ChannelViewHolderFactory

class EncryptedMessageChannelViewHolderFactory : ChannelViewHolderFactory() {
    override fun createChannelViewHolder(
        adapter: ChannelListItemAdapter?,
        parent: ViewGroup?,
        viewType: Int
    ): BaseChannelListItemViewHolder? {
        val style = adapter!!.style
        val v = LayoutInflater.from(parent!!.context)
            .inflate(style.channelPreviewLayout, parent, false)
        val holder = EncryptedMessageChannelListItemViewHolder(v)
        holder.setStyle(style)
        holder.setMarkdownListener(MarkdownImpl.getMarkdownListener())
        holder.setChannelClickListener(adapter.channelClickListener)
        holder.setChannelLongClickListener(adapter.channelLongClickListener)
        holder.setUserClickListener(adapter.userClickListener)
        return holder
    }
}
