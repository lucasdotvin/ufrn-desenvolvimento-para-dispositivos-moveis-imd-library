package vin.lucas.imdlibrary.dependencies

import android.content.Context
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.dependencies.ServiceContainer
import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.validation.CpfValidator
import vin.lucas.imdlibrary.repositories.BcryptHashRepository
import vin.lucas.imdlibrary.repositories.SharedPreferencesSessionRepository
import vin.lucas.imdlibrary.repositories.SqliteUserRepository
import vin.lucas.imdlibrary.services.DefaultUserService
import vin.lucas.imdlibrary.validation.DefaultCpfValidator

class DefaultServiceContainer(context: Context) : ServiceContainer {
    override val cpfValidator: CpfValidator = DefaultCpfValidator()

    override val hashRepository: HashRepository = BcryptHashRepository()
    override val sessionRepository: SessionRepository by lazy {
        SharedPreferencesSessionRepository(context)
    }

    override val userRepository: UserRepository by lazy {
        SqliteUserRepository(
            context,
            context.getString(R.string.sqlite_database_name),
        )
    }

    override val userService: UserService by lazy {
        DefaultUserService(
            cpfValidator,
            hashRepository,
            sessionRepository,
            userRepository,
        )
    }
}
