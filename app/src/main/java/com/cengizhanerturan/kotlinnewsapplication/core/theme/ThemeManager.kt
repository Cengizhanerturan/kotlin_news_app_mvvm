package com.cengizhanerturan.kotlinnewsapplication.core.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SP_IS_DARK_MODE
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.THEME_PREFS

object ThemeManager {
    fun applyTheme(context: Context) {
        //val isDarkMode = getThemePreference(context)
        val isDarkMode = true
        setTheme(isDarkMode)
    }

    fun changeTheme(context: Context, isDarkMode: Boolean) {
        saveThemePreference(context, isDarkMode)
        setTheme(isDarkMode)
    }

    fun isDarkMode(context: Context): Boolean {
        return getThemePreference(context)
    }

    private fun setTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun saveThemePreference(context: Context, isDarkMode: Boolean) {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(SP_IS_DARK_MODE, isDarkMode)
        editor.apply()
    }

    private fun getThemePreference(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(SP_IS_DARK_MODE, false)
    }
}
