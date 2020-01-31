package com.lukelorusso.moneydivider.mapper

import com.lukelorusso.moneydivider.extensions.getCreditOrDebit
import com.lukelorusso.moneydivider.models.BalanceRefund
import com.lukelorusso.moneydivider.models.Constant
import com.lukelorusso.moneydivider.models.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

class BalanceMapper {
    /**
     * Try to map the input to a balance log.
     * @param transactionList to be analysed.
     * @return a [List] of [String] as a balance log if successfully mapped, null otherwise
     */
    fun map(transactionList: List<Transaction>): List<String>? {
        val balanceLog = mutableListOf<String>()
        val participantSituationMap = mapParticipantSituation(transactionList)
        val refundList = mutableListOf<BalanceRefund>()

        populateBalance(participantSituationMap, refundList)

        refundList.forEach { refund ->
            // formatting value
            val value = BigDecimal(refund.value)
                .setScale(2, RoundingMode.HALF_EVEN)

            // formatting output message
            balanceLog.add("${refund.senderSubject} ${Constant.Message.OWES} ${refund.receiverSubject} $value")
        }

        return balanceLog
    }

    /**
     * Map all subjects for each transaction to a list.
     * @param transactionList to be analysed.
     * @return a [List] of [String] containing all subjects for each transaction
     */
    private fun mapAllParticipants(transactionList: List<Transaction>): List<String> {
        val participantList = mutableListOf<String>()
        transactionList.forEach { transaction ->
            participantList.add(transaction.sender)
            transaction.participantNameList.forEach { participant ->
                participantList.add(participant)
            }
        }
        return participantList.sorted().distinct()
    }

    /**
     * Map the situation (credit or debit amount) for each participant considering all transactions.
     * @param transactionList to be analysed.
     * @return a [Map] for each participant with his/her negative (credit) or positive (debit) [Double] amount
     */
    private fun mapParticipantSituation(transactionList: List<Transaction>): MutableMap<String, Double> {
        val participantList = mapAllParticipants(transactionList)
        val participantSituationMap = mutableMapOf<String, Double>()
        transactionList.forEach { transaction ->
            participantList.forEach { participant ->
                val transactionBalance = transaction.getCreditOrDebit(participant)
                transactionBalance?.also { balance ->
                    participantSituationMap[participant] =
                        (participantSituationMap[participant] ?: 0.0) + balance
                }
            }
        }
        return participantSituationMap
    }

    /**
     * Recursively minimize the balance, saving the refund steps needed to reach a 0 balance.
     * @param participantSituationMap the situation (credit or debit amount) for each participant considering all transactions.
     * ATTENTION: it mutates during the process!
     * @param refundList the steps needed to reach a 0 balance.
     * ATTENTION: it mutates during the process!
     */
    private fun populateBalance(
        participantSituationMap: MutableMap<String, Double>,
        refundList: MutableList<BalanceRefund>
    ) {
        val maxCreditEntry = participantSituationMap.minBy { it.value } ?: return
        if (maxCreditEntry.value >= 0)
            return
        val maxDebitEntry = participantSituationMap.maxBy { it.value } ?: return

        // at this point we are sure this balance can be optimized
        when {

            maxDebitEntry.value.absoluteValue < maxCreditEntry.value.absoluteValue -> {
                // use all debit to repay the credit
                val newCredit = maxCreditEntry.value + maxDebitEntry.value
                participantSituationMap[maxCreditEntry.key] = newCredit
                participantSituationMap.remove(maxDebitEntry.key)
                refundList.add(
                    BalanceRefund(
                        maxDebitEntry.key,
                        maxCreditEntry.key,
                        maxDebitEntry.value.absoluteValue
                    )
                )
            }

            maxDebitEntry.value.absoluteValue > maxCreditEntry.value.absoluteValue -> {
                // use partial debit to repay the credit
                val newDebit = maxCreditEntry.value + maxDebitEntry.value
                participantSituationMap[maxDebitEntry.key] = newDebit
                participantSituationMap.remove(maxCreditEntry.key)
                refundList.add(
                    BalanceRefund(
                        maxDebitEntry.key,
                        maxCreditEntry.key,
                        maxCreditEntry.value.absoluteValue
                    )
                )
            }

            else -> {
                // we call this a draw (or a rounding error)
                participantSituationMap.remove(maxDebitEntry.key)
                participantSituationMap.remove(maxCreditEntry.key)
                if (maxDebitEntry.key != maxCreditEntry.key) // avoiding weird rounding error cases
                    refundList.add(
                        BalanceRefund(
                            maxDebitEntry.key,
                            maxCreditEntry.key,
                            maxCreditEntry.value.absoluteValue
                        )
                    )
            }
        }

        return populateBalance(participantSituationMap, refundList)
    }

}
