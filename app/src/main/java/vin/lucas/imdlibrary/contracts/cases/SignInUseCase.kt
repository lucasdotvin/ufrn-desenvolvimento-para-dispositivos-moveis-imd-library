package vin.lucas.imdlibrary.contracts.cases

import vin.lucas.imdlibrary.entities.User

interface SignInUseCase {
    fun execute(username: String, plainPassword: String): User
}
