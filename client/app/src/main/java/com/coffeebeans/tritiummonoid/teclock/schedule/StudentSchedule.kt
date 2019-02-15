package com.coffeebeans.tritiummonoid.teclock.schedule

import java.text.SimpleDateFormat
import java.util.*

class StudentSchedule(val days: List<List<StudentClass>>) {

    fun getCurrentClass(): StudentClass? {
        val now = Date()
        val day = now.toDay()
        val time = now.toTime()
        val classes = days[day]
        val i = classes.binarySearch { studentClass -> studentClass.start - time }
        if (time >= classes[i].start && time < classes[i].end) {
            return classes[i]
        } else {
            return null
        }
    }

    fun Date.toDay(): Int {
        return dayFormatter.format(this).toInt();
    }

    fun Date.toTime(): Int {
        return timeFormatter.format(this).toInt();
    }

    companion object {
        val dayFormatter: SimpleDateFormat = SimpleDateFormat("u")
        val timeFormatter: SimpleDateFormat = SimpleDateFormat("HHmm")
    }
}