package com.project.ecommercecineplanet.domain.model

import com.project.ecommercecineplanet.data.model.CandyItemModel

data class CandyItemDomainModel (
    val name:String?="",
    val description:String?="",
    val price:String?="",
    val url:String?="https://img.grouponcdn.com/needish/4DUQVZ4JnQCbYFmrWP6j/MC-700x420/v1/c700x420.jpg",
    var cantidad:Int?=0
)

fun CandyItemModel.toDomain()=CandyItemDomainModel(name, description, price, url ="https://img.grouponcdn.com/needish/4DUQVZ4JnQCbYFmrWP6j/MC-700x420/v1/c700x420.jpg",cantidad = 0)
