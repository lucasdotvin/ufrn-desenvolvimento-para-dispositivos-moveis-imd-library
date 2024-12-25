package vin.lucas.imdlibrary.contracts.repositories

import vin.lucas.imdlibrary.entities.User
import vin.lucas.imdlibrary.entities.UserKey

interface SessionRepository {
    fun store(user: User, durationInSeconds: Long?)

    fun retrieve(): UserKey?

    fun clear()
}
