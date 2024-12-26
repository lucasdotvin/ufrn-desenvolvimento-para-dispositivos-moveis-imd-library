package vin.lucas.imdlibrary.cases.books

import vin.lucas.imdlibrary.contracts.cases.books.GetAllBooksUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book

class DefaultGetAllBooksUseCase(private val bookService: BookService) : GetAllBooksUseCase {
    override fun execute(): List<Book> {
        return bookService.getAll()
    }
}
