package se.ju.agileandroidproject.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.BackgroundTravelService
import se.ju.agileandroidproject.Fragments.*
import se.ju.agileandroidproject.R

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


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

        navView.setNavigationItemSelectedListener(this)
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
        } else {
            super.onBackPressed()
        }
    }

    fun switchFragment(fragment: android.support.v4.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack("")
//        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        fragmentTransaction.replace(R.id.fragment_holder, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
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
}