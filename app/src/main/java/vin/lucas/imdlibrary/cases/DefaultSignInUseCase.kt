package vin.lucas.imdlibrary.cases

import vin.lucas.imdlibrary.contracts.cases.SignInUseCase
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.entities.User

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
