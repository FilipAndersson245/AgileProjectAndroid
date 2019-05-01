package se.ju.agileandroidproject

import kotlinx.coroutines.*
import java.net.URL
import kotlin.system.*
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitObjectResponse
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result

import se.ju.agileandroidproject.Models.Coordinate
import se.ju.agileandroidproject.Models.Gantry

object APIHandler {

    val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    val token = "fhsakdjhjkfds"

    suspend fun testAsync() {
        println("---------------------------> Start fetch")

        val coords = Coordinate(123, 123)
        val gantry = Gantry("abc123", coords, "14/4/2019", 23.99F)

        println("---------------------------> REQUEST")
        val (request, response, result) =
            Fuel.post(url + "/gantries/" + gantry.id)
                .authentication()
                .bearer(token)
                .jsonBody("{ \"position\": [3.213134, 12.438324] }")
                .awaitStringResponseResult()

        println("---------------------------> RESPONSE")
        println("Message")
        println(response.responseMessage)
        println("Response")
        println(response)

        println("---------------------------> RESULT")
        result.fold(
            { data -> println(data) /* "{"origin":"127.0.0.1"}" */ },
            { error -> println("An error of type ${error.exception} happened: ${error.message}") }
        )
        println("---------------------------> END")

        println("------------------------------> Done test")
    }
}