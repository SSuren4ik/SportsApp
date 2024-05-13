package com.example.mysportsapp.training

import android.os.Bundle
import android.os.SystemClock

class Stopwatch(
    private var startTime: Long = 0L,
    private var pauseTime: Long = 0L,
    private var running: Boolean = false,
) {

    fun start() {
        if (!running) {
            startTime = SystemClock.elapsedRealtime() - pauseTime
            running = true
        }
    }
    fun saveState(state: Bundle) {
        state.putLong("startTime", startTime)
        state.putLong("pauseTime", pauseTime)
        state.putBoolean("running", running)
    }
    fun isRunning(): Boolean {
        return running
    }
    fun reset() {
        startTime = 0L
        pauseTime = 0L
        running = false
    }
    fun getStartTime(): Long {
        return startTime
    }
    fun getPauseTime(): Long {
        return pauseTime
    }
}