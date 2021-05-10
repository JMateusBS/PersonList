package com.project.pessoas.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.project.pessoas.R
import com.project.pessoas.databinding.ActivityMainBinding
import com.project.pessoas.teste.SupportScreenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val _compositeDisposable = CompositeDisposable()
    private lateinit var _mainNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        _mainNavController = Navigation.findNavController(this, R.id.nav_host)

        SupportScreenManager.mainFragmentId = R.id.fragment_person

        _compositeDisposable.add(SupportScreenManager.fragmentDestination
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (_mainNavController.currentDestination?.id != it.fragmentId) {

                    _mainNavController.popBackStack()
                    _mainNavController.navigate(it.fragmentId, it.extras)

                }
            }, { t ->
                t.printStackTrace()
            })
        )
    }


}