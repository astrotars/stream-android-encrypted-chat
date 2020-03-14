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
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class MainActivity : AppCompatActivity() {
  val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
  var client: OkHttpClient = OkHttpClient()

  fun post(url: String, json: String): String {
    val body: RequestBody = json.toRequestBody(JSON)
    val request: Request = Request.Builder()
      .url(url)
      .post(body)
      .build()
    client.newCall(request).execute().use {
      return it.body!!.string()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val intent = ChannelsActivity.newIntent(this, "jeff")
    startActivity(intent)
  }
}