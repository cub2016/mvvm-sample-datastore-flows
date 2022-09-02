package com.jerry.samsung_mvvm2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.EventLogTags
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences(
    val widgetClicked: Boolean
)


class CheckStateRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val CLICKED = booleanPreferencesKey("clicked")
    }

    val clickedFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val widgetClicked = preferences[PreferencesKeys.CLICKED] ?: false
            UserPreferences(widgetClicked)
        }

    companion object {
        val TAG = "CheckStateRepository"
    }

    suspend fun setWidgetClicked(clicked: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CLICKED] = clicked
        }
    }

    suspend fun fetchInitialPreferences() =
        UserPreferences(dataStore.data.first().toPreferences()[PreferencesKeys.CLICKED] ?: false)
}