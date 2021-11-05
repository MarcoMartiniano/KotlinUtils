package com.example.kotlinutils

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinutils.biometricauthentication.BiometricAuthActivity
import com.example.kotlinutils.countdowntime.CountdowntimeActivity
import com.example.kotlinutils.databinding.ActivityMainBinding
import com.example.kotlinutils.flowandlivedata.FlowLiveDataActivity
import com.example.kotlinutils.whatzup.WhatzupActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listeners()
    }


    private fun listeners(){
        binding.btCountdowntime.setOnClickListener {
            startActivity(Intent(this@MainActivity,CountdowntimeActivity::class.java))
        }
        binding.btFlowlivedata.setOnClickListener {
            startActivity(Intent(this@MainActivity,FlowLiveDataActivity::class.java))
        }
        binding.btBiometricauth.setOnClickListener {
            startActivity(Intent(this@MainActivity,BiometricAuthActivity::class.java))
        }
        binding.btWhatzup.setOnClickListener {
            startActivity(Intent(this@MainActivity,WhatzupActivity::class.java))
        }
    }
}