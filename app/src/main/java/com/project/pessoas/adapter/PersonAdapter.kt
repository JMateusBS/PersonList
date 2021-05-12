package com.project.pessoas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.teste.FragmentInfo
import com.project.pessoas.teste.SupportScreenManager
import com.project.pessoas.util.Constants.Companion.PERSON_BUNDLE

class PersonRecyclerViewAdapter(val person: List<PersonEntity>) : RecyclerView.Adapter<PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_row_layout, parent, false)

        return PersonViewHolder(view)

    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(
            person[position]
        )
    }

    override fun getItemCount(): Int {
        return person.size
    }
}


class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mId = itemView.findViewById<TextView>(R.id.person_id)
    private val mName = itemView.findViewById<TextView>(R.id.person_name)
    private val mInfo = itemView.findViewById<TextView>(R.id.person_info)
    private val reservado1 = itemView.findViewById<TextView>(R.id.person_reservado1)
    private val reservado2 = itemView.findViewById<TextView>(R.id.person_reservado2)

    fun bind(person: PersonEntity) {

        mId.text = person.codPessoa.toString()
        mName.text = person.nomePessoa
        mInfo.text = person.complemento
        reservado1.text = person.reservado1
        reservado2.text = person.reservado2

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                PERSON_BUNDLE,
                person
            )
            SupportScreenManager.goTo(FragmentInfo(R.id.fragment_update_person, bundle))
        }
    }

}