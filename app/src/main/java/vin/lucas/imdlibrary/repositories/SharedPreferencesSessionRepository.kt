package vin.lucas.imdlibrary.repositories

import android.content.Context
import android.content.SharedPreferences
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.entities.User
import vin.lucas.imdlibrary.entities.UserKey

class SharedPreferencesSessionRepository(private val context: Context) : SessionRepository {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.auth_shared_preferences_key),
            Context.MODE_PRIVATE,
        )
    }

    override fun clear() {
        with(sharedPreferences.edit()) {
            remove(context.getString(R.string.auth_shared_preferences_user_identifier_key))
            remove(context.getString(R.string.auth_shared_preferences_until_key))
            apply()
        }
    }

    override fun store(user: User, durationInSeconds: Long?) {
        with(sharedPreferences.edit()) {
            putString(
                context.getString(R.string.auth_shared_preferences_user_identifier_key),
                user.id.toString(),
            )

            durationInSeconds?.let {
                putLong(
                    context.getString(R.string.auth_shared_preferences_until_key),
                    System.currentTimeMillis() + it * 1000,
                )
            }

            apply()
        }
    }

    override fun retrieve(): UserKey? {
        val storedKey = sharedPreferences.getString(
            context.getString(R.string.auth_shared_preferences_user_identifier_key),
            null,
        )?.toInt() ?: return null

        val until = sharedPreferences.getLong(
            context.getString(R.string.auth_shared_preferences_until_key),
            0,
        )

        if (until != 0L && until < System.currentTimeMillis()) {
            return null
        }

        return storedKey
    }
}
