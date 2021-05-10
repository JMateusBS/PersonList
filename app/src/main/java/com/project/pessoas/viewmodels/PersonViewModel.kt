package com.project.pessoas.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.data.repository.PersonRepository
import com.project.pessoas.file.FileAccess
import com.project.pessoas.util.UploadState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PersonViewModel (application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private var mutablePersonList: MutableLiveData<List<PersonEntity>> = MutableLiveData()
    val personList: LiveData<List<PersonEntity>> get() = mutablePersonList

    private val mutableDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mutableDataError


    val isUploadSuccess: LiveData<UploadState> get() = FileAccess.getLiveData()


    private val composeDispose = CompositeDisposable()
    private val personRep = PersonRepository(application)

    init {
        clearError()
    }

    fun saveFileDataOnDatabase(person: MutableList<PersonEntity>) {
        composeDispose.add(personRep.insertManyInDB(person)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    getPersonFromDataBase()
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            )
        )

    }

    fun getPersonFromDataBase() {
        composeDispose.add(personRep.getAllPersonFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    mutablePersonList.postValue(list)
                },
                { err ->
                    mutableDataError.postValue(true)
                }
            )
        )

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getDataFromFile() {

        composeDispose.add(
            Observable.just(personRep.getAllPersonFromTXT())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { list ->
                    saveFileDataOnDatabase(list)
                }
        )
    }

    fun getDataFromUploadedFile(uri: Uri) {

        composeDispose.add(Observable.just(personRep.getDataFromUploadedFile(uri))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        )
    }


    fun clearError() {
        mutableDataError.value = false
    }

    fun setUploadIdle() {
        FileAccess.setState(UploadState.IDLE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun dispose() {
        composeDispose.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        composeDispose.clear()
    }

}