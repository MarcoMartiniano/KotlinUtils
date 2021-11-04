package com.example.kotlinutils.biometricauthentication



import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executor

class BiometricAuthViewModel : ViewModel() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var executor: Executor
    private lateinit var promptInfo: PromptInfo

    private val _biometricAuthenticationState = MutableLiveData<BiometricAuthenticationState>()
    val biometricAuthenticationState: LiveData<BiometricAuthenticationState> = _biometricAuthenticationState


    fun createPromptInfo(context: Context, activity: FragmentActivity) {
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(activity,executor, object : AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                _biometricAuthenticationState.postValue(BiometricAuthenticationState.Error(errorCode,errString))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                _biometricAuthenticationState.postValue(BiometricAuthenticationState.Failed)
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                _biometricAuthenticationState.postValue(BiometricAuthenticationState.Succeeded(result))
            }

        })

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}

sealed class BiometricAuthenticationState {
    data class Succeeded(val result: AuthenticationResult) : BiometricAuthenticationState()
    data class Error(val errorCode: Int, val errString: CharSequence) : BiometricAuthenticationState()
    object Failed : BiometricAuthenticationState()
}