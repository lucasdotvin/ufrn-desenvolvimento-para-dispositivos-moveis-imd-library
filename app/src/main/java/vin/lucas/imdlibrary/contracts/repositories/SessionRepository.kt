package vin.lucas.imdlibrary.contracts.repositories

import vin.lucas.imdlibrary.entities.User

interface SessionRepository {
    fun store(user: User, durationInSeconds: Long?)

    fun retrieve(): Long?

    fun clear()
}
