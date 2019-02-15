package com.coffeebeans.tritiummonoid.teclock.schedule

import java.util.concurrent.Future

class StudentScheduleCacheProvider(val provider: StudentScheduleProvider): StudentScheduleProvider {
    var schedule: Future<StudentSchedule>? = null

    override fun getStudentSchedule(): Future<StudentSchedule> {
        if (schedule == null) {
            schedule = provider.getStudentSchedule()
        }
        return schedule!!
    }
}