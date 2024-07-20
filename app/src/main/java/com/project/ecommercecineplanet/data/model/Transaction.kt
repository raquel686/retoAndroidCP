package com.project.ecommercecineplanet.data.model

import com.project.ecommercecineplanet.domain.model.TransactionDomainModel

data class Transaction (
    val order: Order,
    val payer: Payer,
    val creditCard: CreditCard,
    val extraParameters: ExtraParameters,
    val type: String,
    val paymentMethod: String,
    val paymentCountry: String,
    val deviceSessionId: String,
    val ipAddress: String,
    val cookie: String,
    val userAgent: String
)

fun TransactionDomainModel.toModel()=Transaction(
    order.toModel(),
    payer,
    creditCard,
    ExtraParameters(1),
    "AUTHORIZATION_AND_CAPTURE",
    paymentMethod,
    "PE",
    deviceSessionId,
    ipAddress,
    "pt1t38347bs6jc9ruv2ecpv7o2",
    "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0"
)
