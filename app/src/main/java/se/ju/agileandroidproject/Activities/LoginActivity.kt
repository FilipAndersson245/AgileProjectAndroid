package se.ju.agileandroidproject.Activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.ChooseLoginRegister
import se.ju.agileandroidproject.Fragments.Register
import se.ju.agileandroidproject.R

class LoginActivity: AppCompatActivity() {

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = this.getSharedPreferences("CRED", Context.MODE_PRIVATE)
        APIHandler.token = sharedPref.getString("TOKEN", "")
        APIHandler.personalId = sharedPref.getString("ID", "")

        if(!APIHandler.token.equals("")) {
            startActivity(Intent(this, Main2Activity::class.java))
        }

        setContentView(R.layout.activity_main2)
    }

    fun switchFragment(fragment: Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        fragmentTransaction.replace(R.id.fragment_holder, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
    }

    override fun onStart() {
        super.onStart()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_holder, ChooseLoginRegister.newInstance())
        fragmentTransaction.commit()
    }
}