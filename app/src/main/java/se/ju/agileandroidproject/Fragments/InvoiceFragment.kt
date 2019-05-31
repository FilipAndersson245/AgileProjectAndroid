package se.ju.agileandroidproject.Fragments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Fragments.Adapters.MyInvoiceRecyclerViewAdapter
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R


class InvoiceFragment : androidx.fragment.app.Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    private lateinit var invoiceData: List<Invoice>

    @UnstableDefault
    @ImplicitReflectionSerializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val (notEmpty, data) = APIHandler.invoices(APIHandler.personalId)

        invoiceData = when (notEmpty) {
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

        if (invoiceData.isEmpty()) {
            view.findViewById<TextView>(R.id.no_invoices_message).visibility = View.VISIBLE
        }

        recyclerView = view.findViewById(R.id.gantry_list)
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
