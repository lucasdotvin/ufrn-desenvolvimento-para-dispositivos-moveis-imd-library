package vin.lucas.imdlibrary.dependencies

import android.content.Context
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.cases.auth.DefaultResetPasswordUseCase
import vin.lucas.imdlibrary.cases.auth.DefaultSignInUseCase
import vin.lucas.imdlibrary.cases.auth.DefaultSignOutUseCase
import vin.lucas.imdlibrary.cases.auth.DefaultSignUpUseCase
import vin.lucas.imdlibrary.cases.books.DefaultCreateBookUseCase
import vin.lucas.imdlibrary.cases.books.DefaultDeleteBookUseCase
import vin.lucas.imdlibrary.cases.books.DefaultFindBookByIsbnUseCase
import vin.lucas.imdlibrary.cases.books.DefaultFindBookUseCase
import vin.lucas.imdlibrary.cases.books.DefaultGetAllBooksUseCase
import vin.lucas.imdlibrary.cases.books.DefaultUpdateBookUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.ResetPasswordUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.SignInUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.SignOutUseCase
import vin.lucas.imdlibrary.contracts.cases.auth.SignUpUseCase
import vin.lucas.imdlibrary.contracts.cases.books.CreateBookUseCase
import vin.lucas.imdlibrary.contracts.cases.books.DeleteBookUseCase
import vin.lucas.imdlibrary.contracts.cases.books.FindBookByIsbnUseCase
import vin.lucas.imdlibrary.contracts.cases.books.FindBookUseCase
import vin.lucas.imdlibrary.contracts.cases.books.GetAllBooksUseCase
import vin.lucas.imdlibrary.contracts.cases.books.UpdateBookUseCase
import vin.lucas.imdlibrary.contracts.dependencies.ServiceContainer
import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator
import vin.lucas.imdlibrary.repositories.BcryptHashRepository
import vin.lucas.imdlibrary.repositories.SharedPreferencesSessionRepository
import vin.lucas.imdlibrary.repositories.sqlite.SqliteBookRepository
import vin.lucas.imdlibrary.repositories.sqlite.SqliteDriver
import vin.lucas.imdlibrary.repositories.sqlite.SqliteUserRepository
import vin.lucas.imdlibrary.services.DefaultBookService
import vin.lucas.imdlibrary.services.DefaultSessionService
import vin.lucas.imdlibrary.services.DefaultUserService
import vin.lucas.imdlibrary.validation.DefaultCpfValidator

class DefaultServiceContainer(context: Context) : ServiceContainer {
    /** Validation */
    override val cpfValidator: CpfValidator = DefaultCpfValidator()

    /** Repositories */
    private val sqliteDriver: SqliteDriver by lazy {
        SqliteDriver(
            context,
            context.getString(R.string.sqlite_database_name),
        )
    }

    override val bookRepository: BookRepository by lazy {
        SqliteBookRepository(sqliteDriver)
    }

    override val hashRepository: HashRepository = BcryptHashRepository()
    override val sessionRepository: SessionRepository by lazy {
        SharedPreferencesSessionRepository(context)
    }

    override val userRepository: UserRepository by lazy {
        SqliteUserRepository(sqliteDriver)
    }

    /** Services */
    override val bookService: BookService by lazy {
        DefaultBookService(
            bookRepository,
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

    /** Use Cases */
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

    override val signOutUseCase: SignOutUseCase by lazy {
        DefaultSignOutUseCase(
            sessionService,
        )
    }

    override val createBookUseCase: CreateBookUseCase by lazy {
        DefaultCreateBookUseCase(
            bookService,
        )
    }

    override val getAllBooksUseCase: GetAllBooksUseCase by lazy {
        DefaultGetAllBooksUseCase(
            bookService,
        )
    }

    override val findBookUseCase: FindBookUseCase by lazy {
        DefaultFindBookUseCase(
            bookService,
        )
    }

    override val deleteBookUseCase: DeleteBookUseCase by lazy {
        DefaultDeleteBookUseCase(
            bookService,
        )
    }

    override val updateBookUseCase: UpdateBookUseCase by lazy {
        DefaultUpdateBookUseCase(
            bookService,
        )
    }

    override val findBookByIsbnUseCase: FindBookByIsbnUseCase by lazy {
        DefaultFindBookByIsbnUseCase(
            bookService,
        )
    }
}
