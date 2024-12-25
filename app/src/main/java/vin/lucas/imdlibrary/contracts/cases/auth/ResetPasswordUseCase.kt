package vin.lucas.imdlibrary.contracts.cases.auth

interface ResetPasswordUseCase {
    fun execute(username: String, plainCpf: String, plainPassword: String)
}
