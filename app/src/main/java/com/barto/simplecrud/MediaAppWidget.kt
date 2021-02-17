package com.barto.simplecrud

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class MediaAppWidget : AppWidgetProvider() {

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
        super.onReceive(context, intent)
        val appWidgetId = intent?.getIntExtra("appWidgetId", 0)
        val views = RemoteViews(context?.packageName, R.layout.media_app_widget)

        if(intent?.action == context?.getString(R.string.action1)){
            Toast.makeText(context, "Action1", Toast.LENGTH_SHORT).show()
        }
        else if(intent?.action == context?.getString(R.string.action2)){
            views.setImageViewResource(R.id.widget_iv, R.drawable.sakura_tree)
            }
        else if(intent?.action == context?.getString(R.string.action3)){
            views.setImageViewResource(R.id.widget_iv, R.drawable.berserk_armor)
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        if (appWidgetId != null) {
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.media_app_widget)
    views.setTextViewText(R.id.widget_tv, widgetText)

    val intentWWW = Intent(Intent.ACTION_VIEW)
    intentWWW.data = Uri.parse("https://www.google.com")
    val pendingWWW = PendingIntent.getActivity(
        context,
        0,
        intentWWW,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt1, pendingWWW)

    val intentAction = Intent(context.getString(R.string.action1))
    intentAction.component = ComponentName(context, MediaAppWidget::class.java)

    val pendingAction = PendingIntent.getBroadcast(
        context,
        0,
        intentAction,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt2, pendingAction)

    val intentImage = Intent(context.getString(R.string.action2))
    intentImage.putExtra("appWidgetId", appWidgetId)
    intentImage.component = ComponentName(context, MediaAppWidget::class.java)

    val pendingImageChange = PendingIntent.getBroadcast(
        context,
        0,
        intentImage,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt3, pendingImageChange)

    val intentImage2 = Intent(context.getString(R.string.action3))
    intentImage2.putExtra("appWidgetId", appWidgetId)
    intentImage2.component = ComponentName(context, MediaAppWidget::class.java)

    val pendingImageChange2 = PendingIntent.getBroadcast(
            context,
            0,
            intentImage2,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt4, pendingImageChange2)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}