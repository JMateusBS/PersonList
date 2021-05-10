package com.project.pessoas.data

import androidx.room.*
import com.project.pessoas.data.entities.PersonEntity
import io.reactivex.Single

@Dao
interface PersonDAO {

    @Query("SELECT * FROM person_table")
    fun getAll(): Single<List<PersonEntity>>

    @Delete
    fun deletePerson(person: PersonEntity): Single<Int>

    @Update
    fun updatePerson(person: PersonEntity): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(person: PersonEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(person: List<PersonEntity>): Single<List<Long>>
}