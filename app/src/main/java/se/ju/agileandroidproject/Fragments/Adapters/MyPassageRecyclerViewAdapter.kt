package se.ju.agileandroidproject.Fragments.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import se.ju.agileandroidproject.Models.Gantry
import se.ju.agileandroidproject.Models.Passage
import se.ju.agileandroidproject.R

class MyPassageRecyclerViewAdapter(
    private val gantryData: List<Passage>
//    val context: Context
) : RecyclerView.Adapter<MyPassageRecyclerViewAdapter.GantryViewHolder>() {

    // Viewholder class
    class GantryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coordinate : TextView = itemView.findViewById(R.id.gantry_coordinate)
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
        holder.time.text = gantryData[position].time
        holder.fee.text = gantryData[position].price.toString()
    }

    override fun getItemCount() = gantryData.size
}
