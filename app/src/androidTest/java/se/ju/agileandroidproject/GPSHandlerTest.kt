package se.ju.agileandroidproject

import android.location.Location
import android.location.LocationManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import se.ju.agileandroidproject.Models.Coordinate

@RunWith(AndroidJUnit4::class)
class GPSHandlerTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("se.ju.agileandroidproject", appContext.packageName)
    }

    @Test
    fun updatesCoordsWhenBetter() {

        val appContext = InstrumentationRegistry.getTargetContext()

        GPSHandler.initializeContext(appContext)

        var originalLocation = Location(LocationManager.GPS_PROVIDER)
        originalLocation.latitude = 55.1
        originalLocation.longitude = 55.1
        originalLocation.accuracy = 10f
        originalLocation.time = 100000


        var badAccuracyLocation = Location(LocationManager.GPS_PROVIDER)
        badAccuracyLocation.latitude = 56.1
        badAccuracyLocation.longitude = 56.1
        badAccuracyLocation.accuracy = 100f
        badAccuracyLocation.time = 100000

        var longTimeLocation = Location(LocationManager.GPS_PROVIDER)
        longTimeLocation.latitude = 57.1
        longTimeLocation.longitude = 57.1
        longTimeLocation.accuracy = 10f
        longTimeLocation.time = 101500

        assertFalse(GPSHandler.isBetterLocation(badAccuracyLocation, originalLocation))

        assertTrue(GPSHandler.isBetterLocation(longTimeLocation, originalLocation))

    }


    @Test
    fun checkDistance() {

        val appContext = InstrumentationRegistry.getTargetContext()

        GPSHandler.initializeContext(appContext)

        var jkpgLon: Float = 14.15618.toFloat()
        var jkpgLat: Float = 57.78145.toFloat()
        var sthlmLon: Float = 18.063240.toFloat()
        var sthlmLat: Float = 59.334591.toFloat()
        var distance = GPSHandler.coordinatesDistance(jkpgLat, jkpgLon, sthlmLat, sthlmLon)
        assertTrue(distance < 290000 && distance > 280000)
    }


    @Test
    fun correctlyDeterminePassage() {
        val appContext = InstrumentationRegistry.getTargetContext()

        GPSHandler.initializeContext(appContext)

        val mockGantryCoordinate: Coordinate = Coordinate(14.2180352.toFloat(), 57.7847296.toFloat())

        val gantryPassageList = mutableListOf<Coordinate>()
        val gantryNonPassageList = mutableListOf<Coordinate>()

        gantryPassageList.add(Coordinate(14.217343.toFloat(), 57.784494.toFloat()))
        gantryPassageList.add(Coordinate(14.217767.toFloat(), 57.784655.toFloat()))
        gantryPassageList.add(Coordinate(14.218203.toFloat(), 57.784830.toFloat()))
        gantryPassageList.add(Coordinate(14.218431.toFloat(), 57.784959.toFloat()))

        gantryNonPassageList.add(Coordinate(14.217350.toFloat(), 57.784490.toFloat()))
        gantryNonPassageList.add(Coordinate(14.217383.toFloat(), 57.784652.toFloat()))
        gantryNonPassageList.add(Coordinate(14.217420.toFloat(), 57.784772.toFloat()))
        gantryNonPassageList.add(Coordinate(14.217456.toFloat(), 57.784877.toFloat()))

        assertTrue(GPSHandler.wasGantryPassed(gantryPassageList, mockGantryCoordinate))
        assertFalse(GPSHandler.wasGantryPassed(gantryNonPassageList, mockGantryCoordinate))


    }

}
