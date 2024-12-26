package vin.lucas.imdlibrary.contracts.dependencies

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
import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.contracts.repositories.HashRepository
import vin.lucas.imdlibrary.contracts.repositories.SessionRepository
import vin.lucas.imdlibrary.contracts.repositories.UserRepository
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.contracts.services.SessionService
import vin.lucas.imdlibrary.contracts.services.UserService
import vin.lucas.imdlibrary.contracts.validation.CpfValidator

interface ServiceContainer {
    /** Validation */
    val cpfValidator: CpfValidator

    /** Repositories */
    val bookRepository: BookRepository
    val hashRepository: HashRepository
    val sessionRepository: SessionRepository
    val userRepository: UserRepository

    /** Services */
    val bookService: BookService
    val sessionService: SessionService
    val userService: UserService

    /** Use Cases */
    val signInUseCase: SignInUseCase
    val signUpUseCase: SignUpUseCase
    val resetPasswordUseCase: ResetPasswordUseCase
    val signOutUseCase: SignOutUseCase

    val createBookUseCase: CreateBookUseCase
    val getAllBooksUseCase: GetAllBooksUseCase
    val findBookUseCase: FindBookUseCase
    val deleteBookUseCase: DeleteBookUseCase
    val updateBookUseCase: UpdateBookUseCase
    val findBookByIsbnUseCase: FindBookByIsbnUseCase
}
