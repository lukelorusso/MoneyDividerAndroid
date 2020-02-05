package com.lukelorusso.data.mapper

import com.lukelorusso.data.extensions.containsDuplicates
import com.lukelorusso.domain.model.Constant
import com.lukelorusso.domain.model.Group
import com.lukelorusso.domain.model.Transaction

open class AddTransactionMapper {
    /**
     * Try to map the input to a [Transaction] (or "expense") object.
     * @param messageTimestamp the timestamp as [Long] of 13 positions, mapped by ambrogio.
     * @param messageSender the sender as [String], mapped by ambrogio.
     * @param input to be mapped.
     * @return a [Transaction] if successfully mapped, null otherwise
     */
    fun map(
        messageTimestamp: Long,
        messageSender: String,
        input: String,
        groupList: List<Group>
    ): Transaction? {
        if (input.isEmpty()
            || input.split("|").size != 2
        ) return null

        val value = mapValue(input)
        var participantNameList: List<String>
        var unevenPlusMap: Map<String, Double>? = null
        var unevenStarMap: Map<String, Int>? = null
        mapNameListAndParameter(messageSender, input, groupList).also { (nameList, parameterMap) ->
            participantNameList = nameList
            mapUnevenParameters(parameterMap)?.also { (plusMap, starMap) ->
                unevenPlusMap = plusMap
                unevenStarMap = starMap
            }
        }

        if (value != null
            && participantNameList.isNotEmpty()
            && validUnevenOperations(value, unevenPlusMap, unevenStarMap)
        ) return Transaction(
            messageTimestamp,
            messageSender,
            value,
            participantNameList,
            mapDescription(input) ?: "",
            unevenPlusMap!!,
            unevenStarMap!!
        )

        return null
    }

    /**
     * Try to map the input looking for the value.
     * @param input to be mapped.
     * @return the value as [Double], null otherwise
     *
     * Accepted examples:
     * > 20|MD,LF "Take away" -> (in this case value is 20)
     * > 19.50|PB,AC -> (in this case holder name is 19.50)
     */
    private fun mapValue(input: String): Double? = when {
        (input.isEmpty()) -> null

        input.contains("|") -> {
            // let's get everything before "|"
            val leftmost = input.split("|").first()

            when {
                // cannot be empty
                leftmost.isEmpty() -> null

                // here I'm saying: if it's a number with max 2 decimal digits, return it as double
                Constant.RegExp.VALUE.matches(leftmost) -> leftmost.toDoubleOrNull()

                else -> null
            }
        }

        else -> null
    }

    /**
     * Map participants' name into a list (each name should match PARTICIPANT_REGEXP) and optional parameters into a map for following mapping.
     * @param messageSender the sender as [String], mapped by ambrogio.
     * @param input to be mapped.
     * @param groupList to replace a group name with the group's  participants.
     * @return a [Pair] containing:
     * first: the (sorted) participants' name list as [List], can be empty
     * second: the parameters' map for each participant who has uneven parameters, can be empty
     *
     * Accepted examples:
     * > 20|MD,LF "Take away" -> (["MD", "LF"])
     * > 10|GP "Ice cream" -> (["GP"])
     * > 19.50|PB,AC -> (["AC", "PB"])
     * > 70|MD,PB,AC,PB -> (["AC", "MD", "PB"] meaning no repetitions allowed)
     * > 19.50|RC+5.50,TG -> (["RC", "TG"], {RC="+5.50"})
     * > 62|MM+2*3,LQ*2,FP -> (["FP", "LQ", "MM"], {LQ="*2", MM="+2*3"})
     */
    private fun mapNameListAndParameter(
        messageSender: String,
        input: String,
        groupList: List<Group>
    ): Pair<List<String>, Map<String, String>> =
        when {
            input.isEmpty() -> Pair(emptyList(), emptyMap())

            input.contains("|") -> {
                // let's get everything after "|"
                var rightmost = input.split("|").last()

                // let's get everything before a potential " "
                if (rightmost.contains(" "))
                    rightmost = rightmost.substring(0, rightmost.indexOf(" "))

                // now we put each name into a list
                val lines = rightmost
                    .split(",")

                // checking naming format and populating a parameters' map
                val nameList = mutableListOf<String>()
                val parameterMap = mutableMapOf<String, String>()
                lines.forEach { nameAndParameters ->
                    val (name, parameters) = splitNameAndParameters(nameAndParameters)

                    when {
                        Constant.RegExp.PARTICIPANT_NAME.matches(name) -> {
                            nameList.add(name)
                            if (parameters.isNotEmpty()) parameterMap[name] = parameters
                        }

                        Constant.RegExp.GROUP_NAME.matches(name) -> {
                            val existentGroup = groupList.find { group -> group.name == name }
                            if (existentGroup == null)
                                return Pair(emptyList(), emptyMap())
                            else existentGroup.participantNameList.forEach { groupName ->
                                nameList.add(groupName)
                                if (parameters.isNotEmpty()) parameterMap[groupName] = parameters
                            }
                        }

                        else -> {
                            return Pair(emptyList(), emptyMap())
                        }
                    }
                }

                Pair(
                    // cannot accept duplicates inside name list, nor single-self-transactions
                    if (nameList.containsDuplicates() || (nameList.size == 1 && nameList.first() == messageSender))
                        emptyList()
                    else
                        nameList.toList().sorted(),
                    parameterMap
                )
            }

            else -> Pair(emptyList(), emptyMap())
        }

    /**
     * Make the separation between a (participant or group) name and the string of parameters.
     * @param nameAndParameters the name and parameters joined together.
     * @return a [Pair] containing:
     * first: the (participant or group) name as [String], should not be empty but can be in case of bad input
     * second: the parameters as [String], can be empty
     */
    private fun splitNameAndParameters(nameAndParameters: String): Pair<String, String> {
        var name = ""
        var parameters = ""
        var hasParameters = false
        nameAndParameters.forEach { char ->
            // while I see upper-cased letters, a name is gonna be populated
            if (!hasParameters && Constant.RegExp.PARTICIPANT_NAME_SINGLE_CHAR.matches(char.toString()))
                name += char
            // until some whatever-not-upper-cased-letters is found, then a parameters' string is gonna be populated
            else {
                parameters += char
                if (!hasParameters) hasParameters = true
            }
        }
        return Pair(name, parameters)
    }

    /**Try to map, for each [String] of parameters after a value, a potential plus or star operation. Can be null if cannot be mapped or validated.
     * @param parameterMap to be mapped.
     * @return a [Pair] containing:
     * first: uneven plus operations as [Map], can be empty
     * second: uneven star operations as [Map], can be empty
     *
     * Accepted examples:
     * > {RC="+5.50"} -> ({RC=5.5}, {})
     * > {LG="*2", MM="*2"} -> ({}, {LG=2, MM=2})
     * > {LQ="*2", MM="+2*3"} -> ({MM=2.0}, {LQ=2, MM=3})
     * > {LQ="*2", MM="*3+2"} -> ({MM=2.0}, {LQ=2, MM=3} yes, it's equivalent to the example before)
     */
    private fun mapUnevenParameters(parameterMap: Map<String, String>):
            Pair<Map<String, Double>, Map<String, Int>>? = if (parameterMap.isEmpty())
        Pair(emptyMap(), emptyMap())
    else {
        val unevenPlusMap = mutableMapOf<String, Double>()
        val unevenStarMap = mutableMapOf<String, Int>()

        // for each string containing parameters
        parameterMap.forEach { (participantName, parameter) ->
            when {
                parameter.isEmpty() -> {
                    // not expecting this to happen, but better be safe! Nothing to do anyway
                }

                parameter.startsWith(Constant.Operator.PLUS) -> {
                    val parameterWithoutPlusOperator = parameter.replace(Constant.Operator.PLUS, "")
                    if (parameter.contains(Constant.Operator.STAR)) {
                        // ex: +2*3
                        val parameterSplit =
                            parameterWithoutPlusOperator.split(Constant.Operator.STAR)
                        if (parameterSplit.size != 2)
                            return null
                        val plusValue = parameterSplit[0].toDoubleOrNull() ?: return null
                        val starValue = parameterSplit[1].toIntOrNull() ?: return null
                        unevenPlusMap[participantName] = plusValue
                        unevenStarMap[participantName] = starValue
                    } else {
                        // ex: +2
                        val plusValue = parameterWithoutPlusOperator.toDoubleOrNull() ?: return null
                        unevenPlusMap[participantName] = plusValue
                    }
                }

                parameter.startsWith(Constant.Operator.STAR) -> {
                    val parameterWithoutStarOperator = parameter.replace(Constant.Operator.STAR, "")
                    if (parameter.contains(Constant.Operator.PLUS)) {
                        // ex: *3+2
                        val parameterSplit =
                            parameterWithoutStarOperator.split(Constant.Operator.PLUS)
                        if (parameterSplit.size != 2)
                            return null
                        val starValue = parameterSplit[0].toIntOrNull() ?: return null
                        val plusValue = parameterSplit[1].toDoubleOrNull() ?: return null
                        unevenStarMap[participantName] = starValue
                        unevenPlusMap[participantName] = plusValue
                    } else {
                        // ex: *3
                        val starValue = parameterWithoutStarOperator.toIntOrNull() ?: return null
                        unevenStarMap[participantName] = starValue

                    }
                }

                else -> return null // unattended parameter
            }
        }

        Pair(unevenPlusMap, unevenStarMap)
    }

    /**
     * Check for un-even operation, in particular:
     * @param value
     * @param unevenPlusMap non-nullability AND total sum of values <= value
     * @param unevenStarMap non-nullability
     * @return [Boolean] if it's valid
     */
    private fun validUnevenOperations(
        value: Double,
        unevenPlusMap: Map<String, Double>?,
        unevenStarMap: Map<String, Int>?
    ): Boolean = when {
        unevenPlusMap == null -> false

        unevenStarMap == null -> false

        else -> {
            var sum = 0.0
            unevenPlusMap.forEach { (_, value) -> sum += value }
            sum <= value
        }
    }

    /**
     * Map description if any.
     * @param input to be mapped.
     * @return the description as [String], null otherwise
     *
     * Accepted examples:
     * > 20|MD,LF "Take away" ("Take away")
     * > 10|GP "Ice cream" ("Ice cream")
     * > 19.50|PB,AC (null)
     */
    private fun mapDescription(input: String): String? = when {
        (input.isEmpty()) -> null

        input.contains("|") -> {
            // let's get everything after "|"
            var rightmost = input.split("|").last()

            // let's get everything after a potential " "
            if (rightmost.contains(" ")) {
                rightmost = rightmost
                    .substring(rightmost.indexOf(" ") + 1, rightmost.lastIndex + 1)

                // let's isolate the text inside quotes
                if (rightmost.startsWith("\"") && rightmost.endsWith("\""))
                    rightmost
                        .replace("\"", "")
                else
                    null
            } else
                null
        }

        else -> null
    }

}
