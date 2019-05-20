package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.R

class DefaultTravelFragment : Fragment() {

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
            (parentFragment as MasterTravelFragment).switchFragment(StartTravelFragment.newInstance())
        }
    }

    companion object {
        fun newInstance(): DefaultTravelFragment {
            return DefaultTravelFragment()
        }
    }
}
