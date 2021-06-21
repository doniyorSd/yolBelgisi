package com.example.trafficregulations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trafficregulations.adapters.RvAdapter
import com.example.trafficregulations.databinding.FragmentLikeBinding
import com.example.trafficregulations.db.MyDbHelper
import com.example.trafficregulations.models.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LikeFragment : Fragment() {
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
    lateinit var rvAdapter: RvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_like, container, false)
        val bind = FragmentLikeBinding.bind(view)
        val myDbHelper = MyDbHelper(requireContext())
        val allLikeUser = myDbHelper.getAllLikeUser(1)
        rvAdapter = RvAdapter(allLikeUser,object :RvAdapter.MyInterface{
            override fun loveCLick(user: User, position: Int) {
                myDbHelper.edit(user)
                allLikeUser.remove(user)
                rvAdapter.notifyItemRemoved(position)
                rvAdapter.notifyItemRangeChanged(position,allLikeUser.size)
            }

            override fun deleteClick(user: User, position: Int) {

            }

            override fun editClick(user: User, position: Int) {

            }

        })
        bind.rv.adapter = rvAdapter
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LikeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LikeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}