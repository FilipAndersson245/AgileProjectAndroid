package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.LoginActivity
import se.ju.agileandroidproject.Models.User
import se.ju.agileandroidproject.R

class Register : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view!!.findViewById<Button>(R.id.button)

        button.setOnClickListener(View.OnClickListener {
            val username = view!!.findViewById<EditText>(R.id.Username).text.toString()
            val password = view!!.findViewById<EditText>(R.id.Password).text.toString()
            val password2 = view!!.findViewById<EditText>(R.id.PasswordConfirmation).text.toString()
            val emailAddress = view!!.findViewById<EditText>(R.id.EmailAdress).text.toString()
            val firstName = view!!.findViewById<EditText>(R.id.UserFirstName).text.toString()
            val lastName = view!!.findViewById<EditText>(R.id.UserLastName).text.toString()
            val billingAddress = view!!.findViewById<EditText>(R.id.UserBillingAddress).text.toString()
            val personalId = view!!.findViewById<EditText>(R.id.UserPersonalSocialNumber).text.toString()

            if (password != password2) {
                Toast.makeText(activity, "Passwords does not match.", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            val user = User(username, personalId, password, emailAddress, billingAddress, firstName, lastName)

            val validResponse = user.validate()

            if (validResponse.first) {
                Thread {
                    runBlocking {
                        APIHandler.register(user) {
                            val didRegister = it

                            activity?.runOnUiThread {
                                if (didRegister) {
                                    Toast.makeText(activity, "Registed successfully!", Toast.LENGTH_LONG).show()

                                    (activity as LoginActivity).switchFragment(Login.newInstance())
                                } else {
                                    Toast.makeText(activity, "Registration failed.", Toast.LENGTH_LONG).show()
                                }
                            }

                        }
                    }
                }.start()
            } else {
                Toast.makeText(activity, "Invalid ${validResponse.second}.", Toast.LENGTH_LONG).show()
            }
        })
    }


    companion object {
        fun newInstance(): Register {
            return Register()
        }
    }
}
