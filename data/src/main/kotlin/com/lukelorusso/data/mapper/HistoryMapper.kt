package com.lukelorusso.data.mapper

import com.lukelorusso.data.extensions.getCreditOrDebit
import com.lukelorusso.data.extensions.toIntlCurrencyString
import com.lukelorusso.domain.model.Constant
import com.lukelorusso.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

class HistoryMapper(
    private val giveSuffix: String = Constant.Message.YOU_OWE,
    private val takeSuffix: String = Constant.Message.YOU_GET
) {

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd-MM-YY"
    }

    /**
     * Try to map the input to a history log related to the message sender.
     * @param messageSender mapped by ambrogio.
     * @param transactionList to be analysed.
     * @return a [List] of [String] as a history log if successfully mapped, null otherwise
     */
    fun map(
        messageSender: String,
        transactionList: List<Transaction>
    ): List<String>? {
        val historyLog = mutableListOf<String>()
        transactionList.forEach { transaction ->
            if (transaction.participantNameList.plus(transaction.sender).contains(messageSender)) {
                mapAsDescribedTransaction(
                    messageSender,
                    transaction
                )?.also { historyLog.add(it) }
            }
        }
        return historyLog
    }

    /**
     * Map a transaction to a description form a message sender perspective.
     * @param messageSender the subject.
     * @param transaction the transaction.
     * @return the description of the transaction as [String], null in case of sender not part of the transaction
     */
    private fun mapAsDescribedTransaction(
        messageSender: String,
        transaction: Transaction
    ): String? {
        val formattedDate = mapAsFormattedDate(transaction.timestamp)
        val description = transaction.description.let { it + if (it.isNotEmpty()) " -" else "-" }
        val value = transaction.getCreditOrDebit(messageSender) ?: return null
        val formattedValue = value.toIntlCurrencyString(abs = true)
        val messageSenderGet = messageSender == transaction.sender
        val whatToDo = if (messageSenderGet) takeSuffix else giveSuffix
        return "$formattedDate $description $whatToDo $formattedValue"
    }

    /**
     * Map a timestamp into a formatted date.
     * @param timestamp as [Long].
     * @return the formatted date as [String]
     */
    private fun mapAsFormattedDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

}
