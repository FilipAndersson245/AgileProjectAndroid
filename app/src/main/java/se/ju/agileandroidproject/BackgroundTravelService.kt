package se.ju.agileandroidproject

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.Activities.MainActivity
import se.ju.agileandroidproject.Models.Gantry
import kotlin.concurrent.thread

class BackgroundTravelService: Service(){

    public val CHANNEL_ID = "backgroundServiceChannel"

    public var inTravelMode = false

    lateinit var isTravelingThreadLoop : Thread

    override fun onCreate() {
        super.onCreate()
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        inTravelMode = true

        GPSHandler.initializeContext(this)

        var notificationIntent = Intent(this, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this,
            0, notificationIntent,0)

        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("service title")
            .setContentText("test text")
            .setSmallIcon(R.drawable.icon_notification)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
            travelingThreadLoop()
        }

        isTravelingThreadLoop.start()
        //TODO: skapa tråd med allt arbete.

        /*thread (start = true, name = "backgroundThreadLoop") {
            gpsHandler.startListening(1000)
        }*/

        GPSHandler.startListening(1000)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        inTravelMode = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun travelingThreadLoop() = runBlocking<Unit> {

        Log.d("EH", "Thread started")
        while (inTravelMode) {
            Log.d("EH", "Looping in thread")
            launch {

                if (GPSHandler.locationExists){
                    Log.d("EH", "currentlocation exists")
                    val closeGantries = APIHandler.requestGantries(0f, 0f)

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

}