package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.Main2Activity
import se.ju.agileandroidproject.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Login.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Login : Fragment() {

    @ImplicitReflectionSerializer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view!!.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val username = view!!.findViewById<EditText>(R.id.Username).text.toString()
            val password = view!!.findViewById<EditText>(R.id.Password).text.toString()
    //
    //            Toast.makeText(activity, "$username $password", Toast.LENGTH_SHORT).show()

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

                                startActivity(Intent(activity, Main2Activity::class.java))
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
