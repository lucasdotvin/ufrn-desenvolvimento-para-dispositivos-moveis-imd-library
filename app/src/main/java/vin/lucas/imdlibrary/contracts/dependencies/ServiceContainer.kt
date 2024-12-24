package vin.lucas.imdlibrary.contracts.dependencies

import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService

interface ServiceContainer {
    val hashRepository: HashRepository
    val userRepository: UserRepository

    val userService: UserService
}
