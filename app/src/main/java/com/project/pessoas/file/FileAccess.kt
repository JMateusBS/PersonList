package com.project.pessoas.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.util.Constants
import com.project.pessoas.util.UploadState
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

class FileAccess {

    companion object{
        private const val FILE_NAME = Constants.FILE_NAME

        private var currentUploadState: MutableLiveData<UploadState> = MutableLiveData<UploadState>().apply { value = UploadState.IDLE}

        fun getLiveData() = currentUploadState
        fun getState() = currentUploadState.value

        fun setState(state: UploadState){
            currentUploadState.postValue(state)
        }

        fun getPersonFromFile(context: Context): MutableList<PersonEntity> {
            val personList = mutableListOf<PersonEntity>()

            val downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, FILE_NAME)

            if (file.exists()) {
                file.readLines(Charset.forName("ISO-8859-1")).forEach { text ->
                    text.split(";").let {

                        val newPerson = PersonEntity(it[0].toLong(), it[1], it[2], it[3], it[4])
                        personList.add(newPerson)

                    }
                }
            } else {
                file.createNewFile()
            }
            return personList
        }

        fun savePersonInFile(context: Context, personList: MutableList<PersonEntity>) {

            val downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, FILE_NAME)

            file.writeText(getLinesFromPersonModelList(personList), Charset.forName("ISO-8859-1"))
        }

        private fun getLinesFromPersonModelList(personList: MutableList<PersonEntity>): String {

            var linesList = ""

            personList.forEach {
                linesList += "${it.toString()}\n"
            }

            return linesList
        }

        fun getDataFromUploadedFile(uri: Uri, context: Context) {

            val regex = "\\d*;[^0-9]*;[^0-9]*;\\d*;\\d*".toRegex()
            val personList = mutableListOf<PersonEntity>()

            context.contentResolver.openInputStream(uri).use { inputStream ->
                BufferedReader(
                    InputStreamReader(inputStream, Charset.forName("ISO-8859-1"))
                ).use { reader ->

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {

                        line?.also { fileLine ->
                            if (fileLine.matches(regex)) {
                                fileLine.split(";").let {

                                    val newPerson =
                                        PersonEntity(it[0].toLong(), it[1], it[2], it[3], it[4])
                                    personList.add(newPerson)

                                }
                            }
                        }
                    }

                }
            }

            if (personList.isNotEmpty()) {
                val oldPerson = getPersonFromFile(context)


                val listToSave = (oldPerson + personList.filter { newPerson ->
                    newPerson !in oldPerson
                }) as MutableList<PersonEntity>

                savePersonInFile(context, listToSave)

                setState(UploadState.SUCCESS)
            } else {
                setState(UploadState.ERROR)

            }

        }
    }
}