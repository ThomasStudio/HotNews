package com.hotnews.base.util

/**
 * Common constants used throughout the application
 */
object Constants {
    // Network timeout constants
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val CONNECT_TIMEOUT_SECONDS = 10L
    const val READ_TIMEOUT_SECONDS = 15L
    const val WRITE_TIMEOUT_SECONDS = 15L
    
    // SharedPreferences keys
    const val PREF_NAME = "hotnews_prefs"
    
    // API constants
    const val BASE_URL = "https://api.example.com/"
    const val API_VERSION_V1 = "/v1"
    
    // Database constants
    const val DATABASE_NAME = "hotnews_database"
    const val DATABASE_VERSION = 1
    
    // Cache constants
    const val CACHE_SIZE_MB = 10L
    const val CACHE_DIR_NAME = "cache"
    
    // Permissions
    const val REQUEST_CODE_PERMISSIONS = 1000
    const val REQUEST_CODE_LOCATION = 1001
}