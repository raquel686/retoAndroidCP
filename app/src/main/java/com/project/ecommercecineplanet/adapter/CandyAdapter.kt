package com.project.ecommercecineplanet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.ecommercecineplanet.R
import com.project.ecommercecineplanet.domain.model.CandyItemDomainModel

class CandyAdapter(private val candyList:MutableList<CandyItemDomainModel>, private val onClick:(CandyItemDomainModel)->Unit,private val onClick2:(CandyItemDomainModel)->Unit,private val context: Context)
    : RecyclerView.Adapter<CandyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CandyViewHolder(layoutInflater.inflate(R.layout.item_dulce,parent,false ))
    }

    override fun getItemCount(): Int = candyList.size

    override fun onBindViewHolder(holder: CandyViewHolder, position: Int) {
        val item=candyList[position]
        holder.render(item,onClick,onClick2,context)
    }

    fun updateItem(item: CandyItemDomainModel) {
        val index = candyList.indexOf(item)
        if (index != -1) {
            candyList[index] = item
            notifyItemChanged(index)
        }
    }

    fun getCandyList(): List<CandyItemDomainModel> = candyList

}