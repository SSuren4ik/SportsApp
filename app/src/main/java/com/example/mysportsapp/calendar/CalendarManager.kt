package com.example.mysportsapp.calendar

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarManager {
    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getStartOfYear(currentYear: Int): Long {
        return Calendar.getInstance().apply {
            set(currentYear, Calendar.JANUARY, 1)
        }.timeInMillis
    }

    fun getSelectedDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}