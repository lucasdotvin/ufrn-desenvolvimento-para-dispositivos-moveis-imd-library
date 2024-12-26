package vin.lucas.imdlibrary.cases.books

import vin.lucas.imdlibrary.contracts.cases.books.FindBookUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book

class DefaultFindBookUseCase(private val bookService: BookService) : FindBookUseCase {
    override fun execute(id: Long): Book {
        return bookService.findById(id)
    }
}
