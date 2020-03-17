package io.getstream.encryptedchat

import android.content.Context
import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.adapter.MessageListItemViewHolder
import com.getstream.sdk.chat.rest.response.ChannelState
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EncryptedMessageViewHolder(
    resId: Int,
    viewGroup: ViewGroup?,
    private val eThree: EThree
) :
    MessageListItemViewHolder(resId, viewGroup) {

    override fun bind(
        context: Context,
        channelState: ChannelState,
        messageListItem: MessageListItem,
        position: Int
    ) {
        doAsync {
            val item = messageListItem.copy()
            val message = messageListItem.message
            if(item.isMine) {
                message.text = eThree.decrypt(message.text)
            } else {
                val publicKey = eThree.lookupPublicKeys(message.user.id).get()[message.user.id]
                message.text = eThree.decrypt(message.text, publicKey)
            }

            uiThread {
                super.bind(
                    context,
                    channelState,
                    item,
                    position
                )
            }
        }
    }
}