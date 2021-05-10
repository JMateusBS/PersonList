package com.project.pessoas.data.repository

import android.app.Application
import android.net.Uri
import com.project.pessoas.data.PersonDatabase
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.file.FileAccess
import io.reactivex.Single

class PersonRepository (val application: Application) {

    private val roomDataBase = PersonDatabase.getDataBase(application)


    fun getAllPersonFromTXT(): MutableList<PersonEntity> {
        return FileAccess.getPersonFromFile(application)
    }

    fun saveAllPersonInTXT(list: MutableList<PersonEntity>) {
        return FileAccess.savePersonInFile(application, list)
    }

    fun getDataFromUploadedFile(uri: Uri){
        return FileAccess.getDataFromUploadedFile(uri, application)
    }

    fun getAllPersonFromDB(): Single<List<PersonEntity>> {
        return roomDataBase.PersonDAO().getAll()
    }

    fun insertManyInDB(list: List<PersonEntity>): Single<List<Long>> {
        return roomDataBase.PersonDAO().insertMany(list)
    }

    fun insertOneInDB(person: PersonEntity): Single<Long> {
        return roomDataBase.PersonDAO().insertOne(person)
    }

    fun deleteFromDB(person: PersonEntity): Single<Int> {
        return roomDataBase.PersonDAO().deletePerson(person)
    }

    fun updateInDB(person: PersonEntity): Single<Int> {
        return roomDataBase.PersonDAO().updatePerson(person)
    }
}