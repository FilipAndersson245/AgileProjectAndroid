package se.ju.agileandroidproject.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.MainActivity
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R

class DefaultTravelFragment : androidx.fragment.app.Fragment() {

    lateinit var textLoop: Thread

    @SuppressLint("SetTextI18n")
    fun updateTextLoop(view: View) {
        if (GPSHandler.locationExists) {
            view.post {
                view.findViewById<TextView>(R.id.coordinatesText).text =
                    "(${(GPSHandler.currentLocation.latitude * 1000).toInt().toDouble() / 1000}, ${(GPSHandler.currentLocation.longitude * 1000).toInt().toDouble() / 1000})"
                if (GPSHandler.distanceToClosestGantry != null) {
                    view.findViewById<TextView>(R.id.distanceText).text =
                        "${(GPSHandler.distanceToClosestGantry!! / 100).toInt().toDouble() / 10} km"
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

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stopTravelButton = view.findViewById<Button>(R.id.buttonStopTravel)

        stopTravelButton.setOnClickListener {
            APIHandler.isTraveling = false
            (activity as MainActivity).stopBackgroundService()
            (parentFragment as MasterTravelFragment).switchFragment(StartTravelFragment.newInstance())
        }

        val showMapButton = view.findViewById<Button>(R.id.buttonShowMap)

        showMapButton.setOnClickListener {
            (parentFragment as MasterTravelFragment).switchFragment(MapTravelFragment.newInstance())
        }

        textLoop = Thread {
            while (APIHandler.isTraveling) {
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
