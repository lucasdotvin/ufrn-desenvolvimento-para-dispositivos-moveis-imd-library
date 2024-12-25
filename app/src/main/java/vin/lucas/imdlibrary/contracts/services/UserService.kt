package vin.lucas.imdlibrary.contracts.services

import vin.lucas.imdlibrary.entities.User

interface UserService {
    val currentUser: User?

    val isSignedIn: Boolean get() = currentUser != null

    fun signUp(username: String, cpf: String, plainPassword: String): User

    fun signIn(username: String, plainPassword: String): User

    fun resetPassword(username: String, plainCpf: String, plainNewPassword: String)
}
