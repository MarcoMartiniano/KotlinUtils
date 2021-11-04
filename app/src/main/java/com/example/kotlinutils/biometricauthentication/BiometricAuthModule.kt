package com.example.kotlinutils.biometricauthentication

import com.example.kotlinutils.flowandlivedata.FlowLiveDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object BiometricAuthModule {
    fun load() {
        loadKoinModules(viewModelModules())
    }

    private fun viewModelModules(): Module {
        return module {
            viewModel { BiometricAuthViewModel() }
        }
    }
}