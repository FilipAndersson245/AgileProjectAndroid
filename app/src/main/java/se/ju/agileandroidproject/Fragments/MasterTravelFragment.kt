package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.R

class MasterTravelFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_master_travel, container, false)
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCorrectFragment()
    }

    @UnstableDefault
    @ImplicitReflectionSerializer
    fun loadCorrectFragment() {
        if (APIHandler.isTraveling) {
            switchFragment(DefaultTravelFragment.newInstance())
        } else {
            switchFragment(StartTravelFragment.newInstance())
        }
    }

    fun switchFragment(fragment: androidx.fragment.app.Fragment) {
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
