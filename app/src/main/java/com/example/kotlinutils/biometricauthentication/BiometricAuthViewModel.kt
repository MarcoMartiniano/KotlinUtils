package com.example.kotlinutils.biometricauthentication



import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.app.ActivityCompat.startActivityForResult
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

    private val _sharedflowbiometricHardwareVerificationState = MutableSharedFlow<HardwareVerificationState>()
    val sharedflowbiometricHardwareVerificationState = _sharedflowbiometricHardwareVerificationState.asSharedFlow()



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
                emitBiometricDataFlow(BiometricAuthenticationState.Error(errorCode,errString))
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                emitBiometricDataFlow(BiometricAuthenticationState.Failed)
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                emitBiometricDataFlow(BiometricAuthenticationState.Succeeded(result))
            }

        })

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()
        biometricPrompt.authenticate(promptInfo)
    }



    private fun emitBiometricDataFlow(biometricAuthenticationState: BiometricAuthenticationState){
        viewModelScope.launch {
            _sharedflowbiometricAuthenticationState.emit(biometricAuthenticationState)
        }
    }

    private fun emitHardwareVerificationDataFlow(hardwareVerificationState: HardwareVerificationState){
        viewModelScope.launch {
            _sharedflowbiometricHardwareVerificationState.emit(hardwareVerificationState)
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun hardwareVerification (context: Context, activity: Activity) : Boolean{
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->{
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                emitHardwareVerificationDataFlow(HardwareVerificationState.Success)
               return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->{
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                emitHardwareVerificationDataFlow(HardwareVerificationState.Error_No_Hardware)
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->{
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                emitHardwareVerificationDataFlow(HardwareVerificationState.Error_HW_Unavailable)
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                emitHardwareVerificationDataFlow(HardwareVerificationState.Error_None_Enrolled)
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                startActivityForResult(activity,enrollIntent,0,null)
                return false
            }
        }
        return false
    }

}

sealed class HardwareVerificationState {
    object Success : HardwareVerificationState()
    object Error_No_Hardware : HardwareVerificationState()
    object Error_HW_Unavailable : HardwareVerificationState()
    object Error_None_Enrolled : HardwareVerificationState()
}

sealed class BiometricAuthenticationState {
    data class Succeeded(val result: AuthenticationResult) : BiometricAuthenticationState()
    data class Error(val errorCode: Int, val errString: CharSequence) : BiometricAuthenticationState()
    object Failed : BiometricAuthenticationState()
}