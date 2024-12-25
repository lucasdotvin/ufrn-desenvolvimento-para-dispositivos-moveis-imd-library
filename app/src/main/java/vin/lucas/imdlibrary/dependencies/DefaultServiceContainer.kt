package vin.lucas.imdlibrary.dependencies

import android.content.Context
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.cases.auth.DefaultResetPasswordUseCase
import vin.lucas.imdlibrary.cases.auth.DefaultSignInUseCase
import vin.lucas.imdlibrary.cases.auth.DefaultSignUpUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.ResetPasswordUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.SignInUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.SignUpUseCase
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.dependencies.ServiceContainer
import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator
import vin.lucas.imdlibrary.repositories.BcryptHashRepository
import vin.lucas.imdlibrary.repositories.SharedPreferencesSessionRepository
import vin.lucas.imdlibrary.repositories.SqliteBookRepository
import vin.lucas.imdlibrary.repositories.SqliteUserRepository
import vin.lucas.imdlibrary.services.DefaultSessionService
import vin.lucas.imdlibrary.services.DefaultUserService
import vin.lucas.imdlibrary.validation.DefaultCpfValidator

class DefaultServiceContainer(context: Context) : ServiceContainer {
    override val cpfValidator: CpfValidator = DefaultCpfValidator()

    override val bookRepository: BookRepository by lazy {
        SqliteBookRepository(
            context,
            context.getString(R.string.sqlite_database_name),
        )
    }

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

    override val sessionService: SessionService by lazy {
        DefaultSessionService(
            context.getString(R.string.session_duration_in_seconds).toLong(),
            sessionRepository,
            userRepository,
        )
    }

    override val userService: UserService by lazy {
        DefaultUserService(
            hashRepository,
            userRepository,
        )
    }

    override val signInUseCase: SignInUseCase by lazy {
        DefaultSignInUseCase(
            sessionService,
            userService,
        )
    }

    override val signUpUseCase: SignUpUseCase by lazy {
        DefaultSignUpUseCase(
            cpfValidator,
            sessionService,
            userService,
        )
    }

    override val resetPasswordUseCase: ResetPasswordUseCase by lazy {
        DefaultResetPasswordUseCase(
            cpfValidator,
            userService,
        )
    }
}
