package com.example.trafficregulations.db

import com.example.trafficregulations.models.User

interface DbHelper {

    fun insert(user: User)

    fun getAllUser(str:String):ArrayList<User>

    fun getAllLikeUser(isHave:Int):ArrayList<User>

    fun edit(user: User):Int

    fun deleteUser(user: User)
}