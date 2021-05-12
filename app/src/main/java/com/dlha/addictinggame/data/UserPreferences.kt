package com.dlha.addictinggame.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.flow.first


class UserPreferences(context : Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore("user_pref")
    companion object {
        val user_auth_token  = preferencesKey<String>("auth_token")
    }
     suspend fun saveAuthToken(authToken : String) {
        dataStore.edit {
            preferences -> preferences[user_auth_token]  = authToken
        }
    }
    suspend fun getAuthToken() : String? {
        val dataStoreKey = preferencesKey<String>("auth_token")
        val preferences= dataStore.data.first()
        return preferences[dataStoreKey]
    }
    suspend fun deleteAuthToken() {
        dataStore.edit {
            it.clear()
        }
    }
}