package com.example.mysportsapp.training.TimerSpace

import android.os.CountDownTimer
import android.util.Log


class Timer(private var time: Long = 0L) {
    private lateinit var timer: CountDownTimer
    private var running = false

    fun startTimer(onTickAction: (TimerTime) -> Unit, onFinishAction: () -> Unit) {
        Log.d("Timer", "startTimer")
        running = true
        timer = object : CountDownTimer(time * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val toSeconds = millisUntilFinished / 1000
                val minutes = (toSeconds % 3600) / 60
                val seconds = toSeconds % 60
                val timerTime = TimerTime(seconds, minutes)
                onTickAction(timerTime)
            }

            override fun onFinish() {
                running = false
                onFinishAction()
            }
        }.start()
    }

    fun getRunning(): Boolean {
        return running
    }
}
