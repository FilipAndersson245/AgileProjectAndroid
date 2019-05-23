package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.delay
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.Main2Activity
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R

class DefaultTravelFragment : Fragment() {

    lateinit var textLoop: Thread

    fun updateTextLoop(view: View) {
        if(GPSHandler.locationExists) {
            view.findViewById<TextView>(R.id.coordinatesText).text = "(${GPSHandler.currentLocation.latitude}, ${GPSHandler.currentLocation.longitude})"
            view.findViewById<TextView>(R.id.distanceText).text = "${GPSHandler.distanceToClosestGantry} meters"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_default_travel, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stopTravelButton = view!!.findViewById<Button>(R.id.buttonStopTravel)

        stopTravelButton.setOnClickListener {
            APIHandler.isTraveling = false
            (activity as Main2Activity).stopBackgroundService()
            (parentFragment as MasterTravelFragment).switchFragment(StartTravelFragment.newInstance())
        }

        textLoop = Thread {
            while (true)
            {
                Log.d("EH", "Updating UI")
                updateTextLoop(view)
                Thread.sleep(GPSHandler.updateTime.toLong())
            }
        }
        textLoop.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textLoop.stop()
    }

    companion object {
        fun newInstance(): DefaultTravelFragment {
            return DefaultTravelFragment()
        }
    }
}
