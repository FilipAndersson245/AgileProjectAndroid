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
import android.widget.AdapterView
import android.widget.Button
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    object APIHandler

    lateinit var gpsHandler: GPSHandler

    private val gantry = Gantry()

    private val invoice = Invoice()

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
        setContentView(R.layout.activity_main)


        val btnOne = findViewById(R.id.btn_one_sec) as Button

        btnOne.setOnClickListener {
            updateOne()
        }

        val btnFive = findViewById(R.id.btn_five_sec) as Button

        btnFive.setOnClickListener {
            updateFive()
        }

        val btnTen = findViewById(R.id.btn_ten_sec) as Button

        btnTen.setOnClickListener {
            updateTen()
        }



    }

    override fun onStart() {
        super.onStart()
        gpsHandler = GPSHandler(applicationContext)
        checkPermissions()

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



    fun updateOne(){
        gpsHandler.setUpdateTime(1000)
    }

    fun updateFive(){
        gpsHandler.setUpdateTime(5000)
    }

    fun updateTen(){
        gpsHandler.setUpdateTime(10000)
    }

}
