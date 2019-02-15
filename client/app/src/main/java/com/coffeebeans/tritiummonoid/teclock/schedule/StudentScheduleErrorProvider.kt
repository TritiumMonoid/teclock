package com.coffeebeans.tritiummonoid.teclock.schedule

import java.util.concurrent.Future

class StudentScheduleErrorProvider: StudentScheduleProvider {

    override fun getStudentSchedule(): Future<StudentSchedule> {
        throw RuntimeException()
    }

    companion object {
        val instance: StudentScheduleErrorProvider = StudentScheduleErrorProvider()
    }
}