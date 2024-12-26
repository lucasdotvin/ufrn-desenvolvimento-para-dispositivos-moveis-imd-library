package vin.lucas.imdlibrary.cases.books

import vin.lucas.imdlibrary.contracts.cases.books.FindBookByIsbnUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book

class DefaultFindBookByIsbnUseCase(private val bookService: BookService) : FindBookByIsbnUseCase {
    override fun execute(isbn: String): Book {
        return bookService.findByIsbn(isbn)
    }
}
