package com.example.mysportsapp.training.TimerSpace

class TimerTime(
    private var seconds: Long = 0L,
    private var minutes: Long = 0L,
    private var hours: Long = 0L,
) {
    fun getSeconds(): Long {
        return seconds
    }
    fun getMinutes(): Long {
        return minutes
    }

    fun getHours(): Long {
        return hours
    }
}