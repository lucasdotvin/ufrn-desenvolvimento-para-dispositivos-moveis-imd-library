package vin.lucas.imdlibrary.contracts.cases.auth

interface SignUpUseCase {
    fun execute(username: String, plainCpf: String, plainPassword: String)
}
