package com.naufaldystd.core.domain.model


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {

	fun getUser(): Flow<UserModel> {
		return dataStore.data.map { preference ->
			UserModel(
				preference[NAME_KEY] ?: "Tamu",
				preference[USERID_KEY] ?: "",
				preference[TOKEN_KEY] ?: "",
				preference[STATE_KEY] ?: false,
			)
		}
	}

	suspend fun saveUserSession(user: UserModel) {
		dataStore.edit { preferences ->
			preferences[NAME_KEY] = user.name
			preferences[USERID_KEY] = user.userId
			preferences[TOKEN_KEY] = user.token
			preferences[STATE_KEY] = user.isLogin
		}
	}

	suspend fun logout() {
		dataStore.edit {
			it.clear()
		}
	}

	companion object {
		private val NAME_KEY = stringPreferencesKey("name")
		private val USERID_KEY = stringPreferencesKey("userId")
		private val TOKEN_KEY = stringPreferencesKey("token")
		private val STATE_KEY = booleanPreferencesKey("state")
	}

}