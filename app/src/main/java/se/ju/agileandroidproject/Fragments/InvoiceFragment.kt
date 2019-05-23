package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.Adapters.MyInvoiceRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R
import kotlin.math.log


class InvoiceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var invoiceData : List<Invoice>

    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (notEmpty, data) = APIHandler.invoices(APIHandler.personalId)

        invoiceData = when(notEmpty) {
            true -> data
            else -> listOf()
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

        recyclerView = view.findViewById<RecyclerView>(R.id.gantry_list)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = MyInvoiceRecyclerViewAdapter(invoiceData)
        recyclerView.setHasFixedSize(true)

    }

    companion object {
        fun newInstance(): InvoiceFragment {
            return InvoiceFragment()
        }
    }

}
