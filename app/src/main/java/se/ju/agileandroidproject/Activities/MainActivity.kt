package se.ju.agileandroidproject.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import android.widget.Button
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R
import kotlinx.coroutines.*
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Models.Coordinate
import kotlin.concurrent.thread


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

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking<Unit> {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gpsHandler = GPSHandler(applicationContext)

        Log.d("EH","${Thread.currentThread()} has run.")

        isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
            Log.d("EH","${Thread.currentThread()} has run.")
            travelingThreadLoop()
            Log.d("EH","Thread Ended!!")

//            launch {
//                Log.d("EH","Thing in launch!!")
////                travelingThreadLoop()
//            }
        }


//        Log.d("EH", "Important stuff: " + isTravelingThreadLoop.state.toString())

    }

    // Remove "= runBlocking" when not using async here
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        checkPermissions()

        Log.d("EH","${Thread.currentThread()} has run. (Should be main)")

//        isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
//            Log.d("EH","${Thread.currentThread()} has run.")
//            launch {
//                Log.d("EH","DELAY")
//                delay(2000)
//                Log.d("EH","DELAY")
//                travelingThreadLoop()
//                delay(2000)
//                Log.d("EH","AFTER DELAY")
//            }
//        }



//        launch(newSingleThreadContext("ThreadLoop")) {
//            Log.d("EH","${Thread.currentThread()} has run.")
//            threadLoop()
//        }

        val btnOne = findViewById(R.id.btn_one_sec) as Button

        btnOne.setOnClickListener {
            changeUpdateTime(1000)
        }

        val btnFive = findViewById(R.id.btn_five_sec) as Button

        btnFive.setOnClickListener {
            changeUpdateTime(5000)
        }

        val btnTen = findViewById(R.id.btn_ten_sec) as Button

        btnTen.setOnClickListener {
            Log.d("EH", "Useless button!!!")
        }

        val start_travel = findViewById(R.id.start_travel) as Button
        start_travel.setOnClickListener {
            Log.d("EH", "Clicked start")

            if (!isTraveling) {
                Log.d("EH", "START TRAVEL")

                isTravelingThreadLoop =  thread(start = false, name = "ThreadLoop") {
                    Log.d("EH","${Thread.currentThread()} has run.")
                    travelingThreadLoop()
                    Log.d("EH","Thread Ended!!")
                }

                isTraveling = ENTER_TRAVEL
                isTravelingThreadLoop.start()
            }

        }

        val stop_travel = findViewById(R.id.stop_travel) as Button
        stop_travel.setOnClickListener {
            Log.d("EH", "Clicked stop")

            if (isTraveling) {
                Log.d("EH", "STOP TRAVEL")
                isTraveling = EXIT_TRAVEL
                isTravelingThreadLoop.join()
            }
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



    fun changeUpdateTime(updateTime: Int){
        gpsHandler.setGPSUpdateTime(updateTime)
    }


    fun travelingThreadLoop() = runBlocking<Unit> {

        launch {
            while (isTraveling) {

                Log.d("EH", "LOOOOOOOOOOOOOOOOP!")
                Log.d("EH","${Thread.currentThread()} has run.")
//                if (gpsHandler.currentLocation != null) {
//                    val positions = APIHandler.getClosestGantries(Coordinate(gpsHandler.currentLocation.longitude, gpsHandler.currentLocation.latitude))
//                    Log.d("EH", "Done!")
//                }

                
                delay(1000)
            }
        }

    }


}
