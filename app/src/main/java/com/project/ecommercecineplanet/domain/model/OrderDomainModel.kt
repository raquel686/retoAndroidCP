package com.project.ecommercecineplanet.domain.model

import com.project.ecommercecineplanet.data.model.AdditionalValues
import com.project.ecommercecineplanet.data.model.Buyer
import com.project.ecommercecineplanet.data.model.ShippingAddress
import com.project.ecommercecineplanet.data.model.Transaction

data class OrderDomainModel (
    val referenceCode: String,
    val description: String,
    val signature: String,
    val additionalValues: AdditionalValues,
)

