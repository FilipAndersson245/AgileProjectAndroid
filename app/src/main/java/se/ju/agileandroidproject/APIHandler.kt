package se.ju.agileandroidproject

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Session
import se.ju.agileandroidproject.Models.User

@UnstableDefault
@ImplicitReflectionSerializer
object APIHandler {

    private const val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    var token = ""

    suspend fun returnGantry(lon: Float, lat: Float): List<Gantry> {

        val (_, _, result) =
            Fuel.post("$url/gantries/abc123")
                .authentication()
                .bearer(token)
                .jsonBody("{ \"position\": [3.213134, 12.438324] }")
                .awaitStringResponseResult()


        val responseData = mutableListOf<Gantry>()

        result.fold(
            { data ->
                responseData.add(Json.parse(Gantry.serializer(), data))
            },
            { error ->
                println("An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }

    fun loginRequest(username: String, password: String): Session {
        val (_, _, result) = runBlocking {
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

    fun login(username: String, password: String): Boolean {
        val session = loginRequest(username, password)
        return when (session.auth) {
            true -> {
                this.token = session.token
                true
            }
            else -> {
                token = ""
                false
            }
        }
    }

    fun login(user: User): Boolean = login(user.personalIdNumber, user.password)

    fun registerRequest(user: User): Boolean {
        val (_, _, result) = runBlocking {
            Fuel.post("$url/user/")
                .jsonBody(Json.stringify(user))
                .awaitStringResponseResult()
        }
        return result.fold({ true }, { error -> print(error.message); false })
    }

    fun register(user: User): Boolean {
        return when (registerRequest(user)) {
            true -> {
                login(user)
            }
            else -> {
                print("Registration failed!"); false
            }
        }
    }
}
