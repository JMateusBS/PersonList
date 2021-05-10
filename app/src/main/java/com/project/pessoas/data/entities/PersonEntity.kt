package com.project.pessoas.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.pessoas.util.Constants.Companion.PERSON_TABLE
import java.io.Serializable

@Entity(tableName = PERSON_TABLE)
data class PersonEntity (

    @PrimaryKey(autoGenerate = true)
    var codPessoa: Long = 0,
    var nomePessoa: String = "",
    var complemento: String = "",
    var reservado1: String = "",
    var reservado2: String = ""
) : Serializable{
    override fun toString(): String {
        return "${codPessoa};${nomePessoa};${complemento};${reservado1};${reservado2}"
    }
}