package io.getstream.encryptedchat

import android.content.Context
import android.view.View
import com.getstream.sdk.chat.adapter.ChannelListItemViewHolder
import com.getstream.sdk.chat.rest.response.ChannelState
import org.jetbrains.annotations.NotNull

class SimpleChannelListItemViewHolder(
  @NotNull itemView: View
) : ChannelListItemViewHolder(itemView) {
  override fun bind(context: Context?, channelState: ChannelState, position: Int) {
    super.bind(context, SimpleChannelState(channelState), position)
  }
}
