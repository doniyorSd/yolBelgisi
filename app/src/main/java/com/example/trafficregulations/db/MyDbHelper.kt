package com.example.trafficregulations.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.trafficregulations.models.User

class MyDbHelper(context:Context) :SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION),DbHelper {

    companion object{
        val DB_VERSION = 1
        val DB_NAME = "sc_traffic_db"
        val TABLE_NAME = "traffic_table"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table traffic_table(id integer not null primary key autoincrement unique,image_path text not null,traffic_name text not null,traffic_about text not null,mark text not null,love int not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun insert(user: User) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("traffic_name",user.name)
        contentValues.put("traffic_about",user.about)
        contentValues.put("mark",user.mark)
        contentValues.put("image_path",user.path_img)
        contentValues.put("love",user.love)
        database.insert("traffic_table",null,contentValues)
    }

    override fun getAllUser(str:String): ArrayList<User> {
        val list = ArrayList<User>()
        val query = "select * from traffic_table where mark = '$str'"
        val database = this.readableDatabase

        val cursor = database.rawQuery(query,null)
        if (cursor.moveToFirst()){
            do {
                val user = User()
                user.id = cursor.getInt(0)
                user.path_img = cursor.getString(1)
                user.name = cursor.getString(2)
                user.about = cursor.getString(3)
                user.mark = cursor.getString(4)
                user.love = cursor.getInt(5)

                list.add(user)
            }while (cursor.moveToNext())
        }
        return list
    }

    override fun getAllLikeUser(isHave:Int): ArrayList<User> {
        val list = ArrayList<User>()
        val query = "select * from traffic_table where love = '$isHave'"
        val database = this.readableDatabase

        val cursor = database.rawQuery(query,null)
        if (cursor.moveToFirst()){
            do {
                val user = User()
                user.id = cursor.getInt(0)
                user.path_img = cursor.getString(1)
                user.name = cursor.getString(2)
                user.about = cursor.getString(3)
                user.mark = cursor.getString(4)
                user.love = cursor.getInt(5)

                list.add(user)
            }while (cursor.moveToNext())
        }
        return list
    }

    override fun edit(user: User): Int {
        val dataBase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("traffic_name",user.name)
        contentValues.put("traffic_about",user.about)
        contentValues.put("mark",user.mark)
        contentValues.put("image_path",user.path_img)
        contentValues.put("love",user.love)
        return dataBase.update(
            "traffic_table",
            contentValues,
            "id =?",
            arrayOf("${user.id}")
        )
    }

    override fun deleteUser(user: User) {
        val database = this.writableDatabase
        database.delete(
            TABLE_NAME,
            "id =?",
            arrayOf("${user.id}")
        )
        database.close()
    }

}