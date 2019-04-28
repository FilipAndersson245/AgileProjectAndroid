package se.ju.agileandroidproject

import kotlinx.coroutines.*
import java.net.URL
import kotlin.system.*
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.coroutines.awaitObjectResponse
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.result.Result


object APIHandler {

    suspend fun testAsync() {
        println("---------------------------> Start fetch")

        println("---------------------------> REQUEST")
        val (request, response, result) = Fuel.get("https://www.google.se/").awaitStringResponseResult()

        println("---------------------------> RESPONSE")
        println(response.responseMessage)

//        println("---------------------------> RESULT")
//        result.fold(
//            { data -> println(data) /* "{"origin":"127.0.0.1"}" */ },
//            { error -> println("An error of type ${error.exception} happened: ${error.message}") }
//        )
        println("---------------------------> END")


//        Fuel.get("https://www.google.se/")
//            .response() { request, response, result ->
//                println("---------------------------> REQUEST")
//                println(request)
//                println("---------------------------> RESPONSE")
//                println(response.responseMessage)
//                println("---------------------------> END")
//            }

//        Fuel.get("https://www.google.se/")
//            .response() { request, response, result ->
//                println("---------------------------> REQUEST")
//                println(request)
//                println("---------------------------> RESPONSE")
//                println(response.responseMessage)
////                println("---------------------------> RESULT")
////                val (bytes, error) = result
////                if (bytes != null) {
////                    println("[response bytes] ${String(bytes)})
////                }
//                println("---------------------------> END")
//            }


        println("------------------------------> Done test")
    }
}