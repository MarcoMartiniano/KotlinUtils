package com.example.kotlinutils.whatzup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinutils.databinding.ActivityWhatzupBinding

class WhatzupActivity : AppCompatActivity() {

    private val binding by lazy {ActivityWhatzupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btWhatzupOpenAndSendMsg.setOnClickListener {
            openWhatsappContact("Hey, I need some help!", "(99) 9 9999-9999")
        }
    }

    private fun openWhatsappContact(message: String, contact: String) {
        val wppIntent = Intent(Intent.ACTION_VIEW)
        val wppUrl = "https://api.whatsapp.com/send?phone=${message}&text=${contact}"
        wppIntent.data = Uri.parse(wppUrl)
        startActivity(wppIntent)
    }
}