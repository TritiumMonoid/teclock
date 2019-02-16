package com.coffeebeans.tritiummonoid.teclock

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.coffeebeans.tritiummonoid.teclock.schedule.StudentScheduleProviderFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val provider = StudentScheduleProviderFactory().create(this, "", "")
        try {
            val schedule = provider.getStudentSchedule().get()
            val schedule2 = provider.getStudentSchedule().get()
        } catch (ex: Exception) {
            Log.e("FATAL ERROR", ex.message)
        }
    }
}