package com.example.mysportsapp

class Training(
    private var name: String = "",
    private var calories: Int = 0,
    private var date: String = ""
) {
    fun getName(): String {
        return name
    }

    fun getCalories(): Int {
        return calories
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setCalories(calories: Int) {
        this.calories = calories
    }

    fun getDate(): String {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }
}

