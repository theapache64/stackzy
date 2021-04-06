package com.theapache64.stackzy.util


import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * To do basic text encryption and decryption with predefined salt.
 */
class Crypto(
    private val salt: ByteArray
) {

    companion object {
        private const val ALGORITHM = "AES"
    }

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(salt, ALGORITHM))
        val encodedValue = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encodedValue)
    }

    fun decrypt(encodedText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(salt, ALGORITHM))
        val decodedValue = Base64.getDecoder().decode(encodedText)
        val decValue = cipher.doFinal(decodedValue)
        return String(decValue)
    }

}
