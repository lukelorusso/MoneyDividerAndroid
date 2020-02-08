package com.lukelorusso.data.mapper

import com.lukelorusso.domain.model.Constant
import com.lukelorusso.domain.model.AddAppTransactionListException
import com.lukelorusso.domain.model.Transaction
import javax.inject.Inject

class AddAppTransactionListMapper
@Inject constructor() : AddTransactionMapper() {

    companion object {
        private const val ALL_PARTICIPANTS_PLACEHOLDER = "[!ALL_PARTICIPANTS_PLACEHOLDER]"
    }

    fun map(
        messageTimestamp: Long,
        unParsedInputList: List<String>
    ): List<Transaction> {
        val (senderList, parsedInputList) = parseToInput(unParsedInputList)
        return parsedInputList.mapIndexed { i, inputLine ->
            super.map(
                messageTimestamp,
                senderList[i],
                inputLine,
                emptyList()
            )
        }.filterNotNull()
    }

    /**
     * Parse (better: convert):
     * - a "human friendly" list of transaction
     * - to a pair of: list of payers + an "algorithm understandable" list of transaction
     *
     * Examples:
     * "18.00 LL Beer" -> LL pays 18 for all participants
     * "16,50 GS (LL)" -> GS pays 16.50 (we convert "," to ".") ONLY for LL
     * "3 CC (LL, CC) Dinner with LL" -> CC pays 3 for himself and LL (1.50 per head)
     * At the end of the parsing we know there are 3 total participants, so LL pays 6 per head
     */
    private fun parseToInput(inputList: List<String>): Pair<List<String>, List<String>> {
        var allParticipantList = mutableListOf<String>()
        val senderList = mutableListOf<String>()
        val parsedInputList = mutableListOf<String>()

        inputList.forEachIndexed { i, line ->
            try {
                var remaining = line.replace(Constant.RegExp.WHITE_SPACES, " ")

                // checking participants
                var participants =
                    ALL_PARTICIPANTS_PLACEHOLDER
                if (remaining.contains("(")) {
                    if (!remaining.contains(")")) throw AddAppTransactionListException(
                        i
                    )
                    participants =
                        remaining.substring(remaining.indexOf("("), remaining.indexOf(")"))
                    remaining = remaining
                        .replace("$participants) ", "") // space might be after...
                        .replace(" $participants)", "") // ...or before
                    participants = participants
                        .replace("(", "")
                        .replace(Constant.RegExp.WHITE_SPACES, "")
                    if (participants.isNotEmpty())
                        allParticipantList.addAll(
                            participants.split(',').map { it.trim() }
                        )
                }

                // expecting the first thing to be the value
                var valueAsString = remaining.substring(0, remaining.indexOf(" "))
                remaining = remaining.replace("$valueAsString ", "")
                valueAsString = valueAsString.replace(',', '.')
                if (valueAsString.isBlank() || valueAsString.toDoubleOrNull() == null || remaining.isBlank())
                    throw AddAppTransactionListException(i)

                // expecting the second thing to be the payer
                val payer: String
                if (remaining.contains(" ")) {
                    payer = remaining.substring(0, remaining.indexOf(" "))
                    remaining = remaining.replace("$payer ", "")
                } else {
                    payer = remaining
                    remaining = remaining.replace(payer, "")
                }
                if (payer.isBlank()) throw AddAppTransactionListException(
                    i
                )
                allParticipantList.add(payer)
                senderList.add(payer)

                // the remaining is the description
                var parsedInput = "$valueAsString|$participants"
                if (remaining.isNotEmpty()) parsedInput += " \"$remaining\""
                parsedInputList.add(parsedInput)

                allParticipantList = allParticipantList.distinct().sorted().toMutableList()

            } catch (e: StringIndexOutOfBoundsException) {
                throw AddAppTransactionListException(i)
            }
        }

        return Pair(
            senderList,
            parsedInputList.map { line ->
                line.replace(
                    ALL_PARTICIPANTS_PLACEHOLDER,
                    allParticipantList.joinToString(",")
                )
            }
        )

    }

}
