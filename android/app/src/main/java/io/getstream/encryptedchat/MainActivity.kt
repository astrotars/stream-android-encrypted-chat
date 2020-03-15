package io.getstream.encryptedchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.getstream.encryptedchat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // we're using data binding in this example
    val binding: ActivityMainBinding =
      DataBindingUtil.setContentView(this, R.layout.activity_main)
    // Specify the current activity as the lifecycle owner.
    binding.lifecycleOwner = this
    binding.username.setOnClickListener {
    }

    binding.submit.setOnClickListener {
      val intent = ChannelsActivity.newIntent(this, "jeff")
      startActivity(intent)

    }

  }
}