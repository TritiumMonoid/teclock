package com.coffeebeans.tritiummonoid.teclock.schedule

import android.util.Log
import java.util.concurrent.Future

class StudentScheduleCacheProvider(val provider: StudentScheduleProvider): StudentScheduleProvider {
    var schedule: Future<StudentSchedule>? = null

    override fun getStudentSchedule(): Future<StudentSchedule> {
        if (schedule == null) {
            schedule = provider.getStudentSchedule()
            Log.d("CacheProvider", "Cache data")
        }
        return schedule!!
    }
}