package io.getstream.encryptedchat

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

    // we're using data binding in this example
    val binding: ActivityMainBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_main)
    // Specify the current activity as the lifecycle owner.
    binding.lifecycleOwner = this

    binding.submit.setOnClickListener {
      val user: String = binding.user.text.toString()
      val context = this

      doAsync {
        val authToken = signIn(user)
        val streamToken = getStreamToken(authToken)
        val virgilToken = getVirgilToken(authToken)

        val eThree = EThree.initialize(context, object : OnGetTokenCallback {
          override fun onGetToken() = virgilToken
        }).get()

        try {
          eThree.register().execute()
        } catch (e: RegistrationException) {
          // already registered
        }

        uiThread { context ->
          initStream(user, streamToken)
          val intent = UsersActivity.newIntent(context, user, authToken, virgilToken)
          startActivity(intent)
        }
      }
    }
  }


  private fun initStream(user: String, token: String) {
    StreamChat.init("whe3wer2pf4r", this.applicationContext)
    val client = StreamChat.getInstance(this.application)
    val currentUser = User(user, hashMapOf<String, Any>("name" to user))
    client.setUser(currentUser, token)
  }
}