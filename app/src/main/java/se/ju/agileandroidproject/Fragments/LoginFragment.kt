package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.MainActivity
import se.ju.agileandroidproject.R

class Login : androidx.fragment.app.Fragment() {

    @ImplicitReflectionSerializer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.Username).text.toString()
            val password = view.findViewById<EditText>(R.id.Password).text.toString()

            Thread {
                runBlocking {
                    APIHandler.login(username, password) {
                        val didLogin = it

                        activity?.runOnUiThread {
                            if (didLogin) {
                                Toast.makeText(activity, "Signed in successfully!", Toast.LENGTH_SHORT).show()

                                val sharedPref = context!!.getSharedPreferences("CRED", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("TOKEN", APIHandler.token)
                                editor.putString("ID", APIHandler.personalId)
                                editor.apply()

                                startActivity(Intent(activity, MainActivity::class.java))
                                activity!!.finish()
                            } else {
                                Toast.makeText(activity, "Failed signing in.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }.start()
        }
    }

    companion object {
        fun newInstance(): Login {
            return Login()
        }
    }
}
