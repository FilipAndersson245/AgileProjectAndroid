package se.ju.agileandroidproject

import android.location.Location
import android.location.LocationManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

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

        val gpsHandler = GPSHandler(appContext)

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

        assertFalse(gpsHandler.isBetterLocation(badAccuracyLocation, originalLocation))

        assertTrue(gpsHandler.isBetterLocation(longTimeLocation, originalLocation))


    }
}
