package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.Adapters.MyPassageRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Passage
import se.ju.agileandroidproject.R


class PassageFragment : androidx.fragment.app.Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    private lateinit var passageData: List<Passage>

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (notEmpty, data) = APIHandler.passages(APIHandler.personalId)

        passageData = when (notEmpty) {
            true -> data
            else -> listOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gantry_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (passageData.isEmpty()) {
            view.findViewById<TextView>(R.id.no_invoices_message).visibility = View.VISIBLE
        }


        recyclerView = view.findViewById(R.id.gantry_list)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
        recyclerView.adapter = MyPassageRecyclerViewAdapter(passageData)
        recyclerView.setHasFixedSize(true)

    }

    companion object {
        fun newInstance(): PassageFragment {
            return PassageFragment()
        }
    }

}
