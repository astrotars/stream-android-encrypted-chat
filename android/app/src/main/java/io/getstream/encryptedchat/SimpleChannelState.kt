package io.getstream.encryptedchat

import com.getstream.sdk.chat.rest.Message
import com.getstream.sdk.chat.rest.response.ChannelState
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree

class SimpleChannelState(channelState: ChannelState) :
  ChannelState(channelState.channel) {
  override fun getLastMessage(): Message? {
    return null
  }
}
