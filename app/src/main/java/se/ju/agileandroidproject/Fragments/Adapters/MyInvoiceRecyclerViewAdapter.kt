package se.ju.agileandroidproject.Fragments.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import se.ju.agileandroidproject.Fragments.InvoiceFragment.OnListFragmentInteractionListener

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R

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

class MyInvoiceRecyclerViewAdapter(
    private val invoiceData: List<Invoice>
) : RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.InvoiceViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class InvoiceViewHolder(val date: TextView, val dueDate: TextView, val cost: TextView, itemView: View?) : RecyclerView.ViewHolder(itemView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_text_view, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        ...
        return InvoiceViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
