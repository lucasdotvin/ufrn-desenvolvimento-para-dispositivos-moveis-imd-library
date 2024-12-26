package vin.lucas.imdlibrary.contracts.cases.books

import vin.lucas.imdlibrary.entities.Book

interface DeleteBookUseCase {
    fun execute(book: Book)
}
