package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.User

interface UserService {
    fun signUp(username: String, plainCpf: String, plainPassword: String): User

    fun signIn(username: String, plainPassword: String): User

    fun resetPassword(username: String, plainCpf: String, plainNewPassword: String)
}
