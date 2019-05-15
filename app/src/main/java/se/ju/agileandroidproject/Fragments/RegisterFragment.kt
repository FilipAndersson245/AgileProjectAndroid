package se.ju.agileandroidproject.Fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Models.User
import se.ju.agileandroidproject.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Register.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Register : Fragment() {

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

            if(password != password2) {
                Toast.makeText(activity, "Passwords does not match.", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            val user = User(username, personalId, password, emailAddress, billingAddress, firstName, lastName)

            val validResponse = user.validate()

            if(validResponse.first) {
                if(APIHandler.register(user)) {
                    Toast.makeText(activity, "Registed correctly!", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(activity, "Register failed.", Toast.LENGTH_LONG).show()
                }
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
