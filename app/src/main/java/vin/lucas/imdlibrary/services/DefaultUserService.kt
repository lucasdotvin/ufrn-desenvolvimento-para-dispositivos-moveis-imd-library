package vin.lucas.imdlibrary.services

import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator
import vin.lucas.imdlibrary.entities.User

class DefaultUserService(
    private val cpfValidator: CpfValidator,
    private val hashRepository: HashRepository,
    private val sessionDurationInSeconds: Long,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : UserService {
    private var _currentUser: User? = null

    override val currentUser: User? get() = _currentUser

    init {
        loadSessionUser()
    }

    override fun signUp(username: String, cpf: String, plainPassword: String): User {
        require(username.isNotBlank()) {
            "Nome de usuário não pode ser vazio"
        }

        require(cpf.isNotBlank()) {
            "CPF não pode ser vazio"
        }

        require(plainPassword.isNotBlank()) {
            "Senha não pode ser vazia"
        }

        require(cpfValidator.validate(cpf)) {
            "CPF inválido"
        }

        require(!userRepository.existsByUsername(username)) {
            "Nome de usuário já existe"
        }

        val hashedCpf = hashRepository.hash(cpf)
        val hashedPassword = hashRepository.hash(plainPassword)

        val user = userRepository.store(
            username,
            hashedCpf,
            hashedPassword,
        )

        authenticate(user)

        return user
    }

    override fun signIn(username: String, plainPassword: String): User {
        require(username.isNotBlank()) {
            "Nome de usuário não pode ser vazio"
        }

        require(plainPassword.isNotBlank()) {
            "Senha não pode ser vazia"
        }

        val user = userRepository.findByUsername(username)

        requireNotNull(user) {
            "Usuário não encontrado"
        }

        require(hashRepository.verify(plainPassword, user.password)) {
            "Senha incorreta"
        }

        authenticate(user)

        return user
    }

    override fun resetPassword(username: String, plainCpf: String, plainNewPassword: String) {
        require(username.isNotBlank()) {
            "Nome de usuário não pode ser vazio"
        }

        require(plainCpf.isNotBlank()) {
            "CPF não pode ser vazio"
        }

        require(plainNewPassword.isNotBlank()) {
            "Nova senha não pode ser vazia"
        }

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

    private fun loadSessionUser() {
        val userKey = sessionRepository.retrieve()
        val sessionUser = userKey?.let { userRepository.findByKey(it) }
        sessionUser?.let { authenticate(it) }
    }

    private fun authenticate(user: User) {
        sessionRepository.store(user, sessionDurationInSeconds)
        _currentUser = user
    }
}
