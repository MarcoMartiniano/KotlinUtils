package com.example.kotlinutils.cryptography

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.kotlinutils.databinding.ActivityCryptographyBinding
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptographyActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCryptographyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        generateKey()

        binding.btEncryptography.setOnClickListener {
            val data = binding.edtCrypto.text.toString()
            val pair = encryptData(data)
            val encryptedData = pair.second.toString(Charsets.UTF_8)
            binding.tvEncryptography.text = encryptedData
        }
        binding.btDecryptography.setOnClickListener {
            val data = binding.edtCrypto.text.toString()
            val pair = encryptData(data)
            val decryptedData = decrypData(pair.first,pair.second)
            binding.tvDecryptography.text = decryptedData
        }
    }

    fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder("MyKeyAlias",
            KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun getKey(): SecretKey {
        val keystore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)

        val secretKeyEntry = keystore.getEntry("MyKeyAlias",null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encryptData (data: String): Pair<ByteArray,ByteArray>{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        var temp: String = data
        while (temp.toByteArray().size % 16 != 0){
            temp += "\u0020"
        }
        cipher.init(Cipher.ENCRYPT_MODE,getKey())

        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(temp.toByteArray(Charsets.UTF_8))
        return Pair(ivBytes,encryptedBytes)
    }

    fun decrypData(ivBytes: ByteArray, data: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, getKey(),spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }
}