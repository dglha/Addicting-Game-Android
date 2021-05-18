package com.dlha.addictinggame.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.Preferences
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore("user_pref")

    companion object {
        val userAuthToken = preferencesKey<String>("auth_token")
    }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[userAuthToken] = authToken
        }
    }

    suspend fun getAuthToken(): String? {
        val dataStoreKey = preferencesKey<String>("auth_token")
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun deleteAuthToken() {
        dataStore.edit {
            it.clear()
        }
    }
}