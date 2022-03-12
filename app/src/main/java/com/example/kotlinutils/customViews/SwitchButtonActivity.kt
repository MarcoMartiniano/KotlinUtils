package com.example.kotlinutils.customViews

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinutils.customViews.components.CustomSwitchButton
import com.example.kotlinutils.databinding.ActivitySwitchButtonBinding

class SwitchButtonActivity : AppCompatActivity(),
    CustomSwitchButton.Companion.SwitchChangeListener {

    private val binding by lazy { ActivitySwitchButtonBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.customSwitchButton.setSwitchChangeListener(this)
        binding.customSwitchButton2.setSwitchBackError(
            thumbAnimatorDuration = 1,
            isSwitchOn = true
        )
        binding.customSwitchButton2.setSwitchInitialValor()
    }

    override fun onSwitchToggleChange(isOpen: Boolean, button: CustomSwitchButton) {
        if (isOpen) {
            Toast.makeText(this, "SwitchToggleChange: ON", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "SwitchToggleChange: OFF", Toast.LENGTH_LONG).show()
        }
    }
}