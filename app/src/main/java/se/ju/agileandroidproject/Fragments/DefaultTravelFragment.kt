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

    var inTravel = false

    fun updateTextLoop(view: View) {
        if(GPSHandler.locationExists) {
            view.post {
                view.findViewById<TextView>(R.id.coordinatesText).text = "(${(GPSHandler.currentLocation.latitude * 1000).toInt().toDouble() / 1000}, ${(GPSHandler.currentLocation.longitude * 1000).toInt().toDouble() / 1000})"
                if (GPSHandler.distanceToClosestGantry != null) {
                    view.findViewById<TextView>(R.id.distanceText).text = "${(GPSHandler.distanceToClosestGantry!!/100).toInt().toDouble()/10} km"
                }
            }
        }
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

        val showMapButton = view!!.findViewById<Button>(R.id.buttonShowMap)

        showMapButton.setOnClickListener {
            (parentFragment as MasterTravelFragment).switchFragment(MapTravelFragment.newInstance())
        }
        
        textLoop = Thread {
            while (inTravel)
            {
                Log.d("EH", "Updating UI")
                updateTextLoop(view)
                Thread.sleep(GPSHandler.updateTime.toLong())
            }
        }
        textLoop.start()
    }

    companion object {
        fun newInstance(): DefaultTravelFragment {
            return DefaultTravelFragment()
        }
    }
}
