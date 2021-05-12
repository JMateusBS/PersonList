package com.project.pessoas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.pessoas.R
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.teste.FragmentInfo
import com.project.pessoas.teste.SupportScreenManager
import com.project.pessoas.viewmodels.NewPersonViewModel

class NewPersonFragment: Fragment() {

    private lateinit var personName: EditText
    private lateinit var info: EditText
    private lateinit var res1: EditText
    private lateinit var res2: EditText

    private lateinit var mViewModel: NewPersonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_person, container, false)

        personName = view.findViewById(R.id.person_name)
        info = view.findViewById(R.id.person_info)
        res1 = view.findViewById(R.id.person_reservado1)
        res2 = view.findViewById(R.id.person_reservado2)

        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(NewPersonViewModel::class.java)

        mViewModel.dataError.observe(viewLifecycleOwner, dataErrorObserver)
        mViewModel.isPersonSaved.observe(viewLifecycleOwner, personSavedObserver)

        view.findViewById<MaterialButton>(R.id.save_new_person)
            .setOnClickListener(savePersonListener)


        return view
    }


    private val savePersonListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.save_new_person -> {

                if (personName.text.isNullOrEmpty() ||
                    info.text.isNullOrEmpty() || res1.text.isNullOrEmpty() || res2.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Fill all the information", Toast.LENGTH_LONG).show()
                } else {
                    val newPerson = PersonEntity(
                        nomePessoa = personName.text.toString().toUpperCase(),
                        complemento = info.text.toString(),
                        reservado1 = res1.text.toString(),
                        reservado2 = res2.text.toString()
                    )

                    mViewModel.saveNewPerson(newPerson)
                }
            }
        }
    }

    private val dataErrorObserver = Observer<Boolean> { error ->
        if (error) {
            mViewModel.resetValues()
        }
    }

    private val personSavedObserver = Observer<Boolean> { saved ->
        if (saved) {
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
        }
    }
}