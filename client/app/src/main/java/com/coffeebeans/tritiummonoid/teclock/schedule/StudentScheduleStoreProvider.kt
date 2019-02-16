package com.coffeebeans.tritiummonoid.teclock.schedule

import android.content.Context
import android.util.Log
import java.io.File
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class StudentScheduleStoreProvider(
    val context: Context,
    val provider: StudentScheduleProvider
): StudentScheduleProvider {
    val executor: ExecutorService = Executors.newSingleThreadExecutor();

    override fun getStudentSchedule(): Future<StudentSchedule> {
        if (isStored()) {
            return executor.retrieveStudentSchedule()
        } else {
            return executor.storeStudentSchedule()
        }
    }

    fun isStored(): Boolean {
        val file = File(context.getExternalFilesDir("Documents"), "schedule.csv")
        return file.exists() && file.readText(charset("UTF-8")) != ""
    }

    fun ExecutorService.retrieveStudentSchedule(): Future<StudentSchedule> {
        return this.submit<StudentSchedule> {
            val file = File(context.getExternalFilesDir("Documents"), "schedule.csv")
            val schedule = file.readText(charset("UTF-8")).fromCsv()
            Log.d("StoreProvider", "Read data")
            schedule
        }
    }

    fun ExecutorService.storeStudentSchedule(): Future<StudentSchedule> {
        return this.submit<StudentSchedule> {
            val schedule: StudentSchedule = provider.getStudentSchedule().get()
            val file = File(context.getExternalFilesDir("Documents"), "schedule.csv")
            file.writeText(schedule.toCsv())
            Log.d("StoreProvider", "Wrote data")
            schedule
        }
    }

    fun StudentSchedule.toCsv(): String {
        return week
            .mapIndexed { day, data -> day.toString() + "," + data.joinToString(separator = ",") }
            .joinToString(separator = "\n")
    }

    fun String.fromCsv(): StudentSchedule {
        val weekData = this
            .split("\n")
            .map { it.split(",") }
        val week = MutableList<MutableList<StudentClass>>(7) { _ -> ArrayList() }
        weekData.forEach {
            val i = it[0].toInt()
            val iterator = it.drop(1).iterator()
            while (iterator.hasNextStudentClass()) {
                week[i].add(iterator.nextStudentClass())
            }
        }
        return StudentSchedule(week)
    }

    var iteratorStudentClass: StudentClass? = null
    fun Iterator<String>.hasNextStudentClass(): Boolean {
        iteratorStudentClass = null
        if (!hasNext())
            return false
        val classSeries = next()
        if (!hasNext())
            return false
        val className = next()
        if (!hasNext())
            return false
        val teacher = next()
        if (!hasNext())
            return false
        val classroom = next()
        if (!hasNext())
            return false
        val start = next().toInt()
        if (!hasNext())
            return false
        val end = next().toInt()
        iteratorStudentClass = StudentClass(classSeries, className, teacher, classroom, start, end)
        return true
    }

    fun Iterator<String>.nextStudentClass(): StudentClass {
        val studentClass = iteratorStudentClass
        iteratorStudentClass = null
        return studentClass!!
    }
}