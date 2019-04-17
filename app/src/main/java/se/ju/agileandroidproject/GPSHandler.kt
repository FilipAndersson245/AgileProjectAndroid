package se.ju.agileandroidproject

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat

class GPSHandler(context: Context): Thread() {

    private val context: Context = context

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    lateinit var location: Location

    private val updateInterval = 5

    private val locationProvider: String = LocationManager.GPS_PROVIDER

    lateinit var lastKnownLocation: Location

    val locationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onLocationChanged(location: Location?) {
            //TODO: Do somewthing with new location.
        }

    }



    public override fun run() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,locationListener)

        } else{
            //TODO: Handle it.
        }
    }

    public fun getLastLocation(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)

        } else{
            //TODO: Handle it.
        }
    }


}