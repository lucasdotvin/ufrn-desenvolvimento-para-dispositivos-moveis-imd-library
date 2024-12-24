package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.User

interface UserService {
    fun signUp(username: String, plainPassword: String): User

    fun signIn(username: String, plainPassword: String): User
}
