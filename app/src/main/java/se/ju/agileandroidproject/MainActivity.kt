package se.ju.agileandroidproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    object APIHandler

    private val gpsHandler = GPSHandler()

    private val gantry = Gantry()

    private val invoice = Invoice()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
