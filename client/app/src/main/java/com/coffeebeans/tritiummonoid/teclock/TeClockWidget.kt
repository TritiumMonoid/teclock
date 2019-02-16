package com.coffeebeans.tritiummonoid.teclock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.coffeebeans.tritiummonoid.teclock.schedule.StudentScheduleProvider
import com.coffeebeans.tritiummonoid.teclock.schedule.StudentScheduleProviderFactory

/**
 * Implementation of App Widget functionality.
 */
class TeClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        var provider: StudentScheduleProvider? = null

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            if (provider == null) {
                provider = StudentScheduleProviderFactory().create(context)
            }
            //val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.te_clock_widget)
            val schedule = provider?.getStudentSchedule()?.get()
            val studentClass = schedule?.getClassAt(1, 1400)
            if (studentClass != null) {
                views.setTextViewText(R.id.txtClassName, studentClass?.className!!)
                views.setTextViewText(R.id.txtTeacherName, studentClass?.teacher!!)
                views.setTextViewText(R.id.txtClassSeries, studentClass?.classSeries!!)
                views.setTextViewText(R.id.txtClassroom, studentClass?.classroom!!)
            }
            //views.setTextViewText(R.id.appwidget_text, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

