package vin.lucas.imdlibrary.services

import vin.lucas.imdlibrary.contracts.repositories.BookRepository
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload

class DefaultBookService(
    private val bookRepository: BookRepository,
) : BookService {
    override fun store(payload: BookChangePayload): Book {
        require(!bookRepository.existsByIsbn(payload.isbn)) {
            "ISBN já cadastrado"
        }

        return bookRepository.store(payload)
    }

    override fun findByIsbn(isbn: String): Book {
        val book = bookRepository.findByIsbn(isbn)

        requireNotNull(book) {
            "Livro não encontrado"
        }

        return book
    }

    override fun findById(id: Long): Book {
        val book = bookRepository.findById(id)

        requireNotNull(book) {
            "Livro não encontrado"
        }

        return book
    }

    override fun update(book: Book) {
        val wasUpdated = bookRepository.update(book)

        require(wasUpdated) {
            "Livro não encontrado"
        }
    }

    override fun delete(book: Book) {
        val wasDeleted = bookRepository.delete(book)

        require(wasDeleted) {
            "Livro não encontrado"
        }
    }
}
