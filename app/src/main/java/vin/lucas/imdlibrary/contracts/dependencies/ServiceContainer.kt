package vin.lucas.imdlibrary.contracts.dependencies

import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator

interface ServiceContainer {
    val cpfValidator: CpfValidator

    val hashRepository: HashRepository
    val sessionRepository: SessionRepository
    val userRepository: UserRepository

    val userService: UserService
}
