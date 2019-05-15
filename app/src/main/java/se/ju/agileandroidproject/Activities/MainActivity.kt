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
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        checkPermissions()

        Log.d("EH", "${Thread.currentThread()} has run. (Should be main)")
    }



//        val start_travel = findViewById(R.id.start_travel) as Button
//        start_travel.setOnClickListener {
//            Log.d("EH", "Clicked start")
//
//            if (!isTraveling) {
//                Log.d("EH", "START TRAVEL")
//
//                isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
//                    Log.d("EH","${Thread.currentThread()} has run.")
//                    travelingThreadLoop()
//                    Log.d("EH","Thread Ended!!")
//                }
//
//                isTraveling = ENTER_TRAVEL
//                isTravelingThreadLoop.start()
//            }
//
//        }
//
//        val stop_travel = findViewById(R.id.stop_travel) as Button
//        stop_travel.setOnClickListener {
//            Log.d("EH", "Clicked stop")
//
//            if (isTraveling) {
//                Log.d("EH", "STOP TRAVEL")
//                isTraveling = EXIT_TRAVEL
//                isTravelingThreadLoop.join()
//            }
//        }

    fun changeUpdateTime(updateTime: Int){
        gpsHandler.setGPSUpdateTime(updateTime)
    }


    fun travelingThreadLoop() = runBlocking<Unit> {

        while (isTraveling) {

            Log.d("EH", "LOOOOOOOOOOOOOOOOP!")
            Log.d("EH","${Thread.currentThread()} has run.")

            launch {

//                if (gpsHandler.currentLocation != null) {
//                    val positions = APIHandler.getClosestGantries(Coordinate(gpsHandler.currentLocation.longitude, gpsHandler.currentLocation.latitude))
//                    Log.d("EH", "Done!")
//                }

                Log.d("EH", "Get Gantries!")

                // get gantries

                Log.d("EH", "Update Gantries!")

                // gpsHandler.updateClosestGantry()
            }



            delay(gpsHandler.updateTime.toLong())

            Log.d("EH", "HERE WE GO AGAIN")
        }
    }



}
