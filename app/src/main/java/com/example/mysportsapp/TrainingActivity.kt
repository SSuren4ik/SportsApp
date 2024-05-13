package com.example.mysportsapp

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.example.mysportsapp.databinding.ActivityTrainingBinding
import com.example.mysportsapp.training.Stopwatch
import com.example.mysportsapp.training.TimerSpace.Timer
import com.example.mysportsapp.training.TimerSpace.TimerTime
import com.example.mysportsapp.training.TimerSpace.TimerTimePicker


class TrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingBinding
    private lateinit var stopwatch: Stopwatch
    private var timer = Timer()
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
            startTrainingButton.text = "Завершить тренировку"
        }
    }

    private fun resetChronometer() {
        stopwatch.reset()
        updateChronometerBaseTime()
        with(binding) {
            chronometerView.stop()
            startTrainingButton.text = "Начать тренировку"
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
        binding.startTimerButton.setOnClickListener {
            if (timer.getRunning()) {
                restartTimer()
            } else {
                val timePickerDialog = TimerTimePicker(
                    this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, { _, minutes, seconds ->
                        val time = minutes * 60 + seconds
                        startTimer(time.toLong())
                    }, 0, 0
                )
                timePickerDialog.show()
            }
        }
    }
    private fun startTimer(time: Long) {
        binding.startTimerButton.text = "Отменить таймер"
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
        createVibrator()
    }
    private fun restartTimer() {
        with(binding) {
            timerText.text = resources.getString(R.string.timer_text)
            startTimerButton.text = "Запустить таймер"
        }
    }
    private fun createVibrator() {
        val vibrator = getSystemService(Vibrator::class.java)
        if (vibrator.hasVibrator()) {
            val vibrationPattern = longArrayOf(0, 500, 50, 300)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
            } else {
                vibrator.vibrate(vibrationPattern, -1)
            }
        }
    }
}