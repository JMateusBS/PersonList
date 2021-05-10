package com.project.pessoas.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.data.repository.PersonRepository
import com.project.pessoas.util.Operation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UpdatePersonViewModel (application: Application): AndroidViewModel(application),
    LifecycleObserver {

    private val composeDispose = CompositeDisposable()
    private val personRep = PersonRepository(application)

    private val mutableDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mutableDataError

    private val mutablePersonUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isPersonUpdated: LiveData<Boolean> get() = mutablePersonUpdated

    private val mutablePersonDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isPersonDeleted: LiveData<Boolean> get() = mutablePersonDeleted


    fun updatePersonDataBase(personModel: PersonEntity){
        composeDispose.add(personRep.updateInDB(personModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateDataFromDataBase(Operation.UPDATE)
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            ))
    }


    fun deletePersonDataBase(personModel: PersonEntity){
        composeDispose.add(personRep.deleteFromDB(personModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateDataFromDataBase(Operation.DELETE)
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            ))
    }


    fun updateDataFromDataBase(op: Operation){
        composeDispose.add(personRep.getAllPersonFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    updateTXTfile(list as MutableList<PersonEntity>, op)
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            )
        )
    }

    fun updateTXTfile(list: MutableList<PersonEntity>, op: Operation){

        composeDispose.add(
            Observable.just(personRep.saveAllPersonInTXT(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{

                    when(op){

                        Operation.DELETE -> {
                            mutablePersonDeleted.postValue(true)
                        }

                        Operation.UPDATE -> {
                            mutablePersonUpdated.postValue(true)
                        }
                    }

                }
        )

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resetValues() {
        mutableDataError.value = false
        mutablePersonUpdated.value = false
        mutablePersonDeleted.value = false
    }


    override fun onCleared() {
        super.onCleared()
        composeDispose.clear()
    }

}