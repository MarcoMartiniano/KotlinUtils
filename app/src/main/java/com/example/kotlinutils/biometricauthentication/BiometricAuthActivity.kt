package com.example.kotlinutils.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinutils.databinding.ActivityBiometricAuthBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BiometricAuthActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBiometricAuthBinding.inflate(layoutInflater)}
    private val viewModel by viewModel<BiometricAuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listeners()
        observers()

    }

    private fun listeners(){
        binding.btBiometricAuthentication.setOnClickListener {
            viewModel.createPromptInfo(this,this@BiometricAuthActivity)
        }
    }

    private fun observers(){
        viewModel.biometricAuthenticationState.observe(this){
            when (it){
                is BiometricAuthenticationState.Failed -> {
                    setAuthStatus("Authentication Failed")
                }
                is BiometricAuthenticationState.Succeeded -> {
                    setAuthStatus("Authentication Succeeded ${it.result}")

                }
                is BiometricAuthenticationState.Error -> {
                    setAuthStatus("Authentication Error: ${it.errString} ${it.errorCode}")

                }
            }
        }
    }

    private fun setAuthStatus (string: String){
        binding.tvBiometricAuthenticationStatus.text = string
    }


}