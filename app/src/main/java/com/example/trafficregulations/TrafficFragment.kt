    package com.example.trafficregulations

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trafficregulations.adapters.RvAdapter
import com.example.trafficregulations.adapters.SpinnerAdapter
import com.example.trafficregulations.adapters.ViewpagerAdapter
import com.example.trafficregulations.databinding.EditDialogBinding
import com.example.trafficregulations.databinding.FragmentTrafficBinding
import com.example.trafficregulations.db.MyDbHelper
import com.example.trafficregulations.models.User

    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TrafficFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrafficFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)

        }
    }
    lateinit var myDbHelper:MyDbHelper
    lateinit var allUser:ArrayList<User>
    lateinit var viewpagerAdapter: ViewpagerAdapter
    lateinit var rvAdapter: RvAdapter
    lateinit var listSpinner : ArrayList<String>
    lateinit var bind :FragmentTrafficBinding
    lateinit var onPassData: OnPassData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_traffic, container, false)
        bind = FragmentTrafficBinding.bind(view)
         myDbHelper = MyDbHelper(requireContext())
        listSpinner = arrayListOf(
            "Ogohlantiruvchi belgilar",
            "Imtiyozli belgilar",
            "Taqiqlovchi belgilar",
            "Buyuruvchi belgilar",
            "Axborot-ishora belgilar",
            "Servis belgilar",
            "Qoshimcha axborot belgilar"
        )

        allUser = myDbHelper.getAllUser(listSpinner[param1!!])
        println(allUser)
        rvAdapter = RvAdapter(allUser, object : RvAdapter.MyInterface {
            override fun loveCLick(user: User, position: Int) {
                myDbHelper.edit(user)
            }

            override fun deleteClick(user: User, position: Int) {
                val filesDir = requireActivity().filesDir
                if (filesDir.isDirectory) {
                    myDbHelper.deleteUser(user)
                    allUser.remove(user)
                    rvAdapter.notifyItemRemoved(position)
                    rvAdapter.notifyItemRangeChanged(position, allUser.size)
                }
            }

            override fun editClick(user: User, position: Int) {
                val dialog = AlertDialog.Builder(requireContext()).create()

                val myView = layoutInflater.inflate(R.layout.edit_dialog, null, false)
                val binding = EditDialogBinding.bind(myView)
                dialog.setView(myView)
                dialog.show()
                val listSpinner = arrayListOf(
                    "Ogohlantiruvchi belgilar",
                    "Imtiyozli belgilar",
                    "Taqiqlovchi belgilar",
                    "Buyuruvchi belgilar",
                    "Axborot-ishora belgilar",
                    "Servis belgilar",
                    "Qoshimcha axborot belgilar"
                )
                binding.spinner.adapter = SpinnerAdapter(listSpinner)
                val indexOf = listSpinner.indexOf(user.mark)
                binding.spinner.setSelection(indexOf)
                binding.about.setText(user.about)
                binding.name.setText(user.name)

                binding.back.setOnClickListener {
                    dialog.dismiss()
                }
                binding.save.setOnClickListener {
                    val name = binding.name.text.toString()
                    val about = binding.about.text.toString()
                    val mark = binding.spinner.selectedItem.toString()

                    if (name.trim().isNotEmpty() && about.trim().isNotEmpty() && mark.trim()
                            .isNotEmpty()
                    ) {
                        user.name = name
                        user.about = about
                        user.mark = mark
                        val selectedItemPosition = binding.spinner.selectedItemPosition

                        myDbHelper.edit(user)
                        if (selectedItemPosition == indexOf) {
                            rvAdapter.notifyItemChanged(position)
                        } else {
                            allUser.remove(user)
                            rvAdapter.notifyItemRemoved(position)
                            rvAdapter.notifyItemRangeChanged(position, allUser.size)
                        }
                        dialog.dismiss()
                    }
                }
            }
        })
        bind.rv.adapter = rvAdapter
        return view
    }

    override fun onResume() {
        super.onResume()
        bind.rv.adapter = rvAdapter
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            TrafficFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

}