package com.lukelorusso.moneydivider.extensions

import com.lukelorusso.domain.model.Transaction

fun List<Transaction>.getTotalMap(): Map<String, Double> {
    val totalMap = mutableMapOf<String, Double>()
    this.forEach { transition ->
        totalMap[transition.sender] =
            totalMap[transition.sender] ?: 0.0 + transition.value
    }
    return totalMap
}
