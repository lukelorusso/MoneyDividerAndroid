package com.lukelorusso.data.functions

import com.lukelorusso.domain.exception.MappingException
import com.lukelorusso.domain.model.AddAppTransactionListException
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Function

class MappingExceptionFunction<T> : Function<Throwable, SingleSource<T>> {

    @Throws(Exception::class)
    override fun apply(error: Throwable): SingleSource<T> =
        Single.just(error)
            .flatMap {
                if (it is AddAppTransactionListException) {
                    Single.error<T>(MappingException(it.line))
                } else {
                    Single.error<T>(it)
                }
            }

}
