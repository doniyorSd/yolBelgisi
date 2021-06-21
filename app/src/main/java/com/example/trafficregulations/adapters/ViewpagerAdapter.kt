package com.example.trafficregulations.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.trafficregulations.TrafficFragment
import com.example.trafficregulations.db.MyDbHelper

class ViewpagerAdapter(fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        return TrafficFragment.newInstance(position)
    }

}