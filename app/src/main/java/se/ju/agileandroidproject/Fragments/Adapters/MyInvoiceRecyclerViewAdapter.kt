package se.ju.agileandroidproject.Fragments.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R



class MyInvoiceRecyclerViewAdapter(
    private val invoiceData: List<Invoice>
//    val context: Context
) : RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.InvoiceViewHolder>() {

    class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date : TextView = itemView.findViewById(R.id.invoice_listitem_date)
        val dueDate : TextView = itemView.findViewById(R.id.invoice_listitem_due_date)
        val cost : TextView = itemView.findViewById(R.id.invoice_listitem_cost)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        return InvoiceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.invoice_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {

        holder.cost.text = invoiceData[position].amount.toString()
        holder.date.text = invoiceData[position].issuedAt
        holder.dueDate.text = invoiceData[position].dueDate
    }

    override fun getItemCount() = invoiceData.size
}

//class MyInvoiceRecyclerViewAdapter(
//    private val invoiceData: List<Invoice>
//) : RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.ViewHolder>() {
//
//    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.invoice_list_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = invoiceData[position]
//
////        holder.mIdView.text = item.id
////        holder.mContentView.text = item.content
//
//    }
//
//    override fun getItemCount(): Int = invoiceData.size
//
//    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        val mIdView: TextView = mView.item_number
//        val mContentView: TextView = mView.content
//
//        override fun toString(): String {
//            return super.toString() + " '" + mContentView.text + "'"
//        }
//    }
//}
