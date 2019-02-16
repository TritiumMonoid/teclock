package com.coffeebeans.tritiummonoid.teclock.schedule

import java.text.SimpleDateFormat
import java.util.*

class StudentSchedule(val week: List<List<StudentClass>>) {

    fun getClassAt(day: Int, time: Int): StudentClass? {
        if (week.isEmpty()) {
            return null
        }
        val classes = week[day - 1]
        var i = classes.binarySearch {
            var comparison = 0
            if (time < it.start) {
                comparison = 1
            }
            if (time >= it.end) {
                comparison = -1
            }
            comparison
        }
        if (i < 0) {
            return null
        }
        return classes[i]
    }

    fun getCurrentClass(): StudentClass? {
        val now = Date()
        return getClassAt(now.toDayOfWeek(), now.toTimeOfDay())
    }

    fun Date.toDayOfWeek(): Int {
        return dayOfWeekFormatter.format(this).toInt();
    }

    fun Date.toTimeOfDay(): Int {
        return timeOfDayFormatter.format(this).toInt();
    }

    companion object {
        val dayOfWeekFormatter: SimpleDateFormat = SimpleDateFormat("u")
        val timeOfDayFormatter: SimpleDateFormat = SimpleDateFormat("HHmm")
    }
}