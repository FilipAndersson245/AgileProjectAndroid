package se.ju.agileandroidproject

import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import org.junit.Test

import org.junit.Assert.*
import se.ju.agileandroidproject.Models.Session
import se.ju.agileandroidproject.Models.User

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ImplicitReflectionSerializer
@UnstableDefault
class APIHandler_tests {

    @Test
    fun getGantry_notNull() = runBlocking {
        // Assert
        assertNotNull(APIHandler.returnGantry(5f, 5f))
    }


    @Test
    fun login_should_succeed() {
        val spy = spyk(APIHandler)

        every { spy.loginRequest(any(), any()) } answers { Session(true, "token") }

        val result = runBlocking { spy.login("foo", "bar") }
        assertTrue(result)
        assertTrue(spy.token == "token")
    }

    @Test
    fun login_should_fail() {
        val spy = spyk(APIHandler)

        every { spy.loginRequest(any(), any()) } answers { Session(false, "") }

        val result = runBlocking { spy.login("foo", "bar") }
        assertFalse(result)
        assertTrue(spy.token == "")
    }

    @Test
    fun login_should_work_with_user() {
        val spy = spyk(APIHandler)

        every { spy.loginRequest(any(), any()) } answers { Session(true, "token") }

        val result =
            runBlocking {
                spy.login(
                    User(
                        "bob",
                        "123456789",
                        "foo",
                        "abc@gmail.com",
                        "london",
                        "bob",
                        "kent"
                    )
                )
            }
        assertTrue(result)
        assertTrue(spy.token == "token")
    }

    @Test
    fun login_should_fail_with_user() {
        val spy = spyk(APIHandler)

        every { spy.loginRequest(any(), any()) } answers { Session(false, "") }

        val result =
            runBlocking {
                spy.login(
                    User(
                        "bob",
                        "123456789",
                        "foo",
                        "abc@gmail.com",
                        "london",
                        "bob",
                        "kent"
                    )
                )
            }
        assertFalse(result)
        assertTrue(spy.token == "")
    }

    @Test
    fun register_should_work() {
        val spy = spyk(APIHandler)

        every { spy.loginRequest(any(), any()) } answers { Session(true, "token") }
        every { spy.registerRequest(any()) } answers { true }

        val result =
            runBlocking {
                spy.register(
                    User(
                        "bob",
                        "123456789",
                        "foo",
                        "abc@gmail.com",
                        "london",
                        "bob",
                        "kent"
                    )
                )
            }
        assertTrue(result)
        assertTrue(spy.token == "token")
    }
}
