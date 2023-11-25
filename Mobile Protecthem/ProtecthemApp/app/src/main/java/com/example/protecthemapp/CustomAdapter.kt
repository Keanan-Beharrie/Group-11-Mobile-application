package com.example.protecthemapp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(
    private val context: Context,
    private val birdList: ArrayList<Bird>,
    private val rarityTypes: Map<Int, String> = mapOf(
        Pair(0, "Verbal abuse"),
        Pair(1, "Sexual abuse"),
        Pair(2, "Physical abuse")
    )

) : BaseAdapter() {
    private val inflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): Any {
        return birdList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return birdList.size
    }
//(Adapter (no date))
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        Log.i("VIEW", convertView.toString())
        val dataitem = birdList[position]

        val rowView = inflater.inflate(R.layout.list_row, parent, false)

        rowView.findViewById<TextView>(R.id.row_name).text = dataitem.name
        rowView.findViewById<TextView>(R.id.row_rarity).text = "(${rarityTypes[dataitem.rarity]})"
        rowView.findViewById<TextView>(R.id.row_notes).text = dataitem.notes
        rowView.findViewById<TextView>(R.id.row_date).text = dataitem.date
        rowView.findViewById<TextView>(R.id.row_address).text = dataitem.address



        rowView.tag = position
        return rowView
    }
}
//Adapter (no date) Android Developers.
// Available at: https://developer.android.com/reference/kotlin/android/widget/Adapter
// (Accessed: November 23, 2023).