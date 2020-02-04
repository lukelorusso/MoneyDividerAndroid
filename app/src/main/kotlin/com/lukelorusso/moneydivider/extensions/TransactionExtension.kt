package com.lukelorusso.moneydivider.extensions

import com.lukelorusso.moneydivider.models.Transaction

/**
 * Map a subject's amount as NEGATIVE if he GET BACK (credit) or as POSITIVE if he PAY BACK (debit).
 * @param messageSender the subject.
 * @return as [Double] the NEGATIVE subject's bill in case he get back, the POSITIVE other's total otherwise, null if not present in the transaction at all
 */
fun Transaction.getCreditOrDebit(messageSender: String): Double? {
    if (messageSender != this.sender && !this.participantNameList.contains(messageSender))
        return null // not a subject in this transaction

    // calculating the base (without plus operations, for each participant's time)
    var dividend = this.value
    this.unevenPlusMap.forEach { (_, toSubtract) ->
        dividend -= toSubtract
    }
    var divisor = this.participantNameList.size
    this.unevenStarMap.forEach { (_, toMultiply) ->
        divisor += toMultiply - 1 // -1 because it has already been counted in participantNameList.size
    }
    val base = dividend / divisor

    // calculating sender's bill
    val hisPresence = if (this.participantNameList.contains(messageSender)) 1 else 0
    val hisBill = base
        .times(this.unevenStarMap[messageSender] ?: hisPresence)
        .plus(this.unevenPlusMap[messageSender] ?: 0.0)
    val notHisBill = this.value - hisBill

    // it's pay time
    val messageSenderGet = messageSender == this.sender
    return if (messageSenderGet) (-1 * notHisBill) else hisBill
}

fun List<Transaction>.getTotalMap(): Map<String, Double> {
    val totalMap = mutableMapOf<String, Double>()
    this.forEach { transition ->
        totalMap[transition.sender] =
            totalMap[transition.sender] ?: 0.0 + transition.value
    }
    return totalMap
}
