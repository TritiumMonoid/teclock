package com.coffeebeans.tritiummonoid.teclock.schedule

import android.content.Context
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
        return false
    }

    fun ExecutorService.retrieveStudentSchedule(): Future<StudentSchedule> {
        return this.submit<StudentSchedule> {
            StudentSchedule(listOf())
        }
    }

    fun ExecutorService.storeStudentSchedule(): Future<StudentSchedule> {
        return this.submit<StudentSchedule> {
            val schedule: StudentSchedule = provider.getStudentSchedule().get()
            schedule
        }
    }
}