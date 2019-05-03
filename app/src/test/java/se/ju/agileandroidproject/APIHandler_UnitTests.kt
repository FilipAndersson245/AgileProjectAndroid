package se.ju.agileandroidproject

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class APIHandler_UnitTests {

    @Test
    fun getGantry_notNull() = runBlocking<Unit> {
        // Assert
        assertNotNull(APIHandler.returnGantry(5f, 5f))
    }
}
