package com.project.pessoas.adapter

import android.app.Person
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.pessoas.data.entities.PersonEntity
import com.project.pessoas.data.entities.Persons
import com.project.pessoas.databinding.PersonBindingRowLayoutBinding
import com.project.pessoas.util.PersonDiffUtil

class PersonAdapterBiding : RecyclerView.Adapter<PersonAdapterBiding.MyViewHolder>() {

    private var person = emptyList<PersonEntity>()

    class MyViewHolder(private val binding: PersonBindingRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: PersonEntity){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PersonBindingRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPerson = person[position]
        holder.bind(currentPerson)
    }

    override fun getItemCount(): Int {
        return person.size
    }

    fun setData(newData: Persons){
        val movieDiffUtil = PersonDiffUtil(person, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        person = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}