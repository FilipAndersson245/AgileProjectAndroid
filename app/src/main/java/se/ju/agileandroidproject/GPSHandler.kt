package se.ju.agileandroidproject

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlin.math.log

private const val FIVE_SECONDS: Long = 5 * 1000
<<<<<<< HEAD
=======

private const val TEN_SECONDS: Long = 10 * 1000

class GPSHandler(context: Context): Thread() {
>>>>>>> 70866e7... Adds WIP on GPSHandler class.

private const val TEN_SECONDS: Long = 10 * 1000

class GPSHandler constructor(val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    lateinit var location: Location

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
            Log.d("EH", location.toString())
        }

    }



    public fun startListening() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,locationListener)
            Log.d("EH", "bok")

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

    public fun stopListening(){
        locationManager.removeUpdates(locationListener)
    }

    //Determine if the new GPS coordinate is accurate enough to warrant an update
    fun isBetterLocation(location: Location, currentBestLocation: Location?): Boolean {
        if (currentBestLocation == null) {

            return true
        }

        val timeDelta: Long = location.time - currentBestLocation.time
        val isSignificantlyNewer: Boolean = timeDelta > TEN_SECONDS
        val isSignificantlyOlder:Boolean = timeDelta < - TEN_SECONDS

        when {
            isSignificantlyNewer -> return true
            isSignificantlyOlder -> return false
        }

        val isNewer: Boolean = timeDelta > 0L
        val accuracyDelta: Float = location.accuracy - currentBestLocation.accuracy
        val isLessAccurate: Boolean = accuracyDelta > 0f
        val isMoreAccurate: Boolean = accuracyDelta < 0f
        val isSignificantlyLessAccurate: Boolean = accuracyDelta > 200f

        val isFromSameProvider: Boolean = location.provider == currentBestLocation.provider

        return when {
            isMoreAccurate -> true
            isNewer && !isLessAccurate -> true
            isNewer && !isSignificantlyLessAccurate && isFromSameProvider -> true
            else -> false
        }
    }


}