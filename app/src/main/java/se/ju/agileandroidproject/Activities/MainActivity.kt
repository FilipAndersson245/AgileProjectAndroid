package se.ju.agileandroidproject.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.location.Location
import android.location.LocationManager
import android.content.Intent
import android.content.Context
import android.widget.Toast
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R
import se.ju.agileandroidproject.APIHandler
import kotlinx.coroutines.*
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.Models.Coordinate
import kotlin.concurrent.thread
import kotlin.system.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    private val ENTER_TRAVEL = true

    private val EXIT_TRAVEL = false

    private var isTraveling = false

    lateinit var gpsHandler: GPSHandler

    lateinit var isTravelingThreadLoop : Thread

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION)

            // TODO: Handle if the user denies access to GPS and then show a popup that explains why the app needs access to GPS.

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                //TODO: Gör saker för att börja läsa GPS-koordinater

                gpsHandler.startListening(30000)


            } else {
                Toast.makeText( this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gpsHandler = GPSHandler(applicationContext)
    }

    // Remove "= runBlocking" when not using async here
    @ImplicitReflectionSerializer
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        checkPermissions()

        isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
            travelingThreadLoop()
        }

        isTraveling = ENTER_TRAVEL
        isTravelingThreadLoop.start()
    }

    fun changeUpdateTime(updateTime: Int){
        gpsHandler.setGPSUpdateTime(updateTime)
    }


    @UnstableDefault
    @ImplicitReflectionSerializer
    fun travelingThreadLoop() = runBlocking<Unit> {

        while (isTraveling) {

            launch {

                // Change later
                val closeGantries = APIHandler.requestGantries(0f, 0f)

                val coordinatesList = mutableListOf<Coordinate>()

                for (gantry in closeGantries) {
                    coordinatesList.add(Coordinate(gantry.longitude, gantry.latitude))
                }

                gpsHandler.updateClosestGantry(coordinatesList)

            }

            delay(gpsHandler.updateTime.toLong())
        }
    }



}
