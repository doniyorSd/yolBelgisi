package com.example.trafficregulations.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trafficregulations.R
import com.example.trafficregulations.databinding.ItemTrafficBinding
import com.example.trafficregulations.models.User
import java.io.File

class RvAdapter(private val list:ArrayList<User>,var myInterface: MyInterface) :RecyclerView.Adapter<RvAdapter.Vh>(){

    inner class Vh(itemView:View):RecyclerView.ViewHolder(itemView){
        fun onBind(user: User,position: Int){
            val bind = ItemTrafficBinding.bind(itemView)
            bind.name.text = user.name
            println(user.love)
            if (user.love!! == 0){
                bind.love.setImageResource(R.drawable.ic_lovefalse)
            }else if (user.love == 1){
                bind.love.setImageResource(R.drawable.ic_heart_1)
            }
            bind.trafficImg.setImageURI(Uri.parse(user.path_img))
            bind.delete.setOnClickListener {
                myInterface.deleteClick(user,position)
            }
            bind.edit.setOnClickListener {
                myInterface.editClick(user,position)
            }
            bind.love.setOnClickListener {
                if (user.love == 0){
                    bind.love.setImageResource(R.drawable.ic_heart_1)
                    user.love = 1
                }else{
                    user.love = 0
                    bind.love.setImageResource(R.drawable.ic_lovefalse)
                }
                myInterface.loveCLick(user,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return  Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_traffic,parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int){
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int = list.size

    interface MyInterface{
        fun loveCLick(user: User,position: Int)

        fun deleteClick(user: User,position: Int)

        fun editClick(user: User,position: Int)
    }
}