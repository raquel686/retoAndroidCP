package com.project.ecommercecineplanet.data.model

data class PayUResponse(
    val code: String,
    val error: String?,
    val transactionResponse: TransactionResponse
)
