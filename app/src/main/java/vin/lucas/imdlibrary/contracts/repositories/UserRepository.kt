package vin.lucas.imdlibrary.contracts.repositories

import vin.lucas.imdlibrary.entities.User

interface UserRepository {
    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?

    fun store(username: String, cpf: String, password: String): User

    fun update(user: User)
}
