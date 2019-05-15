package se.ju.agileandroidproject

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Session
import se.ju.agileandroidproject.Models.User
import java.util.logging.Handler
import javax.security.auth.callback.Callback

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
            "" -> listOf<Gantry>()
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

    fun registerGantryRequest(personalId: String, gantryId: String): Boolean {
        val (_, _, result) = runBlocking {
            Fuel.post("$url/passages/")
                .jsonBody("{ \"personalId\": \"$personalId\", \"gantryId\": \"$gantryId\"}")
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()
        }
        return result.fold({ true }, { error -> print(error.message); false })
    }

    fun registerGantry(personalId: String, gantryId: String): Boolean {
        return when {
            (personalId.length == 10 || personalId.length == 12) && gantryId.isNotEmpty() -> {
                registerGantryRequest(
                    personalId,
                    gantryId
                )
            }
            else -> false
        }
    }
}
