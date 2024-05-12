package com.example.mysportsapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mysportsapp.databinding.ActivityTrainingBinding
import com.example.mysportsapp.training.Stopwatch
import com.example.mysportsapp.training.TimerSpace.Timer
import com.example.mysportsapp.training.TimerSpace.TimerTime


class TrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingBinding
    private lateinit var stopwatch: Stopwatch
    private lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createStopwatch(savedInstanceState)
        createTimer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stopwatch.saveState(outState)
    }

    private fun createStopwatch(savedInstanceState: Bundle?) {
        stopwatch = if (savedInstanceState != null) {
            val startTime = savedInstanceState.getLong("startTime")
            val pauseTime = savedInstanceState.getLong("pauseTime")
            val running = savedInstanceState.getBoolean("running")
            Stopwatch(startTime, pauseTime, running)
        } else {
            Stopwatch()
        }
        updateChronometerBaseTime()
        binding.startTrainingButton.setOnClickListener {
            if (!stopwatch.isRunning()) startChronometer()
            else resetChronometer()
        }
    }

    private fun startChronometer() {
        stopwatch.start()
        with(binding) {
            chronometerView.base = SystemClock.elapsedRealtime() - stopwatch.getPauseTime()
            chronometerView.start()
            startTimerButton.text = "Завершить тренировку"
        }
    }

    private fun resetChronometer() {
        stopwatch.reset()
        updateChronometerBaseTime()
        with(binding) {
            chronometerView.stop()
            startTimerButton.text = "Начать тренировку"
        }
    }

    private fun updateChronometerBaseTime() {
        with(binding) {
            if (stopwatch.isRunning()) {
                chronometerView.base = stopwatch.getStartTime() + stopwatch.getPauseTime()
                chronometerView.start()
            } else {
                chronometerView.base = SystemClock.elapsedRealtime() - stopwatch.getPauseTime()
            }
        }
    }

    private fun createTimer() {
        with(binding) {
            timerText.visibility = View.GONE
            numberPicker.minValue = 0
            numberPicker.maxValue = 1
            numberPicker.displayedValues = arrayOf("0:05", "1:00")
            startTimerButton.setOnClickListener {
                val time = when (binding.numberPicker.value) {
                    0 -> 5 // 30 секунд
                    1 -> 60 // 1 минута
                    else -> 0
                }
                startTimer(time.toLong())
            }
        }
    }


    private fun startTimer(time: Long) {
        with(binding) {
            timerText.visibility = View.VISIBLE
            numberPicker.visibility = View.GONE
            startTimerButton.text = "Отменить таймер"
        }
        timer = Timer(time)
        timer.startTimer({ timerTime -> onTickAction(timerTime) }, {
            onFinishAction()
        })
    }

    private fun onTickAction(timerTime: TimerTime) {
        val minutes = timerTime.getMinutes()
        val seconds = timerTime.getSeconds()
        val stringTime = String.format("%02d:%02d", minutes, seconds)
        binding.timerText.text = stringTime
    }

    private fun onFinishAction() {
        restartTimer()
        val vibratorManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        } else {
            TODO("VERSION.SDK_INT < S")
        }
        val vibrator = vibratorManager.defaultVibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    private fun restartTimer() {
        with(binding) {
            timerText.text = "00:00"
            numberPicker.visibility = View.VISIBLE
            timerText.visibility = View.GONE
            startTimerButton.text = "Запустить таймер"
        }
    }
}