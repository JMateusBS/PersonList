package com.project.pessoas.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.pessoas.PersonRecyclerViewAdapter
import com.project.pessoas.R
import com.project.pessoas.adapter.PersonAdapterBiding
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.databinding.FragmentPersonBinding
import com.project.pessoas.databinding.FragmentPersonBindingBinding
import com.project.pessoas.file.FileAccess
import com.project.pessoas.teste.FragmentInfo
import com.project.pessoas.teste.SupportScreenManager
import com.project.pessoas.util.UploadState
import com.project.pessoas.viewmodels.PersonBindingViewModel
import com.project.pessoas.viewmodels.PersonViewModel
import kotlinx.coroutines.launch

class PersonFragmentBinding: Fragment() {

    private var _binding: FragmentPersonBindingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: PersonBindingViewModel
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter by lazy { PersonAdapterBiding() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(PersonBindingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPersonBindingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.personViewModel = mViewModel

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.personList.adapter = mAdapter
        binding.personList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun readDatabase() {
        lifecycleScope.launch{

        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }

}