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
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.Adapters.MyInvoiceRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R


class InvoiceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.InvoiceViewHolder>
//    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var invoiceData : List<Invoice>

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (notEmpty, data) = APIHandler.invoices(APIHandler.personalId)

        if (notEmpty) {
            invoiceData = data
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invoice_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.invoice_list)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = MyInvoiceRecyclerViewAdapter(invoiceData)
        recyclerView.setHasFixedSize(true)

//        viewManager = LinearLayoutManager(this.context)
//        viewAdapter = MyInvoiceRecyclerViewAdapter(invoiceData)
//
//        recyclerView = view.findViewById<RecyclerView>(R.id.invoice_list).apply {
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//        }
    }


}
