package vin.lucas.imdlibrary.contracts.cases.auth

interface SignInUseCase {
    fun execute(username: String, plainPassword: String)
}
