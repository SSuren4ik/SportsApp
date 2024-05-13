package com.example.mysportsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysportsapp.DBHelper
import com.example.mysportsapp.calendar.CalendarManager
import com.example.mysportsapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val calendarManager = CalendarManager()
    private lateinit var dataBase: DBHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dataBase = DBHelper(requireContext(), null)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDay = calendarManager.getSelectedDate(year, month, dayOfMonth)
            val trainings = dataBase.getTrainingsByDate(selectedDay)
            if (trainings.isEmpty()) {
                binding.Name.text = ""
                binding.Calories.text = ""
            }
            for (training in trainings) {
                binding.Name.append(training.getName() + "\n")
                binding.Calories.append(training.getCalories().toString() + "\n")
            }
        }
    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}