package com.example.trafficregulations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.trafficregulations.R
import com.example.trafficregulations.databinding.ItemSpinerBinding

class SpinnerAdapter(var list: ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewItem: View = convertView ?: LayoutInflater.from(parent!!.context)
            .inflate(R.layout.item_spiner, parent, false)
        val bind = ItemSpinerBinding.bind(viewItem)
        bind.tvSpiner.text = list[position]
        return viewItem
    }
}