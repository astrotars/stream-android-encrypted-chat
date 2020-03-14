package io.getstream.encryptedchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.enums.Filters.*
import com.getstream.sdk.chat.rest.User
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel
import io.getstream.encryptedchat.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // setup the client using the example API key
    // normally you would call init in your Application class and not the activity
    StreamChat.init("whe3wer2pf4r", this.applicationContext)
    val client = StreamChat.getInstance(this.application)
    val extraData = HashMap<String, Any>()
    extraData["name"] = "John Smith"
    val currentUser = User("john", extraData)
    // User token is typically provided by your server when the user authenticates
    client.setUser(
      currentUser,
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiam9obiJ9.mIGaHTUCpX3BwcH1taQMpPld6j0m6iFNHjrvcb0puZs"
    )

    // we're using data binding in this example
    val binding: ActivityMainBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_main)
    // Specify the current activity as the lifecycle owner.
    binding.lifecycleOwner = this

    // most the business logic for chat is handled in the ChannelListViewModel view model
    val viewModel = ViewModelProviders.of(this).get(ChannelListViewModel::class.java)
    binding.viewModel = viewModel
    binding.channelList.setViewModel(viewModel, this)
    binding.channelList.setViewHolderFactory(EncryptedMessageChannelViewHolderFactory())

    // query all channels of type messaging
    val filter = and(eq("type", "messaging"), `in`("members", currentUser.id))
    viewModel.setChannelFilter(filter)

    // click handlers for clicking a user avatar or channel
    binding.channelList.setOnChannelClickListener { channel ->
      val intent = ChannelActivity.newIntent(this, channel)
      startActivity(intent)
    }
  }
}