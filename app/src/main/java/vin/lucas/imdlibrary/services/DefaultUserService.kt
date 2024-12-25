package vin.lucas.imdlibrary.services

import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.entities.User

class DefaultUserService(
    private val hashRepository: HashRepository,
    private val userRepository: UserRepository,
) : UserService {
    override fun signUp(username: String, plainCpf: String, plainPassword: String): User {
        require(!userRepository.existsByUsername(username)) {
            "Nome de usuário já existe"
        }

        val hashedCpf = hashRepository.hash(plainCpf)
        val hashedPassword = hashRepository.hash(plainPassword)

        return userRepository.store(
            username,
            hashedCpf,
            hashedPassword,
        )
    }

    override fun signIn(username: String, plainPassword: String): User {
        val user = userRepository.findByUsername(username)

        requireNotNull(user) {
            "Usuário não encontrado"
        }

        require(hashRepository.verify(plainPassword, user.password)) {
            "Senha incorreta"
        }

        return user
    }

    override fun resetPassword(username: String, plainCpf: String, plainNewPassword: String) {
        val user = userRepository.findByUsername(username)

        requireNotNull(user) {
            "Usuário não encontrado"
        }

        require(hashRepository.verify(plainCpf, user.cpf)) {
            "CPF incorreto"
        }

        val hashedNewPassword = hashRepository.hash(plainNewPassword)
        user.password = hashedNewPassword

        userRepository.update(user)
    }
}
