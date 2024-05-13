package com.example.mysportsapp.training.TimerSpace

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import android.widget.NumberPicker
import android.widget.TimePicker


class TimerTimePicker(
    context: Context?, themeId: Int,
    private val callback: OnTimeSetListener?,
    private val initialMinutes: Int,
    seconds: Int,
) : TimePickerDialog(context, themeId, callback, initialMinutes, seconds, true) {

    private lateinit var timePicker: TimePicker

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (callback != null) {
            timePicker.clearFocus()
            callback.onTimeSet(timePicker, timePicker.hour, timePicker.minute)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            timePicker = findViewById<View>(
                Resources.getSystem().getIdentifier("timePicker", "id", "android")
            ) as TimePicker
            val hoursPicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier("hour", "id", "android")
            ) as NumberPicker

            val maxMinutes = 60
            hoursPicker.minValue = 0
            hoursPicker.maxValue = maxMinutes

            val displayedValues: MutableList<String> = ArrayList()

            for (i in 0..maxMinutes) displayedValues.add(String.format("%d", i))

            hoursPicker.displayedValues = displayedValues.toTypedArray()
            hoursPicker.value = initialMinutes

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}