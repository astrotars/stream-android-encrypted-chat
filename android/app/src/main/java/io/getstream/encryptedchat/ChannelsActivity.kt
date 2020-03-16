package io.getstream.encryptedchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.getstream.sdk.chat.enums.Filters.*
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel
import io.getstream.encryptedchat.databinding.ActivityChannelsBinding

class ChannelsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // we're using data binding in this example
    val binding: ActivityChannelsBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_channels)
    // Specify the current activity as the lifecycle owner.
    binding.lifecycleOwner = this

    // most the business logic for chat is handled in the ChannelListViewModel view model
    val viewModel = ViewModelProviders.of(this).get(ChannelListViewModel::class.java)
    binding.viewModel = viewModel
    binding.channelList.setViewModel(viewModel, this)
    binding.channelList.setViewHolderFactory(EncryptedMessageChannelViewHolderFactory())

    // query all channels of type messaging
    val filter = and(eq("type", "messaging"), `in`("members", intent.getStringExtra(EXTRA_USER)))
    viewModel.setChannelFilter(filter)

    // click handlers for clicking a user avatar or channel
    binding.channelList.setOnChannelClickListener { channel ->
      val intent = ChannelActivity.newIntent(this, channel)
      startActivity(intent)
    }
  }

  companion object {
    private val EXTRA_USER = "io.getstream.encryptedchat.USER"
    private val EXTRA_VIRGIL_TOKEN = "io.getstream.encryptedchat.VIRGIL_TOKEN"

    fun newIntent(context: Context, user: String, virgilToken: String): Intent {
      val intent = Intent(context, ChannelsActivity::class.java)
      intent.putExtra(EXTRA_USER, user)
      intent.putExtra(EXTRA_VIRGIL_TOKEN, virgilToken)
      return intent
    }
  }
}