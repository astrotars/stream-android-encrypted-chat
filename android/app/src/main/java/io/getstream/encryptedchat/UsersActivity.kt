package io.getstream.encryptedchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray


class UsersActivity : AppCompatActivity() {
    var client: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        val user = intent.getStringExtra(UsersActivity.EXTRA_USER)!!
        val authToken = intent.getStringExtra(UsersActivity.EXTRA_AUTH_TOKEN)!!
        val virgilToken = intent.getStringExtra(UsersActivity.EXTRA_VIRGIL_TOKEN)!!

        val list: ListView = findViewById<View>(R.id.listview) as ListView

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        list.adapter = adapter

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val otherUser = adapter.getItem(position).toString()
            val intent = ChannelActivity.newIntent(this, user, otherUser, virgilToken)
            startActivity(intent)
        }

        doAsync {
            val users = getUsers(authToken).filterNot { it == user }
            uiThread {
                adapter.addAll(users)
            }
        }
    }

    private fun getUsers(authToken: String): List<String> {
        val request = Request.Builder()
            .url("https://96154c61.ngrok.io/v1/users")
            .addHeader("Authorization", "Bearer $authToken")
            .get()

        client.newCall(request.build()).execute().use {
            val jsonArray = JSONArray(it.body!!.string())
            return List(jsonArray.length()) { i -> jsonArray.get(i).toString() }
        }
    }

    companion object {
        private val EXTRA_USER = "io.getstream.encryptedchat.USER"
        private val EXTRA_AUTH_TOKEN = "io.getstream.encryptedchat.AUTH_TOKEN"
        private val EXTRA_VIRGIL_TOKEN = "io.getstream.encryptedchat.VIRGIL_TOKEN"

        fun newIntent(
            context: Context,
            user: String,
            authToken: String,
            virgilToken: String
        ): Intent {
            val intent = Intent(context, UsersActivity::class.java)
            intent.putExtra(EXTRA_USER, user)
            intent.putExtra(EXTRA_AUTH_TOKEN, authToken)
            intent.putExtra(EXTRA_VIRGIL_TOKEN, virgilToken)
            return intent
        }
    }
}