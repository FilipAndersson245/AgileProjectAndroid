package se.ju.agileandroidproject

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.Models.Session
import se.ju.agileandroidproject.Models.User

@UnstableDefault
@ImplicitReflectionSerializer
object APIHandler {

    private const val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    var token = ""

    suspend fun requestGantries(lon: Float, lat: Float): List<Gantry> {
        val (_, _, result) = run {
            Fuel.get("$url/gantrie?lat=$lat&lon=$lon")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }


        val responseData = mutableListOf<Gantry>()

        result.fold(
            { data ->
                responseData.add(Json.parse(Gantry.serializer(), data))
            },
            { error ->
                print("An error of type ${error.exception} happened: ${error.message}")
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
        return when(token) {
            "" -> {
                false
            }
            else -> {
                token = ""
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
            Fuel.post("$url/user/")
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

    fun invoiceRequest(personalId: String): List<Invoice> {
        val (_, _, result) = runBlocking {
            Fuel.get("$url/invoices?personalId=$personalId")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }

        val responseData = mutableListOf<Invoice>()

        result.fold(
            { data ->
                responseData.add(Json.parse(Invoice.serializer(), data))
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
