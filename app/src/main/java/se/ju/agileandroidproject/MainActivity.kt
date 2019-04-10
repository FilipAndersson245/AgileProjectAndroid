package se.ju.agileandroidproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.location.LocationManager
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    object APIHandler

    private val gpsHandler = GPSHandler()

    private val gantry = Gantry()

    private val invoice = Invoice()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                //TODO: Gör saker för att börja läsa GPS-koordinater
            } else {
                Toast.makeText( this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }




}
