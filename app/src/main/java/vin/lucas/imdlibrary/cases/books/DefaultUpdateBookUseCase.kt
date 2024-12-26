package vin.lucas.imdlibrary.cases.books

import android.util.Patterns
import vin.lucas.imdlibrary.contracts.cases.books.UpdateBookUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.entities.Book
import vin.lucas.imdlibrary.values.BookChangePayload

class DefaultUpdateBookUseCase(private val bookService: BookService) : UpdateBookUseCase {
    override fun execute(book: Book, payload: BookChangePayload) {
        require(payload.isbn.isNotBlank()) {
            "ISBN não pode ser vazio"
        }

        require(payload.title.isNotBlank()) {
            "Título não pode ser vazio"
        }

        require(payload.author.isNotBlank()) {
            "Autor não pode ser vazio"
        }

        require(payload.publisher.isNotBlank()) {
            "Editora não pode ser vazio"
        }

        require(payload.publicationYear > 0) {
            "Ano de publicação não pode ser vazio"
        }

        require(payload.coverUrl.isNotBlank()) {
            "URL da capa não pode ser vazio"
        }

        require(Patterns.WEB_URL.matcher(payload.coverUrl).matches()) {
            "URL da capa precisa ser um endereço válido da Web"
        }

        return bookService.update(book.apply {
            isbn = payload.isbn
            title = payload.title
            author = payload.author
            publisher = payload.publisher
            publicationYear = payload.publicationYear
            coverUrl = payload.coverUrl
        })
    }
}
