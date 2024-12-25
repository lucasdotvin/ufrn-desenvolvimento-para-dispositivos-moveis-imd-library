package vin.lucas.imdlibrary.services

import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.entities.User

class DefaultSessionService(
    private val sessionDurationInSeconds: Long,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : SessionService {
    private var _currentUser: User? = null

    override val currentUser: User? get() = _currentUser

    init {
        loadSessionUser()
    }

    override fun store(user: User) {
        sessionRepository.store(user, sessionDurationInSeconds)
        _currentUser = user
    }

    override fun clear() {
        sessionRepository.clear()
        _currentUser = null
    }

    private fun loadSessionUser() {
        val userKey = sessionRepository.retrieve()
        val sessionUser = userKey?.let { userRepository.findByKey(it) }
        sessionUser?.let { store(it) }
    }
}
