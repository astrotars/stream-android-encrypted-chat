package io.getstream.encryptedchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.model.Channel
import com.getstream.sdk.chat.viewmodel.ChannelViewModel
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory
import com.virgilsecurity.android.common.exceptions.RegistrationException
import com.virgilsecurity.android.ethree.kotlin.callback.OnGetTokenCallback
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import io.getstream.encryptedchat.databinding.ActivityChannelBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ChannelActivity : AppCompatActivity() {
    private var viewModel: ChannelViewModel? = null
    private var binding: ActivityChannelBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelType = intent.getStringExtra(EXTRA_CHANNEL_TYPE)!!
        val channelID = intent.getStringExtra(EXTRA_CHANNEL_ID)!!
        val virgilToken = intent.getStringExtra(EXTRA_VIRGIL_TOKEN)!!

        val client = StreamChat.getInstance(application)
        val eThree = EThree.initialize(this, object : OnGetTokenCallback {
            override fun onGetToken() = virgilToken
        }).get()

        doAsync {
            val channel = client.channel(channelType, channelID)

            try {
                eThree.register().execute()
            } catch (e: RegistrationException) {
                // already registered
            }

            val receiverPublicKeys = eThree.lookupPublicKeys("dewey").get()

            uiThread { context ->
                viewModel = ViewModelProviders.of(
                    context,
                    ChannelViewModelFactory(context.application, channel)
                ).get(ChannelViewModel::class.java)

                binding!!.viewModel = viewModel
                binding!!.messageList.setViewHolderFactory(EncryptedMessageViewHolderFactory(eThree))
                binding!!.messageList.setViewModel(viewModel!!, context)
                binding!!.messageInput.setViewModel(viewModel, context)
                binding!!.messageInput.eThree = eThree
                binding!!.messageInput.receiverPublicKeys = receiverPublicKeys
                binding!!.channelHeader.setViewModel(viewModel, context)
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel)
        binding!!.lifecycleOwner = this

    }

    companion object {
        private val EXTRA_CHANNEL_TYPE = "io.getstream.encryptedchat.CHANNEL_TYPE"
        private val EXTRA_CHANNEL_ID = "io.getstream.encryptedchat.CHANNEL_ID"
        private val EXTRA_VIRGIL_TOKEN = "io.getstream.encryptedchat.VIRGIL_TOKEN"

        fun newIntent(context: Context, channel: Channel, virgilToken: String): Intent {
            val intent = Intent(context, ChannelActivity::class.java)
            intent.putExtra(EXTRA_CHANNEL_TYPE, channel.type)
            intent.putExtra(EXTRA_CHANNEL_ID, channel.id)
            intent.putExtra(EXTRA_VIRGIL_TOKEN, virgilToken)
            return intent
        }
    }

}