package se.ju.agileandroidproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.Models.Coordinate
import se.ju.agileandroidproject.Models.Gantry

private const val GANTRY_INNER_CIRCLE_DISTANCE = 25

private const val GANTRY_OUTER_CIRCLE_DISTANCE = 50

@SuppressLint("StaticFieldLeak")
object GPSHandler {

    lateinit var context: Context

    lateinit var locationManager: LocationManager

    lateinit var currentLocation: Location

    lateinit var locationProvider: String

    private var lastKnownLocation: Location? = null

    var updateTime = 3 * 1000

    var longestUpdateTime = updateTime * 2.5

    private var newUpdateTime = 30 * 1000

    private var closestGantry: Gantry? = null

    private var distanceToClosestGantry: Int? = null

    var closeProximityToGantryCoordinatesList = mutableListOf<Coordinate>()

    fun initializeContext(context: Context){
        this.context = context
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationProvider = LocationManager.GPS_PROVIDER
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    val locationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onLocationChanged(location: Location?) {
            Log.d("EH", location.toString())
            if (location != null){
                if (isBetterLocation(location, lastKnownLocation)){
                    currentLocation = location
                    lastKnownLocation = location
                    Log.d("EH","updated location")
                    if (distanceToClosestGantry != null && closestGantry != null){
                        if(distanceToClosestGantry!! < GANTRY_OUTER_CIRCLE_DISTANCE){
                            Log.d("EH", "Driving close to a gantry")
                            closeProximityToGantryCoordinatesList.add(Coordinate(currentLocation.longitude.toFloat(), currentLocation.latitude.toFloat()))
                        }
                        else{
                            if (closeProximityToGantryCoordinatesList.size > 0){
                                if (wasGantryPassed(closeProximityToGantryCoordinatesList, Coordinate(closestGantry!!.longitude, closestGantry!!.latitude))){
                                    APIHandler.registerPassage("TEMP", closestGantry!!.id)
                                    //TODO: Send correct userID to function
                                    Log.d("EH", "Gantry was passed")
                                }
                                else{
                                     Log.d("EH", "Gantry was not passed")
                                }
                                closeProximityToGantryCoordinatesList.clear()
                            }
                        }
                    }
                }
            }
        }
    }

    fun wasGantryPassed(coordList: List<Coordinate>, closestGantry: Coordinate): Boolean{
        val midPoint = middlePointOfPassage(coordList)
        if (coordinatesDistance(midPoint.lat, midPoint.lon, closestGantry.lat, closestGantry.lon) < GANTRY_INNER_CIRCLE_DISTANCE){
            return true
        }
        return false
    }

    fun middlePointOfPassage (coordList: List<Coordinate>): Coordinate{
        val midLat = (coordList.first().lat + coordList.last().lat) / 2
        val midLong = (coordList.first().lon + coordList.last().lon) / 2
        return Coordinate(midLong, midLat)
    }

    fun coordinatesDistance(lat1: Float, lng1: Float, lat2: Float, lng2: Float): Double {
        val earthRadius = 6371000.0 //meters
        val latDistance = Math.toRadians((lat2 - lat1).toDouble())
        val lonDistance = Math.toRadians((lng2 - lng1).toDouble())
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1.toDouble())) *
                Math.cos(Math.toRadians(lat2.toDouble())) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c)
    }

    public fun updateClosestGantry(gantries: List<Gantry>){
        for (gantry in gantries){
            val distance = coordinatesDistance(currentLocation.latitude.toFloat(), currentLocation.longitude.toFloat(), gantry.latitude, gantry.longitude)
            if (distanceToClosestGantry == null){
                distanceToClosestGantry = distance.toInt()
                closestGantry = gantry
                Log.d("EH", "Updated closest coordinate to" + gantry.toString())
            }
            else if (distance.toInt() < distanceToClosestGantry!!){
                distanceToClosestGantry = distance.toInt()
                closestGantry = gantry
                Log.d("EH", "Updated closest coordinate to" + gantry.toString())
            }
        }
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun setGPSUpdateTime(newTime: Int) {
        newUpdateTime = newTime
        if (newUpdateTime != updateTime){
            stopListening()
            Log.d("EH", "Stopped listening with " + updateTime / 1000 + " second interval")
            startListening(newUpdateTime)
            Log.d("EH", "Started listening with " + newUpdateTime / 1000 + " second interval")
            updateTime = newUpdateTime
            longestUpdateTime = updateTime * 2.5
        }
    }


    @UnstableDefault
    @ImplicitReflectionSerializer
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

    @UnstableDefault
    @ImplicitReflectionSerializer
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
        val isSignificantlyNewer: Boolean = timeDelta > longestUpdateTime
        val isSignificantlyOlder:Boolean = timeDelta < - longestUpdateTime

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