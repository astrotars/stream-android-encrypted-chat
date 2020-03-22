package io.getstream.encryptedchat

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object BackendService {
    private val http = OkHttpClient()
    private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private const val apiRoot = "https://3c3e4c3a.ngrok.io"

    fun signIn(user: String): String {
        return post("/v1/authenticate", mapOf("user" to user))
            .getString("authToken")
    }

    fun getVirgilToken(authToken: String): String {
        return post("/v1/virgil-credentials", mapOf(), authToken)
            .getString("token")
    }

    fun getStreamToken(authToken: String): String {
        return post("/v1/stream-credentials", mapOf(), authToken)
            .getString("token")
    }

    private fun post(path: String, body: Map<String, Any>, authToken: String? = null): JSONObject {
        val request = Request.Builder()
            .url("${apiRoot}${path}")
            .post(JSONObject(body).toString().toRequestBody(JSON))

        if (authToken != null) {
            request.addHeader("Authorization", "Bearer $authToken")
        }

        http.newCall(request.build()).execute().use {
            return JSONObject(it.body!!.string())
        }
    }

    fun getUsers(authToken: String): List<String> {
        val request = Request.Builder()
            .url("${apiRoot}/v1/users")
            .addHeader("Authorization", "Bearer $authToken")
            .get()

        http.newCall(request.build()).execute().use {
            val jsonArray = JSONArray(it.body!!.string())
            return List(jsonArray.length()) { i -> jsonArray.get(i).toString() }
        }
    }
}