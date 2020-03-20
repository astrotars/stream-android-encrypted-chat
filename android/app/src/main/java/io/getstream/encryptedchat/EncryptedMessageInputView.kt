package io.getstream.encryptedchat

import android.content.Context
import android.util.AttributeSet
import com.getstream.sdk.chat.rest.Message
import com.getstream.sdk.chat.view.MessageInputView
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import com.virgilsecurity.sdk.crypto.VirgilPublicKey

class EncryptedMessageInputView : MessageInputView {
    var receiverPublicKeys: Map<String, VirgilPublicKey>? = null
    var eThree: EThree? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun prepareMessage(input: String): Message {
        assert(eThree != null)
        assert(receiverPublicKeys != null)

        val encryptedText = eThree!!.encrypt(input, receiverPublicKeys)

        return super.prepareMessage(encryptedText)
    }
}