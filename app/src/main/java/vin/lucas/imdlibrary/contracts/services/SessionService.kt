package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.User

interface SessionService {
    val currentUser: User?

    val isSignedIn: Boolean get() = currentUser != null

    fun store(user: User)

    fun clear()
}
