package se.ju.agileandroidproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.coroutines.*
import se.ju.agileandroidproject.Models.Coordinate
import kotlin.concurrent.thread


private const val TEN_SECONDS: Long = 10 * 1000

@SuppressLint("StaticFieldLeak")
object GPSHandler {

    lateinit var context: Context

    lateinit var locationManager: LocationManager

    lateinit var currentLocation: Location

    lateinit var locationProvider: String

    private var lastKnownLocation: Location? = null

    var updateTime = 3 * 1000

    private var newUpdateTime = 30 * 1000

    private var coordinateOfClosestGantry: Coordinate? = null

    private var distanceToClosestGantry: Int? = null

    fun initializeContext(context: Context){
        this.context = context
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationProvider = LocationManager.GPS_PROVIDER
    }

    val locationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onLocationChanged(location: Location?) {
//            //TODO: Do somewthing with new location.
            Log.d("EH", location.toString())
            if (location != null){
                if (isBetterLocation(location, lastKnownLocation)){
                    currentLocation = location
                    lastKnownLocation = location
                    Log.d("EH","updated location")
                }
            }
        }

    }

    fun coordinatesDistance(lat1: Double, lng1: Double, lat2: Float, lng2: Float): Double {
        val earthRadius = 6371000.0 //meters
        val latDistance = Math.toRadians((lat2 - lat1).toDouble())
        val lonDistance = Math.toRadians((lng2 - lng1).toDouble())
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1.toDouble())) *
                Math.cos(Math.toRadians(lat2.toDouble())) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c)
    }

    public fun updateClosestGantry(coordinates: List<Coordinate>){
        for (coordinate in coordinates){
            if (currentLocation != null){
                val distance = coordinatesDistance(currentLocation.latitude, currentLocation.longitude, coordinate.lat, coordinate.lon)
                if (distanceToClosestGantry == null){
                    distanceToClosestGantry = distance.toInt()
                    coordinateOfClosestGantry = coordinate
                    Log.d("EH", "Updated closest coordinate to" + coordinate.toString())
                }
                else if (distance.toInt() < distanceToClosestGantry!!){
                    distanceToClosestGantry = distance.toInt()
                    coordinateOfClosestGantry = coordinate
                    Log.d("EH", "Updated closest coordinate to" + coordinate.toString())
                }
            }
        }
    }

    fun setGPSUpdateTime(newTime: Int) {
        newUpdateTime = newTime
        if (newUpdateTime != updateTime){
            stopListening()
            Log.d("EH", "Stopped listening with " + updateTime / 1000 + " second interval")
            startListening(newUpdateTime)
            Log.d("EH", "Started listening with " + newUpdateTime / 1000 + " second interval")
            updateTime = newUpdateTime
        }
    }


    public fun startListening(updateTime: Int) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime.toLong(), 0f,locationListener)
            Log.d("EH", "bok")

        } else{
            //TODO: Handle it.
        }
    }


    public fun getLastLocation(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
            if (lastKnownLocation != null){
                currentLocation = lastKnownLocation!!
            }
        } else{
            //TODO: Handle it.
        }
    }

    public fun stopListening(){
        locationManager.removeUpdates(locationListener)
    }

    //Determine if the new GPS coordinate is accurate enough to warrant an update
    fun isBetterLocation(location: Location, currentBestLocation: Location?): Boolean {
        Log.d("EH", "new location" + location.toString())
        Log.d("EH", "last known: " + currentBestLocation.toString())
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