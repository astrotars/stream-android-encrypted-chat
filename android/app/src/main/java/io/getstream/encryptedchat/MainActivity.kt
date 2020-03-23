package io.getstream.encryptedchat

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.rest.User
import com.virgilsecurity.android.common.exceptions.RegistrationException
import com.virgilsecurity.android.ethree.kotlin.callback.OnGetTokenCallback
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import io.getstream.encryptedchat.BackendService.getStreamToken
import io.getstream.encryptedchat.BackendService.getVirgilToken
import io.getstream.encryptedchat.BackendService.signIn
import io.getstream.encryptedchat.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.submit.setOnClickListener {
            val user: String = binding.user.text.toString()

            doAsync {
                val authToken = signIn(user)
                val streamToken = getStreamToken(authToken)
                val virgilToken = getVirgilToken(authToken)
                initVirgil(it.context, virgilToken)

                uiThread { context ->
                    initStream(user, streamToken)
                    val intent = UsersActivity.newIntent(context, user, authToken, virgilToken)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initVirgil(context: Context, virgilToken: String) {
        val eThree = EThree.initialize(context, object : OnGetTokenCallback {
            override fun onGetToken() = virgilToken
        }).get()

        try {
            eThree.register().execute()
        } catch (e: RegistrationException) {
            // already registered
        }
    }


    private fun initStream(user: String, token: String) {
        StreamChat.init("<INSERT_STREAM_API_KEY_HERE>", this.applicationContext)
        val client = StreamChat.getInstance(this.application)
        val currentUser = User(user, hashMapOf<String, Any>("name" to user))
        client.setUser(currentUser, token)
    }
}