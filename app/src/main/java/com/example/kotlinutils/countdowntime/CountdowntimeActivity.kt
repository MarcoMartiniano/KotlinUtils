package com.example.kotlinutils.countdowntime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.kotlinutils.databinding.ActivityCountdowntimeBinding



class CountdowntimeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCountdowntimeBinding.inflate(layoutInflater) }

    private var counter = 0
    private var countDownTimer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listeners()

    }


    private fun listeners(){
        binding.btCountdowntimeStart.setOnClickListener {
            countDownTimer = createCountdownTimer()
            countDownTimer!!.start()
        }

        binding.btCountdowntimeStop.setOnClickListener {
            countDownTimer?.cancel()
            binding.tvCountdowtime.text = "$counter - Canceled"
        }
    }

    private fun createCountdownTimer() : CountDownTimer{
        counter = 10
        val countDownTimer = object : CountDownTimer(10000,1000){

            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdowtime.text = "$counter - Tick"
                counter--
            }

            override fun onFinish() {
                binding.tvCountdowtime.text = "$counter - Finished"
            }

        }
        return countDownTimer
    }
}