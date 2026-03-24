package com.hotnews.base.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Utility functions for common operations
 */

/**
 * Show a toast message
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Show a toast message using string resource
 */
fun Context.showToast(@StringRes resourceId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resourceId, duration).show()
}

/**
 * Safe call function that executes the lambda only if the receiver is not null
 */
inline fun <T : Any, R> T?.safeLet(block: (T) -> R): R? {
    return if (this != null) block(this) else null
}

/**
 * Extension function to check if a string is not null and not empty
 */
fun String?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * Extension function to check if a collection is not null and not empty
 */
fun <T : Any> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}