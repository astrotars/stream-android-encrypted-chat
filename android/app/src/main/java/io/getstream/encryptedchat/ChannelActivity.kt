package io.getstream.encryptedchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.model.Channel
import com.getstream.sdk.chat.model.ModelType
import com.getstream.sdk.chat.rest.interfaces.ChannelCallback
import com.getstream.sdk.chat.rest.response.ChannelResponse
import com.getstream.sdk.chat.viewmodel.ChannelViewModel
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory
import com.virgilsecurity.android.common.data.model.LookupResult
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

        val user = intent.getStringExtra(EXTRA_USER)!!
        val otherUser = intent.getStringExtra(EXTRA_OTHER_USER)!!
        val virgilToken = intent.getStringExtra(EXTRA_VIRGIL_TOKEN)!!

        val client = StreamChat.getInstance(application)
        val eThree = EThree.initialize(this, object : OnGetTokenCallback {
            override fun onGetToken() = virgilToken
        }).get()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel)
        binding!!.lifecycleOwner = this

        doAsync {
            val users = listOf(user, otherUser).sorted()
            val receiverPublicKeys = eThree.lookupPublicKeys(otherUser).get()
            val channel = client.channel(ModelType.channel_messaging, users.joinToString("-"))

            channel.name = users.joinToString(", ")
            channel.image = "https://robohash.org/${channel.name}"
            channel.update(object : ChannelCallback {
                override fun onSuccess(response: ChannelResponse?) {
                    uiThread { loadMessages(it, channel, eThree, receiverPublicKeys) }
                }

                override fun onError(errMsg: String?, errCode: Int) {

                }
            })
        }
    }

    private fun loadMessages(
        context: ChannelActivity,
        channel: Channel?,
        eThree: EThree,
        receiverPublicKeys: LookupResult
    ) {
        viewModel =
            ViewModelProviders.of(context, ChannelViewModelFactory(context.application, channel))
                .get(ChannelViewModel::class.java)

        binding!!.viewModel = viewModel
        binding!!.messageList.setViewHolderFactory(EncryptedMessageViewHolderFactory(eThree))
        binding!!.messageList.setViewModel(viewModel!!, context)
        binding!!.messageInput.setViewModel(viewModel, context)
        binding!!.messageInput.eThree = eThree
        binding!!.messageInput.receiverPublicKeys = receiverPublicKeys
        binding!!.channelHeader.setViewModel(viewModel, context)
    }

    companion object {
        private val EXTRA_USER = "io.getstream.encryptedchat.USER"
        private val EXTRA_OTHER_USER = "io.getstream.encryptedchat.OTHER_USER"
        private val EXTRA_VIRGIL_TOKEN = "io.getstream.encryptedchat.VIRGIL_TOKEN"

        fun newIntent(
            context: Context,
            user: String,
            otherUser: String,
            virgilToken: String
        ): Intent {
            val intent = Intent(context, ChannelActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            intent.putExtra(EXTRA_OTHER_USER, otherUser)
            intent.putExtra(EXTRA_VIRGIL_TOKEN, virgilToken)
            return intent
        }
    }

}