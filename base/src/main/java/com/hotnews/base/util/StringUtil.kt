package com.hotnews.base.util

import java.net.URLEncoder

/**
 * Created by thomas on 3/29/2026.
 */

object StringUtil {
    fun encode(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())
}