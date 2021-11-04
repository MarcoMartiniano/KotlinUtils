package com.example.kotlinutils

import android.app.Application
import com.example.kotlinutils.biometricauthentication.BiometricAuthModule
import com.example.kotlinutils.flowandlivedata.FlowLiveDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }
        FlowLiveDataModule.load()
        BiometricAuthModule.load()
    }
}