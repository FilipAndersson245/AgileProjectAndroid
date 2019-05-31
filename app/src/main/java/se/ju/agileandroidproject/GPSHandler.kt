package se.ju.agileandroidproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.Log
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.Activities.MainActivity
import se.ju.agileandroidproject.Models.Coordinate
import se.ju.agileandroidproject.Models.Gantry

private const val GANTRY_INNER_CIRCLE_DISTANCE = 25

private const val GANTRY_OUTER_CIRCLE_DISTANCE = 50

private const val BIG_DISTANCE = 5000

private const val MEDIUM_DISTANCE = 1000

private const val SMALL_DISTANCE = 500

private const val SMALLEST_DISTANCE = 200


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

    var distanceToClosestGantry: Int? = null

    var closeProximityToGantryCoordinatesList = mutableListOf<Coordinate>()

    var locationExists = false

    var closeGantries: List<Gantry> = listOf()

    fun initializeContext(context: Context) {
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

            if (location != null) {
                if (isBetterLocation(location, lastKnownLocation)) {
                    currentLocation = location
                    locationExists = true
                    lastKnownLocation = location

                    if (distanceToClosestGantry != null && closestGantry != null) {


                        if (distanceToClosestGantry!! < GANTRY_OUTER_CIRCLE_DISTANCE) {
                            closeProximityToGantryCoordinatesList.add(
                                Coordinate(
                                    currentLocation.longitude.toFloat(),
                                    currentLocation.latitude.toFloat()
                                )
                            )
                        } else {
                            if (closeProximityToGantryCoordinatesList.size > 0) {
                                if (wasGantryPassed(
                                        closeProximityToGantryCoordinatesList,
                                        Coordinate(closestGantry!!.longitude, closestGantry!!.latitude)
                                    )
                                ) {
                                    APIHandler.registerPassage(APIHandler.personalId, closestGantry!!.id)

                                    (context as BackgroundTravelService).pushNotification(closestGantry!!)
                                }
                                closeProximityToGantryCoordinatesList.clear()
                            }
                        }
                    }

                    //Change update time of gps and requests based on distance to Gantry:
                    if (distanceToClosestGantry != null) {
                        when {
                            distanceToClosestGantry!! > BIG_DISTANCE && updateTime != 30000 -> {
                                setGPSUpdateTime(30000)
                            }
                            distanceToClosestGantry!! > MEDIUM_DISTANCE && distanceToClosestGantry!! <= BIG_DISTANCE && updateTime != 10000 -> {
                                setGPSUpdateTime(10000)
                            }
                            distanceToClosestGantry!! > SMALL_DISTANCE && distanceToClosestGantry!! <= MEDIUM_DISTANCE && updateTime != 4000 -> {
                                setGPSUpdateTime(4000)
                            }
                            distanceToClosestGantry!! > SMALLEST_DISTANCE && distanceToClosestGantry!! <= SMALL_DISTANCE && updateTime != 2000 -> {
                                setGPSUpdateTime(1000)
                            }
                            distanceToClosestGantry!! >= 0 && distanceToClosestGantry!! <= SMALLEST_DISTANCE && updateTime != 1000 -> {
                                setGPSUpdateTime(500)
                            }
                        }
                    }
                }
            }
        }
    }

    fun wasGantryPassed(coordList: List<Coordinate>, closestGantry: Coordinate): Boolean {
        val midPoint = middlePointOfPassage(coordList)

        if (coordinatesDistance(
                midPoint.lat,
                midPoint.lon,
                closestGantry.lat,
                closestGantry.lon
            ) < GANTRY_INNER_CIRCLE_DISTANCE
        ) {
            return true
        }
        return false
    }

    fun middlePointOfPassage(coordList: List<Coordinate>): Coordinate {
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

    fun updateClosestGantry(gantries: List<Gantry>) {
        closeGantries = gantries

        val listOfClosest = mutableListOf<Pair<Int, Gantry>>()

        for (gantry in gantries) {
            val distance = coordinatesDistance(
                currentLocation.latitude.toFloat(),
                currentLocation.longitude.toFloat(),
                gantry.latitude,
                gantry.longitude
            )

            listOfClosest.add(Pair(distance.toInt(), gantry))
        }

        listOfClosest.sortBy { x -> x.first }

        distanceToClosestGantry = listOfClosest.first().first
        closestGantry = listOfClosest.first().second
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun setGPSUpdateTime(newTime: Int) {
        newUpdateTime = newTime
        if (newUpdateTime != updateTime) {
            stopListening()
            startListening(newUpdateTime)
            updateTime = newUpdateTime
            longestUpdateTime = updateTime * 2.5
        }
    }


    @UnstableDefault
    @ImplicitReflectionSerializer
    fun startListening(updateTime: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                updateTime.toLong(),
                0f,
                locationListener
            )

        } else {
            //TODO: Handle permission not given.
        }
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun stopListening() {
        locationManager.removeUpdates(locationListener)
    }

    //Determine if the new GPS coordinate is accurate enough to warrant an update
    fun isBetterLocation(location: Location, currentBestLocation: Location?): Boolean {
        if (currentBestLocation == null) {

            return true
        }

        val timeDelta: Long = location.time - currentBestLocation.time
        val isSignificantlyNewer: Boolean = timeDelta > longestUpdateTime
        val isSignificantlyOlder: Boolean = timeDelta < -longestUpdateTime

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