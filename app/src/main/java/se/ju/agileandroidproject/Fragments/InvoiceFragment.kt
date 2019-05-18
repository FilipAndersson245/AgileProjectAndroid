package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.agileandroidproject.Fragments.Adapters.MyInvoiceRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [InvoiceFragment.OnListFragmentInteractionListener] interface.
 */
class InvoiceFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invoice_list, container, false)

        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = MyInvoiceRecyclerViewAdapter(
//                    DummyContent.ITEMS,
//                    listener
//                )
//            }
//        }
        return view
    }


    companion object {
        fun newInstance(): InvoiceFragment {
            return InvoiceFragment()
        }
    }
}
