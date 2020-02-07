package com.lukelorusso.domain.exception

sealed class AppException(message: String) : RuntimeException(message)

/**
 * Exception used when it is impossible to get data due to a lack of connection
 */
class NoConnectedException : AppException("No connection")

/**
 * Exception used when a mapper fails a mapping
 */
class MappingException(val line: Int) : AppException("Cannot map this input")
