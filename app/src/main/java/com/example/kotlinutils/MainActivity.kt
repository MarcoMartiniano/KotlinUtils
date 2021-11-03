package com.example.kotlinutils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinutils.countdowntime.CountdowntimeActivity
import com.example.kotlinutils.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listeners()
    }


    fun listeners(){
        binding.btCountdowntime.setOnClickListener {
            startActivity(Intent(this@MainActivity,CountdowntimeActivity::class.java))
        }
    }
}