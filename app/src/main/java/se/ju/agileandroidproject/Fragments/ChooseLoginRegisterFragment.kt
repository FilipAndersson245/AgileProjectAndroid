package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.Activities.LoginActivity
import se.ju.agileandroidproject.R


class ChooseLoginRegister : androidx.fragment.app.Fragment() {

    @ImplicitReflectionSerializer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_login_register, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            (activity as LoginActivity).switchFragment(Login.newInstance())
        }

        registerButton.setOnClickListener {
            (activity as LoginActivity).switchFragment(Register.newInstance())
        }
    }

    companion object {
        fun newInstance(): ChooseLoginRegister {
            return ChooseLoginRegister()
        }
    }
}
