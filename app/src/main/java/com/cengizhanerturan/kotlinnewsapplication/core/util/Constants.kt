package com.cengizhanerturan.kotlinnewsapplication.core.util

object Constants {
    const val ERROR_MSG = "An error occurred, try again later."

    //firestore
    const val USER_PATH = "users"

    const val ERROR_INVALID_EMAIL = "Please enter a valid email address."
    const val ERROR_INVALID_PASSWORD =
        "Password must be at least 8 characters long, containing at least one uppercase letter, one lowercase letter, one digit, and one special character."
    const val ERROR_DO_NOT_MATCH_PASSWORD = "Passwords do not match."
    const val ERROR_INVALID_NAME = "Please enter your name."
    const val ERROR_INVALID_SURNAME = "Please enter your surname."

    //auth
    const val ERROR_USER_CREDENTIAL = "User credentials are no longer valid. Please sign in again."

    //api
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "97495f96cf464857a304730c57a29346"
    const val DEFAULT_COUNTRY = "us"
    const val NEWS_COUNT = 30
    const val SLIDER_ITEM_COUNT = 5

    //details screen
    const val NEWS_MODEL = "newsModel"

    //room
    const val DB_NAME = "newsDatabase"
    const val DB_TABLE_NAME = "news_table"

    //settings screen
    const val SUCCESS_UPDATE_USER_INFO = "Your information has been changed successfully."
    const val SUCCESS_UPDATE_EMAIL =
        "To complete the process of updating your email address, please click the verification link sent to your new email address. Your account will be updated after the confirmation."
    const val SUCCESS_UPDATE_PASSWORD = "Your password has been changed successfully."
    const val LOGOUT_DIALOG_TITLE = "Are you sure you want to log out of your account?"

    //Shared Preferences
    const val THEME_PREFS = "ThemePrefs"
    const val SP_IS_DARK_MODE = "isDarkMode"
}