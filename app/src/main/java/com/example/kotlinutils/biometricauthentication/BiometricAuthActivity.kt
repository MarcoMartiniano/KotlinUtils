package com.example.kotlinutils.biometricauthentication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.kotlinutils.databinding.ActivityBiometricAuthBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class BiometricAuthActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBiometricAuthBinding.inflate(layoutInflater)}
    private val viewModel by viewModel<BiometricAuthViewModel>()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listeners()
        observers()
    }

    //Comment one of the lines above
    @RequiresApi(Build.VERSION_CODES.R)
    private fun listeners(){
        binding.btBiometricAuthentication.setOnClickListener {
            if(viewModel.hardwareVerification(this,this@BiometricAuthActivity)){
                // viewModel.createPromptInfoWithLiveData(this,this@BiometricAuthActivity)
                viewModel.createPromptInfoWithFlow(this,this@BiometricAuthActivity)
            }
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

        lifecycleScope.launchWhenStarted {
            viewModel.sharedflowbiometricAuthenticationState.collectLatest {
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
    }

    private fun setAuthStatus (string: String){
        binding.tvBiometricAuthenticationStatus.text = string
    }

}