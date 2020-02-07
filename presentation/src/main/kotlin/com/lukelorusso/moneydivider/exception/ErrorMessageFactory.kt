package com.lukelorusso.moneydivider.exception

import android.content.Context
import com.lukelorusso.domain.exception.MappingException
import com.lukelorusso.moneydivider.R
import timber.log.Timber
import javax.inject.Inject

open class ErrorMessageFactory
@Inject internal constructor(private val context: Context) {

    /**
     * Creates a String representing an error message.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return [String] an error message.
     */
    open fun getError(exception: Throwable?): String =
        exception?.let {
            when (it) {
                is MappingException -> context.getString(R.string.error_parsing) + " (${it.line})"
                else -> context.getString(R.string.error_generic)
            }.apply { Timber.e(it) }
        } ?: getGenericError()

    private fun getGenericError() = context.getString(R.string.error_generic)

}
