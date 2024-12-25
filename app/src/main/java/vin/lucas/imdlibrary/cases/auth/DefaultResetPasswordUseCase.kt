package vin.lucas.imdlibrary.cases.auth

import vin.lucas.imdlibrary.contracts.cases.auth.ResetPasswordUseCase
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator

class DefaultResetPasswordUseCase(
    private val cpfValidator: CpfValidator,
    private val userService: UserService,
) : ResetPasswordUseCase {
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

        userService.resetPassword(username, plainCpf, plainPassword)
    }
}
