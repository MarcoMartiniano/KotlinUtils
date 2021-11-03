package com.example.kotlinutils.flowandlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.kotlinutils.databinding.ActivityFlowLiveDataBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FlowLiveDataActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFlowLiveDataBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<FlowLiveDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listeners()
        subscribeToObservables()
    }

    private fun listeners() {

        // LIVEDATA
        binding.btLivedata.setOnClickListener {
        viewModel.triggerLiveData()
        }

        // STATEFLOW
        binding.btStateflow.setOnClickListener {
        viewModel.triggerStateFlow()
        }

        // FLOW
        //The value will be lost when you have SCREEN ROTATIONS, you can use more then one time

        binding.btFlow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.triggerFlow().collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }

        // SHAREDFLOW
        binding.btSharedflow.setOnClickListener {
        viewModel.triggerSharedFlow()
        }
    }


    private fun subscribeToObservables(){

        // LIVEDATA
        //Became obsolete
        //If your are using Fragments you change this for viewLifecycleOwner
        //When you rotate the screen it will not be lost
        viewModel.liveData.observe(this){
            binding.tvLivedata.text = it;
        }

        // STATEFLOW
        //When you rotate the screen it will not be lost
        //If you show a snack bar using stateFlow, everytime you rotate the screen this will be shown,
        //and when you start the Activity will show it with the initial value
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.tvStateflow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // SHAREDFLOW
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }
}