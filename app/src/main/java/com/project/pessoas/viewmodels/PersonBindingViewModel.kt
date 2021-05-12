package com.project.pessoas.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.data.repository.PersonRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PersonBindingViewModel @ViewModelInject constructor(
    private val repository: PersonRepository,
    application: Application
) : AndroidViewModel(application){

//   fun loadData() {
//       repository.getAllPersonFromDB()
//           .subscribeOn(Schedulers.io())
//           .observeOn(AndroidSchedulers.mainThread())
//           .doOnSubscribe() { persons ->
//               // o persons é seu retorno, vc usa a lista aqui
//
//           }
//    }

}