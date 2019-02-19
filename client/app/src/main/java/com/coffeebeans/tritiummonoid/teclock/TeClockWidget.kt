package com.coffeebeans.tritiummonoid.teclock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.coffeebeans.tritiummonoid.teclock.schedule.StudentScheduleProvider
import com.coffeebeans.tritiummonoid.teclock.schedule.StudentScheduleProviderFactory
import kotlinx.android.synthetic.main.te_clock_widget.view.*

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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent);
        when (intent?.action) {
            NEXT_CLASS -> {
                val appWidgetManager = AppWidgetManager.getInstance(context!!)
                val views = RemoteViews(context.packageName, R.layout.te_clock_widget)
                val widget = ComponentName(context!!, TeClockWidget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(widget)
                time += 100
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
            PREVIOUS_CLASS -> {
                val appWidgetManager = AppWidgetManager.getInstance(context!!)
                val widget = ComponentName(context!!, TeClockWidget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(widget)
                time -= 100
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    companion object {

        const val NEXT_CLASS = "NEXT_CLASS"
        const val PREVIOUS_CLASS = "PREVIOUS_CLASS"
        var provider: StudentScheduleProvider? = null
        var time: Int = 1500

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
            val studentClass = schedule?.getClassAt(1, time)
            if (studentClass == null) {
                views.setTextViewText(R.id.txtClassName, context.getString(R.string.no_classes_esp))
                views.setViewVisibility(R.id.txtClassName, View.VISIBLE)
                views.setViewVisibility(R.id.txtTeacherName, View.INVISIBLE)
                views.setViewVisibility(R.id.txtClassSeries, View.INVISIBLE)
                views.setViewVisibility(R.id.txtClassroom, View.INVISIBLE)
                views.setViewVisibility(R.id.txtClassTime, View.INVISIBLE)
                views.setViewVisibility(R.id.txtEmoji, View.INVISIBLE)
                views.setViewVisibility(R.id.btnNext, View.INVISIBLE)
                views.setViewVisibility(R.id.btnPrevious, View.INVISIBLE)
                views.setViewVisibility(R.id.barTime, View.INVISIBLE)
            } else if (studentClass != null) {
                views.setViewVisibility(R.id.txtClassName, View.VISIBLE)
                views.setViewVisibility(R.id.txtTeacherName, View.VISIBLE)
                views.setViewVisibility(R.id.txtClassSeries, View.VISIBLE)
                views.setViewVisibility(R.id.txtClassroom, View.VISIBLE)
                views.setViewVisibility(R.id.txtClassTime, View.VISIBLE)
                views.setViewVisibility(R.id.txtEmoji, View.VISIBLE)
                views.setViewVisibility(R.id.btnNext, View.VISIBLE)
                views.setViewVisibility(R.id.btnPrevious, View.VISIBLE)
                views.setViewVisibility(R.id.barTime, View.VISIBLE)
                views.setTextViewText(R.id.txtClassName, studentClass?.className!!)
                views.setTextViewText(R.id.txtTeacherName, studentClass?.teacher!!)
                views.setTextViewText(R.id.txtClassSeries, studentClass?.classSeries!!)
                views.setTextViewText(R.id.txtClassroom, studentClass?.classroom!!)
                views.setTextViewText(R.id.txtClassTime, studentClass?.classTime!!)
                val nextIntent = Intent(context, TeClockWidget::class.java)
                nextIntent.action = NEXT_CLASS
                val nextPending = PendingIntent.getBroadcast(context, 0, nextIntent, 0)
                views.setOnClickPendingIntent(R.id.btnNext, nextPending)
                val previousIntent = Intent(context, TeClockWidget::class.java)
                previousIntent.action = PREVIOUS_CLASS
                val previousPending = PendingIntent.getBroadcast(context, 0, previousIntent, 0)
                views.setOnClickPendingIntent(R.id.btnPrevious, previousPending)
                //views.setTextViewText(R.id.txtEmoji, studentClass?.classroom!!)
                views.setProgressBar(R.id.barTime, 100, 60, true)
            }
            //views.setTextViewText(R.id.appwidget_text, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

