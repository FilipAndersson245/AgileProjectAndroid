package se.ju.agileandroidproject.Activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import android.widget.Button
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R
import kotlinx.coroutines.*
import se.ju.agileandroidproject.BackgroundTravelService


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_LOCATION = 10

    public val CHANNEL_ID = "backgroundServiceChannel"

    lateinit var gpsHandler: GPSHandler

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION)

            // TODO: Handle if the user denies access to GPS and then show a popup that explains why the app needs access to GPS.

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANNEL_ID,
                "backgroundService",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    // Remove "= runBlocking" when not using async here
    override fun onStart() = runBlocking<Unit> {
        super.onStart()
        //gpsHandler = GPSHandler(applicationContext)
        checkPermissions()

        startBackgroundService()

        val btnOne = findViewById(R.id.btn_one_sec) as Button

        btnOne.setOnClickListener {
            changeUpdateTime(1000)
        }

        val btnFive = findViewById(R.id.btn_five_sec) as Button

        btnFive.setOnClickListener {
            changeUpdateTime(5000)
        }

        val btnTen = findViewById(R.id.btn_ten_sec) as Button

        btnTen.setOnClickListener {
            changeUpdateTime(10000)
        }

        // Remove later, for remember purpose only
        /*async {
            val a = APIHandler.returnGantry(5f, 5f)
            Log.d("EH","---------------------------> RESULT GANTRY START")
            Log.d("EH", a[0].id)
            Log.d("EH", a[0].coordinates.toString())
            Log.d("EH", a[0].lastUpdated)
            Log.d("EH", a[0].price.toString())
            Log.d("EH", "---------------------------> RESULT GANTRY END")
        }*/
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
                //TODO: Gör saker för att börja läsa GPS-koordinater

                //gpsHandler.startListening(30000)


            } else {
                Toast.makeText( this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun changeUpdateTime(updateTime: Long){
        //gpsHandler.setUpdateTime(updateTime)
    }

    fun startBackgroundService(){
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        startService(serviceIntent)
    }

    fun stopBackgroundService(){
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        stopService(serviceIntent)
    }

}
