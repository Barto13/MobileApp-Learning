package com.barto.simplecrud

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {

    private var id = 0
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        createChannel(context)
        val event = GeofencingEvent.fromIntent(intent)
        val shopList = Intent(context, MapsActivity::class.java)

        for(geo in event.triggeringGeofences){
                if(event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                    Toast.makeText(context, "Geofence with id: ${geo.requestId} triggered. Welcome ", Toast.LENGTH_SHORT).show()
                    val pendingIntent = PendingIntent.getActivity(
                            context,
                            id,
                            shopList,
                            PendingIntent.FLAG_ONE_SHOT
                    )

                    //builder
                    val notification = NotificationCompat.Builder(context, context.getString(R.string.channelID))
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("Triggered geofence for shop with geofence : ${geo.requestId}  Entered shop radius, Welcome back!")
                            .setContentText("Entered: ${geo.requestId} Welcome!")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build()

                    NotificationManagerCompat.from(context).notify(id++, notification)
                }
            if(event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                Toast.makeText(context, "Geofence with id: ${geo.requestId} triggered. Bye Bye", Toast.LENGTH_SHORT).show()
                val pendingIntent = PendingIntent.getActivity(
                        context,
                        id,
                        shopList,
                        PendingIntent.FLAG_ONE_SHOT
                )

                //builder
                val notification = NotificationCompat.Builder(context, context.getString(R.string.channelID))
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Triggered geofence for shop with geofence : ${geo.requestId}  Exited shop!! Bye Bye")
                        .setContentText("Exited: ${geo.requestId} Bye Bye!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()

                NotificationManagerCompat.from(context).notify(id++, notification)
            }
        }

    }

    fun createChannel(context: Context){
        val channel = NotificationChannel(
                context.getString(R.string.channelID),
                context.getString(R.string.channelName),
                NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}