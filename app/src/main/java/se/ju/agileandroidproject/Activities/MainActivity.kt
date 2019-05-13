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
import android.widget.ArrayAdapter
import android.widget.ListView


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    //object APIHandler

    lateinit var gpsHandler: GPSHandler

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(se.ju.agileandroidproject.R.layout.activity_main)


    }

    // Remove "= runBlocking" when not using async here
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        gpsHandler = GPSHandler(applicationContext)
        checkPermissions()


//        val btnOne = findViewById(R.id.btn_one_sec) as Button
//
//        btnOne.setOnClickListener {
//            changeUpdateTime(1000)
//        }
//
//        val btnFive = findViewById(R.id.btn_five_sec) as Button
//
//        btnFive.setOnClickListener {
//            changeUpdateTime(5000)
//        }
//
//        val btnTen = findViewById(R.id.btn_ten_sec) as Button
//
//        btnTen.setOnClickListener {
//            changeUpdateTime(10000)
//        }
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



    fun changeUpdateTime(updateTime: Long){
        gpsHandler.setUpdateTime(updateTime)
    }


}
