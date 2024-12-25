package vin.lucas.imdlibrary.contracts.cases.auth

import vin.lucas.imdlibrary.entities.User

interface SignUpUseCase {
    fun execute(username: String, plainCpf: String, plainPassword: String): User
}