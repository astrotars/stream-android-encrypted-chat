package io.getstream.encryptedchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.rest.User
import io.getstream.encryptedchat.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
  val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
  var client: OkHttpClient = OkHttpClient()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // we're using data binding in this example
    val binding: ActivityMainBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_main)
    // Specify the current activity as the lifecycle owner.
    binding.lifecycleOwner = this

    binding.submit.setOnClickListener {
      val user: String = binding.user.text.toString()

      doAsync {
        val authToken = signIn(user)
        val streamToken = getStreamToken(authToken)
        val virgilToken = getVirgilToken(authToken)

        uiThread { context ->
          initStream(user, streamToken)
          val intent = ChannelsActivity.newIntent(context, user, virgilToken)
          startActivity(intent)
        }
      }
    }
  }

  private fun signIn(user: String): String {
    val response = post("/v1/authenticate", mapOf("user" to user))
    return response.getString("authToken")
  }

  private fun getVirgilToken(authToken: String): String {
    val response = post("/v1/virgil-credentials", mapOf(), authToken)
    return response.getString("token")
  }

  private fun getStreamToken(authToken: String): String {
    val response = post("/v1/stream-credentials", mapOf(), authToken)
    return response.getString("token")
  }

  private fun initStream(user: String, token: String) {
    StreamChat.init("whe3wer2pf4r", this.applicationContext)
    val client = StreamChat.getInstance(this.application)
    val currentUser = User(user, hashMapOf<String, Any>("name" to user))
    client.setUser(currentUser, token)
  }

  private fun post(path: String, body: Map<String, Any>, authToken: String? = null): JSONObject {
    val request = Request.Builder()
      .url("https://96084840.ngrok.io${path}")
      .post(JSONObject(body).toString().toRequestBody(JSON))

    if (authToken != null) {
      request.addHeader("Authorization", "Bearer $authToken")
    }

    client.newCall(request.build()).execute().use {
      return JSONObject(it.body!!.string())
    }
  }

}