package vin.lucas.imdlibrary.cases.books

import vin.lucas.imdlibrary.contracts.cases.books.DeleteBookUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book

class DefaultDeleteBookUseCase(private val bookService: BookService) : DeleteBookUseCase {
    override fun execute(book: Book) {
        return bookService.delete(book)
    }
}
