package com.example.mysportsapp

import android.os.SystemClock

class Stopwatch(
    var startTime: Long = 0L,
    var pauseTime: Long = 0L,
    var running: Boolean = false) {

    fun start() {
        if (!running) {
            startTime = SystemClock.elapsedRealtime() - pauseTime
            running = true
        }
    }

    fun stop() {
        if (running) {
            pauseTime = SystemClock.elapsedRealtime() - startTime
            running = false
        }
    }

    fun getElapsedTime(): Long {
        return if (running) {
            SystemClock.elapsedRealtime() - startTime
        } else {
            pauseTime
        }
    }
}