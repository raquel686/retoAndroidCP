package com.project.ecommercecineplanet.domain.model

import com.project.ecommercecineplanet.data.model.CreditCard
import com.project.ecommercecineplanet.data.model.ExtraParameters
import com.project.ecommercecineplanet.data.model.Order
import com.project.ecommercecineplanet.data.model.Payer

data class TransactionDomainModel(
    val order: OrderDomainModel,
    val payer: Payer,
    val creditCard: CreditCard,
    val paymentMethod: String,
    val deviceSessionId: String,
    val ipAddress: String,
)
