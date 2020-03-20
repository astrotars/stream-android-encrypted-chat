package io.getstream.encryptedchat

import android.content.Context
import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.adapter.MessageListItemViewHolder
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory.MESSAGEITEM_MESSAGE
import com.getstream.sdk.chat.model.ModelType
import com.getstream.sdk.chat.rest.Message
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
        if (messageListItem.type != MESSAGEITEM_MESSAGE || messageListItem.message.type != ModelType.message_regular) {
            super.bind(context, channelState, messageListItem, position)
            return
        }

        doAsync {
            val decryptedMessage = messageListItem.message.copy()
            if (messageListItem.isMine) {
                decryptedMessage.text = eThree.decrypt(decryptedMessage.text)
            } else {
                val publicKey = eThree.lookupPublicKeys(decryptedMessage.user.id)
                    .get()[decryptedMessage.user.id]
                decryptedMessage.text = eThree.decrypt(decryptedMessage.text, publicKey)
            }

            val decryptedItem =
                MessageListItem(decryptedMessage, messageListItem.positions, messageListItem.isMine)

            uiThread {
                super.bind(context, channelState, decryptedItem, position)
            }
        }
    }

}