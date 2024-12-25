package vin.lucas.imdlibrary.cases.auth

import vin.lucas.imdlibrary.contracts.cases.auth.SignInUseCase
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.services.UserService

class DefaultSignInUseCase(
    private val sessionService: SessionService,
    private val userService: UserService,
) : SignInUseCase {
    override fun execute(username: String, plainPassword: String) {
        require(username.isNotBlank()) {
            "Nome de usuário não pode ser vazio"
        }

        require(plainPassword.isNotBlank()) {
            "Senha não pode ser vazia"
        }

        val user = userService.signIn(username, plainPassword)
        sessionService.store(user)
    }
}
