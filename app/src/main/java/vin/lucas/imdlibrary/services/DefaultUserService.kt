package vin.lucas.imdlibrary.services

import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.entities.User

class DefaultUserService(
    private val hashRepository: HashRepository,
    private val userRepository: UserRepository,
) : UserService {
    override fun signUp(username: String, plainPassword: String): User {
        require(username.isNotBlank()) {
            "Username cannot be blank"
        }

        require(plainPassword.isNotBlank()) {
            "Password cannot be blank"
        }

        require(!userRepository.existsByUsername(username)) {
            "Username already exists"
        }

        val hashedPassword = hashRepository.hash(plainPassword)

        return userRepository.store(username, hashedPassword)
    }

    override fun signIn(username: String, plainPassword: String): User {
        require(username.isNotBlank()) {
            "Username cannot be blank"
        }

        require(plainPassword.isNotBlank()) {
            "Password cannot be blank"
        }

        val user = userRepository.findByUsername(username)

        requireNotNull(user) {
            "User not found"
        }

        require(hashRepository.verify(plainPassword, user.password)) {
            "Invalid password"
        }

        return user
    }
}
