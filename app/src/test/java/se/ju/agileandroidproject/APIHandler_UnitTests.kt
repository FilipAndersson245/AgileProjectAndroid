package se.ju.agileandroidproject

import io.mockk.coEvery
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import org.junit.Test

import org.junit.Assert.*
import se.ju.agileandroidproject.Models.Gantry
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
    fun gantry_should_be_good() {
        val spy = spyk(APIHandler)
        val mockGantries = listOf(Gantry("foo", listOf<Float>(1.7f, 14.2f), "123", 10f))

        coEvery { spy.requestGantries(any(), any()) } answers { mockGantries }
        coEvery { spy.token } answers { "token" }

        runBlocking {
            spy.gantries(10.3f, 2.3f) {
                run {
                    assertTrue(it.first)
                    assertEquals(it.second, mockGantries)
                    assertTrue(spy.token == "token")
                }
            }
        }

    }

    @Test
    fun gantry_should_fail_with_bad_request() = runBlocking {
        val spy = spyk(APIHandler)
        val mockGantries = listOf<Gantry>()

        coEvery { spy.requestGantries(any(), any()) } answers { mockGantries }
        every { spy.token } answers { "token" }

        runBlocking {
            spy.gantries(10.3f, 2.3f) {
                run {
                    assertFalse(it.first)
                    assertEquals(it.second, mockGantries)
                    assertTrue(spy.token == "token")
                }
            }
        }

    }

    @Test
    fun gantry_should_be_fail_with_no_token() = runBlocking {
        val spy = spyk(APIHandler)
        val mockGantries = listOf(Gantry("foo", listOf<Float>(1.7f, 14.2f), "123", 10f))

        coEvery { spy.requestGantries(any(), any()) } answers { mockGantries }
        every { spy.token } answers { "" }

        runBlocking {
            spy.gantries(10.3f, 2.3f) {
                run {
                    assertFalse(it.first)
                    assertTrue(it.second.isEmpty())
                    assertTrue(spy.token == "")
                }
            }
        }
    }


    @Test
    fun login_should_succeed() {
        val spy = spyk(APIHandler)

        coEvery { spy.loginRequest(any(), any()) } answers { Session(true, "token") }

        val result = runBlocking {
            spy.login("foo", "bar") {
                run {
                    assertTrue(it)
                    assertTrue(spy.token == "token")
                }
            }
        }
    }

    @Test
    fun login_should_fail() {
        val spy = spyk(APIHandler)

        coEvery { spy.loginRequest(any(), any()) } answers { Session(false, "") }

        runBlocking {
            spy.login("foo", "bar") {
                run {
                    assertFalse(it)
                    assertTrue(spy.token == "")
                }
            }
        }
    }

    @Test
    fun login_should_work_with_user() {
        val spy = spyk(APIHandler)

        coEvery { spy.loginRequest(any(), any()) } answers { Session(true, "token") }

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
            ) {
                run {
                    assertTrue(it)
                    assertTrue(spy.token == "token")
                }
            }
        }
    }

    @Test
    fun login_should_fail_with_user() {
        val spy = spyk(APIHandler)

        coEvery { spy.loginRequest(any(), any()) } answers { Session(false, "") }

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
                ) {
                    run {
                        assertFalse(it)
                        assertTrue(spy.token == "")
                    }
                }
            }

    }

    @Test
    fun register_should_work() {
        val spy = spyk(APIHandler)

        coEvery { spy.loginRequest(any(), any()) } answers { Session(true, "token") }
        coEvery { spy.registerRequest(any()) } answers { true }

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
            ) {
                run {
                    assertTrue(it)
                    assertTrue(spy.token == "token")
                }
            }
        }

    }
}
