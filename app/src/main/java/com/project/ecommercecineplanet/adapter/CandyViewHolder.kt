package com.project.ecommercecineplanet.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ecommercecineplanet.databinding.ItemDulceBinding
import com.project.ecommercecineplanet.domain.model.CandyItemDomainModel

class CandyViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding=ItemDulceBinding.bind(view)

    fun render(candyItem: CandyItemDomainModel, onClick:(CandyItemDomainModel)->Unit,onClick2:(CandyItemDomainModel)->Unit, context: Context){
        Glide.with(context).load(candyItem.url).into(binding.imageItem)
        binding.nameItem.text=candyItem.name
        binding.descriptionItem.text=candyItem.description
        binding.priceItem.text=candyItem.price

        binding.countItem.text=candyItem.cantidad.toString()

        binding.addItem.setOnClickListener {
           onClick(candyItem)
        }

        binding.removeItem.setOnClickListener {
           onClick2(candyItem)
        }

    }
}