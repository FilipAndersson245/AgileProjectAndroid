package se.ju.agileandroidproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.Activities.MainActivity
import se.ju.agileandroidproject.Models.Gantry
import kotlin.concurrent.thread

class BackgroundTravelService : Service() {

    private var inTravelMode = false

    var notificationManager: NotificationManager? = null

    lateinit var isTravelingThreadLoop: Thread
    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        inTravelMode = true

        GPSHandler.initializeContext(this)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            packageName,
            "Gantry passing",
            "Gantry passing information."
        )

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, packageName)
            .setContentTitle("Tollgate")
            .setContentText("You are in travel mode")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        isTravelingThreadLoop = thread(start = false, name = "ThreadLoop") {
            travelingThreadLoop()
        }

        isTravelingThreadLoop.start()

        GPSHandler.startListening(1000)
        GPSHandler.updateTime = 1000

        return START_NOT_STICKY
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onDestroy() {
        inTravelMode = false
        GPSHandler.stopListening()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun travelingThreadLoop() = runBlocking<Unit> {

        while (inTravelMode) {
            launch {

                if (GPSHandler.locationExists) {
                    val closeGantries = APIHandler.requestGantries(
                        GPSHandler.currentLocation.longitude.toFloat(),
                        GPSHandler.currentLocation.latitude.toFloat()
                    )

                    val gantriesList = mutableListOf<Gantry>()

                    for (gantry in closeGantries) {
                        gantriesList.add(gantry)
                    }

                    GPSHandler.updateClosestGantry(gantriesList)

                }

            }

            delay(GPSHandler.updateTime.toLong())
        }
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun pushNotification(closestGantry: Gantry) {
        val notificationID = System.currentTimeMillis().toInt()

        val channelID = packageName

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification =
            NotificationCompat.Builder(
                this,
                channelID
            )
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Gantry passed")
                .setContentText("${closestGantry.price} kr")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channelID)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager!!.notify(notificationID, notification)
    }

}