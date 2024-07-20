package com.project.ecommercecineplanet.data.model

import com.project.ecommercecineplanet.domain.model.PayuDtoDomainModel

data class PayuDto(
    val language: String,
    val command: String,
    val merchant: Merchant,
    val transaction: Transaction,
    val test: Boolean = true
)

fun PayuDtoDomainModel.toModel()=PayuDto(
    "es",
    "SUBMIT_TRANSACTION",
    Merchant("4Vj8eK4rloUd272L48hsrarnUA","pRRXKOl8ikMmt9u"),
    transaction.toModel(),
    true)