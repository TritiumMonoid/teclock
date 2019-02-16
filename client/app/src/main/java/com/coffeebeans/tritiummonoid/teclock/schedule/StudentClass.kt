package com.coffeebeans.tritiummonoid.teclock.schedule

class StudentClass(
    val classSeries: String,
    val className: String,
    val teacher: String,
    val classroom: String,
    val start: Int,
    val end: Int): Comparable<StudentClass> {

    override fun compareTo(other: StudentClass): Int {
        return start - other.start;
    }

    override fun toString(): String {
        return "$classSeries,$className,$teacher,$classroom,$start,$end"
    }
}