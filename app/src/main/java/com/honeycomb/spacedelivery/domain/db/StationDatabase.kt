package com.honeycomb.spacedelivery.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.honeycomb.spacedelivery.domain.db.entity.StationDao

@Database(entities = [StationItem::class] , version = 1)
abstract class StationDatabase: RoomDatabase() {

    abstract fun getGroceryDao() : StationDao

    companion object{
        @Volatile
        private var instance : StationDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance?:synchronized(LOCK){

            instance?:createDatabase(context).also{
                instance = it
            }
        }

        private fun createDatabase(context: Context)  =
            Room.databaseBuilder(context.applicationContext,StationDatabase::class.java,
                "GroceryDatabase.db").build()
    }
}