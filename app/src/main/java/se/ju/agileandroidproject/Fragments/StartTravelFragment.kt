package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.Main2Activity
import se.ju.agileandroidproject.R

class StartTravelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start_travel, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startTravelButton = view!!.findViewById<Button>(R.id.button)

        startTravelButton.setOnClickListener {
            APIHandler.isTraveling = true
            (activity as Main2Activity).startBackgroundService()
            (parentFragment as MasterTravelFragment).switchFragment(DefaultTravelFragment.newInstance())
        }
    }

    companion object {
        fun newInstance(): StartTravelFragment {
            return StartTravelFragment()
        }
    }
}
