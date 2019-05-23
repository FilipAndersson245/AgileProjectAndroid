package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.Adapters.MyPassageRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Passage
import se.ju.agileandroidproject.R


class PassageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var passageData : List<Passage>

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (notEmpty, data) = APIHandler.passages(APIHandler.personalId)

        passageData = when(notEmpty) {
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

        recyclerView = view.findViewById(R.id.gantry_list)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = MyPassageRecyclerViewAdapter(passageData)
        recyclerView.setHasFixedSize(true)

    }

    companion object {
        fun newInstance(): PassageFragment {
            return PassageFragment()
        }
    }

}
