package com.example.kotlinutils.flowandlivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowLiveDataViewModel: ViewModel() {

    private val _liveData = MutableLiveData("Hello World")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow("Hello World")
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(){
        _liveData.value = "LiveData"
    }

    fun triggerStateFlow(){
        _stateFlow.value = "StateFlow"
    }

    fun triggerFlow() : Flow<String> {
        return flow {
            repeat(5){
                emit("Item $it")
                delay(1000L)
            }
        }
    }

    fun triggerSharedFlow(){
        viewModelScope.launch {
            _sharedFlow.emit("SharedFlow")
        }
    }

}