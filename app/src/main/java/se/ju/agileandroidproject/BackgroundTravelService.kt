package se.ju.agileandroidproject

import android.app.IntentService
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BackgroundTravelService: IntentService(BackgroundTravelService::class.simpleName) {


    override fun onHandleIntent(intent: Intent?) {
        if (intent != null){
            val dataString = intent!!.dataString
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = runBlocking {

        while (true){
            Log.d("EH", "Hej hej service")
            delay(1000)

        }

        return@runBlocking super.onStartCommand(intent, flags, startId)
    }

}