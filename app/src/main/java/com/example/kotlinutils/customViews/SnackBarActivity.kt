package com.example.kotlinutils.customViews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinutils.customViews.components.CustomSnackBar
import com.example.kotlinutils.databinding.ActivitySnackBarBinding

class SnackBarActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySnackBarBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btSnackBarAlert.setOnClickListener {
            CustomSnackBar.make(view = it, CustomSnackBar.Style.WARNING).show()
        }
        binding.btSnackBarError.setOnClickListener {
            CustomSnackBar.make(view = it, CustomSnackBar.Style.ERROR).show()
        }
        binding.btSnackBarCustom.setOnClickListener {
            CustomSnackBar.make(view = it, CustomSnackBar.Style.CUSTOM).show()
        }
        binding.btSnackBarSuccess.setOnClickListener {
            CustomSnackBar.make(view = it, CustomSnackBar.Style.SUCCESS).show()
        }
    }
}