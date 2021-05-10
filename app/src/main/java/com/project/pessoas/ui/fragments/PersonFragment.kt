package com.project.pessoas.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.pessoas.PersonRecyclerViewAdapter
import com.project.pessoas.R
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.file.FileAccess
import com.project.pessoas.teste.FragmentInfo
import com.project.pessoas.teste.SupportScreenManager
import com.project.pessoas.util.UploadState
import com.project.pessoas.viewmodels.PersonViewModel

class PersonFragment: Fragment() {

    private lateinit var mViewModel: PersonViewModel
    private lateinit var mRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_person, container, false)

        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(PersonViewModel::class.java)

        view.findViewById<FloatingActionButton>(R.id.new_person).setOnClickListener(mListener)
        view.findViewById<FloatingActionButton>(R.id.import_person).setOnClickListener(mListener)


        mRecyclerView = view.findViewById(R.id.person_list)
        mRecyclerView.adapter = PersonRecyclerViewAdapter(listOf())

        viewLifecycleOwner.lifecycle.addObserver(mViewModel)
        mViewModel.personList.observe(viewLifecycleOwner, mListObserver)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUploadSuccess.observe(viewLifecycleOwner, mUploadSuccess)


        return view
    }

    private val mListObserver = Observer<List<PersonEntity>>{ personList ->
        mRecyclerView.apply {
            adapter = PersonRecyclerViewAdapter(personList)
            mRecyclerView.visibility = View.VISIBLE
        }

    }

    private val mDataErrorObserver = Observer<Boolean>{ error ->
        if(error) {
            Toast.makeText(context, "Loading error", Toast.LENGTH_SHORT).show()
            mViewModel.clearError()
        }
    }

    private val mUploadSuccess = Observer<UploadState>{ state ->
        if(state != UploadState.IDLE){
            if(state == UploadState.SUCCESS) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                mViewModel.clearError()
            }else{
                Toast.makeText(context, "No person found", Toast.LENGTH_SHORT).show()
                mViewModel.clearError()
            }

            mViewModel.setUploadIdle()
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack()
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
        }
    }

    private val mListener = View.OnClickListener { view ->
        when(view.id){

            R.id.new_person -> {
                val fragmentInfo = FragmentInfo(R.id.fragment_new_person)
                SupportScreenManager.goTo(fragmentInfo)
            }

            R.id.import_person -> {
                if(FileAccess.getState() == UploadState.IDLE) {
                    val intent = Intent()
                        .setType("text/plain")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT)

                    startActivityForResult(Intent.createChooser(intent, "Search the file"), 111)
                }

            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            val selectedFile = data?.data

            selectedFile?.also{ uri ->
                mViewModel.getDataFromUploadedFile(uri)
            }


        }else{
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack()
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
        }

    }
}