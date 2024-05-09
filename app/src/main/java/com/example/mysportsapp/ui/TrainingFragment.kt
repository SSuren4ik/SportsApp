package com.example.mysportsapp.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysportsapp.R
import com.example.mysportsapp.Stopwatch
import com.example.mysportsapp.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private var imageResource: Int = 0
    private lateinit var binding: FragmentTrainingBinding
    private lateinit var stopwatch: Stopwatch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val startTime = savedInstanceState.getLong("startTime")
            val pauseTime = savedInstanceState.getLong("pauseTime")
            val running = savedInstanceState.getBoolean("running")
            stopwatch = Stopwatch(startTime, pauseTime, running)

            imageResource = savedInstanceState.getInt("imageResource")
        } else {
            stopwatch = Stopwatch(0L, 0L, false)
            imageResource = R.drawable.start_training
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTrainingBinding.inflate(inflater)
        updateChronometerBaseTime()
        updateStartPauseImage()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.startPauseImageView.setOnClickListener {
            if (!stopwatch.isRunning()) {
                startChronometer()
            } else {
                pauseChronometer()
            }
        }

        binding.resetImageView.setOnClickListener {
            resetChronometer()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stopwatch.saveState(outState)
        outState.putInt("imageResource", imageResource)
    }

    private fun startChronometer() {
        stopwatch.start()
        binding.chronometerView.base = SystemClock.elapsedRealtime() - stopwatch.getPauseTime()
        binding.chronometerView.start()
        imageResource = R.drawable.pause_training
        updateStartPauseImage()
    }

    private fun pauseChronometer() {
        binding.chronometerView.stop()
        stopwatch.pause()
        imageResource = R.drawable.start_training
        updateStartPauseImage()
    }

    private fun resetChronometer() {
        stopwatch.reset()
        binding.chronometerView.stop()
        updateChronometerBaseTime()
        imageResource = R.drawable.start_training
        updateStartPauseImage()
    }

    private fun updateChronometerBaseTime() {
        if (stopwatch.isRunning()) {
            binding.chronometerView.base = stopwatch.getStartTime() + stopwatch.getPauseTime()
            binding.chronometerView.start()
        } else {
            binding.chronometerView.base = SystemClock.elapsedRealtime() - stopwatch.getPauseTime()
        }
    }

    private fun updateStartPauseImage() {
        binding.startPauseImageView.setImageResource(imageResource)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrainingFragment()
    }
}