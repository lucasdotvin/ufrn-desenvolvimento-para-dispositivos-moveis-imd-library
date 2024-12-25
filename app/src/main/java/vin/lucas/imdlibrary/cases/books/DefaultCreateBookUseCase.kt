package vin.lucas.imdlibrary.cases.books

import android.util.Patterns
import vin.lucas.imdlibrary.contracts.cases.books.CreateBookUseCase
import vin.lucas.imdlibrary.contracts.services.BookService
import vin.lucas.imdlibrary.values.BookChangePayload

class DefaultCreateBookUseCase(
    private val bookService: BookService,
) : CreateBookUseCase {
    override fun execute(payload: BookChangePayload) {
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

        bookService.store(payload)
    }
}
