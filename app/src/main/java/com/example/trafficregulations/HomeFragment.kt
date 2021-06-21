package com.example.trafficregulations

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.trafficregulations.adapters.ViewpagerAdapter
import com.example.trafficregulations.databinding.FragmentHomeBinding
import com.example.trafficregulations.databinding.TabItemBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var isHave = false
    lateinit var bind: FragmentHomeBinding
    lateinit var viepageradapter: ViewpagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        bind = FragmentHomeBinding.bind(view)
        viepageradapter = ViewpagerAdapter(requireActivity())
        bind.viewPager.adapter = viepageradapter
        isHave = false
        val tabList = arrayListOf(
            "Ogohlantiruvchi belgilar",
            "Imtiyozli belgilar",
            "Taqiqlovchi belgilar",
            "Buyuruvchi belgilar",
            "Axborot-ishora belgilar",
            "Servis belgilar",
            "Qoshimcha axborot belgilar"
        )
        TabLayoutMediator(
            bind.tab, bind.viewPager
        ) { tab, position ->
            val inflate =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_item, null, false)
            tab.customView = inflate
            val bind1 = TabItemBinding.bind(inflate)
            if (position == 0) {
                bind1.tv.setTextColor(Color.parseColor("#005CA1"))
                bind1.line.setCardBackgroundColor(Color.parseColor("#ffffff"))
            } else {
                bind1.tv.setTextColor(Color.parseColor("#ffffff"))
                bind1.line.setCardBackgroundColor(Color.parseColor("#005CA1"))
            }
            bind1.tv.text = tabList[position]
        }.attach()
        Log.d("tag", "onCreateView: ")
        bind.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val bind1 = TabItemBinding.bind(customView!!)
                bind1.tv.setTextColor(Color.parseColor("#005CA1"))
                bind1.tv.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val bind1 = TabItemBinding.bind(customView!!)
                bind1.tv.setTextColor(Color.parseColor("#ffffff"))
                bind1.tv.setBackgroundColor(Color.parseColor("#005CA1"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        bind.add.setOnClickListener {
            val bundle = Bundle()
            val currentItem = bind.viewPager.currentItem
            bundle.putInt("post",currentItem)
            findNavController().navigate(R.id.addTrafficFragment,bundle)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        viepageradapter = ViewpagerAdapter(requireActivity())
        bind.viewPager.adapter = viepageradapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}