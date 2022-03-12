package com.example.kotlinutils.customViews

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinutils.databinding.ActivityCustomViewsBinding

class CustomViewsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCustomViewsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btSnackBar.setOnClickListener {
            startActivity(Intent(this@CustomViewsActivity, SnackBarActivity::class.java))
        }
        binding.btSwitchButton.setOnClickListener {  }
    }


}