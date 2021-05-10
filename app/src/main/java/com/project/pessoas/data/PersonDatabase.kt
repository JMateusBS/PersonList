package com.project.pessoas.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.util.Constants.Companion.DATABASE_NAME

@Database(
        entities = [PersonEntity::class],
        version = 1,
        exportSchema = false
)

abstract class PersonDatabase: RoomDatabase() {

    abstract fun  PersonDAO(): PersonDAO
    companion object {

        private var dataBase: PersonDatabase? = null

        fun getDataBase(application: Application): PersonDatabase {
            if (dataBase != null) {
                return dataBase!!
            }
            dataBase = Room.databaseBuilder(
                application,
                PersonDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
            return dataBase!!
        }

    }
}