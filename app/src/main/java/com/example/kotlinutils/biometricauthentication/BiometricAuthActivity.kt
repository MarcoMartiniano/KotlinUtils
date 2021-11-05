package com.example.kotlinutils.biometricauthentication

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
        lifecycleScope.launchWhenStarted {
            viewModel.sharedflowbiometricHardwareVerificationState.collectLatest {
                when (it){
                    is HardwareVerificationState.Success -> {
                        showHardwareVerification(applicationContext, "Your phone has Biometric")
                    }
                    is HardwareVerificationState.Error_No_Hardware -> {
                        showHardwareVerification(applicationContext, "You don´t have Biometric on your phone.")
                    }
                    is HardwareVerificationState.Error_HW_Unavailable -> {
                        showHardwareVerification(applicationContext, "The Biometric in your phone is unavailable")
                    }
                    is HardwareVerificationState.Error_None_Enrolled   -> {
                        showHardwareVerification(applicationContext, "You have to configure your Biometric")
                    }
                }
            }
        }
    }


    private fun setAuthStatus (string: String){
        binding.tvBiometricAuthenticationStatus.text = string
    }

    private fun showHardwareVerification(context: Context, message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}