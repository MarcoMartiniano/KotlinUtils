package com.example.kotlinutils.biometricauthentication



import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class BiometricAuthViewModel : ViewModel() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var executor: Executor
    private lateinit var promptInfo: PromptInfo

    private val _biometricAuthenticationState = MutableLiveData<BiometricAuthenticationState>()
    val biometricAuthenticationState: LiveData<BiometricAuthenticationState> = _biometricAuthenticationState

    private val _sharedflowbiometricAuthenticationState = MutableSharedFlow<BiometricAuthenticationState>()
    val sharedflowbiometricAuthenticationState = _sharedflowbiometricAuthenticationState.asSharedFlow()



    //LIVEDATA is deprecated but still can be used for simple purpose or for beginners

    fun createPromptInfoWithLiveData(context: Context, activity: FragmentActivity) {
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

    

    fun createPromptInfoWithFlow(context: Context, activity: FragmentActivity) {
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(activity,executor, object : AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                emitDataFlow(BiometricAuthenticationState.Error(errorCode,errString))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                emitDataFlow(BiometricAuthenticationState.Failed)
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                emitDataFlow(BiometricAuthenticationState.Succeeded(result))
            }

        })

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }



    private fun emitDataFlow(biometricAuthenticationState: BiometricAuthenticationState){
        viewModelScope.launch {
            _sharedflowbiometricAuthenticationState.emit(biometricAuthenticationState)
        }
    }

}

sealed class BiometricAuthenticationState {
    data class Succeeded(val result: AuthenticationResult) : BiometricAuthenticationState()
    data class Error(val errorCode: Int, val errString: CharSequence) : BiometricAuthenticationState()
    object Failed : BiometricAuthenticationState()
}