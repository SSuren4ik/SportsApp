package com.example.mysportsapp.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysportsapp.R
import com.example.mysportsapp.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private lateinit var binding: FragmentTrainingBinding
    private var startTime = 0L
    private var pauseTime = 0L
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            startTime = savedInstanceState.getLong("startTime")
            pauseTime = savedInstanceState.getLong("pauseTime")
            running = savedInstanceState.getBoolean("running")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTrainingBinding.inflate(inflater)
        if (running) {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
            binding.chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime + pauseTime
            binding.chronometerView.start()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.startPauseImageView.setOnClickListener {
            if (!running) {
                startTime = SystemClock.elapsedRealtime()
                binding.chronometerView.base = SystemClock.elapsedRealtime() - pauseTime
                binding.chronometerView.start()
                running = true
                binding.startPauseImageView.setImageResource(R.drawable.pause_training)
            } else {
                binding.chronometerView.stop()
                pauseTime = SystemClock.elapsedRealtime() - binding.chronometerView.base
                running = false
                binding.startPauseImageView.setImageResource(R.drawable.start_training)
            }
        }

        binding.resetImageView.setOnClickListener {
            startTime = 0;
            pauseTime = 0;
            running = false;
            binding.chronometerView.stop();
            binding.chronometerView.base = SystemClock.elapsedRealtime();
            binding.startPauseImageView.setImageResource(R.drawable.start_training)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("startTime", startTime)
        outState.putLong("pauseTime", pauseTime)
        outState.putBoolean("running", running)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrainingFragment()
    }
}