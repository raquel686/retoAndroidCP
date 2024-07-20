package com.project.ecommercecineplanet.data.model

data class Buyer(
    val merchantBuyerId: String="1",
    val fullName: String="Raquel Chavez",
    val emailAddress: String="raichelle2@gmail.com",
    val contactPhone: String="947238723",
    val dniNumber: String="75266945",
    val shippingAddress: ShippingAddress=ShippingAddress())
