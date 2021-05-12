package com.project.pessoas.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.pessoas.R
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.teste.FragmentInfo
import com.project.pessoas.teste.SupportScreenManager
import com.project.pessoas.util.Constants
import com.project.pessoas.util.Constants.Companion.PERSON_BUNDLE
import com.project.pessoas.viewmodels.UpdatePersonViewModel

class UpdatePersonFragment: Fragment() {

    private lateinit var personId: TextView
    private lateinit var personName: EditText
    private lateinit var info: EditText
    private lateinit var res1: EditText
    private lateinit var res2: EditText

    private lateinit var currentPerson: PersonEntity
    private lateinit var mViewModel: UpdatePersonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_person, container, false)

        personId = view.findViewById(R.id.id_update_person)
        personName = view.findViewById(R.id.person_update_name)
        info = view.findViewById(R.id.person_update_info)
        res1 = view.findViewById(R.id.person_update_reservado1)
        res2 = view.findViewById(R.id.person_update_reservado2)

        currentPerson = arguments?.getSerializable(PERSON_BUNDLE) as PersonEntity

        personId.text = currentPerson.codPessoa.toString()
        personName.setText(currentPerson.nomePessoa)
        info.setText(currentPerson.complemento)
        res1.setText(currentPerson.reservado1)
        res2.setText(currentPerson.reservado2)

        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(UpdatePersonViewModel::class.java)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isPersonDeleted.observe(viewLifecycleOwner, mDeleteObserver)
        mViewModel.isPersonUpdated.observe(viewLifecycleOwner, mUpdateObserver)

        view.findViewById<MaterialButton>(R.id.btn_delete_person).setOnClickListener(mListener)
        view.findViewById<MaterialButton>(R.id.btn_update_person).setOnClickListener(mListener)


        return view
    }

    private val mDataErrorObserver = Observer<Boolean> { error ->
        if (error) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            mViewModel.resetValues()
        }
    }

    private val mUpdateObserver = Observer<Boolean> { updated ->
        if (updated) {
            Toast.makeText(context, "Person updated", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
        }
    }

    private val mDeleteObserver = Observer<Boolean> { deleted ->
        if (deleted) {
            Toast.makeText(context, "Person deleted", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
        }
    }

    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.btn_update_person -> {

                if (personName.text.isNullOrEmpty() || info.text.isNullOrEmpty() || res1.text.isNullOrEmpty() || res2.text.isNullOrEmpty()
                ) {
                    Toast.makeText(context, "Fill all the informantion", Toast.LENGTH_LONG).show()
                } else if (
                    personName.text.toString().toUpperCase() == currentPerson.nomePessoa &&
                    info.text.toString() == currentPerson.complemento &&
                    res1.text.toString() == currentPerson.reservado1 &&
                    res2.text.toString() == currentPerson.reservado2
                ) {
                    Toast.makeText(context, "", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val newPerson = PersonEntity(
                        nomePessoa = personName.text.toString().toUpperCase(),
                        complemento = info.text.toString(),
                        reservado1 = res1.text.toString(),
                        reservado2 = res2.text.toString(),
                        codPessoa = currentPerson.codPessoa
                    )
                    mViewModel.updatePersonDataBase(newPerson)
                }
            }

            R.id.btn_delete_person -> {
                mViewModel.deletePersonDataBase(currentPerson)
            }
        }
//        SupportScreenManager.goTo(FragmentInfo(R.id.fragment_person))
    }

}