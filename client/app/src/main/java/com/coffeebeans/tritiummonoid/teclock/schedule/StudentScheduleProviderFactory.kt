package com.coffeebeans.tritiummonoid.teclock.schedule

import android.content.Context

class StudentScheduleProviderFactory {
    fun create(context: Context): StudentScheduleProvider {
        return StudentScheduleCacheProvider(
            StudentScheduleStoreProvider(
                context,
                StudentScheduleErrorProvider.instance
            )
        )
    }

    fun create(context: Context, username: String, password: String): StudentScheduleProvider {
        return StudentScheduleCacheProvider(
            StudentScheduleStoreProvider(
                context,
                StudentScheduleNetworkProvider(
                    context,
                    username,
                    password
                )
            )
        )
    }
}