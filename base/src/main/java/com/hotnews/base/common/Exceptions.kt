package com.hotnews.base.common

/**
 * Base exception class for the application
 */
open class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception for network-related errors
 */
class NetworkException(message: String? = null, cause: Throwable? = null) : AppException(message, cause)

/**
 * Exception for data parsing errors
 */
class DataParseException(message: String? = null, cause: Throwable? = null) : AppException(message, cause)

/**
 * Exception for authentication errors
 */
class AuthException(message: String? = null, cause: Throwable? = null) : AppException(message, cause)

/**
 * Exception for validation errors
 */
class ValidationException(message: String? = null, cause: Throwable? = null) : AppException(message, cause)