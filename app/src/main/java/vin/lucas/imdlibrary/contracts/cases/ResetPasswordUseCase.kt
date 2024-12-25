package vin.lucas.imdlibrary.contracts.cases

interface ResetPasswordUseCase {
    fun execute(username: String, plainCpf: String, plainPassword: String)
}
