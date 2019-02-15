package com.coffeebeans.tritiummonoid.teclock.schedule

import java.util.concurrent.Future

interface StudentScheduleProvider {
    fun getStudentSchedule(): Future<StudentSchedule>
}