package se.ju.agileandroidproject

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import se.ju.agileandroidproject.Models.Coordinate

import se.ju.agileandroidproject.Models.Gantry

object APIHandler {

    val url = "http://agileserver-env.yttgtpappn.eu-central-1.elasticbeanstalk.com"
    val token = "fhsakdjhjkfds"

    @UnstableDefault
    suspend fun returnGantry(coordinate : Coordinate) : List<Gantry> {

        val (request, response, result) =
            Fuel.post(url + "/gantries/" + "abc123")
                .authentication()
                .bearer(token)
                .jsonBody("{ \"position\": [3.213134, 12.438324] }")
                .awaitStringResponseResult()


        var responseData = mutableListOf<Gantry>()

        result.fold(
            { data ->
                responseData.add(Json.parse(Gantry.serializer(), data))
            },
            { error ->
                println("An error of type ${error.exception} happened: ${error.message}")
            })

        return responseData
    }

    @UnstableDefault
    suspend fun getClosestGantries(coordinate : Coordinate) : List<Coordinate> {

        val (request, response, result) =
            Fuel.get(url + "/gantries", listOf("lon" to coordinate.lon, "lat" to coordinate.lat))
                .authentication()
                .bearer(token)
                .awaitStringResponseResult()


        var responseData = mutableListOf<Gantry>()

        result.fold(
            { data ->
                responseData.add(Json.parse(Gantry.serializer(), data))
            },
            { error ->
                println("An error of type ${error.exception} happened: ${error.message}")
            })

        var postitionData = mutableListOf<Coordinate>()

        for (gantry in responseData)
        {
            postitionData.add(gantry.coordinates)
        }


        return postitionData
    }
}