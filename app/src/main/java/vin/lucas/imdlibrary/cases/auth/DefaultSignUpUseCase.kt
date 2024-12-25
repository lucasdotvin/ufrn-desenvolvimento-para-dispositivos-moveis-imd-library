package vin.lucas.imdlibrary.cases.auth

import vin.lucas.imdlibrary.contracts.cases.auth.SignUpUseCase
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator

class DefaultSignUpUseCase(
    private val cpfValidator: CpfValidator,
    private val sessionService: SessionService,
    private val userService: UserService,
) : SignUpUseCase {
    override fun execute(username: String, plainCpf: String, plainPassword: String) {
        require(username.isNotBlank()) {
            "Nome de usuário não pode ser vazio"
        }

        require(plainCpf.isNotBlank()) {
            "CPF não pode ser vazio"
        }

        require(plainPassword.isNotBlank()) {
            "Senha não pode ser vazia"
        }

        require(cpfValidator.validate(plainCpf)) {
            "CPF inválido"
        }

        val user = userService.signUp(username, plainCpf, plainPassword)
        sessionService.store(user)
    }
}
