package se.ju.agileandroidproject.Fragments.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import se.ju.agileandroidproject.Fragments.InvoiceFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_invoice.view.*
import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R

class MyInvoiceRecyclerViewAdapter(
    private val mValues: List<Invoice>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    // DENNA ÄR FEL, SKA INTE VA GANTRY HÄR, EFFEKT AV SCAFFOLDING TROR JAG /Marcus
    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Gantry
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_invoice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
//        holder.mIdView.text = item.id
//        holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
