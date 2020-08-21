package appvian.water.buddy.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.room.Room
import appvian.water.buddy.R
import appvian.water.buddy.model.datasource.local.WaterBuddyDb
import appvian.water.buddy.view.MainActivity
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class HomeWidget : AppWidgetProvider() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val testWidget = ComponentName(
            context!!.packageName,
            HomeWidget::class.java.name
        )

        val widgetIds = appWidgetManager.getAppWidgetIds(testWidget)

        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            if (widgetIds != null && widgetIds.size > 0) {
                onUpdate(context, AppWidgetManager.getInstance(context), widgetIds)
            }
        }
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.home_widget)

        val target_amounts_int_pref = context.getSharedPreferences("target_amounts_ml", Context.MODE_PRIVATE)
        val target_amounts_pref = context.getSharedPreferences("target_amounts", Context.MODE_PRIVATE)

        val target_amount = target_amounts_int_pref.getInt("target_amounts_ml",0)

        val db: WaterBuddyDb =
            Room.databaseBuilder<WaterBuddyDb>(context, WaterBuddyDb::class.java, "intake").build()
        val today = getToday()
        val tomorrow = getTomorrow()
        var dailyPercent = 0
        var dailyAmount = 0
        val t = Thread(Runnable { kotlin.run {
            dailyPercent = db.intakeDao().getDailyPercentFloat(today, tomorrow, target_amount).toInt()
            dailyAmount = db.intakeDao().getDailyAmountInt(today, tomorrow)
        } })
        t.start()
        t.join()

        views.setTextViewText(R.id.txt_percent_val, dailyPercent.toString())

        views.setTextViewText(
                R.id.txt_intake_amount,
        String.format("%.1fL", dailyAmount.toDouble() / 1000)
        )

        views.setTextViewText(R.id.txt_target_amount, String.format("%.1fL",target_amounts_pref.getFloat("target_amounts", 0f)))



        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.component = ComponentName(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(context, 0, intent, 0)
        views.setOnClickPendingIntent(R.id.layout_back, pending)


        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getToday(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val today = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val tomorrow = calendar.time.time
        return tomorrow
    }
}
