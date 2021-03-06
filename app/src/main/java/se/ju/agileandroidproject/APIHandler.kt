package se.ju.agileandroidproject

import android.util.Log
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import se.ju.agileandroidproject.Models.*

import java.util.logging.Handler
import java.util.*
import javax.security.auth.callback.Callback

@UnstableDefault
@ImplicitReflectionSerializer
object APIHandler {

    var isTraveling = false

    private const val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    var token = ""
    var personalId = ""

    suspend fun requestGantries(lon: Float, lat: Float): List<Gantry> {
        val (_, _, result) =
            Fuel.get(url + "/gantries", listOf("lon" to lon, "lat" to lat))
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()

        var responseData = listOf<Gantry>()

        result.fold(
            { data ->
                responseData = Json.parse(Gantry.serializer().list, data.toString())
                return responseData
            },
            { error ->
                Log.e("error", "An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }

    suspend fun gantries(lon: Float, lat: Float, callback: (Pair<Boolean, List<Gantry>>) -> Unit) {
        val gantries = when (token) {
            "" -> listOf()
            else -> requestGantries(lon, lat)
        }
        callback(gantries.isNotEmpty() to gantries)
    }

    suspend fun loginRequest(username: String, password: String): Session {
        val (_, _, result) = run {
            Fuel.post("$url/sessions/")
                .jsonBody("{ \"username\": \"$username\", \"password\": \"$password\", \"grant_type\": \"password\" }")
                .awaitStringResponseResult()
        }

        var token = Session(false, "")
        result.fold({ data ->
            token = Json.parse(Session.serializer(), data)
        }, {
            print("Failed with login attempt.")
        })

        return token
    }

    fun logout(): Boolean {
        isTraveling = false
        return when (token) {
            "" -> {
                false
            }
            else -> {
                token = ""
                personalId = ""
                true
            }
        }
    }

    suspend fun login(username: String, password: String, callback: (success: Boolean) -> Unit) {
        val session = loginRequest(username, password)
        callback(
            when (session.auth) {
                true -> {
                    this.token = session.token
                    this.personalId = username
                    true
                }
                else -> {
                    token = ""
                    false
                }
            }
        )
    }

    suspend fun login(user: User, callback: (successful: Boolean) -> Unit) =
        login(user.personalIdNumber, user.password, callback)

    suspend fun registerRequest(user: User): Boolean {
        val (_, _, result) = runBlocking {
            Fuel.post("$url/users/")
                .jsonBody(Json.stringify(user))
                .awaitStringResponseResult()
        }
        return result.fold({ true }, { error -> print(error.message); false })
    }

    suspend fun register(user: User, callback: (successful: Boolean) -> Unit) {
        callback(
            when (registerRequest(user)) {
                false -> {
                    print("Registration failed!"); false
                }
                else -> {
                    true
                }
            }
        )
    }

    fun registerPassageRequest(personalId: String, gantryId: String): Boolean {
        val (_, _, result) = runBlocking {
            Fuel.post("$url/passages/")
                .jsonBody("{ \"personalId\": \"$personalId\", \"gantryId\": \"$gantryId\"}")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }
        return result.fold({ true }, { error -> print(error.message); false })
    }

    fun registerPassage(personalId: String, gantryId: String): Boolean {
        return when {
            (personalId.length == 10 || personalId.length == 12) && gantryId.isNotEmpty() -> {
                registerPassageRequest(
                    personalId,
                    gantryId
                )
            }
            else -> false
        }
    }

    private fun passagesRequest(personalId: String): List<Passage> {
        val (_, _, result) = runBlocking {
            Fuel.get("$url/passages?personalId=$personalId")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }

        var responseData = listOf<Passage>()

        result.fold(
            { data ->
                responseData = Json.parse(Passage.serializer().list, data)
                return responseData
            },
            { error ->
                Log.e("error", "An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }

    fun passages(personalId: String): Pair<Boolean, List<Passage>> {
        val passages = when {
            (personalId.length == 10 || personalId.length == 12) -> {
                passagesRequest(personalId)
            }
            else -> listOf()
        }
        return passages.isNotEmpty() to passages
    }

    private fun invoiceRequest(personalId: String): List<Invoice> {
        val (_, _, result) = runBlocking {
            Fuel.get("$url/invoices?personalId=$personalId")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }

        var responseData = listOf<Invoice>()

        result.fold(
            { data ->
                responseData = Json.parse(Invoice.serializer().list, data)
            },
            { error ->
                print("An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }

    fun invoices(personalId: String): Pair<Boolean, List<Invoice>> {
        val gantries = when (token) {
            "" -> listOf()
            else -> invoiceRequest(personalId)
        }
        return gantries.isNotEmpty() to gantries
    }
}
