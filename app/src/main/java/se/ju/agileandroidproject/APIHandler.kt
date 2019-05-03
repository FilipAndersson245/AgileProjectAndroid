package se.ju.agileandroidproject

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

import se.ju.agileandroidproject.Models.Gantry

object APIHandler {

    val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    val token = "fhsakdjhjkfds"

    @UnstableDefault
    suspend fun returnGantry() : Gantry {
        val (request, response, result) =
            Fuel.post(url + "/gantries/" + "abc123")
                .authentication()
                .bearer(token)
                .jsonBody("{ \"position\": [3.213134, 12.438324] }")
                .awaitStringResponseResult()

        lateinit var responseData : Gantry
        result.fold(
            { data ->
                responseData = Json.parse(Gantry.serializer(), data)
            },
            { error ->
                println("An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }
}