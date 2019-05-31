package se.ju.agileandroidproject.Fragments.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.ju.agileandroidproject.Models.Invoice
import se.ju.agileandroidproject.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MyInvoiceRecyclerViewAdapter(
    private val invoiceData: List<Invoice>
) : RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.InvoiceViewHolder>() {

    // Viewholder class
    class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.invoice_listitem_date)
        val dueDate: TextView = itemView.findViewById(R.id.invoice_listitem_due_date)
        val cost: TextView = itemView.findViewById(R.id.invoice_listitem_cost)
        val background: Drawable = itemView.findViewById<ConstraintLayout>(R.id.invoice_list_item_viewHolder).background
        val status: TextView = itemView.findViewById<TextView>(R.id.paid_status)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        return InvoiceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.invoice_list_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val dueDate = toDate(invoiceData[position].dueDate)!!

        holder.cost.text = "${invoiceData[position].amount} kr"
        holder.date.text = toNiceDateString(invoiceData[position].issueDate)
        holder.dueDate.text = toNiceDateString(invoiceData[position].dueDate)

        if (invoiceData[position].isPaid) {
            holder.status.text = "Paid"
            holder.status.setTextColor(Color.GREEN)
            holder.background.setTint(Color.parseColor("#a2f2b2"))
        } else if (dueDate.before(Date())) {
            holder.status.text = "Overdue"
            holder.status.setTextColor(Color.RED)
            holder.background.setTint(Color.parseColor("#ffaaaa"))
        } else {
            holder.status.text = "Not paid"
            holder.background.setTint(Color.parseColor("#ffffff"))
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun toDate(dateString: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val convertedDate: Date? = sdf.parse(dateString)

        return convertedDate
    }

    @SuppressLint("SimpleDateFormat")
    fun toNiceDateString(dateString: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(dateString)
            formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

    override fun getItemCount() = invoiceData.size
}

