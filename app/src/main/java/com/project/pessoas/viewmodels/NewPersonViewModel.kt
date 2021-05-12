package com.project.pessoas.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.data.repository.PersonRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewPersonViewModel(application: Application): AndroidViewModel(application),
    LifecycleObserver {

    private val composeDispose = CompositeDisposable()
    private val personRep = PersonRepository(application)

    private val mutableDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mutableDataError

    private val mutablePersonSaved: MutableLiveData<Boolean> = MutableLiveData()
    val isPersonSaved: LiveData<Boolean> get() = mutablePersonSaved

    fun saveNewPerson(person: PersonEntity){

        composeDispose.add(personRep.insertOneInDB(person)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    updateTXTFileFromDB()
                },
                {   err ->
                    mutableDataError.postValue(true)
                }
            ))
    }

    fun updateTXTFileFromDB(){
        composeDispose.add(personRep.getAllPersonFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    updateTXTfile(list as MutableList<PersonEntity>)
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            )
        )
    }

    fun updateTXTfile(list: MutableList<PersonEntity>){
        composeDispose.add(
            Observable.just(personRep.saveAllPersonInTXT(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    mutablePersonSaved.postValue(true)
                }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resetValues() {
        mutableDataError.value = false
        mutablePersonSaved.value = false
    }

    override fun onCleared() {
        super.onCleared()
        composeDispose.clear()
    }
}