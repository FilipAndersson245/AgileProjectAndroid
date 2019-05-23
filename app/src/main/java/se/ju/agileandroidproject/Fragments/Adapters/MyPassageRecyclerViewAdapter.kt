package se.ju.agileandroidproject.Fragments.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import se.ju.agileandroidproject.Models.Passage
import se.ju.agileandroidproject.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MyPassageRecyclerViewAdapter(
    private val gantryData: List<Passage>
//    val context: Context
) : RecyclerView.Adapter<MyPassageRecyclerViewAdapter.GantryViewHolder>() {

    // Viewholder class
    class GantryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coordinate : TextView = itemView.findViewById(R.id.gantry_coordinate)
        val id : TextView = itemView.findViewById(R.id.gantry_id)
        val time : TextView = itemView.findViewById(R.id.gantry_time)
        val fee : TextView = itemView.findViewById(R.id.gantry_fee)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GantryViewHolder {
        return GantryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gantry_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: GantryViewHolder, position: Int) {

        val coord = gantryData[position].longitude.toString() + "," + gantryData[position].latitude.toString()
        holder.coordinate.text = coord
        holder.time.text = toNiceDateString(gantryData[position].time)
        holder.fee.text = "${gantryData[position].price.toInt()} kr"
        holder.id.text = gantryData[position].gantry_id
    }

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

    override fun getItemCount() = gantryData.size
}
