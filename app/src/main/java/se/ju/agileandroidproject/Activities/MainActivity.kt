package se.ju.agileandroidproject.Activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import android.widget.Button
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R
import kotlinx.coroutines.*
import kotlin.system.*
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Models.Coordinate
import kotlin.concurrent.thread
import kotlin.system.*
import se.ju.agileandroidproject.BackgroundTravelService
import se.ju.agileandroidproject.Models.Gantry


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    private val ENTER_TRAVEL = true

    private val EXIT_TRAVEL = false

    private var isTraveling = false

    lateinit var gpsHandler: GPSHandler

    public val CHANNEL_ID = "backgroundServiceChannel"

    lateinit var isTravelingThreadLoop: Thread

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION
            )

            // TODO: Handle if the user denies access to GPS and then show a popup that explains why the app needs access to GPS.

        }
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                //TODO: Gör saker för att börja läsa GPS-koordinater

                gpsHandler.startListening(30000)


            } else {
                Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(se.ju.agileandroidproject.R.layout.activity_main)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "backgroundService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    // Remove "= runBlocking" when not using async here
    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        checkPermissions()


        isTravelingThreadLoop = thread(start = false, name = "ThreadLoop") {
            travelingThreadLoop()
        }

        startBackgroundService()

        // isTraveling = ENTER_TRAVEL
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun changeUpdateTime(updateTime: Int) {
        gpsHandler.setGPSUpdateTime(updateTime)
    }


    @UnstableDefault
    @ImplicitReflectionSerializer
    fun travelingThreadLoop() = runBlocking<Unit> {

        while (isTraveling) {

            launch {

                // Change later
                val closeGantries = APIHandler.requestGantries(0f, 0f)

                val gantriesList = mutableListOf<Gantry>()

                for (gantry in closeGantries) {
                    gantriesList.add(gantry)
                }

                gpsHandler.updateClosestGantry(gantriesList)

            }

            delay(gpsHandler.updateTime.toLong())
        }
    }

    fun startBackgroundService() {
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        startService(serviceIntent)
    }

    fun stopBackgroundService() {
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        stopService(serviceIntent)
    }

}
