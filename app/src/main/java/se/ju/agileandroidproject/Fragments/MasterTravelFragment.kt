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
import se.ju.agileandroidproject.R

class MasterTravelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_master_travel, container, false)
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCorrectFragment()
    }

    @ImplicitReflectionSerializer
    fun loadCorrectFragment() {
        if(APIHandler.isTraveling) {
            Log.d("EH", "Started default")
            switchFragment(DefaultTravelFragment.newInstance())
        }
        else {
            Log.d("EH", "Started start")
            switchFragment(StartTravelFragment.newInstance())
        }
    }

    fun switchFragment(fragment: android.support.v4.app.Fragment)
    {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_fragment_holder, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        fun newInstance(): MasterTravelFragment {
            return MasterTravelFragment()
        }
    }
}
