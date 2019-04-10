package se.ju.agileandroidproject.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R

class MainActivity : AppCompatActivity() {

    private val gpsHandler = GPSHandler()

    private val gantry = Gantry()

    private val invoice = Invoice()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
