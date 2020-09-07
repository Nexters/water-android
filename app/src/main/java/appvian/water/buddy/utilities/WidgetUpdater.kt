package appvian.water.buddy.utilities

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import appvian.water.buddy.widget.HomeWidget

object WidgetUpdater {
    fun update(context : Context){
        val intent = Intent(context, HomeWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        context.sendBroadcast(intent)
    }
}