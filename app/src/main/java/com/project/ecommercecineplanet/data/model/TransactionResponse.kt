package com.project.ecommercecineplanet.data.model

data class TransactionResponse(
    val orderId: String,
    val transactionId: String,
    val state: String,
    val paymentNetworkResponseCode: String,
    val paymentNetworkResponseErrorMessage: String?,
    val trazabilityCode: String,
    val authorizationCode: String,
    val pendingReason: String?,
    val responseCode: String,
    val errorCode: String?,
    val responseMessage: String,
    val transactionDate: String?,
    val transactionTime: String?,
    val operationDate: String?,
    val referenceQuestionnaire: String?,
    val extraParameters: Map<String, String>,
    val additionalInfo: String?
)
