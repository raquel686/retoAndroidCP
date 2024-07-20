package com.project.ecommercecineplanet.data.model

import com.project.ecommercecineplanet.domain.model.OrderDomainModel

data class Order(
    val accountId: String,
    val referenceCode: String,
    val description: String,
    val language: String,
    val signature: String,
    val notifyUrl: String,
    val additionalValues: AdditionalValues,
    val buyer: Buyer,
    val shippingAddress: ShippingAddress
)

fun OrderDomainModel.toModel()=Order(
    "512323",
    referenceCode,
    description,
    "es",
    signature,
    "http://www.payu.com/notify",
    additionalValues,
    Buyer(),
    ShippingAddress()
)