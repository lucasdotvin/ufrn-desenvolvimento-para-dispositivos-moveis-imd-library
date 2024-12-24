package vin.lucas.imdlibrary.dependencies

import android.content.Context
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.dependencies.ServiceContainer
import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.repositories.BcryptHashRepository
import vin.lucas.imdlibrary.repositories.SqliteUserRepository
import vin.lucas.imdlibrary.services.DefaultUserService

class DefaultServiceContainer(context: Context) : ServiceContainer {
    override val hashRepository: HashRepository by lazy {
        BcryptHashRepository()
    }

    override val userRepository: UserRepository by lazy {
        SqliteUserRepository(
            context,
            context.getString(R.string.sqlite_database_name),
        )
    }

    override val userService: UserService by lazy {
        DefaultUserService(
            hashRepository,
            userRepository,
        )
    }
}
