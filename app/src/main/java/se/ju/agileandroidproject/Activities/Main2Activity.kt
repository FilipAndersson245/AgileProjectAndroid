package se.ju.agileandroidproject.Activities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.BackgroundTravelService
import se.ju.agileandroidproject.Fragments.*
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.R
import java.nio.channels.GatheringByteChannel
import java.sql.Timestamp

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var notificationManager: NotificationManager? = null

    @ImplicitReflectionSerializer
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.getHeaderView(0).findViewById<TextView>(R.id.nav_username).text = "Signed in with personal id ${APIHandler.personalId}"

        navView.setNavigationItemSelectedListener(this)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            packageName,
            "Gantry passing",
            "Gantry passing information.")
    }

    override fun onStart() {
        super.onStart()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_holder, MasterTravelFragment.newInstance())
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if(supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
    }

    fun switchFragment(fragment: android.support.v4.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(fragment.javaClass.toString())
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        fragmentTransaction.replace(R.id.fragment_holder, fragment)
        fragmentTransaction.commit()
    }

    @ImplicitReflectionSerializer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                switchFragment(MasterTravelFragment.newInstance())
            }
            R.id.nav_invoices -> {
                switchFragment(InvoiceFragment.newInstance())
            }
            R.id.nav_passages -> {
                switchFragment(PassageFragment.newInstance())
            }
            R.id.nav_logout -> {
                stopBackgroundService()
                val sharedPref = this.getSharedPreferences("CRED", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.remove("TOKEN")
                editor.remove("ID")
                editor.apply()

                APIHandler.logout()

                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun startBackgroundService() {
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        startService(serviceIntent)
    }

    fun stopBackgroundService() {
        var serviceIntent = Intent(this, BackgroundTravelService::class.java)
        stopService(serviceIntent)
    }



    private fun createNotificationChannel(id: String, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun pushNotificaion(closestGantry: Gantry) {
        val notificationID = System.currentTimeMillis().toInt()

        val channelID = packageName

        val intent = Intent(this, Main2Activity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification =
            NotificationCompat.Builder(this,
                channelID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Gantry passed")
                .setContentText("${closestGantry!!.price} kr")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(channelID)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager!!.notify(notificationID, notification)
    }

}