package com.coffeebeans.tritiummonoid.teclock.schedule

class StudentClass(
    val classSeries: String,
    val className: String,
    val teacher: String,
    val classroom: String,
    val start: Int,
    val end: Int): Comparable<StudentClass> {

    val classTime: String = "${start.formatTime()}:${end.formatTime()}"

    override fun compareTo(other: StudentClass): Int {
        return start - other.start;
    }

    override fun toString(): String {
        return "$classSeries,$className,$teacher,$classroom,$start,$end"
    }

    companion object {
        const val intFormatterString: String = "%02d"

        fun Int.formatString(): String {
            return intFormatterString.format(this)
        }

        fun Int.formatTime(): String {
            val hour = this / 100
            val minute = this - hour * 100
            return "${hour.formatString()}:${minute.formatString()}"
        }
    }
}